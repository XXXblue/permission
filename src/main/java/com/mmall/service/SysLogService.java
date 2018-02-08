package com.mmall.service;

import com.alibaba.druid.sql.PagerUtils;
import com.google.common.base.Preconditions;
import com.mmall.beans.LogType;
import com.mmall.beans.PageQuery;
import com.mmall.beans.PageResult;
import com.mmall.common.RequestHolder;
import com.mmall.dao.*;
import com.mmall.dto.SearchLogDto;
import com.mmall.exception.ParamException;
import com.mmall.exception.PermissionException;
import com.mmall.model.*;
import com.mmall.param.SearchLogParam;
import com.mmall.util.BeanValidator;
import com.mmall.util.IpUtil;
import com.mmall.util.JsonMapper;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.dc.pr.PRError;

import javax.annotation.Resource;
import javax.annotation.Resources;
import java.lang.annotation.Target;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author: XBlue
 * @Date: Create in 2018/2/814:01
 * @Description:
 * @Modified By:
 */
@Service
public class SysLogService {
    @Resource
    private SysLogMapper sysLogMapper;
    @Resource
    private SysDeptMapper sysDeptMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysAclModuleMapper sysAclModuleMapper;
    @Resource
    private SysAclMapper sysAclMapper;
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysRoleAclMapper sysRoleAclMapper;
    @Resource
    private SysRoleUserMapper sysRoleUserMapper;
    @Resource
    private SysRoleAclService sysRoleAclService;
    @Resource
    private SysRoleUserService sysRoleUserService;

