package com.example.companys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    //返回前端页面
    @RequestMapping("/index")
    public String index(){
            return "index";
        }
}

