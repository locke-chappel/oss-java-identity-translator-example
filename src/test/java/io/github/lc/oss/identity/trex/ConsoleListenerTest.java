package io.github.lc.oss.identity.trex;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

public class ConsoleListenerTest extends AbstractMockTest {
    @Mock
    private ApplicationContext context;
    @Mock
    private Environment env;

    /*
     * This class is a testing helper with minimal logic, all we need to do is cover
     * the non-exit case and we are done.
     */
    @Test
    public void test_codeCoverage() {
        Thread thread = Mockito.mock(Thread.class);
        ConsoleListener listener = new ConsoleListener() {
            @Override
            protected synchronized Thread getThread() {
                return thread;
            }
        };

        Mockito.when(this.context.getEnvironment()).thenReturn(this.env);
        Mockito.when(this.env.matchesProfiles("ConsoleListener")).thenReturn(true);
        this.setField("context", this.context, listener);

        listener.init();
        listener.process("");
        listener.process("exit");
    }

    @Test
    public void test_codeCoverage_notEnabled() {
        Thread thread = Mockito.mock(Thread.class);
        ConsoleListener listener = new ConsoleListener() {
            @Override
            protected synchronized Thread getThread() {
                return thread;
            }

            @Override
            public synchronized void start() {
                Assertions.fail("Not supposed to be called");
            }
        };

        this.setField("context", this.context, listener);

        Mockito.when(this.context.getEnvironment()).thenReturn(this.env);
        Mockito.when(this.env.matchesProfiles("ConsoleListener")).thenReturn(false);

        listener.init();
    }
}
