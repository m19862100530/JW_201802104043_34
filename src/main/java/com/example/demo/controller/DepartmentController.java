package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.domain.Department;
import com.example.demo.service.DepartmentService;
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
public class DepartmentController {
    //请使用以下JSON测试增加功能（id为空）
    //{"description":"id为null的新系别","no":"05","remarks":""}
    //请使用以下JSON测试修改功能
    //{"description":"修改id=1的系别","id":1,"no":"05","remarks":""}

    /**
     * POST, http://49.234.69.137:8080/department.ctl, 增加系别
     * 增加一个系别对象：将来自前端请求的JSON对象，增加到数据库表中
     * @param request 请求对象
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/department.ctl",method = RequestMethod.POST)
    protected String doPost(HttpServletRequest request)
            throws ServletException, IOException {
        //所有的资源都要设置响应字符集
        //response.setContentType("text/html;charset = UTF-8");
        //根据request对象，获得代表参数的JSON字串
        String department_json = JSONUtil.getJSON(request);

        //将JSON字串解析为Department对象
        Department departmentToAdd = JSON.parseObject(department_json, Department.class);
        //前台没有为id赋值，此处模拟自动生成id。如果Dao能真正完成数据库操作，删除下一行。
        //departmentToAdd.setId(4 + (int)(Math.random()*100));
        //request.setCharacterEncoding("UTF-8");

        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //在数据库表中增加Department对象
        try {
            DepartmentService.getInstance().add(departmentToAdd);
            message.put("message", "增加成功");
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
     * DELETE, http://49.234.69.137:8080/department.ctl?id=1, 删除id=1的系别
     * 删除一个系别对象：根据来自前端请求的id，删除数据库表中id的对应记录
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/department.ctl",method = RequestMethod.DELETE)
    protected String doDelete(@RequestParam(value = "id",required = false) String id_str) throws ServletException, IOException {
        //所有的资源都要设置响应字符集
        //response.setContentType("text/html;charset = UTF-8");
        //读取参数id
        //String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);

        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //到数据库表中删除对应的系别
        try {
            DepartmentService.getInstance().delete(id);
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
     * PUT, http://49.234.69.137:8080/department.ctl, 修改系别
     *
     * 修改一个系别对象：将来自前端请求的JSON对象，更新数据库表中相同id的记录
     * @param request
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/department.ctl",method = RequestMethod.PUT)
    protected String put(HttpServletRequest request)
            throws ServletException, IOException {
        //所有的资源都要设置响应字符集
        //response.setContentType("text/html;charset = UTF-8");
        String department_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Department对象
        Department departmentToAdd = JSON.parseObject(department_json, Department.class);
        //request.setCharacterEncoding("UTF_8");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //到数据库表修改Department对象对应的记录
        try {
            DepartmentService.getInstance().update(departmentToAdd);
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
     * GET, http://49.234.69.137:8080/department.ctl?id=1, 查询id=1的系别
     * GET, http://49.234.69.137:8080/department.ctl, 查询所有的系别
     * 把一个或所有系别对象响应到前端
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/department.ctl",method = RequestMethod.GET)
    protected String doGet(@RequestParam(value="id",required = false)String id_str,
                           @RequestParam(value="type",required = false)String type)
            throws ServletException, IOException {
        //所有的资源都要设置响应字符集
        //response.setContentType("text/html;charset = UTF-8");
        //读取参数id
        //String id_str = request.getParameter("id");
        //String schoolId_str = request.getParameter("paraType");

        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            if (type == null) {
                //如果id = null, 表示响应所有系别对象，否则响应id指定的系别对象
                if (id_str == null) {
                    return responseDepartments();
                } else {
                    int id = Integer.parseInt(id_str);
                    return responseDepartment(id);
                }
            } else if (type.equals("school")) {
                int schoolId = Integer.parseInt(id_str);
                return responseDepartmentFromSchool(schoolId);
            }
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            //响应message到前端
            //response.getWriter().println(message);
            e.printStackTrace();
            return message.toJSONString();
        } catch (Exception e) {
            message.put("message", "网络异常");
            return message.toJSONString();
            //响应message到前端
            //response.getWriter().println(message);
        }
        return message.toJSONString();
    }
    //响应拥有schooId系别对象
    private String responseDepartmentFromSchool(int schoolId)
            throws ServletException, IOException, SQLException {
        //根据school的id查找所属的系
        Collection<Department> departments = DepartmentService.getInstance().findAllBySchool(schoolId);
        //将对象转为json字符串
        String department_json = JSON.toJSONString(departments, SerializerFeature.DisableCircularReferenceDetect);
        return department_json;
    }
    //响应一个系别对象
    private String responseDepartment(int id)
            throws ServletException, IOException, SQLException {
        //根据id查找系别
        Department department = DepartmentService.getInstance().find(id);
        String department_json = JSON.toJSONString(department);
        return department_json;

    }
    //响应所有系别对象
    private String responseDepartments()
            throws ServletException, IOException, SQLException {
        //获得所有系别
        Collection<Department> departments = DepartmentService.getInstance().findAll();
        String departments_json = JSON.toJSONString(departments, SerializerFeature.DisableCircularReferenceDetect);
        return departments_json;

    }
}
