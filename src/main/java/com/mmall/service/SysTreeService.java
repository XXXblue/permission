package com.mmall.service;

import com.google.common.collect.Lists;
import com.mmall.dao.SysAclMapper;
import com.mmall.dao.SysAclModuleMapper;
import com.mmall.dao.SysDeptMapper;
import com.mmall.dto.AclDto;
import com.mmall.dto.AclModuleLevelDto;
import com.mmall.dto.DeptLevelDto;
import com.mmall.model.SysAcl;
import com.mmall.model.SysAclModule;
import com.mmall.model.SysDept;
import com.mmall.model.SysRole;
import com.mmall.param.AclMoudleParam;
import com.mmall.util.LevelUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.logging.Level;

/**
 * @Author: XBlue
 * @Date: Create in 2018/1/2817:24
 * @Description:
 * @Modified By:
 */
@Service
public class SysTreeService {

    @Resource
    private SysDeptMapper sysDeptMapper;
    @Resource
    private SysAclModuleMapper sysAclModuleMapper;
    @Resource
    private SysCoreService sysCoreService;
    @Resource
    private SysAclMapper sysAclMapper;

    //角色分配权限模块，项目难点核心
    public List<AclModuleLevelDto> roleTree(int roleId) {
        //1.获取用户所有的角色的所有的权限点
        List<SysAcl> userAclList = sysCoreService.getCurrentAclList();
        //2.获取该角色模块已分配权限点
        List<SysAcl> roleAclList = sysCoreService.getRoleAclList(roleId);
        Set<Integer> userAclIdSet = new HashSet<Integer>();
        Set<Integer> roleAclIdSet = new HashSet<Integer>();
        List<AclDto> aclDtoList = Lists.newArrayList();
        for (SysAcl sysAcl : userAclList) {
            userAclIdSet.add(sysAcl.getId());
        }
        for (SysAcl sysAcl : roleAclList) {
            roleAclIdSet.add(sysAcl.getId());
        }
        //3.获取系统所有权限点
        List<SysAcl> allAclList = sysAclMapper.getAll();
        for (SysAcl acl : allAclList) {
            AclDto dto = AclDto.adapt(acl);
            if (userAclIdSet.contains(acl.getId())) {
                //告诉你能不能改
                dto.setHasAcl(true);
            }
            if (roleAclIdSet.contains(acl.getId())) {
                //告诉你改了没有
                dto.setChecked(true);
            }
            aclDtoList.add(dto);
        }
        return aclListToTree(aclDtoList);
    }

    //角色分配权限模块,树的第一层处理和返回
    public List<AclModuleLevelDto> aclListToTree(List<AclDto> aclDtoList) {
        if (CollectionUtils.isEmpty(aclDtoList)) {
            return Lists.newArrayList();
        }
        List<AclModuleLevelDto> aclModuleLevelDtoList = aclModuleTree();
        Map<Integer, List<AclDto>> map = new HashMap<Integer, List<AclDto>>();
        for (AclDto aclDto : aclDtoList) {
            if (aclDto.getStatus() == 1) {
                if (map.containsKey(aclDto.getAclModuleId())) {
                    map.get(aclDto.getAclModuleId()).add(aclDto);
                } else {
                    List<AclDto> temp = Lists.newArrayList();
                    temp.add(aclDto);
                    map.put(aclDto.getAclModuleId(), temp);
                }
            }
        }
        bindAclsWithOther(aclModuleLevelDtoList, map);
        return aclModuleLevelDtoList;
    }
    //角色分配权限模块,递归下面的层
    public void bindAclsWithOther(List<AclModuleLevelDto> aclModuleLevelDtoList, Map<Integer, List<AclDto>> map) {
        if (CollectionUtils.isEmpty(aclModuleLevelDtoList)) {
            return;
        }
        for (AclModuleLevelDto aclModuleLevelDto : aclModuleLevelDtoList) {
            List<AclDto> tempList = map.get(aclModuleLevelDto.getId());
            if (!CollectionUtils.isEmpty(tempList)) {
                Collections.sort(tempList, new Comparator<AclDto>() {
                    public int compare(AclDto o1, AclDto o2) {
                        return o1.getSeq()-o2.getSeq();
                    }
                });
                aclModuleLevelDto.setAclList(tempList);
            }
            bindAclsWithOther(aclModuleLevelDto.getAclModuleList(),map);
        }
    }

    //权限模块树的构建
    public List<AclModuleLevelDto> aclModuleTree() {
        List<SysAclModule> list = sysAclModuleMapper.getAllAclModule();
        List<AclModuleLevelDto> newList = Lists.newArrayList();
        for (SysAclModule sysAclModule : list) {
            newList.add(AclModuleLevelDto.adapt(sysAclModule));
        }
        return aclMoudleListToTree(newList);
    }

