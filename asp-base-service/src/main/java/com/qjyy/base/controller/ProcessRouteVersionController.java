package com.qjyy.base.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qjyy.base.domain.bo.ProcessRouteVersionCopyBo;
import com.qjyy.base.domain.bo.ProcessRouteVersionSaveBo;
import com.qjyy.base.domain.bo.ProcessRouteVersionUpdateBo;
import com.qjyy.base.domain.vo.GraphValidationResultVO;
import com.qjyy.base.domain.vo.ProcessRouteVersionVO;
import com.qjyy.base.service.ProcessRouteVersionService;
import com.qjyy.common.core.domain.R;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "工艺路线版本")
@RestController
@RequiredArgsConstructor
@RequestMapping("/route-versions")
public class ProcessRouteVersionController {

	private final ProcessRouteVersionService routeVersionService;

	@ApiOperation(value = "创建版本")
	@PostMapping
	public R<Long> create(@RequestBody ProcessRouteVersionSaveBo bo) {
		return R.ok(routeVersionService.create(bo));
	}

	@ApiOperation(value = "更新版本")
	@PutMapping("/{id}")
	public R<Boolean> update(@PathVariable("id") Long id, @RequestBody ProcessRouteVersionUpdateBo bo) {
		return R.ok(routeVersionService.update(id, bo));
	}

	@ApiOperation(value = "版本详情")
	@GetMapping("/{id}")
	public R<ProcessRouteVersionVO> detail(@PathVariable("id") Long id) {
		return R.ok(routeVersionService.get(id));
	}

	@ApiOperation(value = "版本列表")
	@GetMapping("/list")
	public R<List<ProcessRouteVersionVO>> list(@RequestParam("routeId") Long routeId) {
		return R.ok(routeVersionService.listByRouteId(routeId));
	}

	@ApiOperation(value = "复制版本")
	@PostMapping("/{id}/copy")
	public R<Long> copy(@PathVariable("id") Long id, @RequestBody ProcessRouteVersionCopyBo bo) {
		return R.ok(routeVersionService.copy(id, bo));
	}

	@ApiOperation(value = "发布版本")
	@PostMapping("/{id}/release")
	public R<GraphValidationResultVO> release(@PathVariable("id") Long id) {
		return R.ok(routeVersionService.release(id));
	}
}
