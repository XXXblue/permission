package com.mmall.controller;

import com.mmall.common.ApplicationContextHelper;
import com.mmall.common.JsonData;
import com.mmall.dao.SysAclModuleMapper;
import com.mmall.exception.ParamException;
import com.mmall.exception.PermissionException;
import com.mmall.model.SysAclModule;
import com.mmall.param.TestVo;
import com.mmall.util.BeanValidator;
import com.mmall.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: XBlue
 * @Date: Create in 2018/1/2720:49
 * @Description:
 * @Modified By:
 */
@Controller
@RequestMapping("/test")
@Slf4j
public class TestController {
    //    全局异常处理器的校验
    @RequestMapping("/hello.json")
    @ResponseBody
    public JsonData hello() {
        log.info("hello");
        throw new PermissionException("test exception");
//        return JsonData.success("hello,permission");
    }

    //全局校验器的校验
    @RequestMapping("/validate.json")
    @ResponseBody
    public JsonData validate(TestVo testVo) throws ParamException{
        log.info("validate");
        //    交验完后如果集合为空不抛出异常，否则抛出异常给全局异常处理器处理
        BeanValidator.check(testVo);
        SysAclModuleMapper moduleMapper= ApplicationContextHelper.popBean(SysAclModuleMapper.class);
        SysAclModule sysAclModule=moduleMapper.selectByPrimaryKey(1);
        log.info(JsonMapper.obj2String(sysAclModule));
        return JsonData.success("test validate");
    }
}
