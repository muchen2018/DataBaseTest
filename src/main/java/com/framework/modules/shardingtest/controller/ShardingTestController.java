package com.framework.modules.shardingtest.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.framework.common.utils.R;
import com.framework.modules.shardingtest.entity.Shardingtest;
import com.framework.modules.shardingtest.service.ShardingTestService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/shardingTest")
@Api(tags = { "shardingTest" })
public class ShardingTestController {

	@Autowired
	private ShardingTestService stService;

	@GetMapping(value = "/getList")
	@ApiOperation("获取列表")
	public R delete(int page, int size) {
		
		PageHelper.startPage(page, size);

		List<Shardingtest> list = stService.getList();
		
		Map<String,Object> data=new HashMap<String,Object>(2);
		
		PageInfo<Shardingtest> pageList = new PageInfo<Shardingtest>(list);
		data.put("total", pageList.getTotal());
		data.put("data", list);
		
		return R.ok(data);
	}

}
