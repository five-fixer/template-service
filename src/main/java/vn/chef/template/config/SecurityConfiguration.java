package vn.chef.template.config;

import vn.chef.common.constant.BuildProfile;
import vn.chef.template.security.AuthoritiesConstants;
import vn.chef.template.security.jwt.AuthenticationConfigurer;
import vn.chef.template.security.jwt.DummyAuthenticationConfigurer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;
import tech.jhipster.config.JHipsterProperties;

import java.util.Arrays;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(SecurityProblemSupport.class)
public class SecurityConfiguration {

    @Order(1)
    @Configuration
    public static class CommonSecurityConfiguration extends AbstractSecurityConfiguration {

        private final Environment env;

        public CommonSecurityConfiguration(final JHipsterProperties properties, final SecurityProblemSupport problemSupport, Environment env) {
            super(properties, problemSupport);
            this.env = env;
        }

        @Override
        public void configure(final HttpSecurity http) throws Exception {
            super.configure(http);

            if (Arrays.stream(env.getActiveProfiles()).noneMatch(BuildProfile.MOCK_AUTH::equals)) {
                http.apply(new AuthenticationConfigurer());
            } else {
                http.apply(new DummyAuthenticationConfigurer());
            }
        }
    }

    private abstract static class AbstractSecurityConfiguration extends WebSecurityConfigurerAdapter {

        private final JHipsterProperties properties;
        private final SecurityProblemSupport problemSupport;

        protected AbstractSecurityConfiguration(final JHipsterProperties properties, final SecurityProblemSupport problemSupport) {
            this.properties = properties;
            this.problemSupport = problemSupport;
        }

        @Override
        public void configure(final HttpSecurity http) throws Exception {
            // @formatter:off
            http
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(problemSupport)
                .accessDeniedHandler(problemSupport)
                .and()
                .headers()
                .contentSecurityPolicy(properties.getSecurity().getContentSecurityPolicy())
                .and()
                .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
                .and()
                .featurePolicy("geolocation 'none'; midi 'none'; sync-xhr 'none'; microphone 'none'; camera 'none'; magnetometer 'none'; gyroscope 'none'; fullscreen 'self'; payment 'none'")
                .and()
                .frameOptions()
                .deny()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/admin/**").hasAnyAuthority(AuthoritiesConstants.SUPER_ADMIN)
                .antMatchers("/management/health").permitAll()
                .antMatchers("/management/health/**").permitAll()
                .antMatchers("/management/info").permitAll()
                .antMatchers("/management/prometheus").permitAll()
                .antMatchers("/management/**").hasAnyAuthority(AuthoritiesConstants.SUPER_ADMIN);
            // @formatter:on
        }

    }

}
