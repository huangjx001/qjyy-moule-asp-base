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

import com.qjyy.base.domain.bo.ProcessBaseSaveBo;
import com.qjyy.base.domain.vo.ProcessBaseOptionVO;
import com.qjyy.base.domain.vo.ProcessBaseVO;
import com.qjyy.base.service.ProcessBaseService;
import com.qjyy.common.core.domain.R;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "工序基础数据")
@RestController
@RequiredArgsConstructor
@RequestMapping("/process-bases")
public class ProcessBaseController {

	private final ProcessBaseService processBaseService;

	@ApiOperation(value = "创建工序基础数据")
	@PostMapping
	public R<Long> create(@RequestBody ProcessBaseSaveBo bo) {
		return R.ok(processBaseService.create(bo));
	}

	@ApiOperation(value = "更新工序基础数据")
	@PutMapping("/{id}")
	public R<Boolean> update(@PathVariable("id") Long id, @RequestBody ProcessBaseSaveBo bo) {
		return R.ok(processBaseService.update(id, bo));
	}

	@ApiOperation(value = "删除工序基础数据")
	@DeleteMapping("/{id}")
	public R<Boolean> delete(@PathVariable("id") Long id) {
		return R.ok(processBaseService.delete(id));
	}

	@ApiOperation(value = "工序基础数据详情")
	@GetMapping("/{id}")
	public R<ProcessBaseVO> detail(@PathVariable("id") Long id) {
		return R.ok(processBaseService.get(id));
	}

	@ApiOperation(value = "工序基础数据列表")
	@GetMapping("/list")
	public R<List<ProcessBaseVO>> list() {
		return R.ok(processBaseService.listAll());
	}

	@ApiOperation(value = "工序基础数据下拉")
	@GetMapping("/options")
	public R<List<ProcessBaseOptionVO>> options(@RequestParam(value = "keyword", required = false) String keyword) {
		return R.ok(processBaseService.listOptions(keyword));
	}
}
