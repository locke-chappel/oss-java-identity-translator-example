package io.github.lc.oss.identity.trex;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;

import io.github.lc.oss.commons.testing.web.DefaultIntegrationConfig;
import io.github.lc.oss.commons.util.TimeIntervalParser;
import io.github.lc.oss.commons.web.services.HttpService;
import io.github.lc.oss.commons.web.services.JsonService;
import io.github.lc.oss.commons.web.util.PropertiesConfigUtil;
import io.github.lc.oss.identity.trex.security.SecureConfig;

/* Note: same annotations as app config (mostly) */
@Configuration
@EnableAutoConfiguration(exclude = { ErrorMvcAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class })
@EnableAspectJAutoProxy
@ComponentScan(basePackages = { "io.github.lc.oss.identity.trex.app" })
public class IntegrationConfig extends DefaultIntegrationConfig {
    @Bean
    public HttpService httpService() {
        return new HttpService();
    }

    @Bean
    public JsonService jsonService() {
        return new JsonService();
    }

    @Bean
    public TimeIntervalParser timeIntervalParser() {
        return new TimeIntervalParser();
    }

    @Bean
    public SecureConfig secureConfigIt(@Autowired Environment env) {
        SecureConfig config = new SecureConfig();
        PropertiesConfigUtil.loadFromEnv(config, env, "application.secure-config.", SecureConfig.Keys.values());
        return config;
    }
}
