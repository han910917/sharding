package com.han.user.filter;

import com.han.user.handler.LoginFailureHandler;
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

    //校验规则
    private void validate(HttpServletRequest request) throws ServletRequestBindingException {

        HttpSession session = request.getSession();

        String checkCode = request.getParameter("code");
        if(StringUtils.isEmpty(checkCode)){
            throw new SessionAuthenticationException("验证码不能为空");
        }

        // 获取session池中的验证码谜底,session中不存在的情况
        String checkCode_session = (String) session.getAttribute("code");
        if(Objects.isNull(checkCode_session)) {
            throw new SessionAuthenticationException("验证码不存在");
        }

        // 请求验证码校验
        if(!StringUtils.equalsIgnoreCase(checkCode_session, checkCode)) {
            throw new SessionAuthenticationException("验证码不匹配");
        }
    }
}
