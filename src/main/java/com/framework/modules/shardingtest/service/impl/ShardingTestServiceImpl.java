package com.framework.modules.shardingtest.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.framework.datasources.DataSourceNames;
import com.framework.datasources.annotation.DataSource;
import com.framework.modules.shardingtest.entity.Shardingtest;
import com.framework.modules.shardingtest.entity.ShardingtestExample;
import com.framework.modules.shardingtest.mapper.ShardingtestMapper;
import com.framework.modules.shardingtest.service.ShardingTestService;

@Service
@CacheConfig(cacheNames="test")
public class ShardingTestServiceImpl implements ShardingTestService{
	
	@Autowired
	private ShardingtestMapper stMapper;

	@Override
	@DataSource(name=DataSourceNames.SECOND)
	@Cacheable
	public List<Shardingtest> getList() {
		
		ShardingtestExample se=new  ShardingtestExample();
		
		return stMapper.selectByExample(se);
	}
}
