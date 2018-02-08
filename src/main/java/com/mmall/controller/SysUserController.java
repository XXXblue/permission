package com.mmall.controller;

import com.alibaba.druid.sql.PagerUtils;
import com.mmall.beans.PageQuery;
import com.mmall.beans.PageResult;
import com.mmall.common.JsonData;
import com.mmall.model.SysUser;
import com.mmall.param.DeptParam;
import com.mmall.param.UserParam;
import com.mmall.service.SysUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * @Author: XBlue
 * @Date: Create in 2018/1/3022:49
 * @Description:
 * @Modified By:
 */
@Controller
@RequestMapping("/sys/user")
public class SysUserController {
    @Resource
    private SysUserService sysUserService;

    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveUser(UserParam userParam){
        sysUserService.save(userParam);
        return JsonData.success();
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateUser(UserParam userParam){
        sysUserService.update(userParam);
        return JsonData.success();
    }

    @RequestMapping("/page.json")
    @ResponseBody
    public JsonData page(@RequestParam("deptId")Integer deptId, PageQuery pageQuery){
        PageResult<SysUser> result=sysUserService.getPageByDeptId(deptId,pageQuery);
        return JsonData.success(result);
    }

    @RequestMapping("/noAuth.page")
    @ResponseBody
    public ModelAndView noAuth(){
        return new ModelAndView("noAuth");
    }
}
