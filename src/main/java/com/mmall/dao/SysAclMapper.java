package com.mmall.dao;

import com.mmall.beans.PageQuery;
import com.mmall.model.SysAcl;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysAclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAcl record);

    int insertSelective(SysAcl record);

    SysAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAcl record);

    int updateByPrimaryKey(SysAcl record);

    int countByNameAndAclModuleId(@Param("aclModuleId")int aclModuleId,@Param("aclName") String name,@Param("id") Integer aclParamId);

    int countByAclModuleId(@Param("aclModuleId")int moduleId);

    List<SysAcl> getPageByAclMoudleId(@Param("aclModuleId")int moduleId,@Param("page") PageQuery pageQuery);

    List<SysAcl> getAll();

    List<SysAcl> getByIdList(@Param("idList")List<Integer>idList);

    List<SysAcl> getByUrl(@Param("url")String url);
}