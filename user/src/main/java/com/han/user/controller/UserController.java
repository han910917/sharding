package com.han.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

    @RequestMapping("/index")
    @ResponseBody
    public String hello() {
        //这边我们,默认是返到templates下的login.html
        return "index";
    }

    @RequestMapping("/login")
    public String login() {
        //这边我们,默认是返到templates下的login.html
        return "login";
    }
}
