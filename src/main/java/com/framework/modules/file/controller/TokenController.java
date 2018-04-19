package com.framework.modules.file.controller;

import java.io.IOException;
import java.util.Map;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.framework.common.utils.R;
import com.framework.modules.sys.entity.SysUserEntity;
import com.framework.modules.sys.service.SysUserService;
import com.framework.modules.sys.service.SysUserTokenService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/")
@Api(tags = { "获取token接口" })
public class TokenController {

	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserTokenService sysUserTokenService;
	
	@ApiOperation("获取token")
	@RequestMapping(value = "/sys/getToken", method = RequestMethod.POST)
	public Map<String, Object> login(String username, String password)throws IOException {

		//用户信息
		SysUserEntity user = sysUserService.queryByUserName(username);

		//账号不存在、密码错误
		if(user == null || !user.getPassword().equals(new Sha256Hash(password, user.getSalt()).toHex())) {
			return R.error("账号或密码不正确");
		}

		//账号锁定
		if(user.getStatus() == 0){
			return R.error("账号已被锁定,请联系管理员");
		}

		//生成token，并保存到数据库
		R r = sysUserTokenService.createToken(user.getUserId());
		return r;
	}
}
