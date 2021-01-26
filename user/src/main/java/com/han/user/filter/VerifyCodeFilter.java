package com.han.user.filter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class VerifyCodeFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(StringUtils.equals("/login", request.getRequestURI()) &&
                StringUtils.equalsIgnoreCase("post", request.getMethod())){

            String inCode = request.getParameter("code");
            if(StringUtils.isBlank(inCode)){
                throw new NullPointerException("验证码不存在");
            }

            String outCode = (String) request.getSession().getAttribute("code");
            if(StringUtils.isBlank(outCode)){
                throw new RuntimeException("验证码过期");
            }
            outCode = outCode.equals("0.0") ? "0" : outCode;

            if(!StringUtils.equals(inCode, outCode)){
                throw new RuntimeException("验证码不正确");
            }
        }

        filterChain.doFilter(request, response);
    }
}
