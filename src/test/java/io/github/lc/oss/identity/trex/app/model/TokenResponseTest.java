package io.github.lc.oss.identity.trex.app.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.lc.oss.identity.trex.AbstractMockTest;

public class TokenResponseTest extends AbstractMockTest {
    @Test
    public void test_constructors_v1() {
        TokenResponse tr = new TokenResponse("t");

        Assertions.assertNull(tr.getExpiration());
        Assertions.assertEquals("t", tr.getToken());
    }

    @Test
    public void test_constructors_v2() {
        TokenResponse tr = new TokenResponse(-1l, "t");

        Assertions.assertEquals(-1, tr.getExpiration());
        Assertions.assertEquals("t", tr.getToken());
    }
}
