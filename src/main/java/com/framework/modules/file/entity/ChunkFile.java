package com.framework.modules.file.entity;

import org.springframework.web.multipart.MultipartFile;

public class ChunkFile extends BaseFile{
	
	/**
	 * 是否分片上传
	 */
	private boolean chunked;

	/**
	 * 分片大小
	 */
	private int chunkSize;
	
	/**
	 * 当前分片 下标
	 */
	private int chunk;
	
	/**
	 * 分片 文件
	 */
	private MultipartFile file;
	

	public boolean isChunked() {
		return chunked;
	}

	public void setChunked(boolean chunked) {
		this.chunked = chunked;
	}

	public int getChunkSize() {
		return chunkSize;
	}

	public void setChunkSize(int chunkSize) {
		this.chunkSize = chunkSize;
	}

	public int getChunk() {
		return chunk;
	}

	public void setChunk(int chunk) {
		this.chunk = chunk;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("File [chunked=");
		builder.append(chunked);
		builder.append(", chunkSize=");
		builder.append(chunkSize);
		builder.append(", chunk=");
		builder.append(chunk);
		builder.append(", file=");
		builder.append(file);
		builder.append("]");
		return builder.toString();
	}
	
}
