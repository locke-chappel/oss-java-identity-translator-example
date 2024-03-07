package io.github.lc.oss.identity.trex.app.service;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.github.lc.oss.commons.jwt.Jwt;
import io.github.lc.oss.commons.jwt.JwtHeader;
import io.github.lc.oss.commons.jwt.Util;
import io.github.lc.oss.commons.signing.Algorithm;
import io.github.lc.oss.commons.signing.Algorithms;
import io.github.lc.oss.commons.util.IoTools;
import io.github.lc.oss.identity.trex.app.model.Credentials;
import io.github.lc.oss.identity.trex.security.SecureConfig;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class IdentityService {
    private static final Algorithm ALGORITHM = Algorithms.ED25519;

    @Autowired
    private Clock clock;
    @Autowired
    private SecureConfig secureConfig;

    @Value("${application.applicationId}")
    private String applicationId;
    @Value("${application.keyStore.path:${user.home}/app-data/app.jks}")
    private String keyStorePath;

    private Map<String, KeyPair> secrets;
    private String currentUserSecretId;

    public String login(String appId, Credentials credentials) {
        if (StringUtils.isAnyBlank(appId, credentials.getUsername(), credentials.getPassword())) {
            return null;
        }

        // TODO call IdM system and map response to JWT

        // TODO if IdM response is null/invalid return null here

        Jwt jwt = this.issueJwt( //
                IdentityService.ALGORITHM, //
                this.now() + this.getSessionTimeout(appId), //
                null, //
                "User ID From IdM response", //
                this.applicationId, //
                appId);

        return this.signAndEncode(jwt);
    }

    public void logout(String appId, HttpServletRequest request) {
        /*
         * TODO extract JWT token from request (could be in a cookie or an HTTP header
         * depending on the call)
         */

        /*
         * TODO optionally validate the token, if valid then revoke, if not valid
         * nothing more to do (might save on resources depending on how costly revoking
         * a token in the IdM system is
         */

        /*
         * TODO call IdM system's logout logic to revoke issues tokens as needed,
         * optionally do this is async since as far as the client is concerned the token
         * is already invalid
         */
    }

    private Jwt issueJwt(Algorithm alg, Long expirationMillis, Long notBeforeMillis, String subject, String issuer,
            String... audience) {
        if (Util.isBlank(alg) || //
                Util.isBlank(expirationMillis) || //
                Util.isBlank(subject) || //
                Util.isBlank(issuer) || //
                Util.isBlank(audience)) {
            throw new IllegalArgumentException("All parameters are required");
        }
        Long now = System.currentTimeMillis() / 1000l;
        Long expires = expirationMillis / 1000l;
        Long notBefore = notBeforeMillis == null ? now : notBeforeMillis / 1000l;

        if (expires <= now) {
            throw new IllegalArgumentException("Expiration must be in the future");
        }

        if (expires < notBefore) {
            throw new IllegalArgumentException("'Not Before' cannot come after 'expires'");
        }

        Jwt t = new Jwt();
        t.getHeader().put(JwtHeader.Keys.TokenType, "JWT");
        t.getHeader().setAlgorithm(alg);
        t.getHeader().setKeyId(this.getCurrentUserSecretId());
        t.getPayload().setSubject(subject);
        t.getPayload().setIssuedAt(now);
        t.getPayload().setNotBefore(notBefore);
        t.getPayload().setExpiration(expires);
        t.getPayload().setTokenId(UUID.randomUUID().toString());
        t.getPayload().setIssuer(issuer);
        t.getPayload().setAudience(audience);

        for (String aud : audience) {
            t.getPayload().setPermissions(aud, this.getPermissions(subject, aud));
        }

        // TODO: Set user's display name if applicable
        // t.getPayload().setDisplayName("Human readable display name");
        return t;
    }

    private List<String> getPermissions(String subject, String aud) {
        /*
         * TODO: Resolve audience specific permissions and return as a collection (IdM
         * permissions may need to either be passes through the call stack from the
         * point where the IdM system was called or possibly a secondary API call is
         * made here).
         */
        return new ArrayList<>();
    }

    public String signAndEncode(Jwt token) {
        return this.signAndEncode((byte[]) null, token);
    }

    public String signAndEncode(byte[] secret, Jwt token) {
        byte[] s = this.getSignSecret(token.getHeader(), secret);
        if (s == null) {
            throw new RuntimeException("Secret cannot be null");
        }
        token.setSignature(
                token.getAlgorithm().getSignature(s, Util.toJsonNoSignature(token).getBytes(StandardCharsets.UTF_8)));
        return Util.toJson(token);
    }

    private String getCurrentUserSecretId() {
        if (this.currentUserSecretId == null) {
            List<String> secrets = this.secureConfig.getUserJwtSecrets();
            this.currentUserSecretId = secrets.get(secrets.size() - 1);
        }
        return this.currentUserSecretId;
    }

    protected byte[] getSignSecret(JwtHeader header, byte[] defaultSecret) {
        if (StringUtils.isBlank(header.getKeyId())) {
            return null;
        }

        KeyPair kp = this.getSecrets().get(header.getKeyId());
        if (kp == null) {
            return null;
        }

        return kp.getPrivate().getEncoded();
    }

    private Map<String, KeyPair> getSecrets() {
        if (this.secrets == null) {
            if (StringUtils.isBlank(this.keyStorePath)) {
                throw new RuntimeException("Key store path cannot be blank.");
            }

            Map<String, KeyPair> map = new HashMap<>();
            List<String> ids = this.secureConfig.getUserJwtSecrets();
            ids.forEach(id -> map.put(id, this.loadKey(this.keyStorePath, id)));
            this.secrets = Collections.unmodifiableMap(map);
        }
        return this.secrets;
    }

    private KeyPair loadKey(String keyStorePath, String alias) {
        char[] password = null;
        try {
            password = this.secureConfig.getKeystorePassword().toCharArray();
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(new FileInputStream(IoTools.getAbsoluteFilePath(keyStorePath)), password);
            PrivateKey pk = (PrivateKey) ks.getKey(alias, password);
            Certificate cert = ks.getCertificate(alias);
            return new KeyPair(cert.getPublicKey(), pk);
        } catch (Exception ex) {
            throw new RuntimeException("Error loading KeyPair", ex);
        } finally {
            Arrays.fill(password, ' ');
        }
    }

    protected long now() {
        return this.clock.instant().toEpochMilli();
    }

    /**
     * Returns the application specific session timeout in milliseconds.
     */
    private long getSessionTimeout(String appId) {
        // TODO: Return the appropriate session timeout based on application ID
        return 30 * 60 * 60;
    }
}
