package com.mmall.filter;

import com.mmall.common.RequestHolder;
import com.mmall.model.SysUser;
import org.springframework.http.HttpRequest;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: XBlue
 * @Date: Create in 2018/2/120:34
 * @Description:
 * @Modified By:
 */
public class LoginFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse= (HttpServletResponse) servletResponse;

        SysUser user = (SysUser) httpServletRequest.getSession().getAttribute("user");
        if(user==null){
            httpServletResponse.sendRedirect("/signin.jsp");
            return;
        }
        RequestHolder.add(user);
        RequestHolder.add(httpServletRequest);
        filterChain.doFilter(servletRequest,servletResponse);
        return;
    }

    public void destroy() {

    }
}
