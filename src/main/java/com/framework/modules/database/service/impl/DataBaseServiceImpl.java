package com.framework.modules.database.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.framework.modules.database.entity.Database;
import com.framework.modules.database.mapper.DataBaseMapper;
import com.framework.modules.database.service.DataBaseService;

@Service
public class DataBaseServiceImpl implements DataBaseService {
	
	@Autowired
	private DataBaseMapper dataBaseMapper;

	@Override
	public void createDatabase(Database db) {
		
		dataBaseMapper.createDatabase(db);
		
	}

}
