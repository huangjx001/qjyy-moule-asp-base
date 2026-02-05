package com.qjyy.base.domain.vo;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "工序节点")
public class ProcessNodeVO {

	@ApiModelProperty(value = "节点ID")
	private Long id;

	@ApiModelProperty(value = "工序基础数据ID")
	private Long processBaseId;

	@ApiModelProperty(value = "工序编码")
	private String nodeCode;

	@ApiModelProperty(value = "工序名称")
	private String nodeName;

	@ApiModelProperty(value = "工序类型")
	private String nodeType;

	@ApiModelProperty(value = "是否关键")
	private Integer criticalFlag;

	@ApiModelProperty(value = "时长(分钟)")
	private Integer durationMinutes;

	@ApiModelProperty(value = "画布X坐标")
	private BigDecimal positionX;

	@ApiModelProperty(value = "画布Y坐标")
	private BigDecimal positionY;

	@ApiModelProperty(value = "状态")
	private String status;
}
