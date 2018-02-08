package com.mmall.service;

import com.google.common.collect.Lists;
import com.mmall.beans.CacheKeyConstants;
import com.mmall.beans.PageResult;
import com.mmall.common.RequestHolder;
import com.mmall.dao.SysAclMapper;
import com.mmall.dao.SysRoleAclMapper;
import com.mmall.dao.SysRoleUserMapper;
import com.mmall.model.SysAcl;
import com.mmall.model.SysUser;
import com.mmall.util.JsonMapper;
import com.mmall.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author: XBlue
 * @Date: Create in 2018/2/414:34
 * @Description:
 * @Modified By:
 */
@Service
public class SysCoreService {

    @Resource
    private SysAclMapper sysAclMapper;
    @Resource
    private SysRoleUserMapper sysRoleUserMapper;
    @Resource
    private SysRoleAclMapper sysRoleAclMapper;
    @Resource
    private SysCacheService sysCacheService;

    public List<SysAcl> getCurrentAclList() {
        int userId = RequestHolder.getCurrentUser().getId();
        return getUserAclList(userId);
    }

    //获取角色已经分配的权限点
    public List<SysAcl> getRoleAclList(int roleId) {
        List<Integer> userAclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(Lists.<Integer>newArrayList(roleId));
        if (CollectionUtils.isEmpty(userAclIdList)) {
            return Lists.newArrayList();
        }
        return sysAclMapper.getByIdList(userAclIdList);
    }

    //获取用户所有的权限点
    public List<SysAcl> getUserAclList(int userId) {
        if (isSuperAdmin()) {
            return sysAclMapper.getAll();
        } else {
            //取出该用户的所有角色
            List<Integer> userRoleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
            if (CollectionUtils.isEmpty(userRoleIdList)) {
                return Lists.newArrayList();
            }
            //通过所有角色id取出权限点
            List<Integer> userAclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(userRoleIdList);
            if (CollectionUtils.isEmpty(userAclIdList)) {
                return Lists.newArrayList();
            }
            return sysAclMapper.getByIdList(userAclIdList);
        }
    }

    //是否为超级 管理员
    public boolean isSuperAdmin() {
        //实际开发中可以指定某个用户，某个角色或者某个配置文件读取到的数据
        SysUser sysUser = RequestHolder.getCurrentUser();
        if(sysUser.getUsername().equals("ADMIN")){
            return true;
        }
        return false;
    }
    //是否有权限
    public boolean hasUrlAcl(String url) {
        if (isSuperAdmin()) {
            return true;
        }
        List<SysAcl> aclList = sysAclMapper.getByUrl(url);
        //这个是没有该权限点，就可以放行
        if (CollectionUtils.isEmpty(aclList)) {
            return true;
        }
        List<SysAcl> userAclList = getCurrentAclListFromCache();
        Set<Integer> userAclSet = new HashSet<Integer>();
        for (SysAcl sysAcl : userAclList) {
            userAclSet.add(sysAcl.getId());
        }
        //定义规则：只要一个权限点有权限就可以访问
        //这个是如果有失效，就可以直接放行
        //如果用户有直接放行
        boolean flag = false;
        for (SysAcl sysAcl : aclList) {
            if (sysAcl.getStatus() == 1) {
                flag = true;
            }
            if (sysAcl.getStatus() == 1 && userAclSet.contains(sysAcl.getId())) {
                return true;
            }
        }
        if (!flag) {
            return true;
        }
        return false;
    }
    public List<SysAcl> getCurrentAclListFromCache(){
        int userId =RequestHolder.getCurrentUser().getId();
        String cacheValue = sysCacheService.getFromCache(CacheKeyConstants.USER_ACLS,String.valueOf(userId));
        if(StringUtils.isBlank(cacheValue)){
            List<SysAcl> aclList = getCurrentAclList();
            if(!CollectionUtils.isEmpty(aclList)){
                sysCacheService.saveCache(JsonMapper.obj2String(aclList),600,CacheKeyConstants.USER_ACLS,String.valueOf(userId));
                return aclList;
            }
        }
        return JsonMapper.string2Object(cacheValue, new TypeReference<List<SysAcl>>() {
        });
    }
}
