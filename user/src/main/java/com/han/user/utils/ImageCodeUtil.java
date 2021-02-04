package com.han.user.utils;

import com.wf.captcha.ArithmeticCaptcha;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class ImageCodeUtil {

    public static final String VALIDATE_CODE = "validate_code";
    public static final String UUID = "validate_code";

    public static Map<String, String> getCode(HttpServletRequest request, HttpServletResponse response){
        String uuid = request.getParameter("uuid");

        ArithmeticCaptcha captcha = new ArithmeticCaptcha(113, 36);

        captcha.setLen(2);

        String result = captcha.text();
        RedisUtil.setStrValue(VALIDATE_CODE + ":" + uuid, result, 3);
        RedisUtil.setStrValue(UUID, uuid, 3);

        Map<String, String> image = new HashMap<>();
        image.put("img", captcha.toBase64());

        return image;
    }
}
