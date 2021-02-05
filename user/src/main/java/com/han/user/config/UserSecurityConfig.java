package com.han.user.config;

import com.han.user.filter.CheckCodeFilter;
import com.han.user.handler.LoginFailureHandler;
import com.han.user.handler.LoginSuccessHandler;
import com.han.user.provider.ComAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class UserSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Autowired
    private CheckCodeFilter checkCodeFilter;

    @Autowired
    ComAuthenticationProvider comAuthenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(checkCodeFilter, UsernamePasswordAuthenticationFilter.class)
            .formLogin()
            .loginPage("/login")
            .loginProcessingUrl("/login")
            .successHandler(loginSuccessHandler)
            .failureHandler(loginFailureHandler)
            .and()
            .cors()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.GET,
                    "/",
                    "/*.html",
                    "/favicon.ico",
                    "/**/*.html",
                    "/**/*.css",
                    "/**/*.js")
            .permitAll()
            .antMatchers("/login", "/getCode", "/getPublicKey")
            .permitAll()
            .antMatchers(HttpMethod.OPTIONS)
            .permitAll()
            .anyRequest()
            .authenticated();

        // 关闭csrf跨域攻击防御
        http.csrf().disable();

        // 禁用缓存
        http.headers().cacheControl();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
       auth.authenticationProvider(comAuthenticationProvider);
    }
}
