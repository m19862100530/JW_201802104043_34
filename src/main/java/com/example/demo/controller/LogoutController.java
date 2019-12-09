package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
public class LogoutController extends HttpServlet {

    @RequestMapping(value = "/logout.ctl",method = RequestMethod.GET)
    protected void doGet(HttpServletRequest request) throws ServletException, IOException {
        //所有资源都设置响应字符编码为UTF-8
        //response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        if(session != null){
            session.invalidate();
        }
    }
}
