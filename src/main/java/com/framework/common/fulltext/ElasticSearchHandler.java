package com.framework.common.fulltext;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.WrapperQueryBuilder;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.script.mustache.SearchTemplateRequestBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

@Component
public class ElasticSearchHandler {
	
	public static final String DEFAULT_INDEX="myindex";
	
	public static final String DEFAULT_TYPE="commonType";

	private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchHandler.class);

	@Autowired
	private TransportClient transportClient;

	private static TransportClient client;

	@PostConstruct
	public void init() {
		client = this.transportClient;
	}

	/**
	 * 创建索引
	 * @param index
	 * @return
	 */
	private static boolean createIndex(String index) {
		if (!isIndexExist(index)) {
			CreateIndexResponse indexresponse = client.admin().indices().prepareCreate(index).execute().actionGet();
			LOGGER.info("执行建立成功？" + indexresponse.isAcknowledged());
			return indexresponse.isAcknowledged();
		}
		LOGGER.info("Index is exits!");
		return false;

	}

	/**
	 * 创建一个自定义的mapping
	 * 
	 * @param index
	 * @param type
	 * @param mappingSource
	 *            json
	 */
	public static void createMappingSource(String index, String type, String mappingSource) {
		createIndex(index);
		PutMappingRequest mapping = Requests.putMappingRequest(index).type(type).source(mappingSource,
				XContentType.JSON);
		client.admin().indices().putMapping(mapping).actionGet();
	}

	/**
	 * 删除索引
	 *
	 * @param index
	 * @return
	 */
	public static boolean deleteIndex(String index) {
		if (!isIndexExist(index)) {
			LOGGER.info("Index is not exits!");
		}
		DeleteIndexResponse dResponse = client.admin().indices().prepareDelete(index).execute().actionGet();
		if (dResponse.isAcknowledged()) {
			LOGGER.info("delete index " + index + "  successfully!");
		} else {
			LOGGER.info("Fail to delete index " + index);
		}
		return dResponse.isAcknowledged();
	}

	/**
	 * 判断索引是否存在
	 *
	 * @param index
	 * @return
	 */
	public static boolean isIndexExist(String index) {
		IndicesExistsResponse inExistsResponse = client.admin().indices().exists(new IndicesExistsRequest(index))
				.actionGet();
		if (inExistsResponse.isExists()) {
			LOGGER.info("Index [" + index + "] is exist!");
		} else {
			LOGGER.info("Index [" + index + "] is not exist!");
		}
		return inExistsResponse.isExists();
	}

	/**
	 * 新增
	 * @param json 要增加的数据
	 * @param index 索引
	 * @param type 类型
	 * @param id 数据ID
	 * @return
	 */
	public static String addDoc(String index, String type,BaseDoc doc) {

		IndexResponse response = client.prepareIndex(index, type, doc.getId()).setSource(JSON.toJSONString(doc), XContentType.JSON).get();

		return response.getId();
	}
	
	/**
	 * 批量新增
	 * @param index
	 * @param type
	 * @param list
	 * @return
	 */
	public static BulkResponse addDocBulk(String index, String type,List<? extends BaseDoc> list) {
		
		BulkRequestBuilder bulkRequest =client.prepareBulk();
		
		list.forEach(data->{
			bulkRequest.add(client.prepareIndex(index, type,data.getId()).setSource(JSON.toJSONString(data), XContentType.JSON));
		});

		BulkResponse bulkResponse = bulkRequest.get();

		return bulkResponse;
	}
	
	/**
	 * 数据添加(通过管道处理附件内容 目前没有用了)
	 * @param json 要增加的数据
	 * @param index 索引
	 * @param type 类型
	 * @param id 数据唯一ID
	 * @pipeline 管道名称
	 * @return
	 */
	public static String addData(String json, String index, String type, String id,String pipeline) {

		IndexResponse response = client.prepareIndex(index, type, id).setPipeline(pipeline).setSource(json, XContentType.JSON).get();
		
		return response.getId();
	}

	/**
	 * 通过ID删除数据
	 * @param index 索引
	 * @param type  类型
	 * @param id 数据ID
	 */
	public static void deleteDataById(String index, String type, String id) {

		DeleteResponse response = client.prepareDelete(index, type, id).execute().actionGet();

		LOGGER.info("deleteDataById response status:{},id:{}", response.status().getStatus(), response.getId());
	}

	/**
	 * 通过ID 更新数据
	 *
	 * @param json
	 *            要增加的数据
	 * @param index
	 *            索引
	 * @param type
	 *            类型
	 * @param id
	 *            数据ID
	 * @return
	 */
	public static void updateDataById(String jsonObject, String index, String type, String id) {

		UpdateRequest updateRequest = new UpdateRequest();

		updateRequest.index(index).type(type).id(id).doc(jsonObject, XContentType.JSON);

		client.update(updateRequest);

	}

	/**
	 * 通过ID获取数据
	 * @param index 索引
	 * @param type 类型
	 * @param id 数据ID
	 * @param fields 需要显示的字段，逗号分隔（缺省为全部字段）
	 * @return
	 */
	public static Map<String, Object> searchDataById(String index, String type, String id, String fields) {

		GetRequestBuilder getRequestBuilder = client.prepareGet(index, type, id);

		if (StringUtils.isNotEmpty(fields)) {
			getRequestBuilder.setFetchSource(fields.split(","), null);
		}

		GetResponse getResponse = getRequestBuilder.execute().actionGet();

		return getResponse.getSource();
	}

	/**
	 * json 查询方式 查询的json 应忽略{"query":}
	 * @param index
	 * @param type
	 * @param jsonCondtion
	 * @return
	 */
	public static SearchHits queryByJson(String index, String type, String jsonCondtion) {
		
		SearchRequestBuilder builder = client.prepareSearch(index).setTypes(type).setSearchType(SearchType.DEFAULT);
		// query接口会把原来的json加一个query的前缀
		WrapperQueryBuilder query=QueryBuilders.wrapperQuery(jsonCondtion);
		
		builder.setQuery(query);
		SearchResponse response = builder.execute().actionGet();
		SearchHits hits = response.getHits();
		return hits;

	}
	
	/**
	 * 由于 setSource(jsonstr)等方法在5.0以后被移除,暂不 searchSourceBuilder 暴露出来 自己去构建 queryJson不能满足的情况
	 * @param index
	 * @param type
	 * @param jsonCondtion
	 * @return
	 */
	public static SearchHits queryBySearchSourceBuilder(String index, String type, SearchSourceBuilder ssb) {
		
		SearchRequestBuilder builder = client.prepareSearch(index).setTypes(type).setSearchType(SearchType.DEFAULT);
		builder.setSource(ssb);
		SearchResponse response = builder.execute().actionGet();
		SearchHits hits = response.getHits();
		return hits;

	}
	
	/**
	 * 由于 setSource(jsonstr)等方法在5.0以后被移除,个人感觉很不方便,提供一个内联模板查询方式,
	 * @param index
	 * @param type
	 * @param template json 模板 
	 * @param params
	 * @return
	 * 
	 * 模板样式
	 * 
	 * {
		  "query": {
		    "match": {
		      "goodsname": {{query_string}}
		    }
		  },	"_source": ["id","goodsname"],
			"from": 0,
			"size": 5
		}
	 * 
	 */
	public static SearchHits queryByTemplate(String index, String type,String template,Map<String,Object> params ) {
		
		SearchResponse response=new SearchTemplateRequestBuilder(client)
        .setScript(template)
        .setScriptType(ScriptType.INLINE)    
        .setScriptParams(params)                  
        .setRequest(new SearchRequest())                   
        .get()                                             
        .getResponse();  
		
		SearchHits hits = response.getHits();
		return hits;

	}
}