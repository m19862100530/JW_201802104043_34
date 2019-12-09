package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.domain.ProfTitle;
import com.example.demo.service.ProfTitleService;
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
public class ProfTitleController {
    //请使用以下JSON测试增加功能（id为空）
    //{"description":"id为null的新proftitle","no":"05","remarks":""}
    //请使用以下JSON测试修改功能
    //{"description":"修改id=1的proftitle","id":1,"no":"05","remarks":""}

    /**
     * POST, http://49.234.69.137:8080/proftitle.ctl, 增加proftitle
     * 增加一个proftitle对象：将来自前端请求的JSON对象，增加到数据库表中
     * @param request 请求对象
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/proftitle.ctl",method = RequestMethod.POST)
    protected String doPost(HttpServletRequest request)
            throws ServletException, IOException {
        //所有的资源都要设置响应字符集
        //response.setContentType("text/html;charset = UTF-8");
        //根据request对象，获得代表参数的JSON字串
        String proftitle_json = JSONUtil.getJSON(request);

        //将JSON字串解析为ProfTitle对象
        ProfTitle proftitleToAdd = JSON.parseObject(proftitle_json, ProfTitle.class);
        //前台没有为id赋值，此处模拟自动生成id。如果Dao能真正完成数据库操作，删除下一行。
        proftitleToAdd.setId(4 + (int)(Math.random()*100));
        request.setCharacterEncoding("UTF-8");

        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //在数据库表中增加ProfTitle对象
        try {
            ProfTitleService.getInstance().add(proftitleToAdd);
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
     * DELETE, http://49.234.69.137:8080/proftitle.ctl?id=1, 删除id=1的proftitle
     * 删除一个proftitle对象：根据来自前端请求的id，删除数据库表中id的对应记录
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/proftitle.ctl",method = RequestMethod.DELETE)
    protected String doDelete(@RequestParam(value = "id",required = false) String id_str) throws ServletException, IOException {
        //所有的资源都要设置响应字符集
        //response.setContentType("text/html;charset = UTF-8");
        //读取参数id
        //String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);

        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();

        //到数据库表中删除对应的proftitle
        try {
            ProfTitleService.getInstance().delete(id);
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
     * PUT, http://49.234.69.137:8080/proftitle.ctl, 修改proftitle
     *
     * 修改一个proftitle对象：将来自前端请求的JSON对象，更新数据库表中相同id的记录
     * @param request
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/proftitle.ctl",method = RequestMethod.PUT)
    protected String doPut(HttpServletRequest request)
            throws ServletException, IOException {
        //所有的资源都要设置响应字符集
        //response.setContentType("text/html;charset = UTF-8");
        String proftitle_json = JSONUtil.getJSON(request);
        //将JSON字串解析为ProfTitle对象
        ProfTitle proftitleToAdd = JSON.parseObject(proftitle_json, ProfTitle.class);
        request.setCharacterEncoding("UTF-8");

        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //到数据库表修改ProfTitle对象对应的记录
        try {
            ProfTitleService.getInstance().update(proftitleToAdd);
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
     * GET, http://49.234.69.137:8080/proftitle.ctl?id=1, 查询id=1的proftitle
     * GET, http://49.234.69.137:8080/proftitle.ctl, 查询所有的proftitle
     * 把一个或所有proftitle对象响应到前端
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/proftitle.ctl",method = RequestMethod.GET)
    protected String doGet(@RequestParam(value = "id",required = false) String id_str)
            throws ServletException, IOException {
        //所有的资源都要设置响应字符集
        //response.setContentType("text/html;charset = UTF-8");
        //读取参数id
        //String id_str = request.getParameter("id");

        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有proftitle对象，否则响应id指定的proftitle对象
            if (id_str == null) {
                return responseProfTitles();
            } else {
                int id = Integer.parseInt(id_str);
                return responseProfTitle(id);
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
        //response.getWriter().println(message);
        //return message.toJSONString();
    }
    //响应一个proftitle对象
    private String  responseProfTitle(int id)
            throws ServletException, IOException, SQLException {
        //根据id查找proftitle
        ProfTitle proftitle = ProfTitleService.getInstance().find(id);
        String proftitle_json = JSON.toJSONString(proftitle);

        //响应proftitle_json到前端response.getWriter().println(proftitle_json);
        return proftitle_json;
    }
    //响应所有proftitle对象
    private String responseProfTitles()
            throws ServletException, IOException, SQLException {
        //获得所有proftitle
        Collection<ProfTitle> proftitles = ProfTitleService.getInstance().findAll();
        String proftitles_json = JSON.toJSONString(proftitles, SerializerFeature.DisableCircularReferenceDetect);

        //响应proftitles_json到前端
        //response.getWriter().println(proftitles_json);
        return proftitles_json;
    }
}