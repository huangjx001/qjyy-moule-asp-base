package com.qjyy.base.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "工艺路线")
public class ProcessRouteVO {

	@ApiModelProperty(value = "路线ID")
	private Long id;

	@ApiModelProperty(value = "路线编码")
	private String routeCode;

	@ApiModelProperty(value = "路线名称")
	private String routeName;

	@ApiModelProperty(value = "备注")
	private String remark;
}
