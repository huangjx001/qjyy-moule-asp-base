package com.qjyy.base.domain.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("nc65_bom_repl")
@ApiModel("NC65 BOM替代料")
public class Nc65BomRepl {

	@TableId(value = "id", type = IdType.INPUT)
	@ApiModelProperty("主键id（NC替代料id）")
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
