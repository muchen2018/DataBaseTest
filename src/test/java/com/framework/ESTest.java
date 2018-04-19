package com.framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.tika.exception.TikaException;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.xml.sax.SAXException;

import com.framework.common.fulltext.ElasticSearchHandler;
import com.framework.common.fulltext.attachment.Attachment;
import com.framework.common.fulltext.attachment.AttachmentProcessor;
import com.framework.modules.fulltext.entity.DiskDoc;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ESTest {

	//@Test
	public void addDoc() throws SAXException, TikaException, Exception {
		
		String id=UUID.randomUUID().toString();
		DiskDoc template=new DiskDoc();
		
		template.setId(id);
		template.setName("11.png");
		template.setUrl("group1/M00/xxx/xxx2");
		template.setPath("我的文档/我的测试文档");
		
		File f=new File("C:\\Users\\26371\\Desktop\\Spring Cloud微服务实战_PDF电子书下载 高清 带索引书签目录_翟永超(著)  @www.java1234.com.pdf");
		
		FileInputStream in=new FileInputStream(f);
		
		/*byte [] b= new byte[in.available()];
		
		in.read(b);
		
		file.setData(b);
		
		in.close();
		
		String json=JSON.toJSONString(file);
		ElasticSearchHandler.addData(json,"myindex", "file", id,"single_attachment");
		*/
		
		Attachment att =AttachmentProcessor.parse(in);
		
		template.setAttachment(att);
		
		ElasticSearchHandler.addDoc(ElasticSearchHandler.DEFAULT_INDEX,ElasticSearchHandler.DEFAULT_TYPE,template);
	}
	
	//@Test
	public void addDocBulk() {
		
		List<DiskDoc> list=new ArrayList<>();
		
		for(int i=0;i<1000;i++) {
			DiskDoc doc=new DiskDoc();
			doc.setName("批量bulk:"+i);
			list.add(doc);
		}
		
		ElasticSearchHandler.addDocBulk(ElasticSearchHandler.DEFAULT_INDEX,ElasticSearchHandler.DEFAULT_TYPE, list);
	}
	
	@Test
	public void mapping() {
		
		String str = "{\"shop\":{\"properties\":{\"shopName\":{\"type\":\"text\",\"analyzer\":\"ik_max_word\",\"search_analyzer\":\"ik_smart\"},\"shopId\":{\"type\":\"keyword\"},\"listGoods.id\":{\"type\":\"keyword\"},\"listGoods.shopId\":{\"type\":\"keyword\"},\"listGoods.goodsname\":{\"type\":\"text\",\"analyzer\":\"ik_max_word\",\"search_analyzer\":\"ik_smart\"}}}}";
		
			String str2="{\"shop\":{\"_all\":{\"analyzer\":\"ik_max_word\",\"search_analyzer\":\"ik_max_word\",\"term_vector\":\"no\",\"store\":\"false\"},"
					+ "\"properties\":{\"id\":{\"type\":\"keyword\",\"index\":\"not_analyzed\"},"
					+ "\"shopPicture\":{\"type\":\"keyword\",\"index\":\"not_analyzed\"},"
					+ "\"location\":{\"type\":\"geo_point\",\"index\":\"not_analyzed\"},"
					+ "\"hot\":{\"type\":\"double\",\"index\":\"not_analyzed\"},"
					+ "\"crdate\":{\"type\":\"double\",\"index\":\"not_analyzed\"}}}}";
			
		String file="{\""+ElasticSearchHandler.DEFAULT_TYPE+"\":{\"_source\":{\"excludes\":[\"attachment.content\"]},\"properties\":{\"name\":{\"type\":\"text\",\"analyzer\":\"ik_max_word\",\"search_analyzer\":\"ik_smart\"},\"id\":{\"type\":\"keyword\"},\"userId\":{\"type\":\"keyword\"},\"url\":{\"type\":\"keyword\"},\"path\":{\"type\":\"keyword\"},\"attachment.content\":{\"type\":\"text\",\"analyzer\":\"ik_max_word\",\"search_analyzer\":\"ik_smart\"},\"attachment.keywords\":{\"type\":\"text\",\"analyzer\":\"ik_max_word\",\"search_analyzer\":\"ik_smart\"},\"attachment.contentType\":{\"type\":\"keyword\"},\"attachment.author\":{\"type\":\"keyword\"},\"attachment.lastAuthor\":{\"type\":\"keyword\"},\"attachment.creationDate\":{\"type\":\"date\"},\"attachment.lastModifiedDate\":{\"type\":\"date\"}}}}";
		
		ElasticSearchHandler.createMappingSource(ElasticSearchHandler.DEFAULT_INDEX,ElasticSearchHandler.DEFAULT_TYPE, file);
	}
	
	/**
	 * <pre>
	 {
		  "query": {
		    "bool": {
		      "must": [
		        {
		          "term": {
		          "id": "9ab3b9f9-33ce-455a-bf97-3d273d0f5e3f"
		          }
		        },{
		          "multi_match": {
		            "query": "zhaoxl",
		            "fields": ["attachment.author","attachment.lastAuthor"]
		          }
		        }
		      ]
		    }
		  }
		 }
	 * </pre>
	 * @throws IOException 
	 */
	
	@Test
	public void queryTemplate() throws IOException {
		
		
		String template_str="{\"query\":{\"bool\":{\"must\":[{\"term\":{\"id\":\"{{id}}\"}},{\"multi_match\":{\"query\":\"{{keyword}}\",\"fields\":[\"attachment.author\",\"attachment.lastAuthor\"]}}]}}}";
		
		Map <String,Object> map=new HashMap<>(4);
		map.put("keyword", "zhaoxl");
		map.put("id", "9ab3b9f9-33ce-455a-bf97-3d273d0f5e3f");
		SearchHits hits=ElasticSearchHandler.queryByTemplate(ElasticSearchHandler.DEFAULT_INDEX,ElasticSearchHandler.DEFAULT_TYPE, template_str,map);
		
		hits.forEach(hit->System.out.println(hit.getSourceAsMap()));
		
		
	}
	
	@Test
	public void queryJsonStr() throws IOException {
		
		String queryStr="{\"bool\":{\"must\":[{\"term\":{\"id\":\"9ab3b9f9-33ce-455a-bf97-3d273d0f5e3f\"}},{\"multi_match\":{\"query\":\"zhaoxl\",\"fields\":[\"attachment.author\",\"attachment.lastAuthor\"]}}]}}";
		
		SearchHits hits=ElasticSearchHandler.queryByJson(ElasticSearchHandler.DEFAULT_INDEX,ElasticSearchHandler.DEFAULT_TYPE, queryStr);
		
		System.out.println(hits.totalHits);
		
	}
	
	@Test
	public void searchSourceBuilder() throws IOException {
		
		String queryStr="{\"bool\":{\"must\":[{\"term\":{\"id\":\"9ab3b9f9-33ce-455a-bf97-3d273d0f5e3f\"}},{\"multi_match\":{\"query\":\"zhaoxl\",\"fields\":[\"attachment.author\",\"attachment.lastAuthor\"]}}]}}";
		
		SearchSourceBuilder ssb=SearchSourceBuilder.searchSource();
		
		ssb.query(QueryBuilders.wrapperQuery(queryStr));
		
		String [] in = {"path","name"};
		
		ssb.fetchSource(in, null);
		
		SearchHits hits=ElasticSearchHandler.queryBySearchSourceBuilder(ElasticSearchHandler.DEFAULT_INDEX,ElasticSearchHandler.DEFAULT_TYPE, ssb);
		
		hits.forEach(hit->System.out.println(hit.getSourceAsMap()));
		
		
	}
	
}
