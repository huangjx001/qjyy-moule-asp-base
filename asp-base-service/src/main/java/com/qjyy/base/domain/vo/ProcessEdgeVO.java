package com.qjyy.base.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "工序依赖边")
public class ProcessEdgeVO {

	@ApiModelProperty(value = "边ID")
	private Long id;

	@ApiModelProperty(value = "前置工序ID")
	private Long fromNodeId;

	@ApiModelProperty(value = "后置工序ID")
	private Long toNodeId;

	@ApiModelProperty(value = "依赖类型")
	private String dependencyType;

	@ApiModelProperty(value = "依赖强度")
	private String dependencyStrength;

	@ApiModelProperty(value = "滞后分钟")
	private Integer lagMinutes;
}
