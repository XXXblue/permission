package com.mmall.dao;

import com.mmall.model.SysRoleUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleUser record);

    int insertSelective(SysRoleUser record);

    SysRoleUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRoleUser record);

    int updateByPrimaryKey(SysRoleUser record);

    List<Integer> getRoleIdListByUserId(@Param("userId")int userId);

    List<Integer> getUserIdListByRoleId(@Param("roleId")int roleId);

    void deleteRoleUsersByRoleId(@Param("roleId")int roleId);

    void batchInsert(@Param("roleUserList") List<SysRoleUser> roleUserList);
}