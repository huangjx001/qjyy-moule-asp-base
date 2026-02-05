package com.qjyy.base.domain.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "工艺路线版本更新请求")
public class ProcessRouteVersionUpdateBo {

	@ApiModelProperty(value = "版本名称")
	private String versionName;

	@ApiModelProperty(value = "是否启用")
	private Integer enabled;

	@ApiModelProperty(value = "备注")
	private String remark;
}
