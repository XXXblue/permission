package com.mmall.service;

import com.google.common.base.Preconditions;
import com.mmall.beans.PageQuery;
import com.mmall.beans.PageResult;
import com.mmall.common.RequestHolder;
import com.mmall.dao.SysAclMapper;
import com.mmall.exception.ParamException;
import com.mmall.model.SysAcl;
import com.mmall.param.AclParam;
import com.mmall.util.BeanValidator;
import com.mmall.util.IpUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author: XBlue
 * @Date: Create in 2018/2/222:25
 * @Description:
 * @Modified By:
 */
@Service
public class SysAclService {
    @Resource
    private SysAclMapper sysAclMapper;
    @Resource
    private SysLogService sysLogService;
    public void save(AclParam aclParam){
        BeanValidator.check(aclParam);
        if(checkExist(aclParam.getAclModuleId(),aclParam.getName(),aclParam.getId())){
            throw new ParamException("同一层级下权限名称相同");
        }
        SysAcl newSysAcl= SysAcl.builder().name(aclParam.getName()).aclModuleId(aclParam.getAclModuleId()).remark(aclParam.getRemark())
                .url(aclParam.getUrl()).type(aclParam.getType()).status(aclParam.getStatus()).seq(aclParam.getSeq()).build();
        newSysAcl.setCode(gengerateCode());
        newSysAcl.setOperator(RequestHolder.getCurrentUser().getUsername()+"-system");//TODO：
        newSysAcl.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentHttpServletRequest()));
        newSysAcl.setOperateTime(new Date());
        sysAclMapper.insertSelective(newSysAcl);
        sysLogService.saveAclLog(null,newSysAcl);
    }

    public void update(AclParam aclParam){
        BeanValidator.check(aclParam);
        if(checkExist(aclParam.getAclModuleId(),aclParam.getName(),aclParam.getId())){
            throw new ParamException("同一层级下权限名称相同");
        }
        SysAcl before=sysAclMapper.selectByPrimaryKey(aclParam.getId());
        Preconditions.checkNotNull(before,"无此权限信息");
        SysAcl after= SysAcl.builder().id(aclParam.getId()).name(aclParam.getName()).aclModuleId(aclParam.getAclModuleId()).remark(aclParam.getRemark())
                .url(aclParam.getUrl()).type(aclParam.getType()).status(aclParam.getStatus()).seq(aclParam.getSeq()).build();
        after.setOperator(RequestHolder.getCurrentUser().getUsername()+"-system-update");//TODO：
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentHttpServletRequest()));
        after.setOperateTime(new Date());
        sysAclMapper.updateByPrimaryKeySelective(after);
        sysLogService.saveAclLog(before,after);
    }

    public boolean checkExist(int aclModuleId,String name,Integer aclParamId){
        return sysAclMapper.countByNameAndAclModuleId(aclModuleId, name, aclParamId) > 0;
    }

    public String gengerateCode(){
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyyMMddHHmmss");
        return simpleDateFormat.format(new Date());
    }

    public PageResult<SysAcl> getPageByAclModuleId(Integer aclModuleId, PageQuery pageQuery) {
        BeanValidator.check(pageQuery);
        int count = sysAclMapper.countByAclModuleId(aclModuleId);
        if(count>0){
            List<SysAcl> list=sysAclMapper.getPageByAclMoudleId(aclModuleId,pageQuery);
            return PageResult.<SysAcl>builder().total(count).data(list).build();
        }
        return PageResult.<SysAcl>builder().build();
    }
}
