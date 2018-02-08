package com.mmall.controller;

import ch.qos.logback.classic.pattern.SyslogStartConverter;
import com.mmall.beans.PageQuery;
import com.mmall.common.JsonData;
import com.mmall.param.SearchLogParam;
import com.mmall.service.SysLogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * @Author: XBlue
 * @Date: Create in 2018/2/814:01
 * @Description:
 * @Modified By:
 */
@Controller
@RequestMapping("/sys/log")
public class SysLogController {
    @Resource
    private SysLogService sysLogService;

    @RequestMapping("/recover.json")
    @ResponseBody
    public JsonData recover(@RequestParam("id")int id){
        sysLogService.recover(id);
        return JsonData.success();
    }

    @RequestMapping("/log.page")
    public ModelAndView page(){
        return new ModelAndView("log");
    }


    @RequestMapping("/page.json")
    @ResponseBody
    public JsonData page(SearchLogParam searchLogParam, PageQuery pageQuery){
        return JsonData.success(sysLogService.searchPageList(searchLogParam,pageQuery));
    }
}
