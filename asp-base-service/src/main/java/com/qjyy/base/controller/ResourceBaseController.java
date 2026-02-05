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

import com.qjyy.base.domain.bo.ResourceBaseSaveBo;
import com.qjyy.base.domain.vo.ResourceBaseOptionVO;
import com.qjyy.base.domain.vo.ResourceBaseVO;
import com.qjyy.base.service.ResourceBaseService;
import com.qjyy.common.core.domain.R;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "资源基础数据")
@RestController
@RequiredArgsConstructor
@RequestMapping("/resource-bases")
public class ResourceBaseController {

	private final ResourceBaseService resourceBaseService;

	@ApiOperation(value = "创建资源基础数据")
	@PostMapping
	public R<Long> create(@RequestBody ResourceBaseSaveBo bo) {
		return R.ok(resourceBaseService.create(bo));
	}

	@ApiOperation(value = "更新资源基础数据")
	@PutMapping("/{id}")
	public R<Boolean> update(@PathVariable("id") Long id, @RequestBody ResourceBaseSaveBo bo) {
		return R.ok(resourceBaseService.update(id, bo));
	}

	@ApiOperation(value = "删除资源基础数据")
	@DeleteMapping("/{id}")
	public R<Boolean> delete(@PathVariable("id") Long id) {
		return R.ok(resourceBaseService.delete(id));
	}

	@ApiOperation(value = "资源基础数据详情")
	@GetMapping("/{id}")
	public R<ResourceBaseVO> detail(@PathVariable("id") Long id) {
		return R.ok(resourceBaseService.get(id));
	}

	@ApiOperation(value = "资源基础数据列表")
	@GetMapping("/list")
	public R<List<ResourceBaseVO>> list() {
		return R.ok(resourceBaseService.listAll());
	}

	@ApiOperation(value = "资源基础数据下拉")
	@GetMapping("/options")
	public R<List<ResourceBaseOptionVO>> options(@RequestParam(value = "keyword", required = false) String keyword) {
		return R.ok(resourceBaseService.listOptions(keyword));
	}
}
