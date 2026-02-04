package com.qjyy.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qjyy.base.domain.entity.SyncTaskInfo;
import com.qjyy.common.core.enums.TaskType;

public interface SyncTaskInfoService extends IService<SyncTaskInfo> {

	/**
	 * 根据任务类型查询最新的任务信息
	 *
	 * @param taskType 任务类型
	 * @return 最新的任务信息
	 */
	SyncTaskInfo getLatestTaskByType(TaskType taskType);
}
