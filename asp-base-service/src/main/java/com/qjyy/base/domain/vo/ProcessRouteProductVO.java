package com.qjyy.base.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "路线-产品关联")
public class ProcessRouteProductVO {

	@ApiModelProperty(value = "主键ID")
	private Long id;

	@ApiModelProperty(value = "路线ID")
	private Long routeId;

	@ApiModelProperty(value = "产品编码(为空表示默认路线)")
	private String productCode;

	@ApiModelProperty(value = "是否默认路线")
	private Integer isDefault;

	@ApiModelProperty(value = "是否启用")
	private Integer enabled;

	@ApiModelProperty(value = "备注")
	private String remark;
}
