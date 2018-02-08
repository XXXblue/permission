package com.mmall.dao;

import com.mmall.beans.PageQuery;
import com.mmall.dto.SearchLogDto;
import com.mmall.model.SysLog;
import com.mmall.model.SysLogWithBLOBs;
import org.apache.ibatis.annotations.Param;

import javax.naming.directory.SearchControls;
import java.util.List;

public interface SysLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysLogWithBLOBs record);

    int insertSelective(SysLogWithBLOBs record);

    SysLogWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysLogWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(SysLogWithBLOBs record);

    int updateByPrimaryKey(SysLog record);

    int countBySearchDto(@Param("searchLogDto")SearchLogDto searchLogDto);

    List<SysLogWithBLOBs> getPageListBySearchDto(@Param("searchLogDto")SearchLogDto searchLogDto, @Param("page")PageQuery pageQuery);
}