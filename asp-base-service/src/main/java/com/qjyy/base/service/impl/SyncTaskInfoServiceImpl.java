package com.qjyy.base.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qjyy.base.domain.entity.SyncTaskInfo;
import com.qjyy.base.mapper.SyncTaskInfoMapper;
import com.qjyy.base.service.SyncTaskInfoService;
import com.qjyy.common.core.enums.TaskType;

@Service
public class SyncTaskInfoServiceImpl extends ServiceImpl<SyncTaskInfoMapper, SyncTaskInfo>
		implements SyncTaskInfoService {

	@Override
	public SyncTaskInfo getLatestTaskByType(TaskType taskType) {
		// 使用 LambdaQueryWrapper 根据任务类型和更新时间排序，获取最新的一条任务
		LambdaQueryWrapper<SyncTaskInfo> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(SyncTaskInfo::getTaskCode, taskType.getCode()) // 按任务类型过滤
				.orderByDesc(SyncTaskInfo::getUpdatedAt); // 按更新时间降序排列
		// 获取最新的一条记录
		return this.getOne(queryWrapper, false); // getOne 会自动返回最新的任务数据（符合查询条件的唯一一条数据）
	}

}
