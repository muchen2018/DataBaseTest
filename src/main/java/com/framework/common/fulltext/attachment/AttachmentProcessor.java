package com.framework.common.fulltext.attachment;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.HttpHeaders;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.Office;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import com.alibaba.fastjson.JSON;

/**
 * 文件内容提取工具类  
 * @author yuan
 */
public class AttachmentProcessor {
	
	private AttachmentProcessor() {
		
	}

	private static Logger logger = LoggerFactory.getLogger(AttachmentProcessor.class);

	public static Attachment<? extends AttachmentExt> parse(InputStream is) throws Exception, SAXException, TikaException {

		Attachment<? extends AttachmentExt> att = null;

		Parser parser = new AutoDetectParser();
		Metadata metadata = new Metadata();

		ContentHandler handler = new BodyContentHandler(-1);
		ParseContext context = new ParseContext();
		context.set(Parser.class, parser);
		parser.parse(is, handler, metadata, context);

		String contentType = metadata.get(HttpHeaders.CONTENT_TYPE);
		

		if (!StringUtils.isBlank(contentType) && contentType.indexOf("image") != -1) {
			
			AttachmentImage image=new AttachmentImage();
			image.setHeight(Long.valueOf(metadata.get(Metadata.IMAGE_LENGTH)));
			image.setWidth(Long.valueOf(metadata.get(Metadata.IMAGE_WIDTH)));
			att  = new Attachment<AttachmentImage>(image);
		} else if (!StringUtils.isBlank(contentType) && contentType.indexOf("officedocument") != -1) {

			AttachmentOffice office=new AttachmentOffice();
			String pageCount = metadata.get(Office.PAGE_COUNT);
			if (pageCount == null) {
				pageCount = metadata.get(Office.SLIDE_COUNT);
			}
			office.setPageCount(Long.valueOf(pageCount));
			office.setWordCount(Long.valueOf(metadata.get(Office.WORD_COUNT)));
			att  = new Attachment<AttachmentOffice>(office);
		}else {
			att=new Attachment<>();
		}
		att.setContent(contentType);
		// 内容
		att.setContent(handler.toString().trim());
		// 创建日期
		String createdDate = metadata.get(TikaCoreProperties.CREATED);
		att.setCreationDate(createdDate);

		String lastModifiedDate = metadata.get(TikaCoreProperties.MODIFIED);
		att.setLastModifiedDate(lastModifiedDate);

		// title
		String title = metadata.get(TikaCoreProperties.TITLE);
		att.setTitle(title);

		String author = metadata.get(TikaCoreProperties.CREATOR);
		att.setAuthor(author);

		String lastAuthor = metadata.get(TikaCoreProperties.MODIFIER);
		att.setLastAuthor(lastAuthor);

		String keywords = metadata.get(TikaCoreProperties.KEYWORDS);
		att.setKeywords(keywords);

		String contentLength = metadata.get(HttpHeaders.CONTENT_LENGTH);
		long length;
		if (!StringUtils.isBlank(contentLength)) {
			length = Long.parseLong(contentLength);
		} else {
			length = handler.toString().length();
		}
		att.setContentLength(Long.valueOf(length));

		for (String name : metadata.names()) {
			logger.info("{}:{}", name, metadata.get(name));
		}

		return att;
	}


	public static void main(String[] a) throws SAXException, TikaException, Exception {

		File file = new File("C:\\Users\\26371\\Desktop\\29所\\29所系统意见.pptx");

		FileInputStream in = new FileInputStream(file);

		Attachment<? extends AttachmentExt> att = AttachmentProcessor.parse(in);

		System.out.println(JSON.toJSONString(att));
	}
}
