package com.han.user.controller;

import com.google.common.collect.Maps;
import com.han.user.utils.ImageCodeUtil;
import com.han.user.utils.RSAEncryptUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Controller
public class UserController {

    /**
    * @description: 默认登录页面
    * @author: hgm
    * @date: 2021/2/2 14:33
    * @param : 
    * @return: java.lang.String
    */
    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    /**
    * @description: 获取验证码
    * @author: hgm
    * @date: 2021/2/2 14:33
    * @param request: 
     * @param response: 
    * @return: java.util.Map<java.lang.String,java.lang.String>
    */
    @RequestMapping("/getCode")
    @ResponseBody
    public Map<String, String> getCode(HttpServletRequest request, HttpServletResponse response){
        return ImageCodeUtil.getCode(request, response);
    }

    /**
     * @description: 获取公私
     * @author: hgm
     * @date: 2021/2/2 14:33
     * @param request:
     * @param response:
     * @return: java.util.Map<java.lang.String,java.lang.String>
     */
    @RequestMapping("/getPublicKey")
    @ResponseBody
    public Map<String, Object> getPublicKey(HttpServletRequest request) throws NoSuchAlgorithmException {
        Map<String, Object> map = RSAEncryptUtil.genKeyPair(request.getParameter("uuid"));
        Map<String, Object> result = Maps.newHashMap();
        result.put("key", map.get("publicKey"));
        return result;
    }
}
