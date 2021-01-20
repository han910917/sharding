package com.han.user.config;

import com.han.user.provider.LoginValidateAuthenticationProvider;
import com.han.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class UserSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoginValidateAuthenticationProvider loginValidateAuthenticationProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        // 指定之定义验证
        auth.authenticationProvider(loginValidateAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/hello", "/login.html").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .successForwardUrl("/success")
            .permitAll();

        // 关闭csrf跨域攻击防御
        http.csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }
}
