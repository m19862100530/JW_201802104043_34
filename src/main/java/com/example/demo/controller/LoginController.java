package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.domain.User;
import com.example.demo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@RestController
public class LoginController{
    @RequestMapping(value = "/login.ctl",method = RequestMethod.GET)
    protected String get(@RequestParam(value = "username",required = false) String username,
                         @RequestParam(value = "password",required = false) String password,HttpServletRequest request) throws ServletException, IOException {
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //在数据库表中增加Department对象
        try {
            User loggedUser = UserService.getInstance().login(username, password);
            if (loggedUser != null) {
                message.put("message", "login successful");
               //返回当前请求关联的Session对象
                HttpSession session = request.getSession();
                //十分中没有操作则失效
                session.setMaxInactiveInterval(10 * 60);
                //将user对象写入session的属性，名称为currentUser，以便在其他请求中使用
                session.setAttribute("currentUser", loggedUser);
                return message.toJSONString();

                //response.getWriter().println(message);
                //重定向到索引页（菜单也）
                //return;
            }
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            e.printStackTrace();
            return message.toJSONString();
        } catch (Exception e) {
            message.put("message", "网络异常");
            e.printStackTrace();
            return message.toJSONString();
        }
        return message.toJSONString();
    }
}

