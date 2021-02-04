package com.han.user.provider;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.han.user.domain.entity.User;
import com.han.user.service.UserService;
import com.han.user.utils.ImageCodeUtil;
import com.han.user.utils.RSAEncryptUtil;
import com.han.user.utils.RedisUtil;
import com.han.user.utils.ServletRequestUtil;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.SubstituteLogger;
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

import javax.servlet.http.HttpServletRequest;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Collection;
import java.util.UUID;

@Component
public class ComAuthenticationProvider implements AuthenticationProvider {
    Logger logger = LoggerFactory.getLogger(ComAuthenticationProvider.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) {

        Object principal = authentication.getPrincipal();
        String userName = authentication.getName();

        String password = "";
        if(null == principal){
            throw new UsernameNotFoundException("用户名或者密码不能为空");
        }
        if(principal instanceof String){
            password = authentication.getCredentials().toString();
        }
        if(principal instanceof User){
            User user = (User) principal;
            password = user.getPassword();
        }

        String decryptPassword = null;
        try {
            decryptPassword = getDecryptPassword(password, RedisUtil.getValue(ImageCodeUtil.UUID));
        } catch (Exception e) {
            e.printStackTrace();
        }
        User userDetails = (User) userService.loadUserByUsername(userName);

        if (!passwordEncoder.matches(decryptPassword, userDetails.getPassword())) {
            throw new UsernameNotFoundException("用户名或者密码不正确");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, passwordEncoder.encode(password), userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }

    /**
     * @description: 解密密码
     * @author: hgm
     * @date: 2021/2/3 13:53
     * @param password:
     * @return: java.lang.String
     */
    public String getDecryptPassword(String password, String uuid) throws Exception {
        String key = RSAEncryptUtil.KEYPAIR + ":" + uuid;

        JSONObject jsonObject = (JSONObject) JSONArray.parse(RedisUtil.getValue(key));

        KeyPair keyPair = null;
        if(null != jsonObject){
            keyPair = new KeyPair(jsonObject.getObject("public", RSAPublicKey.class), jsonObject.getObject("private", RSAPrivateKey.class));
        }

        return RSAEncryptUtil.decrypt(password, keyPair);
    }
}
