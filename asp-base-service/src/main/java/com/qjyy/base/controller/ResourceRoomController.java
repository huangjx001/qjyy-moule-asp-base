package com.qjyy.base.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qjyy.base.domain.bo.ResourceRoomSaveBo;
import com.qjyy.base.domain.vo.ResourceDeviceSummaryVO;
import com.qjyy.base.domain.vo.ResourceRoomVO;
import com.qjyy.base.service.DeviceMountService;
import com.qjyy.base.service.ResourceRoomService;
import com.qjyy.common.core.domain.R;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "工序资源")
@RestController
@RequiredArgsConstructor
public class ResourceRoomController {

	private final ResourceRoomService resourceRoomService;
	private final DeviceMountService deviceMountService;

	@ApiOperation(value = "创建资源")
	@PostMapping("/resources")
	public R<Long> create(@RequestBody ResourceRoomSaveBo bo) {
		return R.ok(resourceRoomService.create(bo));
	}

	@ApiOperation(value = "更新资源")
	@PutMapping("/resources/{id}")
	public R<Boolean> update(@PathVariable("id") Long id, @RequestBody ResourceRoomSaveBo bo) {
		return R.ok(resourceRoomService.update(id, bo));
	}

	@ApiOperation(value = "删除资源")
	@DeleteMapping("/resources/{id}")
	public R<Boolean> delete(@PathVariable("id") Long id) {
		return R.ok(resourceRoomService.delete(id));
	}

	@ApiOperation(value = "工序资源列表")
	@GetMapping("/process-nodes/{processNodeId}/resources")
	public R<List<ResourceRoomVO>> list(@PathVariable("processNodeId") Long processNodeId) {
		return R.ok(resourceRoomService.listByProcessNode(processNodeId));
	}

	@ApiOperation(value = "资源设备汇总")
	@GetMapping("/resources/{resourceRoomId}/devices")
	public R<List<ResourceDeviceSummaryVO>> devices(@PathVariable("resourceRoomId") Long resourceRoomId) {
		return R.ok(deviceMountService.listResourceDevices(resourceRoomId));
	}
}
