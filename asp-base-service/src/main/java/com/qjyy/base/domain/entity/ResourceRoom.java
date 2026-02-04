package com.qjyy.base.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "工序资源(房间)")
@TableName("resource_room")
public class ResourceRoom {

	@TableId
	@ApiModelProperty(value = "主键ID")
	private Long id;

	@ApiModelProperty(value = "版本ID")
	private Long routeVersionId;

	@ApiModelProperty(value = "工序ID")
	private Long processNodeId;

	@ApiModelProperty(value = "资源编码")
	private String resourceCode;

	@ApiModelProperty(value = "资源名称")
	private String resourceName;

	@ApiModelProperty(value = "优先级")
	private Integer priority;

	@ApiModelProperty(value = "清场/换线时间(分钟)")
	private Integer setupMinutes;

	@ApiModelProperty(value = "是否启用")
	private Integer enabled;

	@ApiModelProperty(value = "状态")
	private String status;

	@ApiModelProperty(value = "备注")
	private String remark;

	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createdAt;

	@ApiModelProperty(value = "更新时间")
	private LocalDateTime updatedAt;
}
