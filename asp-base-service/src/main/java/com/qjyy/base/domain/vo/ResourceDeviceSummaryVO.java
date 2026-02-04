package com.qjyy.base.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "资源设备汇总")
public class ResourceDeviceSummaryVO {

	@ApiModelProperty(value = "设备ID")
	private Long deviceId;

	@ApiModelProperty(value = "设备编码")
	private String deviceCode;

	@ApiModelProperty(value = "设备名称")
	private String deviceName;

	@ApiModelProperty(value = "挂载来源")
	private String mountType;

	@ApiModelProperty(value = "来源工步ID")
	private Long sourceStepId;

	@ApiModelProperty(value = "来源工步名称")
	private String sourceStepName;
}
