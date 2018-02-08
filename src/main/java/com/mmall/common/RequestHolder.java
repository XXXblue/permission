package com.mmall.common;

import com.mmall.model.SysUser;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: XBlue
 * @Date: Create in 2018/2/120:14
 * @Description:
 * @Modified By:
 */

public class RequestHolder {
    private static final ThreadLocal<SysUser> userHolder = new ThreadLocal<SysUser>();

    private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<HttpServletRequest>();

    public static void add(SysUser sysUser){
        userHolder.set(sysUser);
    }
    public static void add(HttpServletRequest request){
        requestHolder.set(request);
    }

    public static SysUser getCurrentUser(){
        return userHolder.get();
    }

    public static HttpServletRequest getCurrentHttpServletRequest(){
        return  requestHolder.get();
    }

    public static void remove(){
        userHolder.remove();
        requestHolder.remove();
    }
}