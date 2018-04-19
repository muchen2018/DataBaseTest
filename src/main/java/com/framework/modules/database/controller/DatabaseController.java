package com.framework.modules.database.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.framework.common.utils.R;
import com.framework.modules.database.entity.Database;
import com.framework.modules.database.service.DataBaseService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/dataBase")
@Api(tags= {"数据库管理"})
public class DatabaseController {
	
	@Autowired
	private DataBaseService DataBaseService;
	
	@PutMapping("/createDataBase")
	public R createDataBase(Database db){
		
		DataBaseService.createDatabase(db);
		
		return R.ok().put("msg", "success");
	}

}
