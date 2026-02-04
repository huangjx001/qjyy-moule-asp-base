package com.qjyy.base.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qjyy.base.domain.bo.ProcessGraphSaveBo;
import com.qjyy.base.domain.vo.GraphValidationResultVO;
import com.qjyy.base.domain.vo.ProcessGraphVO;
import com.qjyy.base.service.ProcessGraphService;
import com.qjyy.common.core.domain.R;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "工序画布")
@RestController
@RequiredArgsConstructor
@RequestMapping("/graphs/process")
public class ProcessGraphController {

	private final ProcessGraphService processGraphService;

	@ApiOperation(value = "获取工序图")
	@GetMapping("/{routeVersionId}")
	public R<ProcessGraphVO> get(@PathVariable("routeVersionId") Long routeVersionId) {
		return R.ok(processGraphService.getGraph(routeVersionId));
	}

	@ApiOperation(value = "保存工序图")
	@PostMapping("/save")
	public R<Boolean> save(@RequestBody ProcessGraphSaveBo bo) {
		return R.ok(processGraphService.saveGraph(bo));
	}

	@ApiOperation(value = "校验工序图")
	@PostMapping("/validate")
	public R<GraphValidationResultVO> validate(@RequestBody ProcessGraphSaveBo bo) {
		return R.ok(processGraphService.validateGraph(bo));
	}
}
