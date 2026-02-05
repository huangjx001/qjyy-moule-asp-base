package com.qjyy.base.domain.vo;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "工艺路线版本")
public class ProcessRouteVersionVO {

	@ApiModelProperty(value = "版本ID")
	private Long id;

	@ApiModelProperty(value = "路线ID")
	private Long routeId;

	@ApiModelProperty(value = "版本号")
	private String versionCode;

	@ApiModelProperty(value = "版本名称")
	private String versionName;

	@ApiModelProperty(value = "状态")
	private String status;

	@ApiModelProperty(value = "是否启用")
	private Integer enabled;

	@ApiModelProperty(value = "备注")
	private String remark;

	@ApiModelProperty(value = "发布人")
	private String releasedBy;

	@ApiModelProperty(value = "发布时间")
	private LocalDateTime releasedAt;
}
