package com.example.demo.filter;

import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
@Order(1)
@WebFilter(filterName = "Filter 0",urlPatterns = {"/*"})//所有资源进行过滤
public class Filter00_Encoding implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        //获得资源path
        String path = request.getRequestURI();
        //如果没有字串“/login”，则设置字符编码
            System.out.println("set response");
            //所有的资源都要设置响应字符集
            //request.setCharacterEncoding("UTF_8");
            response.setContentType("text/html;charset = UTF-8");
            //获得请求的方法
            String method = request.getMethod();
            //如果方法是post或put
            if ("POST-PUT".contains(method)) {
                //设置请求字符集
                request.setCharacterEncoding("UTF-8");
                //response.setContentType("text/html;charset = utf8");
            }
        System.out.println("Filter 0 - encoding begins");
        System.out.println("path：" + path + "@"+new Date());
        filterChain.doFilter(servletRequest,servletResponse);//执行其他过滤器，如过滤器已执行完毕，则执行原请求
        System.out.println("Filter 0 - encoding ends");
    }
    @Override
    public void destroy() {

    }
}
