package com.qjyy.base.domain.bo;

import java.math.BigDecimal;

import com.qjyy.common.core.web.page.PageDomain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@ApiModel("月度计划统计分页查询参数")
@EqualsAndHashCode(callSuper = true)
public class YkMonthPlanStatPageQuery extends PageDomain {

	@ApiModelProperty(value = "年度(不传默认当前年)", example = "2026")
	private Integer useYear;

	@ApiModelProperty(value = "月度(1-12，不传则不限制)", example = "2")
	private Integer useMonth;

	@ApiModelProperty(value = "逻辑月useMm(如202602)", example = "202602")
	private BigDecimal useMm;

	@ApiModelProperty(value = "物料编码(模糊)", example = "A01")
	private String code;

	@ApiModelProperty(value = "物料名称/简称(模糊)", example = "阿莫西林")
	private String materialShortName;

	@ApiModelProperty(value = "货品简称(模糊)", example = "胶囊")
	private String currencyName;

	@ApiModelProperty(value = "品规定位(模糊)", example = "0.25g*24粒")
	private String pgdw;

	@ApiModelProperty(value = "所属车间(精确或模糊都行，看你数据)", example = "固体制剂一车间")
	private String workshop;

	@ApiModelProperty(value = "库存率下限", example = "0.5")
	private BigDecimal kclMin;

	@ApiModelProperty(value = "库存率上限", example = "3")
	private BigDecimal kclMax;

	@ApiModelProperty(value = "是否只看有计划数据(销售计划或发货计划任一不为空)", example = "true")
	private Boolean onlyHasPlan;
}
