package com.qjyy.base.domain.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "资源基础数据保存请求")
public class ResourceBaseSaveBo {

	@ApiModelProperty(value = "资源编码")
	private String resourceCode;

	@ApiModelProperty(value = "资源名称")
	private String resourceName;

	@ApiModelProperty(value = "资源类型")
	private String resourceType;

	@ApiModelProperty(value = "能力/规格描述")
	private String capacityDesc;

	@ApiModelProperty(value = "默认清场/换线时间(分钟)")
	private Integer defaultSetupMinutes;

	@ApiModelProperty(value = "是否启用")
	private Integer enabled;

	@ApiModelProperty(value = "备注")
	private String remark;
}
