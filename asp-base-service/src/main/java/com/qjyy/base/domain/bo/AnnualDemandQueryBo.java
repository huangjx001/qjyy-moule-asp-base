package com.qjyy.base.domain.bo;

import com.qjyy.common.core.web.page.PageDomain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@ApiModel("年度需求查询条件")
@EqualsAndHashCode(callSuper = true)
public class AnnualDemandQueryBo extends PageDomain {

	@ApiModelProperty("年份")
	private Integer year;

	@ApiModelProperty("生产车间（生产部门）")
	private String workshopId;

	@ApiModelProperty("产品编码")
	private String productCode;

	@ApiModelProperty("产品名称")
	private String materialName;

	@ApiModelProperty("货品简称")
	private String currencyName;

	@ApiModelProperty("品规类型")
	private String productionType;
}
