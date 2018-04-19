package com.framework.modules.file.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.bcel.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.framework.common.utils.R;
import com.framework.modules.file.entity.ChunkFile;
import com.framework.modules.file.service.FileStorageService;
import com.framework.modules.file.util.ProtoCommon;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.proto.storage.DownloadCallback;
import com.github.tobato.fastdfs.service.FastFileStorageClient;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/file")
@Api(tags = { "文件接口" })
public class UploadController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private FastFileStorageClient storageClient;

	@Autowired
	private FileStorageService fileService;

	@Value("${fdfs.base-url}")
	private String baseUrl;

	@Value("${fdfs.secret-key}")
	private String secretKey;

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ApiOperation("删除")
	public R delete(@ApiParam("路径") @RequestParam String path) {

		try {
			fileService.delete(path);
		} catch (Exception e) {
			logger.warn("FileController delete", e);
		}
		return R.ok();
	}

	/*
	 * @RequestMapping(value = "/chunkUpload", method = RequestMethod.POST)
	 * 
	 * @ApiOperation("分片上传(chunk 必须有序,从0开始 )") public synchronized void
	 * chunkUpload(ChunkUpload chunkUpload) throws IOException, InterruptedException
	 * {
	 * 
	 * MultipartFile file = chunkUpload.getFile();
	 * 
	 * String tempId = file.getOriginalFilename() + file.getSize();
	 * 
	 * String path = map.get(file.getOriginalFilename() + file.getSize());
	 * 
	 * if (StringUtils.isBlank(path)) {
	 * 
	 * StorePath storePath = appendStorageClient.uploadAppenderFile(GROUP_NAME,
	 * file.getInputStream(), file.getSize(),
	 * FilenameUtils.getExtension(file.getOriginalFilename())); map.put(tempId,
	 * storePath.getPath());
	 * 
	 * } else { appendStorageClient.appendFile(GROUP_NAME, path,
	 * file.getInputStream(), file.getSize()); } }
	 */

	@ApiOperation("上传")
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public R upload(ChunkFile fileUpload) {

		try {
			boolean chunked = fileUpload.isChunked();

			// if(chunked) {
			fileService.saveChunk(fileUpload);
			// }else {
			// StorePath sp=fileService.saveFile(fileUpload);

			// return R.ok().put("storePath", sp);
			// }

		} catch (IOException e) {
			logger.error("FileController saveChunk:", e);
			return R.error();
		}
		return R.ok();
	}

	@RequestMapping(value = "/checkChunk", method = RequestMethod.POST)
	@ApiOperation("验证分片是否存在")
	public R checkChunk(@RequestParam(required = true) @ApiParam("文件唯一md5码") String fileMd5,
			@ApiParam("分片下标") int chunk, @ApiParam("分片大小") long chunkSize) {

		boolean isExist = fileService.checkChunk(fileMd5, chunk, chunkSize);

		return R.ok().put("isExist", isExist);
	}

	@ApiOperation("分片合并")
	@RequestMapping(value = "/merge", method = RequestMethod.POST)
	public R merge(@RequestParam(required = true) @ApiParam("文件唯一md5码") String fileMd5,
			@RequestParam(required = true) @ApiParam("文件名") String fileName) {

		StorePath sp = fileService.merge(fileMd5, fileName);

		if (sp == null) {
			return R.error();
		}
		return R.ok().put("storePath", sp);
	}

	/**
	 * 获取访问服务器的token，拼接到地址后面
	 *
	 * @param filepath
	 *            /M00/00/00/wKgzgFnkTPyAIAUGAAEoRmXZPp876.jpeg
	 * @param httpSecretKey
	 *            密钥
	 * @return 返回token，如： token=078d370098b03e9020b82c829c205e1f&ts=1508141521
	 */
	@ApiOperation("获取访问路径")
	@RequestMapping(value = "/getUrl", method = RequestMethod.GET)
	public String getToken(@RequestParam(required = true) @ApiParam("分组") String groupName,
			@RequestParam(required = true) @ApiParam("文件路径") String filepath) {
		// unix seconds
		int ts = (int) Instant.now().getEpochSecond();
		// token
		String token = "null";
		try {
			token = ProtoCommon.getToken(filepath, ts, secretKey);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		StringBuilder sb = new StringBuilder();
		sb.append(baseUrl);
		sb.append(groupName);
		sb.append("/");
		sb.append(filepath);
		sb.append("?token=").append(token);
		sb.append("&ts=").append(ts);

		return sb.toString();
	}

	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public void download(String group, String path, String fileName, HttpServletResponse response) {

		response.reset();
		try {
			fileName = URLEncoder.encode(fileName, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
		response.setHeader("Connection", "close");
		response.setHeader("Content-Type", "application/octet-stream");

		OutputStream out = null;

		try {
			byte[] file = storageClient.downloadFile(group, path, new DownloadCallback<byte[]>() {
				@Override
				public byte[] recv(InputStream ins) throws IOException {
					return IOUtils.toByteArray(ins);
				}
			});
			out = response.getOutputStream();
			out.write(file);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
			}
		}
	}
}
