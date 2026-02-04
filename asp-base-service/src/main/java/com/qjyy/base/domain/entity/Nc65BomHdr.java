package com.qjyy.base.domain.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("nc65_bom_hdr")
@ApiModel("NC65 BOM主表")
public class Nc65BomHdr {

	@TableId(value = "id", type = IdType.INPUT)
	@ApiModelProperty("主键id（NC主表id）")
	private String id;

	@ApiModelProperty("物料编号")
	private String mlId;

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
}
