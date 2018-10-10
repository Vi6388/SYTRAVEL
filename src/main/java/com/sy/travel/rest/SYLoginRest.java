package com.sy.travel.rest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sy.travel.common.AjaxResult;
import com.sy.travel.service.SYLoginService;
import com.sy.travel.utils.JSON;

/**
 * 登陆接口
 * @author liuxin
 *
 */
@RestController
@RequestMapping(value = "/sy/login", produces=MediaType.APPLICATION_JSON_VALUE)
public class SYLoginRest {
	@Autowired
	private SYLoginService syLoginService;
	
	/**
	 * 检测登陆用户信息是否正确
	 */
	@RequestMapping(value="/check", method=RequestMethod.POST, consumes="application/json")
	public AjaxResult<String> loginCheck(@RequestBody JSON json) {
		return syLoginService.loginCheck(json);
	}
}
