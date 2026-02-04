package com.qjyy.base.controller;

import javax.validation.constraints.NotBlank;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qjyy.base.domain.vo.Nc65BomTreeVO;
import com.qjyy.base.service.Nc65BomQueryService;
import com.qjyy.common.core.domain.R;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "NC65 BOM查询")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/nc65/bom")
public class Nc65BomQueryController {

	private final Nc65BomQueryService nc65BomQueryService;

	@ApiOperation(value = "根据物料编码查询BOM树（多版本->子项->替代料）")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "mlId", value = "物料编码", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "onlyDefault", value = "是否只返回默认版本（hbdefault=1）", required = false, dataType = "boolean", paramType = "query"),
			@ApiImplicitParam(name = "includeRepl", value = "是否包含替代料信息", required = false, dataType = "boolean", paramType = "query") })
	@GetMapping("/tree")
	public R<Nc65BomTreeVO> queryBomTree(@RequestParam("mlId") @NotBlank(message = "mlId不能为空") String mlId,
			@RequestParam(value = "onlyDefault", required = false, defaultValue = "false") Boolean onlyDefault,
			@RequestParam(value = "includeRepl", required = false, defaultValue = "true") Boolean includeRepl) {

		Nc65BomTreeVO tree = nc65BomQueryService.queryBomTree(mlId, onlyDefault, includeRepl);
		return R.ok(tree);
	}
}
