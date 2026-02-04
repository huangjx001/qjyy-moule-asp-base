package com.qjyy.base.domain.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("nc65_bom_body")
@ApiModel("NC65 BOM子表")
public class Nc65BomBody {

	@TableId(value = "id", type = IdType.INPUT)
	@ApiModelProperty("主键id（NC子表id）")
	private String id;

	@ApiModelProperty("主表id（parent_bom_id）")
	private String parentBomId;

	@ApiModelProperty("物料编号")
	private String mlId;

	@ApiModelProperty("物料版本")
	private Integer version;

	@ApiModelProperty("子项主单位")
	private String unit;

	@ApiModelProperty("子项主数量")
	private BigDecimal baseNum;

	@ApiModelProperty("子项单位")
	private String nastunit;

	@ApiModelProperty("子项数量")
	private BigDecimal useNum;

	@ApiModelProperty("换算率")
	private String convertRate;

	@ApiModelProperty("是否可替代 Y/N")
	private String bcanreplace;

	@ApiModelProperty("替代类型 1/2")
	private Integer freplacetype;
}
