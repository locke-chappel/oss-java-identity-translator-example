package io.github.lc.oss.identity.trex;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = { IntegrationConfig.class })
@Rollback
@Tag("seleniumTest")
@ActiveProfiles("integrationtest")
public abstract class AbstractSeleniumTest extends io.github.lc.oss.commons.testing.web.AbstractSeleniumTest {
    @Override
    protected void waitForScript(int timeout) {
        /* no-op, this example app doesn't use javascript so nothing to wait for */
    }
}
