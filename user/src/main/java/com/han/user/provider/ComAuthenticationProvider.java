package com.han.user.provider;

import com.han.user.domain.entity.User;
import com.han.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class ComAuthenticationProvider implements AuthenticationProvider {


    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        Object principal = authentication.getPrincipal();
        String userName = authentication.getName();
        String password = "";
        if(null == principal){
            throw new UsernameNotFoundException("用户名或者密码不正确");
        }
        if(principal instanceof String){
            password = authentication.getCredentials().toString();
        }
        if(principal instanceof User){
            User user = (User) principal;
            password = user.getPassword();
        }


        User userDetails = (User) userService.loadUserByUsername(userName);

        if (!passwordEncoder.matches(password,userDetails.getPassword())) {
            throw new UsernameNotFoundException("用户名或者密码不正确");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, passwordEncoder.encode(password), userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
