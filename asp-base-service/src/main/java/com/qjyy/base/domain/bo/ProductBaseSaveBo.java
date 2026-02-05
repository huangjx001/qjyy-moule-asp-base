package com.qjyy.base.domain.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "产品基础数据保存请求")
public class ProductBaseSaveBo {

	@ApiModelProperty(value = "产品编码")
	private String productCode;

	@ApiModelProperty(value = "产品名称")
	private String productName;

	@ApiModelProperty(value = "产品类型")
	private String productType;

	@ApiModelProperty(value = "剂型")
	private String dosageForm;

	@ApiModelProperty(value = "是否启用")
	private Integer enabled;

	@ApiModelProperty(value = "备注")
	private String remark;
}
