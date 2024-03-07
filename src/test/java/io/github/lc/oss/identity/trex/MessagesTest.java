package io.github.lc.oss.identity.trex;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.lc.oss.identity.trex.Messages.Authentication;

public class MessagesTest extends AbstractMockTest {
    @Test
    public void test_application_caching() {
        Set<Authentication> all = new HashSet<>(Authentication.all());
        for (Authentication c : Authentication.values()) {
            Assertions.assertTrue(all.remove(c));
            Assertions.assertTrue(Authentication.hasName(c.name()));
            Assertions.assertSame(c, Authentication.byName(c.name()));
            Assertions.assertSame(c, Authentication.tryParse(c.name()));
        }
        Assertions.assertTrue(all.isEmpty());
    }
}
