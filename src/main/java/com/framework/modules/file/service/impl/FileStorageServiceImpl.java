package com.framework.modules.file.service.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.framework.common.utils.DateUtils;
import com.framework.common.utils.RedisUtils;
import com.framework.modules.file.entity.ChunkFile;
import com.framework.modules.file.service.FileStorageService;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.AppendFileStorageClient;
import com.github.tobato.fastdfs.service.FastFileStorageClient;

@Service
public class FileStorageServiceImpl implements FileStorageService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private FastFileStorageClient storageClient;

	@Autowired
	private AppendFileStorageClient appendStorageClient;

	@Autowired
	private RedisUtils redisUtils;

	@Override
	public void delete(String path) {
		storageClient.deleteFile(path);
	}

	@Override
	public StorePath saveFile(ChunkFile fileUpload) throws IOException {
		
		MultipartFile file=fileUpload.getFile();

		String extName = FilenameUtils.getExtension(file.getOriginalFilename());

		StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extName, null);
		return storePath;
	}

	@Override
	public boolean checkChunk(String md5, int chunk, long chunkSize) {

		boolean isExist = false;

		try {
			if (redisUtils.get(md5) == null || "".equals(redisUtils.get(md5))) {
				redisUtils.set(md5, this.getTempPath(md5));
			}

			File checkFile = new File(redisUtils.get(md5) + "/" + chunk);

			// 检查文件是否存在，且大小是否一致
			if (checkFile.exists() && checkFile.length() == chunkSize) {
				isExist = true;
			}
		} catch (Exception e) {
			logger.error("FileServiceImpl checkChunk:", e);
		}
		return isExist;
	}

	@Override
	public void saveChunk(ChunkFile fileUpload) throws IOException {

		MultipartFile file = fileUpload.getFile();
		
		int chunk = fileUpload.getChunk();

		File folder = new File(redisUtils.get(fileUpload.getFileMd5()));
		if (!folder.exists()) {
			folder.mkdir();
		}
		File tempfile = new File(redisUtils.get(fileUpload.getFileMd5()) + "/" + chunk);
		FileUtils.copyInputStreamToFile(file.getInputStream(), tempfile);
	}

	@Override
	public StorePath merge(String md5, String fileName) {

		File dic = new File(redisUtils.get(md5));
		
		if(!dic.exists()) {
			return null;
		}

		File[] fileArray = dic.listFiles(new FileFilter() {
			// 排除目录只要文件
			@Override
			public boolean accept(File pathname) {
				if (pathname.isDirectory()) {
					return false;
				}
				return true;
			}
		});

		// 转成集合，便于排序
		List<File> fileList = new ArrayList<File>(Arrays.asList(fileArray));
		Collections.sort(fileList, new Comparator<File>() {
			@Override
			public int compare(File o1, File o2) {
				if (Integer.parseInt(o1.getName()) < Integer.parseInt(o2.getName())) {
					return -1;
				}
				return 1;
			}
		});

		StorePath storePath = null;
		// 合并
		FileInputStream in = null;
		try {
			for (int i = 0; i < fileList.size(); i++) {

				File file = fileList.get(i);
				in = new FileInputStream(file);
				
				if (i == 0) {
					storePath = appendStorageClient.uploadAppenderFile(FileStorageService.FASTDFS_GROUP_NAME, in,
							file.length(), FilenameUtils.getExtension(fileName));
				} else {
					appendStorageClient.appendFile(FileStorageService.FASTDFS_GROUP_NAME, storePath.getPath(), in,
							file.length());
				}
				
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				// 删除分片
				// file.delete();
			}
			
			// 清除文件夹 和 临时文件
			new Thread(new Runnable() {
				@Override
				public void run() {
					FileUtils.deleteQuietly(dic);
					redisUtils.delete(md5);
				}
			}).start();
			
		} catch (Exception e) {
			logger.error("FileServiceImpl merge", e);
			//由于异常,造成dfs有垃圾数据 应删除
			if(storePath!=null) {
				this.delete(storePath.getFullPath());
			}
		}finally {
			if(in!=null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return storePath;
	}

	/**
	 * 获取 临时文件存储目录
	 * 
	 * @param md5
	 * @param chunk
	 * @return
	 */
	private String getTempPath(String md5) {

		StringBuffer sb = new StringBuffer("D://upload/");

		sb.append(DateUtils.format(new Date()));
		sb.append("/");
		sb.append(md5);

		return FilenameUtils.normalize(sb.toString(),true);
	}
}
