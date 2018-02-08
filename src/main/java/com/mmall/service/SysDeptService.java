package com.mmall.service;

import com.google.common.base.Preconditions;
import com.mmall.common.RequestHolder;
import com.mmall.dao.SysDeptMapper;
import com.mmall.dao.SysUserMapper;
import com.mmall.exception.ParamException;
import com.mmall.model.SysDept;
import com.mmall.param.DeptParam;
import com.mmall.util.BeanValidator;
import com.mmall.util.IpUtil;
import com.mmall.util.LevelUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Author: XBlue
 * @Date: Create in 2018/1/2816:22
 * @Description:
 * @Modified By:
 */
@Service
public class SysDeptService {
    @Resource
    private SysDeptMapper sysDeptMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysLogService sysLogService;

    public void save(DeptParam deptParam) {
        BeanValidator.check(deptParam);
        if (checkedExist(deptParam.getParentId(), deptParam.getName(), deptParam.getId())) {
            throw new ParamException("同一层级下存在相同名称的部门");
        }
        SysDept sysDept = SysDept.builder().name(deptParam.getName()).parentId(deptParam.getParentId()).seq(deptParam.getSeq())
                .remark(deptParam.getRemark()).build();
        sysDept.setLevel(LevelUtil.calculateLevel(getLevel(deptParam.getParentId()), deptParam.getParentId()));
        sysDept.setOperator(RequestHolder.getCurrentUser().getUsername()+"-system");//TODO：
        sysDept.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentHttpServletRequest()));
        sysDept.setOperateTime(new Date());
        sysDeptMapper.insertSelective(sysDept);
        sysLogService.saveDeptLog(null,sysDept);
    }

    public void updates(DeptParam deptParam) {
        BeanValidator.check(deptParam);
        if (checkedExist(deptParam.getParentId(), deptParam.getName(), deptParam.getId())) {
            throw new ParamException("同一层级下存在相同名称的部门");
        }
        //        看看原来的部门是否存在
        SysDept sysDeptBefore = sysDeptMapper.selectByPrimaryKey(deptParam.getId());
        Preconditions.checkNotNull(sysDeptBefore, "待更新的部门不存在");
        //同一层级不能存在相同的部门
        SysDept sysDeptAfter = SysDept.builder().id(deptParam.getId()).name(deptParam.getName()).parentId(deptParam.getParentId()).seq(deptParam.getSeq())
                .remark(deptParam.getRemark()).build();
        sysDeptAfter.setLevel(LevelUtil.calculateLevel(getLevel(deptParam.getParentId()), deptParam.getParentId()));
        sysDeptAfter.setOperator(RequestHolder.getCurrentUser().getUsername()+"-system-update");//TODO：
        sysDeptAfter.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentHttpServletRequest()));
        sysDeptAfter.setOperateTime(new Date());
        updateWithChild(sysDeptBefore,sysDeptAfter);
        sysLogService.saveDeptLog(sysDeptBefore,sysDeptAfter);
    }

    //    关联更新 要么都成功 要么都失败 更新在一个事务里面
    @Transactional
    public void updateWithChild(SysDept before, SysDept after) {
        String newLevelPrefix =after.getLevel();
        String oldLevelPrefix =before.getLevel();
        if(!newLevelPrefix.equals(oldLevelPrefix)){
            String oldChildrenLevel = LevelUtil.calculateLevel(oldLevelPrefix,before.getId());
            List<SysDept> deptList = sysDeptMapper.getChildDeptListByLevel(oldChildrenLevel);
            if(!CollectionUtils.isEmpty(deptList)){
                for(SysDept sysDept:deptList){
                    String level = sysDept.getLevel();
                    //判断前缀level是否相等
                    if(level.indexOf(oldLevelPrefix)==0){
                        //拼接出新的level
                        level =newLevelPrefix+ level.substring(oldLevelPrefix.length());
                        sysDept.setLevel(level);
                    }
                }
                sysDeptMapper.batchUpdateLevel(deptList);
            }
        }
        sysDeptMapper.updateByPrimaryKey(after);
    }


    public void delete(int deptId){
        SysDept sysDept=sysDeptMapper.selectByPrimaryKey(deptId);
        Preconditions.checkNotNull(sysDept,"删除的部门不存在");
        if(sysDeptMapper.countByParentId(deptId)>0){
            throw new ParamException("部门下含有子部门，不能删除");
        }
        if(sysUserMapper.countByDeptId(deptId)>0){
            throw new ParamException("部门下含有用户，请先删除用户");
        }
        sysDeptMapper.deleteByPrimaryKey(deptId);
    }

    //
    private boolean checkedExist(Integer parentId, String name, Integer deptId) {
        return sysDeptMapper.countByNameAndParentId(parentId,name,deptId)>0;
    }

    private String getLevel(Integer deptId) {
        SysDept dept = sysDeptMapper.selectByPrimaryKey(deptId);
        if (dept == null) {
            return null;
        } else {
            return dept.getLevel();
        }
    }
}
