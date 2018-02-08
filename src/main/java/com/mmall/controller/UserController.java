package com.mmall.controller;

import com.mmall.model.SysUser;
import com.mmall.service.SysUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: XBlue
 * @Date: Create in 2018/1/3114:10
 * @Description:
 * @Modified By:
 */
@Controller
public class UserController {
    @Resource
    private SysUserService sysUserService;


    @RequestMapping("/logout.page")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getSession().invalidate();
        String path = "/signin.jsp";
        response.sendRedirect(path);
    }

    @RequestMapping("/login.page")
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        SysUser sysUser = sysUserService.findByKeyWord(username);
        String errorMsg = "";
        String ret = request.getParameter("ret");//重新跳回上次无权的页面
        if(StringUtils.isBlank(username)){
            errorMsg = "用户名不能为空";
        }else if(StringUtils.isBlank(password)){
            errorMsg = "密码不能为空";
        }else if(sysUser == null){
            errorMsg = "用户不存在";
        }else if(!sysUser.getPassword().equals(MD5Util.encrypt(password))){
            errorMsg = "用户名或密码错误";
        }else if(sysUser.getStatus()!=1){
            errorMsg = "该账户已被冻结";
        }else{
            request.getSession().setAttribute("user",sysUser);
            if(!StringUtils.isBlank(ret)){
                response.sendRedirect(ret);
            }else{
                response.sendRedirect("/admin/index.page");
            }
            return;
        }
        request.setAttribute("error",errorMsg);
        request.setAttribute("username",username);
        if(!StringUtils.isBlank(ret)){
            request.setAttribute("ret",ret);
        }
        String path = "/signin.jsp";
        request.getRequestDispatcher(path).forward(request,response);
    }

}