package com.han.user.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletRequestUtil {

    private static ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

    public static HttpServletRequest getRequest(){
        return requestAttributes.getRequest();
    }

    public static HttpServletResponse getResponse(){
        return requestAttributes.getResponse();
    }
}
