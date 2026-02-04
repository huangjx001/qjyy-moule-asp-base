package com.qjyy.base.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "工步节点")
@TableName("step_node")
public class StepNode {

	@TableId
	@ApiModelProperty(value = "主键ID")
	private Long id;

	@ApiModelProperty(value = "版本ID")
	private Long routeVersionId;

	@ApiModelProperty(value = "资源ID")
	private Long resourceRoomId;

	@ApiModelProperty(value = "工步编码")
	private String nodeCode;

	@ApiModelProperty(value = "工步名称")
	private String nodeName;

	@ApiModelProperty(value = "工步类型")
	private String nodeType;

	@ApiModelProperty(value = "是否QC点")
	private Integer qcFlag;

	@ApiModelProperty(value = "时长(分钟)")
	private Integer durationMinutes;

	@ApiModelProperty(value = "画布X坐标")
	private BigDecimal positionX;

	@ApiModelProperty(value = "画布Y坐标")
	private BigDecimal positionY;

	@ApiModelProperty(value = "状态")
	private String status;

	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createdAt;

	@ApiModelProperty(value = "更新时间")
	private LocalDateTime updatedAt;
}
