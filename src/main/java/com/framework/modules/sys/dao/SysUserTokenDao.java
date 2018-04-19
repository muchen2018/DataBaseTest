package com.framework.modules.sys.dao;

import org.apache.ibatis.annotations.Mapper;

import com.framework.modules.sys.entity.SysUserTokenEntity;

/**
 * 系统用户Token
 */
@Mapper
public interface SysUserTokenDao extends BaseDao<SysUserTokenEntity> {
    
    SysUserTokenEntity queryByUserId(Long userId);

    SysUserTokenEntity queryByToken(String token);
	
}
