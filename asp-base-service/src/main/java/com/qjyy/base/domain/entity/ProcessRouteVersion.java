package com.qjyy.base.domain.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "工艺路线版本")
@TableName("process_route_version")
public class ProcessRouteVersion {

	@TableId
	@ApiModelProperty(value = "主键ID")
	private Long id;

	@ApiModelProperty(value = "路线ID")
	private Long routeId;

	@ApiModelProperty(value = "版本号")
	private String versionCode;

	@ApiModelProperty(value = "版本名称")
	private String versionName;

	@ApiModelProperty(value = "状态:DRAFT/RELEASED")
	private String status;

	@ApiModelProperty(value = "是否启用")
	private Integer enabled;

	@ApiModelProperty(value = "备注")
	private String remark;

	@ApiModelProperty(value = "发布人")
	private String releasedBy;

	@ApiModelProperty(value = "发布时间")
	private LocalDateTime releasedAt;

	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createdAt;

	@ApiModelProperty(value = "更新时间")
	private LocalDateTime updatedAt;
}
