package com.qjyy.base.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "工步基础数据下拉选项")
public class StepBaseOptionVO {

	@ApiModelProperty(value = "主键ID")
	private Long id;

	@ApiModelProperty(value = "工步编码")
	private String stepCode;

	@ApiModelProperty(value = "工步名称")
	private String stepName;

	@ApiModelProperty(value = "工步类型")
	private String stepType;

	@ApiModelProperty(value = "是否QC点")
	private Integer qcFlag;
}
