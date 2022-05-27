package com.cg.challenge4.webapp.security;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;



public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

@Override
protected void configure(HttpSecurity http) throws Exception {
    http
            .csrf()
            .and()
            .oauth2Login()
            .and()
            .logout()
            .logoutSuccessUrl("/");
  }

}
