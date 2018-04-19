package com.framework.modules.fulltext.entity;

import com.framework.common.fulltext.BaseDoc;
import com.framework.common.fulltext.attachment.Attachment;
import com.framework.common.fulltext.attachment.AttachmentExt;

public class DiskDoc extends BaseDoc{
	
	private String url;
	
	private String name;
	
	private String userId;
	
	private String path;
	
	private Attachment<? extends AttachmentExt> attachment;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Attachment<? extends AttachmentExt> getAttachment() {
		return attachment;
	}
	
	public void setAttachment(Attachment<? extends AttachmentExt> attachment) {
		this.attachment = attachment;
	}
}
