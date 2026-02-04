package com.qjyy.base.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("物料计量单位视图")
public class Nc65JydwVO {

	@ApiModelProperty("主键")
	private String id;

	@ApiModelProperty("关联物料主表ID")
	private String materialId;

	@ApiModelProperty("计量单位名称")
	private String proname;

	@ApiModelProperty("换算率(原始)")
	private String prochange;

	@ApiModelProperty("单位包装主键")
	private String dwpk;

	@ApiModelProperty("解析后的换算率（分子部分）")
	private String prochangeValue;
}
