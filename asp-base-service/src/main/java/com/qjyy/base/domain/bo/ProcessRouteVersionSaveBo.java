package com.qjyy.base.domain.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "工艺路线版本保存请求")
public class ProcessRouteVersionSaveBo {

	@ApiModelProperty(value = "路线ID")
	private Long routeId;

	@ApiModelProperty(value = "版本号")
	private String versionCode;

	@ApiModelProperty(value = "版本名称")
	private String versionName;

	@ApiModelProperty(value = "备注")
	private String remark;
}
