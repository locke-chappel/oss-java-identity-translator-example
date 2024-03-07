package io.github.lc.oss.identity.trex.app.controllers.v1;

import org.junit.jupiter.api.Test;

import io.github.lc.oss.identity.trex.AbstractSeleniumTest;

public class PublicControllerUiIT extends AbstractSeleniumTest {
    @Test
    public void test_ui() {
        // --- open app
        this.navigate("/");
        this.waitForNavigate("/");
        this.assertTextContent("content", "Some info about how to integrate the API this service provides.");

        // --- cause error
        this.navigate("/does-not-exist");
        this.waitForNavigate("/does-not-exist");
        this.assertTextContent("content", "Oops...something unexpected happened.");
    }
}
