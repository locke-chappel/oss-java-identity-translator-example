package io.github.lc.oss.identity.trex;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import io.github.lc.oss.commons.web.config.DefaultAppConfiguration;
import io.github.lc.oss.commons.web.filters.UserLocaleFilter;
import io.github.lc.oss.identity.trex.security.SecureConfig;

@Configuration
@EnableAutoConfiguration(exclude = { ErrorMvcAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class })
@EnableAspectJAutoProxy
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@ComponentScan(basePackages = { "io.github.lc.oss.identity.trex.app" })
public class ApplicationConfig extends DefaultAppConfiguration {
    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> customErrorPageCustomizer() {
        return new ErrorPageCustomizer();
    }

    @Bean
    public FilterRegistrationBean<UserLocaleFilter> registerUserLocaleFilter() {
        FilterRegistrationBean<UserLocaleFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(this.userLocaleFilter());
        bean.setUrlPatterns(Arrays.asList("/*"));
        bean.setName("User Locale Filter");
        return bean;
    }

    @Bean
    public ConsoleListener consoleListener() {
        return new ConsoleListener();
    }

    @Bean
    public SecureConfig secureConfig(@Autowired Environment env) {
        return this.loadEncryptedConfig(env, SecureConfig.Keys.values(), SecureConfig.class);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        this.configureDefaultPublicAccessUrls(http);

        /* Public Access */
        http.cors((cors) -> org.springframework.security.config.Customizer.withDefaults());
        http.authorizeHttpRequests((ahr) -> ahr //
                .requestMatchers(this.matchers(HttpMethod.GET, //
                        /* Pages */
                        "^/$", //

                        /* Resources */
                        "^/favicon.ico$", //
                        "^/l10n/[a-z]{2}(?:-[A-Z]{2})?/messages.Application.Error.1$", //

                        /* Error Page */
                        "^/error$"))
                .permitAll(). //
                requestMatchers(this.matchers(HttpMethod.POST, //
                        "^/api/v1/login/[^/]+$"))
                .permitAll(). //
                requestMatchers(this.matchers(HttpMethod.DELETE, //
                        "^/api/v1/login/[^/]+$"))
                .permitAll());

        this.configureDefaultHeaders(http);

        return http.build();
    }
}
