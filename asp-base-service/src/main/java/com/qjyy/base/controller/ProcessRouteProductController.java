package com.qjyy.base.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qjyy.base.domain.bo.ProcessRouteProductSaveBo;
import com.qjyy.base.domain.vo.ProcessRouteProductVO;
import com.qjyy.base.domain.vo.ProcessRouteVO;
import com.qjyy.base.service.ProcessRouteProductService;
import com.qjyy.common.core.domain.R;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "路线-产品关联")
@RestController
@RequiredArgsConstructor
@RequestMapping("/route-products")
public class ProcessRouteProductController {

	private final ProcessRouteProductService routeProductService;

	@ApiOperation(value = "创建路线关联")
	@PostMapping
	public R<Long> create(@RequestBody ProcessRouteProductSaveBo bo) {
		return R.ok(routeProductService.create(bo));
	}

	@ApiOperation(value = "更新路线关联")
	@PutMapping("/{id}")
	public R<Boolean> update(@PathVariable("id") Long id, @RequestBody ProcessRouteProductSaveBo bo) {
		return R.ok(routeProductService.update(id, bo));
	}

	@ApiOperation(value = "删除路线关联")
	@DeleteMapping("/{id}")
	public R<Boolean> delete(@PathVariable("id") Long id) {
		return R.ok(routeProductService.delete(id));
	}

	@ApiOperation(value = "路线关联列表")
	@GetMapping("/list")
	public R<List<ProcessRouteProductVO>> list(
			@RequestParam(value = "productCode", required = false) String productCode) {
		return R.ok(routeProductService.list(productCode));
	}

	@ApiOperation(value = "解析路线")
	@GetMapping("/resolve")
	public R<ProcessRouteVO> resolve(@RequestParam(value = "productCode", required = false) String productCode) {
		return R.ok(routeProductService.resolveRoute(productCode));
	}
}
