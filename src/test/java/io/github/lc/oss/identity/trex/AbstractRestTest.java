package io.github.lc.oss.identity.trex;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import io.github.lc.oss.commons.serialization.Message;
import io.github.lc.oss.commons.testing.web.JsonObject;

@SpringBootTest(classes = { IntegrationConfig.class })
@Rollback
@Tag("restTest")
@ActiveProfiles("integrationtest")
public abstract class AbstractRestTest extends io.github.lc.oss.commons.testing.web.AbstractRestTest {
    protected void assertJsonMessage(JsonObject object, Message message, String text) {
        this.assertJsonMessage(object, message.getCategory().name(), message.getSeverity().name(), message.getNumber(),
                text);
    }

    protected void assertJsonMessage(JsonObject object, Message... messages) {
        for (Message m : messages) {
            this.assertJsonMessage(object, m.getCategory().name(), m.getSeverity().name(), m.getNumber());
        }
    }
}
