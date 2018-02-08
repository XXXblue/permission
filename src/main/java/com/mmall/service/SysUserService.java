package com.mmall.service;

import com.google.common.base.Preconditions;
import com.mmall.beans.PageQuery;
import com.mmall.beans.PageResult;
import com.mmall.common.RequestHolder;
import com.mmall.dao.SysUserMapper;
import com.mmall.exception.ParamException;
import com.mmall.model.SysUser;
import com.mmall.param.UserParam;
import com.mmall.util.BeanValidator;
import com.mmall.util.IpUtil;
import com.mmall.util.MD5Util;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Author: XBlue
 * @Date: Create in 2018/1/3023:22
 * @Description:
 * @Modified By:
 */
@Service
public class SysUserService {
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysLogService sysLogService;
    public List<SysUser> getAll(){
        return sysUserMapper.getAll();
    }

    public void save(UserParam userParam) {
        BeanValidator.check(userParam);
        if (checkEmailExist(userParam.getMail(), userParam.getId())) {
            throw new ParamException("邮箱被占用");
        }
        if (checkTelephoneExist(userParam.getTelephone(), userParam.getId())) {
            throw new ParamException("电话被占用");
        }
        String password = "123456";
        String encryptedPassword = MD5Util.encrypt(password);
        SysUser sysUser = SysUser.builder().username(userParam.getUsername()).telephone(userParam.getTelephone())
                .password(encryptedPassword).mail(userParam.getMail()).deptId(userParam.getDeptId()).status(userParam.getStatus()).remark(userParam.getRemark()).build();
        sysUser.setOperator(RequestHolder.getCurrentUser().getUsername());//TODO：
        sysUser.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentHttpServletRequest()));
        sysUser.setOperateTime(new Date());
        //TODO：
        sysUserMapper.insertSelective(sysUser);
        sysLogService.saveUserLog(null,sysUser);
    }

    public void update(UserParam userParam) {
        BeanValidator.check(userParam);
        if (checkEmailExist(userParam.getMail(), userParam.getId())) {
            throw new ParamException("邮箱被占用");
        }
        if (checkTelephoneExist(userParam.getTelephone(), userParam.getId())) {
            throw new ParamException("电话被占用");
        }
        SysUser sysUserBefore = sysUserMapper.selectByPrimaryKey(userParam.getId());//更新一定要先查看该用户是否存在

        Preconditions.checkNotNull(sysUserBefore, "要更新的用户不存在");

        SysUser sysUserAfter = SysUser.builder().id(userParam.getId()).username(userParam.getUsername()).telephone(userParam.getTelephone())
                .mail(userParam.getMail()).deptId(userParam.getDeptId()).status(userParam.getStatus()).remark(userParam.getRemark()).build();
        sysUserAfter.setOperator(RequestHolder.getCurrentUser().getUsername()+"-system-update");//TODO：
        sysUserAfter.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentHttpServletRequest()));
        sysUserAfter.setOperateTime(new Date());
        sysUserMapper.updateByPrimaryKeySelective(sysUserAfter);
        sysLogService.saveUserLog(sysUserBefore,sysUserAfter);
    }

    //邮箱校验
    public boolean checkEmailExist(String email, Integer userId) {
        return sysUserMapper.countByMail(email, userId) > 0;
    }

    //手机号校验
    public boolean checkTelephoneExist(String telephone, Integer userId) {
        return sysUserMapper.countByTelephone(telephone, userId) > 0;
    }

    public SysUser findByKeyWord(String username) {
        return sysUserMapper.findByKeyWord(username);
    }

    public PageResult<SysUser> getPageByDeptId(int deptId, PageQuery pageQuery) {
        BeanValidator.check(pageQuery);
        int count = sysUserMapper.countByDeptId(deptId);
        if (count > 0) {
            List<SysUser> list = sysUserMapper.getPageByDeptId(deptId, pageQuery);
            return PageResult.<SysUser>builder().total(count).data(list).build();
        }
        return PageResult.<SysUser>builder().build();
    }
}
