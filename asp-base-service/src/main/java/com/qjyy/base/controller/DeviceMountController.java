package com.qjyy.base.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qjyy.base.domain.bo.DeviceMountBo;
import com.qjyy.base.service.DeviceMountService;
import com.qjyy.common.core.domain.R;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "设备挂载")
@RestController
@RequiredArgsConstructor
@RequestMapping("/device-mounts")
public class DeviceMountController {

	private final DeviceMountService deviceMountService;

	@ApiOperation(value = "资源挂设备")
	@PostMapping("/resource")
	public R<Long> addResource(@RequestBody DeviceMountBo bo) {
		return R.ok(deviceMountService.addResourceMount(bo));
	}

	@ApiOperation(value = "工步挂设备")
	@PostMapping("/step")
	public R<Long> addStep(@RequestBody DeviceMountBo bo) {
		return R.ok(deviceMountService.addStepMount(bo));
	}

	@ApiOperation(value = "删除挂载")
	@DeleteMapping("/{id}")
	public R<Boolean> delete(@PathVariable("id") Long id) {
		return R.ok(deviceMountService.deleteMount(id));
	}
}
