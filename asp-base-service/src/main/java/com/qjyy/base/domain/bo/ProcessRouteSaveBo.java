package com.qjyy.base.domain.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "工艺路线保存请求")
public class ProcessRouteSaveBo {

	@ApiModelProperty(value = "路线编码")
	private String routeCode;

	@ApiModelProperty(value = "路线名称")
	private String routeName;

	@ApiModelProperty(value = "备注")
	private String remark;
}
