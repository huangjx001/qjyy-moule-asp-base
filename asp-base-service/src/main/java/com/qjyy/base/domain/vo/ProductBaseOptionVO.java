package com.qjyy.base.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "产品基础数据下拉选项")
public class ProductBaseOptionVO {

	@ApiModelProperty(value = "主键ID")
	private Long id;

	@ApiModelProperty(value = "产品编码")
	private String productCode;

	@ApiModelProperty(value = "产品名称")
	private String productName;

	@ApiModelProperty(value = "剂型")
	private String dosageForm;
}
