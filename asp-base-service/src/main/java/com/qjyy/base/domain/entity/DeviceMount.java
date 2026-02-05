package com.qjyy.base.domain.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "设备挂载")
@TableName("device_mount")
public class DeviceMount {

	@TableId
	@ApiModelProperty(value = "主键ID")
	private Long id;

	@ApiModelProperty(value = "版本ID")
	private Long routeVersionId;

	@ApiModelProperty(value = "资源ID")
	private Long resourceRoomId;

	@ApiModelProperty(value = "工步ID")
	private Long stepNodeId;

	@ApiModelProperty(value = "设备ID")
	private Long deviceId;

	@ApiModelProperty(value = "挂载来源:RESOURCE/STEP")
	private String mountType;

	@ApiModelProperty(value = "备注")
	private String remark;

	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createdAt;

	@ApiModelProperty(value = "更新时间")
	private LocalDateTime updatedAt;
}