    //处理第一层
    public List<AclModuleLevelDto> aclMoudleListToTree(List<AclModuleLevelDto> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Lists.newArrayList();
        } else {
            Map<String, List<AclModuleLevelDto>> map = new HashMap<String, List<AclModuleLevelDto>>();
            List<AclModuleLevelDto> rootList = Lists.newArrayList();
            for (AclModuleLevelDto aclModuleLevelDto : list) {
                if (map.containsKey(aclModuleLevelDto.getLevel())) {
                    map.get(aclModuleLevelDto.getLevel()).add(aclModuleLevelDto);
                } else {
                    List<AclModuleLevelDto> tempList = Lists.newArrayList();
                    tempList.add(aclModuleLevelDto);
                    map.put(aclModuleLevelDto.getLevel(), tempList);
                }
                if (aclModuleLevelDto.getLevel().equals(LevelUtil.ROOT)) {
                    rootList.add(aclModuleLevelDto);
                }
            }
            Collections.sort(rootList, new Comparator<AclModuleLevelDto>() {
                public int compare(AclModuleLevelDto o1, AclModuleLevelDto o2) {
                    return o1.getSeq() - o2.getSeq();
                }
            });
            recuredAclMoudleTree(LevelUtil.ROOT, map, rootList);
            return rootList;
        }
    }

    //递归处理下面的层
    private void recuredAclMoudleTree(String level, Map<String, List<AclModuleLevelDto>> map, List<AclModuleLevelDto> currentList) {
        for (AclModuleLevelDto aclModuleLevelDto : currentList) {
            String nextLevel = LevelUtil.calculateLevel(level, aclModuleLevelDto.getId());
            if (map.containsKey(nextLevel)) {
                List<AclModuleLevelDto> tempList = map.get(nextLevel);
                Collections.sort(tempList, new Comparator<AclModuleLevelDto>() {
                    public int compare(AclModuleLevelDto o1, AclModuleLevelDto o2) {
                        return o1.getSeq() - o2.getSeq();
                    }
                });
                aclModuleLevelDto.setAclModuleList(tempList);
                recuredAclMoudleTree(nextLevel, map, tempList);
            }
        }
    }

    //获取所有数据
    public List<DeptLevelDto> deptTree() {
        List<SysDept> deptList = sysDeptMapper.getAllDept();
        List<DeptLevelDto> dtoList = Lists.newArrayList();
        for (SysDept sysDept : deptList) {
            //适配成dto
            DeptLevelDto deptLevelDto = DeptLevelDto.adapt(sysDept);
            dtoList.add(deptLevelDto);
        }
        //递归组装树数据
        return deptListToTree(dtoList);
    }

    //递归组装list树  第一层比较特殊单独抽开
    public List<DeptLevelDto> deptListToTree(List<DeptLevelDto> dtolist) {
        if (CollectionUtils.isEmpty(dtolist)) {
            return Lists.newArrayList();
        }
        Map<String, List<DeptLevelDto>> map = new HashMap<String, List<DeptLevelDto>>();
        List<DeptLevelDto> rootList = Lists.newArrayList();
        for (DeptLevelDto deptLevelDto : dtolist) {
            //构造map-list集合
            if (map.containsKey(deptLevelDto.getLevel())) {
                map.get(deptLevelDto.getLevel()).add(deptLevelDto);
            } else {
                List<DeptLevelDto> templist = new ArrayList<DeptLevelDto>();
                templist.add(deptLevelDto);
                map.put(deptLevelDto.getLevel(), templist);
            }
            //处理root下的第一层
            if (LevelUtil.ROOT.equals(deptLevelDto.getLevel())) {
                rootList.add(deptLevelDto);
            }
        }
        Collections.sort(rootList, new Comparator<DeptLevelDto>() {
            public int compare(DeptLevelDto o1, DeptLevelDto o2) {
                return o1.getSeq() - o2.getSeq();
            }
        });
        transformDeptTree(rootList, LevelUtil.ROOT, map);
        return rootList;
        //按seq从小到大排序
    }

    //第二层和以下的
    public void transformDeptTree(List<DeptLevelDto> deptLevelDtoList, String level, Map<String, List<DeptLevelDto>> map) {
        for (int i = 0; i < deptLevelDtoList.size(); i++) {
            String nextlevel = LevelUtil.calculateLevel(level, deptLevelDtoList.get(i).getId());
            if (map.containsKey(nextlevel)) {
                List<DeptLevelDto> tempList = map.get(nextlevel);
                Collections.sort(tempList, new Comparator<DeptLevelDto>() {
                    public int compare(DeptLevelDto o1, DeptLevelDto o2) {
                        return o1.getSeq() - o2.getSeq();
                    }
                });
                transformDeptTree(tempList, nextlevel, map);
                deptLevelDtoList.get(i).setDeptList(tempList);
            }
        }
    }


}
