package com.example.demo.controller;

import com.example.demo.domain.Degree;
import com.example.demo.service.DegreeService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
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
public class DegreeController {
    //请使用以下JSON测试增加功能（id为空）
    //{"description":"id为null的新学位","no":"05","remarks":""}
    //请使用以下JSON测试修改功能
    //{"description":"修改id=1的学位","id":1,"no":"05","remarks":""}

    /**
     * POST, http://49.234.69.137:8080/degree.ctl, 增加学位
     * 增加一个学位对象：将来自前端请求的JSON对象，增加到数据库表中
     * @param request 请求对象
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/degree.ctl",method = RequestMethod.POST)
    protected String post(HttpServletRequest request)
            throws ServletException, IOException {
        //所有的资源都要设置响应字符集
        request.setCharacterEncoding("UTF-8");
        //response.setContentType("text/html;charset = UTF-8");
        //根据request对象，获得代表参数的JSON字串
        String degree_json = JSONUtil.getJSON(request);

        //将JSON字串解析为Degree对象
        Degree degreeToAdd = JSON.parseObject(degree_json, Degree.class);
        //前台没有为id赋值，此处模拟自动生成id。如果Dao能真正完成数据库操作，删除下一行。
        degreeToAdd.setId(4 + (int)(Math.random()*100));
        //设置请求字符集
        request.setCharacterEncoding("UTF-8");

        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //在数据库表中增加Degree对象
        try {
            DegreeService.getInstance().add(degreeToAdd);
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
     * DELETE, http://49.234.69.137:8080/degree.ctl?id=1, 删除id=1的学位
     * 删除一个学位对象：根据来自前端请求的id，删除数据库表中id的对应记录
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/degree.ctl",method = RequestMethod.DELETE)
    protected String delete(@RequestParam(value = "id",required = false) String id_str) throws ServletException, IOException {
        //所有的资源都要设置响应字符集
        //request.setCharacterEncoding("UTF_8");
        //response.setContentType("text/html;charset = UTF-8");
        //读取参数id
        //String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);

        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();

        //到数据库表中删除对应的学位
        try {
            DegreeService.getInstance().delete(id);
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
     * PUT, http://49.234.69.137:8080/degree.ctl, 修改学位
     *
     * 修改一个学位对象：将来自前端请求的JSON对象，更新数据库表中相同id的记录
     * @param request
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/degree.ctl",method = RequestMethod.PUT)
    protected String put(HttpServletRequest request)
            throws ServletException, IOException {
        //response.setContentType("text/html;charset = UTF-8");
        request.setCharacterEncoding("UTF-8");
        String degree_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Degree对象
        Degree degreeToAdd = JSON.parseObject(degree_json, Degree.class);
        //设置请求字符集
        request.setCharacterEncoding("UTF-8");

        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //到数据库表修改Degree对象对应的记录
        try {
            DegreeService.getInstance().update(degreeToAdd);
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
        //return message.toJSONString();
        //响应message到前端
        //response.getWriter().println(message);
    }

    /**
     * GET, http://49.234.69.137:8080/degree.ctl?id=1, 查询id=1的学位
     * GET, http://49.234.69.137:8080/degree.ctl, 查询所有的学位
     * 把一个或所有学位对象响应到前端
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/degree.ctl",method = RequestMethod.GET)
    public String get(@RequestParam(value = "id",required = false) String id_str)
            throws ServletException, IOException {
        //response.setContentType("text/html;charset = UTF-8");
        //读取参数id
        //String id_str = request.getParameter("id");

        //request.setCharacterEncoding("UTF_8");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有学位对象，否则响应id指定的学位对象
            if (id_str == null) {
                return responseDegrees();
            } else {
                int id = Integer.parseInt(id_str);
               return responseDegree(id);
            }
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            //响应message到前端
            //response.getWriter().println(message);
            e.printStackTrace();
            return message.toJSONString();
        } catch (Exception e) {
            message.put("message", "网络异常");
            //响应message到前端
            //response.getWriter().println(message);
            return message.toJSONString();
        }
    }
    //响应一个学位对象
    private String responseDegree(int id)
            throws ServletException, IOException, SQLException {
        //根据id查找学位
        Degree degree = DegreeService.getInstance().find(id);
        String degree_json = JSON.toJSONString(degree);
        return degree_json;

    }
    //响应所有学位对象
    private String responseDegrees()
            throws ServletException, IOException, SQLException {
        //获得所有学位
        Collection<Degree> degrees = DegreeService.getInstance().findAll();
        String degrees_json = JSON.toJSONString(degrees, SerializerFeature.DisableCircularReferenceDetect);
        return degrees_json;

    }
}