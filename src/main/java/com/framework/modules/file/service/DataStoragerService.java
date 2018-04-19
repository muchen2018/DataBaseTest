package com.framework.modules.file.service;

import com.framework.modules.file.entity.FileInfo;

/**
 * @author yuan
 */
public interface DataStoragerService {
	
	public FileInfo getFileInfo(String idOrMd5);
	
	public String save(FileInfo info);
	
	public int update(FileInfo info);
	
	public int delete(String ...id);

}
