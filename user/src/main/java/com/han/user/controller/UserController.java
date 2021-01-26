package com.han.user.controller;

import com.han.user.utils.ImageCodeUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class UserController {

    @RequestMapping("/index")
    @ResponseBody
    public String hello() {
        return "index";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/getCode")
    @ResponseBody
    public Map<String, String> getCode(HttpServletRequest request, HttpServletResponse response){
        return ImageCodeUtil.getCode(request, response);
    }
}
