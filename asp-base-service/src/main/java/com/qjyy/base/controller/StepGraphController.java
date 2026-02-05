package com.qjyy.base.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qjyy.base.domain.bo.StepGraphSaveBo;
import com.qjyy.base.domain.vo.GraphValidationResultVO;
import com.qjyy.base.domain.vo.StepGraphVO;
import com.qjyy.base.service.StepGraphService;
import com.qjyy.common.core.domain.R;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "工步画布")
@RestController
@RequiredArgsConstructor
@RequestMapping("/graphs/step")
public class StepGraphController {

	private final StepGraphService stepGraphService;

	@ApiOperation(value = "获取工步图")
	@GetMapping("/{resourceRoomId}")
	public R<StepGraphVO> get(@PathVariable("resourceRoomId") Long resourceRoomId) {
		return R.ok(stepGraphService.getGraph(resourceRoomId));
	}

	@ApiOperation(value = "保存工步图")
	@PostMapping("/save")
	public R<Boolean> save(@RequestBody StepGraphSaveBo bo) {
		return R.ok(stepGraphService.saveGraph(bo));
	}

	@ApiOperation(value = "校验工步图")
	@PostMapping("/validate")
	public R<GraphValidationResultVO> validate(@RequestBody StepGraphSaveBo bo) {
		return R.ok(stepGraphService.validateGraph(bo));
	}
}
