package com.framework.modules.sys.service;

import java.util.List;
import java.util.Map;

import com.framework.modules.sys.entity.SysLogEntity;

/**
 * 系统日志
 * 
 */
public interface SysLogService {
	
	SysLogEntity queryObject(Long id);
	
	List<SysLogEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(SysLogEntity sysLog);

	void delete(Long id);
	
	void deleteBatch(Long[] ids);
}
