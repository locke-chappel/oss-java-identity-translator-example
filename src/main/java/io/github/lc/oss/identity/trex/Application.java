package io.github.lc.oss.identity.trex;

import org.springframework.boot.builder.SpringApplicationBuilder;

public class Application {
    public static void main(String[] args) {
        Application app = new Application();
        app.run(args);
    }

    /*
     * Exposed for testing
     */
    void run(String[] args) {
        try {
            SpringApplicationBuilder builder = this.build();
            builder.run(args);
        } catch (Exception e) {
            Throwable ex = e;
            while (!this.isRoot(ex)) {
                ex = ex.getCause();
            }
        }
    }

    private boolean isRoot(Throwable t) {
        if (t.getCause() == null) {
            return true;
        }

        if (t.getCause().equals(t)) {
            return true;
        }

        return false;
    }

    /*
     * Exposed for testing
     */
    SpringApplicationBuilder build() {
        return new SpringApplicationBuilder(ApplicationConfig.class);
    }

    /*
     * Exposed for testing
     */
    void exit() {
        /*
         * Note: missing code coverage - properly testing this call in JDK17+ is
         * non-trivial, deferred for now.
         */
        System.exit(1);
    }

    /*
     * Exposed for testing
     */
    Application() {
    }
}
