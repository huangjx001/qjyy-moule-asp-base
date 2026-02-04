package com.qjyy.base.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qjyy.base.domain.vo.ProcessNodeSummaryVO;
import com.qjyy.base.service.ProcessGraphService;
import com.qjyy.common.core.domain.R;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "工序图汇总")
@RestController
@RequiredArgsConstructor
@RequestMapping("/versions")
public class ProcessOpsGraphController {

	private final ProcessGraphService processGraphService;

	@ApiOperation(value = "工序图摘要")
	@GetMapping("/{id}/ops-graph")
	public R<List<ProcessNodeSummaryVO>> summary(@PathVariable("id") Long id) {
		return R.ok(processGraphService.listNodeSummary(id));
	}
}
