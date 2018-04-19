package com.framework.common.fulltext;

import java.util.UUID;

/**
 * 基础 document 模型
 * @author yuan
 */
public class BaseDoc {

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public BaseDoc() {
		this.id=UUID.randomUUID().toString();
	}
	
	public BaseDoc(String id) {
		this.id=id;
	}
}
