package com.framework.modules.sys.dao;

import org.apache.ibatis.annotations.Mapper;

import com.framework.modules.sys.entity.SysLogEntity;

/**
 * 系统日志
 * 
 */
@Mapper
public interface SysLogDao extends BaseDao<SysLogEntity> {
	
}
