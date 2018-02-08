package com.mmall.service;

import com.google.common.collect.Lists;
import com.mmall.beans.LogType;
import com.mmall.common.RequestHolder;
import com.mmall.dao.SysLogMapper;
import com.mmall.dao.SysRoleUserMapper;
import com.mmall.dao.SysUserMapper;
import com.mmall.model.SysLogWithBLOBs;
import com.mmall.model.SysRoleUser;
import com.mmall.model.SysUser;
import com.mmall.util.IpUtil;
import com.mmall.util.JsonMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Author: XBlue
 * @Date: Create in 2018/2/515:18
 * @Description:
 * @Modified By:
 */
@Service
public class SysRoleUserService {

    @Resource
    private SysRoleUserMapper sysRoleUserMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysLogMapper sysLogMapper;

    public List<SysUser> getListByRoleId(int roleId) {
        List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }
        return sysUserMapper.getByIdList(userIdList);
    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor={Exception.class, RuntimeException.class})
    public void changeRoleUsers(int roleId, List<Integer> userIds) {
        List<SysRoleUser> roleUserList = Lists.newArrayList();
        List<Integer> originlist = sysRoleUserMapper.getUserIdListByRoleId(roleId);
        sysRoleUserMapper.deleteRoleUsersByRoleId(roleId);
        if (!CollectionUtils.isEmpty(userIds)) {
            for (Integer temp : userIds) {
                SysRoleUser sysRoleUser = SysRoleUser.builder().roleId(roleId).userId(temp)
                        .operator(RequestHolder.getCurrentUser().getUsername() + "-system")
                        .operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentHttpServletRequest()))
                        .operateTime(new Date()).build();
                roleUserList.add(sysRoleUser);
            }
            sysRoleUserMapper.batchInsert(roleUserList);
            saveRoleUserLog(roleId,originlist,userIds);
        }
    }

    public void saveRoleUserLog(int roleId, List<Integer> before, List<Integer> after) {
        SysLogWithBLOBs sysLogWithBLOBs = new SysLogWithBLOBs();
        sysLogWithBLOBs.setType(LogType.TYPE_ROLE_USER);
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
