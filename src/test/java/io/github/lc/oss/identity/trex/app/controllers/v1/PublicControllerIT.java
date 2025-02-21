package io.github.lc.oss.identity.trex.app.controllers.v1;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import io.github.lc.oss.commons.testing.web.JsonObject;
import io.github.lc.oss.identity.trex.AbstractRestTest;
import io.github.lc.oss.identity.trex.Messages;
import io.github.lc.oss.identity.trex.app.model.Credentials;

public class PublicControllerIT extends AbstractRestTest {
    @Test
    public void test_landing_page() {
        ResponseEntity<String> result = this.call(HttpMethod.GET, "/", null, String.class, null);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        HttpHeaders headers = result.getHeaders();
        this.assertHeader("Content-Security-Policy", "default-src 'none'; script-src 'self'; " + //
                "connect-src 'self'; img-src 'self'; style-src 'self'; font-src 'self'; " + //
                "frame-ancestors 'none'; form-action 'none';", headers);
        this.assertHeader("X-Content-Type-Options", "nosniff", headers);
        this.assertHeader("X-Frame-Options", "deny", headers);
        this.assertHeader("X-XSS-Protection", "0", headers);
    }

    @Test
    public void test_error_page() {
        ResponseEntity<String> result = this.call(HttpMethod.GET, "/error", null, String.class, null);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        HttpHeaders headers = result.getHeaders();
        this.assertHeader("Content-Security-Policy", "default-src 'none'; script-src 'self'; " + //
                "connect-src 'self'; img-src 'self'; style-src 'self'; font-src 'self'; " + //
                "frame-ancestors 'none'; form-action 'none';", headers);
        this.assertHeader("X-Content-Type-Options", "nosniff", headers);
        this.assertHeader("X-Frame-Options", "deny", headers);
        this.assertHeader("X-XSS-Protection", "0", headers);
    }

    @Test
    public void test_login_noCredentials() {
        Credentials creds = new Credentials(null, null);
        ResponseEntity<JsonObject> result = this.postJson("/api/v1/login/app-id", creds, null,
                HttpStatus.UNPROCESSABLE_ENTITY);
        this.assertJsonMessage(result.getBody(), Messages.Authentication.InvalidCredentials);
    }

    @Test
    public void test_login_delete() {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-JWT", "<some-jwt-value>");

        ResponseEntity<JsonObject> result = this.deleteJson("/api/v1/login/app-id", headers);
        Assertions.assertNull(result.getBody());
    }
}
