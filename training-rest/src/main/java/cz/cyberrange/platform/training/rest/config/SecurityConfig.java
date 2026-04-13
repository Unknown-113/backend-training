package cz.cyberrange.platform.training.rest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .requestMatchers(matchers -> matchers
                .antMatchers("/training-runs/*/terminal-access"))
            .authorizeRequests(auth -> auth
                .anyRequest().permitAll())
            .csrf().disable();
    }
}
