package com.qjyy.base.domain.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "设备挂载请求")
public class DeviceMountBo {

	@ApiModelProperty(value = "资源ID")
	private Long resourceRoomId;

	@ApiModelProperty(value = "工步ID")
	private Long stepNodeId;

	@ApiModelProperty(value = "设备ID")
	private Long deviceId;
}
