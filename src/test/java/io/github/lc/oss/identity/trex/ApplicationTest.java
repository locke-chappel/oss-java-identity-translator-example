package io.github.lc.oss.identity.trex;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.builder.SpringApplicationBuilder;

public class ApplicationTest extends AbstractMockTest {
    @Test
    public void test_run_otherException() {
        Application app = new Application() {
            private SpringApplicationBuilder builder = Mockito.mock(SpringApplicationBuilder.class);
            private boolean exitCalled = false;

            @Override
            SpringApplicationBuilder build() {
                return this.builder;
            }

            @Override
            void exit() {
                Assertions.assertFalse(this.exitCalled);
                this.exitCalled = true;
            }
        };

        RuntimeException ex = Mockito.mock(RuntimeException.class);
        RuntimeException ex2 = Mockito.mock(RuntimeException.class);
        Mockito.when(ex.getCause()).thenReturn(ex);
        Mockito.when(ex2.getCause()).thenReturn(ex);

        Mockito.when(app.build().run((String[]) null)).thenThrow(ex2);

        Assertions.assertFalse((boolean) this.getField("exitCalled", app));

        app.run(null);

        Assertions.assertFalse((boolean) this.getField("exitCalled", app));
    }

    @Test
    public void test_run_otherException_noCause() {
        Application app = new Application() {
            private SpringApplicationBuilder builder = Mockito.mock(SpringApplicationBuilder.class);
            private boolean exitCalled = false;

            @Override
            SpringApplicationBuilder build() {
                return this.builder;
            }

            @Override
            void exit() {
                Assertions.assertFalse(this.exitCalled);
                this.exitCalled = true;
            }
        };

        RuntimeException ex = Mockito.mock(RuntimeException.class);
        Mockito.when(ex.getCause()).thenReturn(null);

        Mockito.when(app.build().run((String[]) null)).thenThrow(ex);

        Assertions.assertFalse((boolean) this.getField("exitCalled", app));

        app.run(null);

        Assertions.assertFalse((boolean) this.getField("exitCalled", app));
    }

    @Test
    public void test_build() {
        Application app = new Application();

        SpringApplicationBuilder result = app.build();
        Assertions.assertNotNull(result);
        Assertions.assertNotSame(result, app.build());
    }
}
