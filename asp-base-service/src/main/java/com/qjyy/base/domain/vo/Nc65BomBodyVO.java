package com.qjyy.base.domain.vo;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("NC65 BOM子项节点（nc65_bom_body）")
@Data
public class Nc65BomBodyVO {

    @ApiModelProperty("NC物料bom子表主键(id)")
    private String id;

	@ApiModelProperty("主表id（parent_bom_id）")
	private String parentBomId;

	@ApiModelProperty("物料编号")
	private String mlId;
	
	@ApiModelProperty("物料名称")
	private String materialName;

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

    @ApiModelProperty("替代料列表（nc65_bom_repl）")
    private List<Nc65BomReplVO> replacements;
}
