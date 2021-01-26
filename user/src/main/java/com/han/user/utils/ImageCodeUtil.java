package com.han.user.utils;

import com.wf.captcha.ArithmeticCaptcha;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class ImageCodeUtil {

    public static Map<String, String> getCode(HttpServletRequest request, HttpServletResponse response){
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(113, 36);

        captcha.setLen(2);

        String result = captcha.text();
        request.getSession().setAttribute("code", result);

        Map<String, String> image = new HashMap<>();
        image.put("img", captcha.toBase64());

        return image;
    }
}
