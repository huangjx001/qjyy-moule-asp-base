package com.qjyy.base.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 同步任务信息表 记录每个同步任务的基本信息，包括任务名称、CRON 表达式、执行者、状态、时间等
 */
@Data
@ApiModel(description = "同步任务信息表")
@TableName("sync_task_info") // 映射到数据库表 sync_task_info
public class SyncTaskInfo {

	@TableId
	@ApiModelProperty(value = "主键ID", example = "1")
	private Long id; // 主键ID
	
	@ApiModelProperty(value = "任务类型码值(TaskType.code)", example = "NC_SALES_SYNC")
	private String taskCode;

	@ApiModelProperty(value = "任务名称", example = "英克销售出库年度汇总数据同步接口")
	private String taskName; // 任务名称

	@ApiModelProperty(value = "CRON 表达式", example = "0 0 2 * * ?")
	private String cronExpression; // CRON 表达式

	@ApiModelProperty(value = "BEAN 名称", example = "ykSalesYearSummarySyncJobHandler")
	private String beanName; // BEAN 名称

	@ApiModelProperty(value = "任务执行者", example = "huangjx")
	private String executor; // 任务执行者

	@ApiModelProperty(value = "任务状态", example = "RUNNING")
	private String status; // 任务状态

	@ApiModelProperty(value = "上次运行时间", example = "2026-01-01T12:00:00")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime lastRunTime; // 上次运行时间

	@ApiModelProperty(value = "下次运行时间", example = "2026-01-02T12:00:00")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime nextRunTime; // 下次运行时间

	@ApiModelProperty(value = "任务触发方式", example = "XXL_JOB")
	private String taskType; // 任务触发方式：XXL_JOB 或 API_TRIGGER
	
	@ApiModelProperty(value = "最近一次同步总耗时(秒)", example = "1.769")
	private BigDecimal costSeconds;

	@ApiModelProperty(value = "创建时间", example = "2026-01-01T12:00:00")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createdAt; // 创建时间

	@ApiModelProperty(value = "更新时间", example = "2026-01-01T12:00:00")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updatedAt; // 更新时间

}
