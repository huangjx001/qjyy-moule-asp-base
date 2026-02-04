package com.qjyy.base.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qjyy.base.domain.bo.ProcessRouteSaveBo;
import com.qjyy.base.domain.vo.ProcessRouteVO;
import com.qjyy.base.service.ProcessRouteService;
import com.qjyy.common.core.domain.R;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "工艺路线")
@RestController
@RequiredArgsConstructor
@RequestMapping("/routes")
public class ProcessRouteController {

	private final ProcessRouteService processRouteService;

	@ApiOperation(value = "创建路线")
	@PostMapping
	public R<Long> create(@RequestBody ProcessRouteSaveBo bo) {
		return R.ok(processRouteService.create(bo));
	}

	@ApiOperation(value = "更新路线")
	@PutMapping("/{id}")
	public R<Boolean> update(@PathVariable("id") Long id, @RequestBody ProcessRouteSaveBo bo) {
		return R.ok(processRouteService.update(id, bo));
	}

	@ApiOperation(value = "路线详情")
	@GetMapping("/{id}")
	public R<ProcessRouteVO> detail(@PathVariable("id") Long id) {
		return R.ok(processRouteService.get(id));
	}

	@ApiOperation(value = "路线列表")
	@GetMapping("/list")
	public R<List<ProcessRouteVO>> list() {
		return R.ok(processRouteService.listAll());
	}
}
