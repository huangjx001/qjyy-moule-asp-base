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

import com.qjyy.base.domain.bo.StepBaseSaveBo;
import com.qjyy.base.domain.vo.StepBaseOptionVO;
import com.qjyy.base.domain.vo.StepBaseVO;
import com.qjyy.base.service.StepBaseService;
import com.qjyy.common.core.domain.R;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "工步基础数据")
@RestController
@RequiredArgsConstructor
@RequestMapping("/step-bases")
public class StepBaseController {

	private final StepBaseService stepBaseService;

	@ApiOperation(value = "创建工步基础数据")
	@PostMapping
	public R<Long> create(@RequestBody StepBaseSaveBo bo) {
		return R.ok(stepBaseService.create(bo));
	}

	@ApiOperation(value = "更新工步基础数据")
	@PutMapping("/{id}")
	public R<Boolean> update(@PathVariable("id") Long id, @RequestBody StepBaseSaveBo bo) {
		return R.ok(stepBaseService.update(id, bo));
	}

	@ApiOperation(value = "删除工步基础数据")
	@DeleteMapping("/{id}")
	public R<Boolean> delete(@PathVariable("id") Long id) {
		return R.ok(stepBaseService.delete(id));
	}

	@ApiOperation(value = "工步基础数据详情")
	@GetMapping("/{id}")
	public R<StepBaseVO> detail(@PathVariable("id") Long id) {
		return R.ok(stepBaseService.get(id));
	}

	@ApiOperation(value = "工步基础数据列表")
	@GetMapping("/list")
	public R<List<StepBaseVO>> list() {
		return R.ok(stepBaseService.listAll());
	}

	@ApiOperation(value = "工步基础数据下拉")
	@GetMapping("/options")
	public R<List<StepBaseOptionVO>> options(@RequestParam(value = "keyword", required = false) String keyword) {
		return R.ok(stepBaseService.listOptions(keyword));
	}
}
