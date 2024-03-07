package io.github.lc.oss.identity.trex.security;

import org.junit.jupiter.api.Test;

import io.github.lc.oss.identity.trex.AbstractMockTest;

public class SecureConfigTest extends AbstractMockTest {
    /*
     * Coverage filler, a properly running system should never log errors here, when
     * it does it's just to help fix the deployment issue.
     */
    @Test
    public void test_logError() {
        SecureConfig config = new SecureConfig();

        config.logError("message");
    }
}
