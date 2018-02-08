package com.mmall.filter;


import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import com.mmall.common.ApplicationContextHelper;
import com.mmall.common.JsonData;
import com.mmall.common.RequestHolder;
import com.mmall.model.SysUser;
import com.mmall.service.SysCoreService;
import com.mmall.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @Author: XBlue
 * @Date: Create in 2018/2/621:56
 * @Description:
 * @Modified By:
 */
@Slf4j
public class AclControlFilter implements Filter {
    //防止为空
    private static Set<String> exclusionUrlSet = new ConcurrentSkipListSet<String>();

    private final static String noAuthUrl = "/sys/user/noAuth.page";

    public void init(FilterConfig filterConfig) throws ServletException {
        //先处理排出在外的
        String exclusionUrls = filterConfig.getInitParameter("exclusionUrls");
        List<String> list = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(exclusionUrls);
        //每个线程里面都有自己的set
        exclusionUrlSet = Sets.newConcurrentHashSet(list);
        exclusionUrlSet.add(noAuthUrl);
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //获取当前访问的请求
        String servletPath = request.getServletPath();
        //放行公页
        if (exclusionUrlSet.contains(servletPath)) {
            //直接走过滤链，无需拦截
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        SysUser sysUser = RequestHolder.getCurrentUser();
        Map map = request.getParameterMap();
        if (sysUser == null) {
            noAuth(response, request);
            //记录访问页面 在没登录时 防止黑客入侵
            log.info("someone visit {},but no login,parameter:{}", servletPath, JsonMapper.obj2String(map));
            return;
        }
        //之前那些@Controller 那些个都是给spring容器管理的，@Resource随便取出内容，但是这里不行，这时就要用这个工具类取出bean
        SysCoreService sysCoreService = ApplicationContextHelper.popBean(SysCoreService.class);
        if (!sysCoreService.hasUrlAcl(servletPath)) {
            noAuth(response, request);
            log.info("{} visit {},parameter:{}", JsonMapper.obj2String(sysUser), servletPath, JsonMapper.obj2String(map));
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
        return;
    }

    private void noAuth(HttpServletResponse response, HttpServletRequest request) throws IOException{
        //这一块很好的学习了 如果不是在Controller如何处理返回
        String servletPath=request.getServletPath();
        if(servletPath.endsWith(".json")){
            response.setHeader("Content-Type","application/json");
            response.getWriter().print(JsonMapper.obj2String(JsonData.fail("没有访问权限")));
            return;
        }
        if(servletPath.endsWith(".page")){
            clientRedirect(noAuthUrl, response);
            return;
        }
    }

    private void clientRedirect(String url, HttpServletResponse response) throws IOException{
        response.setHeader("Content-Type", "text/html");
        response.getWriter().print("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" + "<head>\n" + "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"/>\n"
                + "<title>跳转中...</title>\n" + "</head>\n" + "<body>\n" + "跳转中，请稍候...\n" + "<script type=\"text/javascript\">//<![CDATA[\n"
                + "window.location.href='" + url + "?ret='+encodeURIComponent(window.location.href);\n" + "//]]></script>\n" + "</body>\n" + "</html>\n");
    }

    public void destroy() {

    }
}
