package com.mmall.controller;

import com.mmall.beans.PageQuery;
import com.mmall.common.JsonData;
import com.mmall.param.AclMoudleParam;
import com.mmall.param.AclParam;
import com.mmall.service.SysAclService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * @Author: XBlue
 * @Date: Create in 2018/2/122:23
 * @Description:
 * @Modified By:
 */
@Controller
@RequestMapping("/sys/acl")
@Slf4j
public class SysAclController {
    @Resource
    private SysAclService sysAclService;

    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveAclModule(AclParam param) {
        sysAclService.save(param);
        return JsonData.success();
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateAclModule(AclParam param) {
        sysAclService.update(param);
        return JsonData.success();
    }

    @RequestMapping("/page.json")
    @ResponseBody
    public JsonData list(@RequestParam("aclModuleId") Integer aclModuleId, PageQuery pageQuery) {
        return JsonData.success(sysAclService.getPageByAclModuleId(aclModuleId, pageQuery));
    }


}
