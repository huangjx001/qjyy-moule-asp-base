package com.qjyy.base.domain.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "工艺路线版本复制请求")
public class ProcessRouteVersionCopyBo {

	@ApiModelProperty(value = "新版本号")
	private String newVersionCode;

	@ApiModelProperty(value = "新版本名称")
	private String newVersionName;

	@ApiModelProperty(value = "备注")
	private String remark;
}
