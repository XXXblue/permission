package com.mmall.service;

import com.google.common.base.Preconditions;
import com.mmall.common.RequestHolder;
import com.mmall.dao.SysRoleMapper;
import com.mmall.dao.SysRoleUserMapper;
import com.mmall.exception.ParamException;
import com.mmall.model.SysRole;
import com.mmall.model.SysRoleUser;
import com.mmall.param.RoleParam;
import com.mmall.util.BeanValidator;
import com.mmall.util.IpUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Author: XBlue
 * @Date: Create in 2018/2/316:10
 * @Description:
 * @Modified By:
 */
@Service
public class SysRoleService {
    @Resource
    private SysRoleMapper sysRoleMapper;


    public void save(RoleParam roleParam){
        BeanValidator.check(roleParam);
        if(checkExist(roleParam.getName(),roleParam.getId())){
            throw new ParamException("角色名重复");
        }
        SysRole sysRole=SysRole.builder().name(roleParam.getName()).status(roleParam.getStatus()).type(roleParam.getType()).remark(roleParam.getRemark()).build();
        sysRole.setOperator(RequestHolder.getCurrentUser().getUsername()+"-system");//TODO：
        sysRole.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentHttpServletRequest()));
        sysRole.setOperateTime(new Date());
        sysRoleMapper.insertSelective(sysRole);
    }

    public void update(RoleParam roleParam){
        BeanValidator.check(roleParam);
        if(checkExist(roleParam.getName(),roleParam.getId())){
            throw new ParamException("角色名重复");
        }
        SysRole before = sysRoleMapper.selectByPrimaryKey(roleParam.getId());
        Preconditions.checkNotNull(roleParam,"角色不存在");
        SysRole after=SysRole.builder().id(roleParam.getId()).name(roleParam.getName()).status(roleParam.getStatus()).type(roleParam.getType())
                .remark(roleParam.getRemark()).build();
        after.setOperator(RequestHolder.getCurrentUser().getUsername()+"-system");//TODO：
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentHttpServletRequest()));
        after.setOperateTime(new Date());
        sysRoleMapper.updateByPrimaryKeySelective(after);
    }

    public List<SysRole> list(){
        return sysRoleMapper.getAll();
    }

    private boolean checkExist(String name,Integer id){
        return sysRoleMapper.countByName(name,id)>0;
    }

}
