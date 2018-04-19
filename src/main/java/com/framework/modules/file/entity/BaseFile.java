package com.framework.modules.file.entity;

/**
 * 文件
 * @author yuan
 */
public class BaseFile {
	
	private String id;
	
	private String fileMd5;
	
	private long size;
	
	private String accessPath;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getFileMd5() {
		return fileMd5;
	}

	public void setFileMd5(String fileMd5) {
		this.fileMd5 = fileMd5;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BaseFile [fileMd5=");
		builder.append(fileMd5);
		builder.append(", size=");
		builder.append(size);
		builder.append("]");
		return builder.toString();
	}

}
