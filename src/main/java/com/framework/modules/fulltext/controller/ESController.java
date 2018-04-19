package com.framework.modules.fulltext.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.framework.common.fulltext.ElasticSearchHandler;
import com.framework.common.utils.R;
import com.framework.modules.fulltext.entity.QueryDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/fulltext")
@Api(tags = { "全文接口" })
public class ESController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ApiOperation("全文查询")
	public R search(QueryDTO query) {
		
		String queryStr="{\"multi_match\":{\"query\":\""+query.getSearchText()+"\",\"fields\":[\"name\",\"attachment.author\",\"attachment.content\",\"attachment.keywords\",\"attachment.lastAuthor\"]}}";
		
		SearchSourceBuilder ssb=SearchSourceBuilder.searchSource();
		
		ssb.query(QueryBuilders.wrapperQuery(queryStr));
		
		/*String [] in = {"path","name"};
		
		ssb.fetchSource(in, null);*/
		
		ssb.size(query.getSize());
		ssb.from(query.getForm());
		
		List<Map<String,Object>> list=new ArrayList<>();
		
		
		SearchHits hits=ElasticSearchHandler.queryBySearchSourceBuilder(ElasticSearchHandler.DEFAULT_INDEX,ElasticSearchHandler.DEFAULT_TYPE, ssb);

		for(SearchHit searchHit:hits) {
			list.add(searchHit.getSource());
		}
		
		Map<String, Object> map = new HashMap<String, Object>(8);
		
		map.put("count", hits.totalHits);
		map.put("hits", list);
		
 		return R.ok().put("data",map);
	}
	
	@RequestMapping(value = "/getById", method = RequestMethod.GET)
	@ApiOperation("查询")
	public R getById(@ApiParam("documemt Id") @RequestParam String id) {
		Map<String, Object> map=ElasticSearchHandler.searchDataById(ElasticSearchHandler.DEFAULT_INDEX, ElasticSearchHandler.DEFAULT_TYPE,id,"");
		return R.ok().put("data", map);
	}
	
	@RequestMapping(value = "/deleteById", method = RequestMethod.GET)
	@ApiOperation("删除")
	public R delete(@ApiParam("documemt Id") @RequestParam String id) {
		ElasticSearchHandler.deleteDataById(ElasticSearchHandler.DEFAULT_INDEX, ElasticSearchHandler.DEFAULT_TYPE,id);
		return R.ok();
	}
}
