package io.github.lc.oss.identity.trex.app.service;

import java.time.Clock;
import java.time.Instant;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import io.github.lc.oss.identity.trex.AbstractMockTest;
import io.github.lc.oss.identity.trex.app.model.Credentials;
import io.github.lc.oss.identity.trex.security.SecureConfig;

public class IdentityServiceTest extends AbstractMockTest {
    @Mock
    private Clock clock;
    @Mock
    private SecureConfig secureConfig;

    @InjectMocks
    private IdentityService service;

    @BeforeEach
    public void init() {
        this.setField("applicationId", "appId", this.service);
        this.setField("keyStorePath", "junit.jks", this.service);
    }

    @Test
    public void test_login_missingValues() {
        String appId = null;
        Credentials creds = new Credentials(null, null);

        String result = this.service.login(appId, creds);
        Assertions.assertNull(result);

        appId = "";
        creds = new Credentials("", "");

        result = this.service.login(appId, creds);
        Assertions.assertNull(result);

        appId = " \t \r \n \t ";
        creds = new Credentials(" \t \r \n \t ", " \t \r \n \t ");

        result = this.service.login(appId, creds);
        Assertions.assertNull(result);

        appId = "appId";
        creds = new Credentials(" \t \r \n \t ", " \t \r \n \t ");

        result = this.service.login(appId, creds);
        Assertions.assertNull(result);

        appId = "appId";
        creds = new Credentials("uName", " \t \r \n \t ");

        result = this.service.login(appId, creds);
        Assertions.assertNull(result);

        appId = "appId";
        creds = new Credentials(null, "pass");

        result = this.service.login(appId, creds);
        Assertions.assertNull(result);

        appId = null;
        creds = new Credentials("uName", "pass");

        result = this.service.login(appId, creds);
        Assertions.assertNull(result);
    }

    @Test
    public void test_login() {
        Mockito.when(this.clock.instant()).thenReturn(Instant.now());
        Mockito.when(this.secureConfig.getUserJwtSecrets()).thenReturn(Arrays.asList("junit-user-1"));
        Mockito.when(this.secureConfig.getKeystorePassword()).thenReturn("junit");

        Credentials creds = new Credentials("uName", "pass");
        String result = this.service.login("appId", creds);
        Assertions.assertNotNull(result);

        // TODO: Validate JWT and all fields
    }

    // TODO: Additional tests to cover exception paths and unexpected user inputs
}
