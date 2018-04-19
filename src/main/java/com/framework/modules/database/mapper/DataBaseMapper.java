package com.framework.modules.database.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.framework.modules.database.entity.Database;

@Mapper
public interface DataBaseMapper {
    
	void createDatabase(Database db);
	
}