package com.framework.modules.sys.dao;

import org.apache.ibatis.annotations.Mapper;

import com.framework.modules.sys.entity.SysRoleEntity;

import java.util.List;

/**
 * 角色管理
 * 
 */
@Mapper
public interface SysRoleDao extends BaseDao<SysRoleEntity> {
	
	/**
	 * 查询用户创建的角色ID列表
	 */
	List<Long> queryRoleIdList(Long createUserId);
}
