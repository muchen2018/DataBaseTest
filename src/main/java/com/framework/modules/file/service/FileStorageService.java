package com.framework.modules.file.service;

import java.io.IOException;

import com.framework.modules.file.entity.ChunkFile;
import com.github.tobato.fastdfs.domain.StorePath;

/**
 * 文件 处理
 * @author yuan
 */
public interface FileStorageService {
	
	/**
	 * 默认group
	 */
	String FASTDFS_GROUP_NAME="group1";
	
	/**
	 * 删除
	 * @param path
	 */
	public void delete(String path);
	
	/**
	 * 上传 (不分片 不支持断点续传)
	 * @param ChunkFile fileUpload
	 * @return
	 */
	public StorePath saveFile(ChunkFile fileUpload)throws IOException;
	
	/**
	 * 判断 当前分片是否存储
	 * @param md5 文件唯一MD5
	 * @param chunk 当前分片下标 (从0开始)
	 * @param chunkSize 当前分片 大小
	 * @return 
	 */
	public boolean checkChunk(String md5,int chunk,long chunkSize);
	
	/**
	 * 保存 分片文件
	 * @param fileUpload
	 */
	public void saveChunk(ChunkFile fileUpload)throws IOException;
	
	
	/**
	 * 合并文件
	 * @param md5
	 * @param fileName
	 * @return
	 */
	public StorePath merge(String md5,String fileName);

}
