package com.han.user.provider;

import com.han.user.domain.entity.User;
import com.han.user.service.UserService;
import com.han.user.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class LoginValidateAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();

        String password = (String) authentication.getPrincipal();

        User user = (User) userService.loadUserByUsername(userName);

        if (!user.isEnabled()) {
            throw new DisabledException("该账户已被禁用，请联系管理员");
        } else if (!user.isAccountNonLocked()) {
            throw new LockedException("该账号已被锁定");
        } else if (!user.isAccountNonExpired()) {
            throw new AccountExpiredException("该账号已过期，请联系管理员");
        } else if (!user.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException("该账户的登录凭证已过期，请重新登录");
        }

        if(!passwordEncoder.matches(password, user.getUsername())){
            throw new BadCredentialsException("输入密码错误!");
        }
        return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
