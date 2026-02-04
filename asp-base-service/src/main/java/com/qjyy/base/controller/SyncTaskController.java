package com.qjyy.base.controller;

import com.qjyy.base.domain.entity.SyncTaskInfo;
import com.qjyy.base.service.SyncTaskInfoService;
import com.qjyy.common.core.domain.R;
import com.qjyy.common.core.enums.TaskType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sync-task")
@Api(tags = "同步任务")
@RequiredArgsConstructor
public class SyncTaskController {

	private final SyncTaskInfoService syncTaskInfoService;

	@GetMapping("/latest/{type}")
	@ApiOperation(value = "查询最新同步记录", notes = "按任务类型(TaskType)获取最新一条刷新/同步信息")
	public R<SyncTaskInfo> latest(
			@PathVariable("type") @ApiParam(value = "任务类型code，如 NC_MATERIAL_SYNC", required = true) TaskType type) {
		SyncTaskInfo info = syncTaskInfoService.getLatestTaskByType(type);
		return info == null ? R.fail("未找到该任务类型的记录") : R.ok(info);
	}
}
