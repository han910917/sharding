package com.han.user.config;

import com.han.user.filter.CheckCodeFilter;
import com.han.user.handler.LoginFailureHandler;
import com.han.user.handler.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class UserSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Autowired
    private CheckCodeFilter checkCodeFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(checkCodeFilter, UsernamePasswordAuthenticationFilter.class)
            .formLogin()
            .loginPage("/login")
            .loginProcessingUrl("/login")
            .successHandler(loginSuccessHandler)
            .failureHandler(loginFailureHandler)
            .and()
            .authorizeRequests()
            .antMatchers("/login", "/getCode")
            .permitAll()
            .anyRequest()
            .authenticated();

        // 关闭csrf跨域攻击防御
        http.csrf().disable();
    }
}
