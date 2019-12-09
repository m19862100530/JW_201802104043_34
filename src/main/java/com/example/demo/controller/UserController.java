package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.domain.User;
import com.example.demo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@RestController
public class UserController{
    @RequestMapping(value = "/user.ctl",method = RequestMethod.GET)
    protected String get(@RequestParam(value = "id",required = false) String id_str,String username)
            throws ServletException, IOException {
        //所有的资源都要设置响应字符集
        //response.setContentType("text/html;charset = UTF-8");
        //读取参数id
        //String id_str = request.getParameter("id");
        //String username = request.getParameter("username");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id值不为空，调用根据id值响应一个用户对象方法，否则，调用根据用户名响应一个用户对象的方法
            if (id_str != null){
                //强制类型转换成int型
                int id = Integer.parseInt(id_str);
                return this.responseUser(id);
            }else if (username != null){
                return this.responseUserByUsername(username);
            }else{
                return responseUsers();
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
        //响应message到前端
        //response.getWriter().println(message);
    }

    @RequestMapping(value = "/user.ctl",method = RequestMethod.PUT)
    protected String doPut(HttpServletRequest request) throws ServletException, IOException {
        String user_json = JSONUtil.getJSON(request);
        //所有的资源都要设置响应字符集
        //response.setContentType("text/html;charset = UTF-8");
        //将JSON字串解析为Department对象
        User passwordTochange = JSON.parseObject(user_json, User.class);
        request.setCharacterEncoding("UTF-8");

        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //到数据库表修改Department对象对应的记录
        try {
            UserService.getInstance().change(passwordTochange);
            message.put("message", "修改成功");
            return message.toJSONString();
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
            e.printStackTrace();
            return message.toJSONString();
        }catch(Exception e){
            message.put("message", "网络异常");
            return message.toJSONString();
        }
        //响应message到前端
        //response.getWriter().println(message);
    }
    @RequestMapping(value = "/user.ctl",method = RequestMethod.DELETE)
    protected String doDelete(@RequestParam(value = "id",required = false) String id_str) throws ServletException, IOException {
        //所有的资源都要设置响应字符集
        //response.setContentType("text/html;charset = UTF-8");
        //读取参数id
        //String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);

        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();

        //到数据库表中删除对应的老师
        try {
            UserService.getInstance().deleteUser(id);
            message.put("message", "删除成功");
            return message.toJSONString();
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
            e.printStackTrace();
            return message.toJSONString();
        }catch(Exception e){
            message.put("message", "网络异常");
            return message.toJSONString();
        }
        //响应message到前端
        //response.getWriter().println(message);
    }

    private String responseUserByUsername(String username)
            throws ServletException, IOException, SQLException {
        //根据school的USERNAME查找所属的系
        User users = UserService.getInstance().findByUsername(username);
        //将对象转为json字符串
        String user_json = JSON.toJSONString(users);
        return user_json;
        //响应message到前端
        //response.getWriter().println(user_json);
    }
    //响应一个user对象
    private String  responseUser(int id)
            throws ServletException, IOException, SQLException {
        //根据id查找user
        User user = UserService.getInstance().find(id);
        String user_json = JSON.toJSONString(user);
        return user_json;

        //响应USER_json到前端
        //response.getWriter().println(user_json);
    }
    //响应所有user对象
    private String responseUsers()
            throws ServletException, IOException, SQLException {
        //获得所有user
        Collection<User> users = UserService.getInstance().findAll();
        String users_json = JSON.toJSONString(users, SerializerFeature.DisableCircularReferenceDetect);
        return users_json;

        //响应departments_json到前端
        //response.getWriter().println(users_json);
    }
}
