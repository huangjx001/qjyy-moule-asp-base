package com.qjyy.base.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "工序基础数据下拉选项")
public class ProcessBaseOptionVO {

	@ApiModelProperty(value = "主键ID")
	private Long id;

	@ApiModelProperty(value = "工序编码")
	private String processCode;

	@ApiModelProperty(value = "工序名称")
	private String processName;

	@ApiModelProperty(value = "工序类型")
	private String processType;
}
