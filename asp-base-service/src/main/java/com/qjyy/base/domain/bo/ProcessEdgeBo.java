package com.qjyy.base.domain.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "工序依赖边")
public class ProcessEdgeBo {

	@ApiModelProperty(value = "边ID")
	private Long id;

	@ApiModelProperty(value = "前置工序ID")
	private Long fromNodeId;

	@ApiModelProperty(value = "后置工序ID")
	private Long toNodeId;

	@ApiModelProperty(value = "依赖类型:FS/SS/FF/SF")
	private String dependencyType;

	@ApiModelProperty(value = "依赖强度:HARD/SOFT")
	private String dependencyStrength;

	@ApiModelProperty(value = "滞后分钟")
	private Integer lagMinutes;
}
