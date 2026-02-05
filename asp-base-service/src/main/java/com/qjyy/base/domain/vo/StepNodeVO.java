package com.qjyy.base.domain.vo;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "工步节点")
public class StepNodeVO {

	@ApiModelProperty(value = "工步ID")
	private Long id;

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
}
