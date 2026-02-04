package com.qjyy.base.domain.vo;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("NC65 BOM子项替代物料节点（nc65_bom_repl）")
@Data
public class Nc65BomReplVO {

	@ApiModelProperty("替代物料行主键(id)")
	private String id;

	@ApiModelProperty("Bom子项物料行主键（cbom_bid）")
	private String cbomBid;

	@ApiModelProperty("替代物料行号")
	private String replRowno;

	@ApiModelProperty("替代物料编号")
	private String replWlcode;

	@ApiModelProperty("替代物料名称")
	private String replWlname;

	@ApiModelProperty("替代系数")
	private BigDecimal vreplaceindex;
}
