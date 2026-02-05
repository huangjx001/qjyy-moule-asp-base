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

import com.qjyy.base.domain.bo.ProductBaseSaveBo;
import com.qjyy.base.domain.vo.ProductBaseOptionVO;
import com.qjyy.base.domain.vo.ProductBaseVO;
import com.qjyy.base.service.ProductBaseService;
import com.qjyy.common.core.domain.R;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "产品基础数据")
@RestController
@RequiredArgsConstructor
@RequestMapping("/product-bases")
public class ProductBaseController {

	private final ProductBaseService productBaseService;

	@ApiOperation(value = "创建产品基础数据")
	@PostMapping
	public R<Long> create(@RequestBody ProductBaseSaveBo bo) {
		return R.ok(productBaseService.create(bo));
	}

	@ApiOperation(value = "更新产品基础数据")
	@PutMapping("/{id}")
	public R<Boolean> update(@PathVariable("id") Long id, @RequestBody ProductBaseSaveBo bo) {
		return R.ok(productBaseService.update(id, bo));
	}

	@ApiOperation(value = "删除产品基础数据")
	@DeleteMapping("/{id}")
	public R<Boolean> delete(@PathVariable("id") Long id) {
		return R.ok(productBaseService.delete(id));
	}

	@ApiOperation(value = "产品基础数据详情")
	@GetMapping("/{id}")
	public R<ProductBaseVO> detail(@PathVariable("id") Long id) {
		return R.ok(productBaseService.get(id));
	}

	@ApiOperation(value = "产品基础数据列表")
	@GetMapping("/list")
	public R<List<ProductBaseVO>> list() {
		return R.ok(productBaseService.listAll());
	}

	@ApiOperation(value = "产品基础数据下拉")
	@GetMapping("/options")
	public R<List<ProductBaseOptionVO>> options(@RequestParam(value = "keyword", required = false) String keyword) {
		return R.ok(productBaseService.listOptions(keyword));
	}
}
