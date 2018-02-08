package com.mmall.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.beans.LogType;
import com.mmall.common.RequestHolder;
import com.mmall.dao.SysLogMapper;
import com.mmall.dao.SysRoleAclMapper;
import com.mmall.model.SysLogWithBLOBs;
import com.mmall.model.SysRole;
import com.mmall.model.SysRoleAcl;
import com.mmall.util.IpUtil;
import com.mmall.util.JsonMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Author: XBlue
 * @Date: Create in 2018/2/515:18
 * @Description:
 * @Modified By:
 */
@Service
public class SysRoleAclService {

    @Resource
    private SysRoleAclMapper sysRoleAclMapper;
    @Resource
    private SysLogMapper sysLogMapper;

    public void changeRoleAcls(Integer roleId, List<Integer> aclIdList) {
        //下面这种写法就是用了google的guava包，本质和new一个list添加一个元素是一样的
        List<Integer> originAclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(Lists.newArrayList(roleId));
        if(CollectionUtils.isEmpty(originAclIdList)){
            originAclIdList=Lists.newArrayList();
        }
        sysRoleAclMapper.deleteByRoleId(roleId);
        updateRoleAcls(roleId,aclIdList);
        saveRoleAclLog(roleId,originAclIdList,aclIdList);
    }

    @Transactional
    public void updateRoleAcls(int roleId,List<Integer>aclIdList){
        sysRoleAclMapper.deleteByPrimaryKey(roleId);
        if(CollectionUtils.isEmpty(aclIdList)){
            return;
        }
        List<SysRoleAcl>roleAclList=Lists.newArrayList();
        for(Integer aclId:aclIdList){
            SysRoleAcl roleAcl = SysRoleAcl.builder().roleId(roleId).aclId(aclId)
                    .operator(RequestHolder.getCurrentUser().getUsername()+"-system")
                    .operatorIp(IpUtil.getRemoteIp(RequestHolder.getCurrentHttpServletRequest()))
                    .operatorTime(new Date()).build();
            roleAclList.add(roleAcl);
        }
        sysRoleAclMapper.batchInsert(roleAclList);
    }

    public void saveRoleAclLog(int roleId, List<Integer> before, List<Integer> after) {
        SysLogWithBLOBs sysLogWithBLOBs = new SysLogWithBLOBs();
        sysLogWithBLOBs.setType(LogType.TYPE_ROLE_ACL);
        //这里要注意，新增的时候是before没有id，删除是after没有id
        sysLogWithBLOBs.setTargetId(roleId);
        sysLogWithBLOBs.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLogWithBLOBs.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLogWithBLOBs.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLogWithBLOBs.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentHttpServletRequest()));
        sysLogWithBLOBs.setOperateTime(new Date());
        sysLogWithBLOBs.setStatus(1);
        sysLogMapper.insertSelective(sysLogWithBLOBs);
    }

}
