package io.github.lc.oss.identity.trex;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Tag("mockTest")
@ActiveProfiles("test")
public abstract class AbstractMockTest extends io.github.lc.oss.commons.testing.web.AbstractLocaleMockTest {
}