    @Transactional
    public void recover(int id) {
        SysLogWithBLOBs log = sysLogMapper.selectByPrimaryKey(id);
        Preconditions.checkNotNull(log, "待还原的记录不存在");
        switch (log.getType()) {
            case LogType.TYPE_DEPT:
                SysDept beforeDept = sysDeptMapper.selectByPrimaryKey(log.getTargetId());
                Preconditions.checkNotNull(beforeDept, "待还原的部门不存在");
                //这里之所以这样设计是因为 删除只要修改状态就行了 至于新增没有意义（直接删除新的就行了）
                if (StringUtils.isBlank(log.getNewValue()) || StringUtils.isBlank(log.getOldValue())) {
                    throw new PermissionException("新增和删除不做还原");
                }
                SysDept afterDept = JsonMapper.string2Object(log.getOldValue(), new TypeReference<SysDept>() {
                });
                afterDept.setOperator(RequestHolder.getCurrentUser().getUsername());
                afterDept.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentHttpServletRequest()));
                afterDept.setOperateTime(new Date());
                sysDeptMapper.updateByPrimaryKeySelective(afterDept);
                saveDeptLog(beforeDept, afterDept);
                break;
            case LogType.TYPE_USER:
                SysUser beforeUser = sysUserMapper.selectByPrimaryKey(log.getTargetId());
                Preconditions.checkNotNull(beforeUser, "待还原的角色不存在");
                //这里之所以这样设计是因为 删除只要修改状态就行了 至于新增没有意义（直接删除新的就行了）
                if (StringUtils.isBlank(log.getNewValue()) || StringUtils.isBlank(log.getOldValue())) {
                    throw new PermissionException("新增和删除不做还原");
                }
                SysUser afterUser = JsonMapper.string2Object(log.getOldValue(), new TypeReference<SysUser>() {
                });
                afterUser.setOperator(RequestHolder.getCurrentUser().getUsername());
                afterUser.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentHttpServletRequest()));
                afterUser.setOperateTime(new Date());
                sysUserMapper.updateByPrimaryKeySelective(afterUser);
                saveUserLog(beforeUser, afterUser);
                break;
            case LogType.TYPE_ACL_MODULE:
                SysAclModule sysAclModuleBefore = sysAclModuleMapper.selectByPrimaryKey(log.getTargetId());
                Preconditions.checkNotNull(sysAclModuleBefore, "待还原的权限模块不存在");
                //这里之所以这样设计是因为 删除只要修改状态就行了 至于新增没有意义（直接删除新的就行了）
                if (StringUtils.isBlank(log.getNewValue()) || StringUtils.isBlank(log.getOldValue())) {
                    throw new PermissionException("新增和删除不做还原");
                }
                SysAclModule sysAclModuleAfter = JsonMapper.string2Object(log.getOldValue(), new TypeReference<SysAclModule>() {
                });
                sysAclModuleAfter.setOperator(RequestHolder.getCurrentUser().getUsername());
                sysAclModuleAfter.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentHttpServletRequest()));
                sysAclModuleAfter.setOperateTime(new Date());
                sysAclModuleMapper.updateByPrimaryKeySelective(sysAclModuleAfter);
                saveAclModuleLog(sysAclModuleBefore, sysAclModuleAfter);
                break;
            case LogType.TYPE_ACL:
                SysAcl sysAclBefore = sysAclMapper.selectByPrimaryKey(log.getTargetId());
                Preconditions.checkNotNull(sysAclBefore, "待还原的权限不存在");
                //这里之所以这样设计是因为 删除只要修改状态就行了 至于新增没有意义（直接删除新的就行了）
                if (StringUtils.isBlank(log.getNewValue()) || StringUtils.isBlank(log.getOldValue())) {
                    throw new PermissionException("新增和删除不做还原");
                }
                SysAcl sysAclAfter = JsonMapper.string2Object(log.getOldValue(), new TypeReference<SysAcl>() {
                });
                sysAclAfter.setOperator(RequestHolder.getCurrentUser().getUsername());
                sysAclAfter.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentHttpServletRequest()));
                sysAclAfter.setOperateTime(new Date());
                sysAclMapper.updateByPrimaryKeySelective(sysAclAfter);
                saveAclLog(sysAclBefore, sysAclAfter);
                break;
            case LogType.TYPE_ROLE:
                SysRole sysRoleBefore = sysRoleMapper.selectByPrimaryKey(log.getTargetId());
                Preconditions.checkNotNull(sysRoleBefore, "待还原的角色不存在");
                //这里之所以这样设计是因为 删除只要修改状态就行了 至于新增没有意义（直接删除新的就行了）
                if (StringUtils.isBlank(log.getNewValue()) || StringUtils.isBlank(log.getOldValue())) {
                    throw new PermissionException("新增和删除不做还原");
                }
                SysRole sysRoleAfter = JsonMapper.string2Object(log.getOldValue(), new TypeReference<SysRole>() {
                });
                sysRoleAfter.setOperator(RequestHolder.getCurrentUser().getUsername());
                sysRoleAfter.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentHttpServletRequest()));
                sysRoleAfter.setOperateTime(new Date());
                sysRoleMapper.updateByPrimaryKeySelective(sysRoleAfter);
                saveRoleLog(sysRoleBefore, sysRoleAfter);
                break;
            case LogType.TYPE_ROLE_ACL:
                SysRole aclRole = sysRoleMapper.selectByPrimaryKey(log.getTargetId());
                Preconditions.checkNotNull(aclRole, "待还原的角色不存在");
                List<Integer> oldlistA = JsonMapper.string2Object(log.getOldValue(), new TypeReference<List<Integer>>() {
                });
                List<Integer> newlistA = JsonMapper.string2Object(log.getNewValue(), new TypeReference<List<Integer>>() {
                });
                sysRoleAclService.changeRoleAcls(log.getTargetId(), oldlistA);
                break;
            case LogType.TYPE_ROLE_USER:
                SysRole useRole = sysRoleMapper.selectByPrimaryKey(log.getTargetId());
                Preconditions.checkNotNull(useRole, "待还原的角色不存在");
                List<Integer> oldlistU = JsonMapper.string2Object(log.getOldValue(), new TypeReference<List<Integer>>() {
                });
                List<Integer> newlistU = JsonMapper.string2Object(log.getNewValue(), new TypeReference<List<Integer>>() {
                });
                sysRoleUserService.changeRoleUsers(log.getTargetId(), oldlistU);
                break;
            default:
                ;
        }
    }

    public PageResult<SysLogWithBLOBs> searchPageList(SearchLogParam searchLogParam, PageQuery pageQuery) {
        BeanValidator.check(pageQuery);
        SearchLogDto dto = new SearchLogDto();
        dto.setType(searchLogParam.getType());
        //学到了新技能，还能这样模糊查询
        if (!StringUtils.isBlank(searchLogParam.getAfterSeg())) {
            dto.setBeforeSeg("%" + searchLogParam.getAfterSeg() + "%");
        }
        if (!StringUtils.isBlank(searchLogParam.getBeforeSeg())) {
            dto.setAfterSeg("%" + searchLogParam.getBeforeSeg() + "%");
        }
        if (!StringUtils.isBlank(searchLogParam.getOperator())) {
            dto.setOperator("%" + searchLogParam.getOperator() + "%");
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (!StringUtils.isBlank(searchLogParam.getFromTime())) {
                dto.setFromTime(df.parse(searchLogParam.getFromTime()));
            }
            if (!StringUtils.isBlank(searchLogParam.getToTime())) {
                dto.setToTime(df.parse(searchLogParam.getToTime()));
            }
        } catch (Exception e) {
            throw new ParamException("传入的日期格式有问题,正确格式yyyy-MM-dd HH:mm:ss");
        }
        int count = sysLogMapper.countBySearchDto(dto);
        if (count > 0) {
            List<SysLogWithBLOBs> list = sysLogMapper.getPageListBySearchDto(dto, pageQuery);
            return PageResult.<SysLogWithBLOBs>builder().total(count).data(list).build();
        }

        return PageResult.<SysLogWithBLOBs>builder().build();//TODO
    }

    public void saveDeptLog(SysDept before, SysDept after) {
        SysLogWithBLOBs sysLogWithBLOBs = new SysLogWithBLOBs();
        sysLogWithBLOBs.setType(LogType.TYPE_DEPT);
        //这里要注意，新增的时候是before没有id，删除是after没有id
        sysLogWithBLOBs.setTargetId(after == null ? before.getId() : after.getId());
        sysLogWithBLOBs.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLogWithBLOBs.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLogWithBLOBs.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLogWithBLOBs.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentHttpServletRequest()));
        sysLogWithBLOBs.setOperateTime(new Date());
        sysLogWithBLOBs.setStatus(1);
        sysLogMapper.insertSelective(sysLogWithBLOBs);
    }

    public void saveUserLog(SysUser before, SysUser after) {
        SysLogWithBLOBs sysLogWithBLOBs = new SysLogWithBLOBs();
        sysLogWithBLOBs.setType(LogType.TYPE_USER);
        //这里要注意，新增的时候是before没有id，删除是after没有id
        sysLogWithBLOBs.setTargetId(after == null ? before.getId() : after.getId());
        sysLogWithBLOBs.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLogWithBLOBs.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLogWithBLOBs.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLogWithBLOBs.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentHttpServletRequest()));
        sysLogWithBLOBs.setOperateTime(new Date());
        sysLogWithBLOBs.setStatus(1);
        sysLogMapper.insertSelective(sysLogWithBLOBs);
    }

    public void saveAclModuleLog(SysAclModule before, SysAclModule after) {
        SysLogWithBLOBs sysLogWithBLOBs = new SysLogWithBLOBs();
        sysLogWithBLOBs.setType(LogType.TYPE_ACL_MODULE);
        //这里要注意，新增的时候是before没有id，删除是after没有id
        sysLogWithBLOBs.setTargetId(after == null ? before.getId() : after.getId());
        sysLogWithBLOBs.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLogWithBLOBs.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLogWithBLOBs.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLogWithBLOBs.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentHttpServletRequest()));
        sysLogWithBLOBs.setOperateTime(new Date());
        sysLogWithBLOBs.setStatus(1);
        sysLogMapper.insertSelective(sysLogWithBLOBs);
    }

    public void saveAclLog(SysAcl before, SysAcl after) {
        SysLogWithBLOBs sysLogWithBLOBs = new SysLogWithBLOBs();
        sysLogWithBLOBs.setType(LogType.TYPE_ACL);
        //这里要注意，新增的时候是before没有id，删除是after没有id
        sysLogWithBLOBs.setTargetId(after == null ? before.getId() : after.getId());
        sysLogWithBLOBs.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLogWithBLOBs.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLogWithBLOBs.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLogWithBLOBs.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentHttpServletRequest()));
        sysLogWithBLOBs.setOperateTime(new Date());
        sysLogWithBLOBs.setStatus(1);
        sysLogMapper.insertSelective(sysLogWithBLOBs);
    }

    public void saveRoleLog(SysRole before, SysRole after) {
        SysLogWithBLOBs sysLogWithBLOBs = new SysLogWithBLOBs();
        sysLogWithBLOBs.setType(LogType.TYPE_ROLE);
        //这里要注意，新增的时候是before没有id，删除是after没有id
        sysLogWithBLOBs.setTargetId(after == null ? before.getId() : after.getId());
        sysLogWithBLOBs.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLogWithBLOBs.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLogWithBLOBs.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLogWithBLOBs.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentHttpServletRequest()));
        sysLogWithBLOBs.setOperateTime(new Date());
        sysLogWithBLOBs.setStatus(1);
        sysLogMapper.insertSelective(sysLogWithBLOBs);
    }

}
