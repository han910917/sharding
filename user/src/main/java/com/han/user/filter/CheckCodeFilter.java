package com.han.user.filter;

import com.han.user.handler.LoginFailureHandler;
import com.han.user.utils.ImageCodeUtil;
import com.han.user.utils.RSAEncryptUtil;
import com.han.user.utils.RedisUtil;
import com.han.user.utils.ServletRequestUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.KeyPair;
import java.util.Objects;

@Component
public class CheckCodeFilter extends OncePerRequestFilter {

    @Resource
    private LoginFailureHandler loginFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(StringUtils.equals("/login",request.getRequestURI())
                && StringUtils.equalsIgnoreCase(request.getMethod(),"post")){

            try{
                //验证谜底与用户输入是否匹配
                validate(request);
            }catch(AuthenticationException e){
                loginFailureHandler.onAuthenticationFailure(request, response, e);
                return; //产生异常就不执行后面的过滤器链
            }
        }
        filterChain.doFilter(request,response);
    }

    /**
    * @description: 验证验证码和解密密码
    * @author: hgm
    * @date: 2021/2/3 14:06
    * @param request: 
    * @return: void
    */
    private void validate(HttpServletRequest request){

        String checkCode = request.getParameter("code");
        if(StringUtils.isEmpty(checkCode)){
            throw new SessionAuthenticationException("验证码不能为空");
        }

        String checkCode_redis = (String) RedisUtil.getValue(ImageCodeUtil.VALIDATE_CODE + ":" + request.getParameter("uuid"));

        if(Objects.isNull(checkCode_redis)) {
            throw new SessionAuthenticationException("验证码不存在");
        }

        // 请求验证码校验
        if(!StringUtils.equalsIgnoreCase(checkCode_redis, checkCode)) {
            throw new SessionAuthenticationException("验证码不匹配");
        }
    }
}
