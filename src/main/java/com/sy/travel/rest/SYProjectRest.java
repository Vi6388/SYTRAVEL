package com.sy.travel.rest;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sy.travel.common.AjaxResult;
import com.sy.travel.service.SYProjectService;
import com.sy.travel.utils.JSON;

/**
 * 项目模块的接口
 * 
 * @author liuxin
 *
 */
@RestController
@RequestMapping(value = "/sy/project", produces = MediaType.APPLICATION_JSON_VALUE)
public class SYProjectRest {
	@Autowired
	private SYProjectService syProjectService;

	/**
	 * 查看所有项目的信息的接口
	 * 根据项目名称模糊查询这个项目的信息
	 * 当项目名称为空时，返回的是所有项目的信息
	 * 
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public AjaxResult<Map<String, Object>> all(
			@RequestParam(defaultValue = "") String name,
			@RequestParam(defaultValue = "1") int currentPage,
			@RequestParam(defaultValue = "10") int pageSize
			) {
		Map<String, Object> data = syProjectService.queryAll(name, currentPage, pageSize);
		return new AjaxResult<Map<String, Object>>(200, "success", data);
	}

	/**
	 * 查询这个项目的详细信息
	 * 这个项目下的团队信息以及产品信息
	 * @return
	 */
	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public AjaxResult<Map<String, Object>> info(@RequestParam(defaultValue = "0") Integer id) {
		return new AjaxResult<Map<String, Object>>(200, "success", syProjectService.info(id));
	}
	/**
	 * 添加项目信息的接口
	 * @param json		以json的形式传入；需要加入@RequestBody
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST, consumes = "application/json")
	public AjaxResult<String> add(@RequestBody JSON json, HttpServletRequest request) {
		return syProjectService.add(json, request.getRemoteUser());
	}
	
	/**
	 * 删除项目信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public AjaxResult<String> delete(@RequestParam("id") Integer id, @RequestParam("operator") String operator, HttpServletRequest request){
		return syProjectService.deleteById(id, operator);
	}
	
	/**
	 * 根据id更新这个项目的信息
	 * @param json
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST, consumes = "application/json")
	public AjaxResult<String> update(@RequestBody JSON json, HttpServletRequest request){
		return syProjectService.update(json, request.getRemoteUser());
	}
}
