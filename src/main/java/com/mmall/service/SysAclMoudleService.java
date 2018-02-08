package com.mmall.service;

import com.google.common.base.Preconditions;
import com.mmall.common.RequestHolder;
import com.mmall.dao.SysAclMapper;
import com.mmall.dao.SysAclModuleMapper;
import com.mmall.exception.ParamException;
import com.mmall.model.SysAclModule;
import com.mmall.model.SysDept;
import com.mmall.param.AclMoudleParam;
import com.mmall.util.BeanValidator;
import com.mmall.util.IpUtil;
import com.mmall.util.LevelUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.security.acl.Acl;
import java.util.Date;
import java.util.List;

/**
 * @Author: XBlue
 * @Date: Create in 2018/2/122:40
 * @Description:
 * @Modified By:
 */
@Service
public class SysAclMoudleService {

    @Resource
    private SysAclModuleMapper sysAclModuleMapper;
    @Resource
    private SysAclMapper sysAclMapper;
    @Resource
    private SysLogService sysLogService;
    public void save(AclMoudleParam aclMoudleParam){
        BeanValidator.check(aclMoudleParam);
        if(checkExist(aclMoudleParam.getParentId(), aclMoudleParam.getName(),aclMoudleParam.getId())) {
            throw new ParamException("同一层级下存在相同名称的权限模块");
        }
        SysAclModule aclModule = SysAclModule.builder().name(aclMoudleParam.getName()).parentId(aclMoudleParam.getParentId()).seq(aclMoudleParam.getSeq())
                .status(aclMoudleParam.getStatus()).remark(aclMoudleParam.getRemark()).build();
        aclModule.setLevel(LevelUtil.calculateLevel(getLevel(aclMoudleParam.getParentId()), aclMoudleParam.getParentId()));
        aclModule.setOperator(RequestHolder.getCurrentUser().getUsername());
        aclModule.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentHttpServletRequest()));
        aclModule.setOperateTime(new Date());
        sysAclModuleMapper.insertSelective(aclModule);
        sysLogService.saveAclModuleLog(null,aclModule);
    }

    public void update(AclMoudleParam aclMoudleParam){
        BeanValidator.check(aclMoudleParam);
        if(checkExist(aclMoudleParam.getParentId(), aclMoudleParam.getName(),aclMoudleParam.getId())) {
            throw new ParamException("同一层级下存在相同名称的权限模块");
        }
        SysAclModule before=sysAclModuleMapper.selectByPrimaryKey(aclMoudleParam.getId());
        Preconditions.checkNotNull(before,"待更新的权限模块不存在");
        SysAclModule after = SysAclModule.builder().id(aclMoudleParam.getId()).name(aclMoudleParam.getName()).parentId(aclMoudleParam.getParentId()).seq(aclMoudleParam.getSeq())
                .status(aclMoudleParam.getStatus()).remark(aclMoudleParam.getRemark()).build();
        after.setLevel(LevelUtil.calculateLevel(getLevel(aclMoudleParam.getParentId()), aclMoudleParam.getParentId()));
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentHttpServletRequest()));
        after.setOperateTime(new Date());
        updateWithChild(before,after);
        sysLogService.saveAclModuleLog(before,after);
    }
    @Transactional
    public void updateWithChild(SysAclModule before,SysAclModule after){
        String newLevelPrefix =after.getLevel();
        String oldLevelPrefix =before.getLevel();
        if(!newLevelPrefix.equals(oldLevelPrefix)){
            String oldChildrenLevel = LevelUtil.calculateLevel(oldLevelPrefix,before.getId());
            List<SysAclModule> sysAclModuletList = sysAclModuleMapper.getChildAclModuleListByLevel(oldChildrenLevel);
            if(!CollectionUtils.isEmpty(sysAclModuletList)){
                for(SysAclModule sysAclModule: sysAclModuletList){
                    String level = sysAclModule.getLevel();
                        //拼接出新的level
                    level =newLevelPrefix+ level.substring(oldLevelPrefix.length());
                    sysAclModule.setLevel(level);

                }
                sysAclModuleMapper.batchUpdateLevel(sysAclModuletList);
            }
        }
        sysAclModuleMapper.updateByPrimaryKeySelective(after);
    }

    private  boolean checkExist(Integer parentId,String aclMoudleName, Integer aclMoudelId){
        return sysAclModuleMapper.countByNameAndParentId(parentId, aclMoudleName, aclMoudelId) > 0;
    }

    private String getLevel(Integer aclMoudleId){
        SysAclModule aclModule = sysAclModuleMapper.selectByPrimaryKey(aclMoudleId);
        if (aclModule == null) {
            return null;
        }
        return aclModule.getLevel();
    }

    public void delete(int aclModuleId){
        SysAclModule sysAclModule = sysAclModuleMapper.selectByPrimaryKey(aclModuleId);
        Preconditions.checkNotNull(sysAclModule,"被删除的模块不存在");
        if(sysAclModuleMapper.countByParentId(aclModuleId)>0){
            throw new ParamException("不能删除，存在子模块");
        }
        if(sysAclMapper.countByAclModuleId(aclModuleId)>0){
            throw new ParamException("不能删除，权限模块下存在权限");
        }
        sysAclModuleMapper.deleteByPrimaryKey(aclModuleId);
    }
}
