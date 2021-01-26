package com.han.user.config;

import com.han.user.filter.VerifyCodeFilter;
import com.han.user.handler.LoginFailureHandler;
import com.han.user.handler.LoginSuccessHandler;
import com.han.user.provider.LoginValidateAuthenticationProvider;
import com.han.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class UserSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoginValidateAuthenticationProvider loginValidateAuthenticationProvider;

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Autowired
    private VerifyCodeFilter verifyCodeFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        // 指定之定义验证
        auth.authenticationProvider(loginValidateAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(verifyCodeFilter, UsernamePasswordAuthenticationFilter.class)
                    .authorizeRequests()
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .defaultSuccessUrl("/index")
                    .successHandler(loginSuccessHandler)
                    .failureHandler(loginFailureHandler)
                    .permitAll();

        // 关闭csrf跨域攻击防御
        http.csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/login", "/getCode","/css/**","/js/**", "/index.html", "/img/**", "/fonts/**","/favicon.ico");
    }
}
