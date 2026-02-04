package com.qjyy.base.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "图校验错误")
public class GraphValidationErrorVO {

	@ApiModelProperty(value = "错误码")
	private String code;

	@ApiModelProperty(value = "错误信息")
	private String message;

	@ApiModelProperty(value = "节点ID")
	private Long nodeId;

	@ApiModelProperty(value = "边ID")
	private Long edgeId;
}
