package com.framework.common.fulltext.attachment;

/**
 * 针对office
 * 
 * @author yuan
 */
public class AttachmentOffice extends AttachmentExt{

	private long pageCount;
	
	private long wordCount;

	public long getPageCount() {
		return pageCount;
	}

	public void setPageCount(long pageCount) {
		this.pageCount = pageCount;
	}

	public long getWordCount() {
		return wordCount;
	}

	public void setWordCount(long wordCount) {
		this.wordCount = wordCount;
	}
}
