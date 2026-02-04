package com.qjyy.base.domain.vo;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("NC65 BOM主表版本节点（nc65_bom_hdr）")
@Data
public class Nc65BomHdrVO {

	@ApiModelProperty("NC物料bom主表主键(id)")
	private String id;

	@ApiModelProperty("物料编号")
	private String mlId;

	@ApiModelProperty("物料名称")
	private String materialName;

	@ApiModelProperty("主单位")
	private String unit;

	@ApiModelProperty("主数量")
	private BigDecimal nnum;

	@ApiModelProperty("单位")
	private String nastunit;

	@ApiModelProperty("数量")
	private BigDecimal nastnum;

	@ApiModelProperty("换算率")
	private String rate;

	@ApiModelProperty("版本")
	private String version;

	@ApiModelProperty("是否默认 1/0")
	private Integer hbdefault;

	@ApiModelProperty("子项列表（nc65_bom_body）")
	private List<Nc65BomBodyVO> items;
}
