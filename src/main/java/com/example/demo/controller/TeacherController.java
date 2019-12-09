package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.domain.Teacher;
import com.example.demo.domain.User;
import com.example.demo.service.TeacherService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

/**
 * 将所有方法组织在一个Controller(Servlet)中
 */
@RestController
public class TeacherController {
    //请使用以下JSON测试增加功能（id为空）
    //{"description":"id为null的新老师","no":"05","remarks":""}
    //请使用以下JSON测试修改功能
    //{"description":"修改id=1的老师","id":1,"no":"05","remarks":""}

    /**
     * POST, http://49.234.69.137:8080/teacher.ctl, 增加老师
     * 增加一个老师对象：将来自前端请求的JSON对象，增加到数据库表中
     * @param request 请求对象
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/teacher.ctl",method = RequestMethod.POST)
    protected String doPost(HttpServletRequest request)
            throws ServletException, IOException {
        //所有的资源都要设置响应字符集
        //response.setContentType("text/html;charset = UTF-8");
        //根据request对象，获得代表参数的JSON字串
        String teacher_json = JSONUtil.getJSON(request);
        String user_json = JSONUtil.getJSON(request);

        //将JSON字串解析为Teacher对象
        Teacher teacherToAdd = JSON.parseObject(teacher_json, Teacher.class);
        User userToAdd = JSON.parseObject(user_json, User.class);
        //前台没有为id赋值，此处模拟自动生成id。如果Dao能真正完成数据库操作，删除下一行。
        //teacherToAdd.setId(4 + (int)(Math.random()*100));
        request.setCharacterEncoding("UTF-8");

        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //在数据库表中增加Teacher对象
        try {
            TeacherService.getInstance().add(teacherToAdd);
            message.put("message", "增加成功");
            return message.toJSONString();
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
            e.printStackTrace();
            return message.toJSONString();
        }catch(Exception e){
            message.put("message", "网络异常");
            e.printStackTrace();
            return message.toJSONString();
        }
        //响应message到前端
        //response.getWriter().println(message);
    }

    /**
     * DELETE, http://49.234.69.137:8080/teacher.ctl?id=1, 删除id=1的老师
     * 删除一个老师对象：根据来自前端请求的id，删除数据库表中id的对应记录
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/teacher.ctl",method = RequestMethod.DELETE)
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
            TeacherService.getInstance().delete(id);
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


    /**
     * PUT, http://49.234.69.137:8080/teacher.ctl, 修改老师
     *
     * 修改一个老师对象：将来自前端请求的JSON对象，更新数据库表中相同id的记录
     * @param request
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/teacher.ctl",method = RequestMethod.PUT)
    protected String doPut(HttpServletRequest request)
            throws ServletException, IOException {
        //所有的资源都要设置响应字符集
        //response.setContentType("text/html;charset = UTF-8");
        String teacher_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Teacher对象
        Teacher teacherToAdd = JSON.parseObject(teacher_json, Teacher.class);
        request.setCharacterEncoding("UTF_8");

        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //到数据库表修改Teacher对象对应的记录
        try {
            TeacherService.getInstance().update(teacherToAdd);
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

    /**
     * GET, http://49.234.69.137:8080/teacher.ctl?id=1, 查询id=1的老师
     * GET, http://49.234.69.137:8080/teacher.ctl, 查询所有的老师
     * 把一个或所有老师对象响应到前端
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/teacher.ctl",method = RequestMethod.GET)
    @ResponseBody
    protected String doGet(@RequestParam(value = "id",required = false) String id_str)
            throws ServletException, IOException {
        //所有的资源都要设置响应字符集
        //response.setContentType("text/html;charset = UTF-8");
        //读取参数id
        //String id_str = request.getParameter("id");

        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有老师对象，否则响应id指定的老师对象
            if (id_str == null) {
                return responseTeachers();
            } else {
                int id = Integer.parseInt(id_str);
                return responseTeacher(id);
            }
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
            //响应message到前端
            //response.getWriter().println(message);
            e.printStackTrace();
            return message.toJSONString();
        }catch(Exception e){
            message.put("message", "网络异常");
            return message.toJSONString();
            //响应message到前端
            //response.getWriter().println(message);
        }
    }
    //响应一个老师对象
    private String responseTeacher(int id)
            throws ServletException, IOException, SQLException {
        //根据id查找老师
        Teacher teacher = TeacherService.getInstance().find(id);
        String teacher_json = JSON.toJSONString(teacher);
        return teacher_json;

        //响应teacher_json到前端
        //response.getWriter().println(teacher_json);
    }
    //响应所有老师对象
    private String responseTeachers()
            throws ServletException, IOException, SQLException {
        //获得所有老师
        Collection<Teacher> teachers = TeacherService.getInstance().findAll();
        String teachers_json = JSON.toJSONString(teachers, SerializerFeature.DisableCircularReferenceDetect);

        return teachers_json;
        //响应teachers_json到前端
        //response.getWriter().println(teachers_json);
    }
}
