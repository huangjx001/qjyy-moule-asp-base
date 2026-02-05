package com.qjyy.base.domain.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "资源房间保存请求")
public class ResourceRoomSaveBo {

	@ApiModelProperty(value = "版本ID")
	private Long routeVersionId;

	@ApiModelProperty(value = "工序ID")
	private Long processNodeId;

	@ApiModelProperty(value = "资源基础数据ID")
	private Long resourceBaseId;

	@ApiModelProperty(value = "资源编码")
	private String resourceCode;

	@ApiModelProperty(value = "资源名称")
	private String resourceName;

	@ApiModelProperty(value = "优先级")
	private Integer priority;

	@ApiModelProperty(value = "清场/换线时间(分钟)")
	private Integer setupMinutes;

	@ApiModelProperty(value = "是否启用")
	private Integer enabled;

	@ApiModelProperty(value = "状态")
	private String status;

	@ApiModelProperty(value = "备注")
	private String remark;
}
