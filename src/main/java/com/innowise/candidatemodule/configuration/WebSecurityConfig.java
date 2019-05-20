package com.innowise.candidatemodule.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${security.enabled}")
    private boolean security;


    @Override
    public void configure(HttpSecurity http) throws Exception {
        if (security) {
            http
                    .antMatcher("/**")
                    .authorizeRequests()
                    .antMatchers("/login**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .oauth2Login();
        } else {
            http
                    .authorizeRequests()
                    .anyRequest().permitAll()
                    .and()
                    .oauth2Login();
        }
    }
}
