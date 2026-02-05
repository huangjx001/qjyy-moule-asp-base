package com.qjyy.base.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "工序节点")
@TableName("process_node")
public class ProcessNode {

	@TableId
	@ApiModelProperty(value = "主键ID")
	private Long id;

	@ApiModelProperty(value = "版本ID")
	private Long routeVersionId;

	@ApiModelProperty(value = "工序基础数据ID")
	private Long processBaseId;

	@ApiModelProperty(value = "工序名称")
	private String nodeName;

	@ApiModelProperty(value = "是否关键工序")
	private Integer criticalFlag;

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
