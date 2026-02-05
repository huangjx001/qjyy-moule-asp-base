package com.qjyy.base.domain.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "工步基础数据")
@TableName("step_base")
public class StepBase {

	@TableId
	@ApiModelProperty(value = "主键ID")
	private Long id;

	@ApiModelProperty(value = "工步编码")
	private String stepCode;

	@ApiModelProperty(value = "工步名称")
	private String stepName;

	@ApiModelProperty(value = "工步类型")
	private String stepType;

	@ApiModelProperty(value = "默认时长(分钟)")
	private Integer defaultDurationMinutes;

	@ApiModelProperty(value = "是否QC点")
	private Integer qcFlag;

	@ApiModelProperty(value = "是否启用")
	private Integer enabled;

	@ApiModelProperty(value = "备注")
	private String remark;

	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createdAt;

	@ApiModelProperty(value = "更新时间")
	private LocalDateTime updatedAt;
}
