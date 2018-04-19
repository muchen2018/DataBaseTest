package com.framework.modules.mq;

import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.framework.common.fulltext.ElasticSearchHandler;
import com.framework.modules.fulltext.entity.DiskDoc;

@Component
public class Receiver {
	
	@RabbitListener(queues = "hello.queue1")
	public void processMessage1(String msg) {
		System.out.println(Thread.currentThread().getName() + "--- 接受到------>" + msg);
		try {
			String id=UUID.randomUUID().toString();
			DiskDoc template=new DiskDoc();
			
			template.setId(id);
			template.setName(msg);
			template.setUrl("group1/M00/xxx/xxx2");
			template.setPath("我的文档/我的测试文档");
			
			/*if("docx".equals(msg)) {
				File f=new File("C:\\Users\\26371\\Desktop\\苏州工业园区政务通技术架构管理及应用插件开发规范接口规范V2.1.docx");
				FileInputStream in=new FileInputStream(f);
				Attachment att = AttachmentProcessor.parse(in);
				template.setAttachment(att);
			}
			*/
			
			ElasticSearchHandler.addDoc(ElasticSearchHandler.DEFAULT_INDEX,ElasticSearchHandler.DEFAULT_TYPE,template);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + "-----完成处理------->" + msg);
	}

}
