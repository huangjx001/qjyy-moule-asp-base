package com.qjyy.base.domain.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("月度计划/发货/库存/纯销统计（Oracle同步表）")
@TableName("yk_month_plan_stat")
public class YkMonthPlanStat {

	@TableId
	@ApiModelProperty("序列号")
	private BigDecimal seqId;

	@ApiModelProperty("逻辑月")
	private BigDecimal useMm;

	@ApiModelProperty("物料编码")
	private String code;

	@ApiModelProperty("物料名称")
	private String materialShortName;

	@ApiModelProperty("货品id")
	private BigDecimal goodsId;

	@ApiModelProperty("货品简称")
	private String currencyName;

	@ApiModelProperty("品规定位")
	private String pgdw;

	@ApiModelProperty("件装量")
	private BigDecimal unitLoad;

	@ApiModelProperty("单价")
	private BigDecimal price;

	@ApiModelProperty("本年月均纯销")
	private BigDecimal netSalesThisYear;

	@ApiModelProperty("近六月月均纯销")
	private BigDecimal netSalesRecentSixMonth;

	@ApiModelProperty("近三月月均纯销")
	private BigDecimal netSalesRecentThreeMonth;

	@ApiModelProperty("本年月均发货")
	private BigDecimal deliverThisYear;

	@ApiModelProperty("近六月月均发货")
	private BigDecimal deliverRecentSixMonth;

	@ApiModelProperty("近三月月均发货")
	private BigDecimal deliverRecentThreeMonth;

	@ApiModelProperty("上月底社会库存")
	private BigDecimal lastMonthKc;

	@ApiModelProperty("库存率")
	private BigDecimal kcl;

	@ApiModelProperty("安全库存率")
	private BigDecimal aqkcl;

	@ApiModelProperty("销售计划数量")
	private BigDecimal salePlanNum;

	@ApiModelProperty("销售计划金额")
	private BigDecimal salePlanCost;

	@ApiModelProperty("安全库存数量")
	private BigDecimal aqkcNum;

	@ApiModelProperty("安全库存金额")
	private BigDecimal aqkcCost;

	@ApiModelProperty("发货计划数量")
	private BigDecimal deliverPlanNum;

	@ApiModelProperty("发货计划金额")
	private BigDecimal deliverPlanCost;

	@ApiModelProperty("修正后发货计划数量")
	private BigDecimal deliverPlanNumAmend;

	@ApiModelProperty("修正后发货计划金额")
	private BigDecimal deliverPlanCostAmend;

	@ApiModelProperty("下月发货计划数量")
	private BigDecimal deliverPlanNumNextMonth;

	@ApiModelProperty("下月发货计划金额")
	private BigDecimal deliverPlanCostNextMonth;

	@ApiModelProperty("下下月发货计划数量")
	private BigDecimal deliverPlanNumNextTwoMonth;

	@ApiModelProperty("下下月发货计划金额")
	private BigDecimal deliverPlanCostNextTwoMonth;

	@ApiModelProperty("创建时间")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	@ApiModelProperty("备注")
	private String memo;

	@ApiModelProperty("年度")
	private Integer  useYear;

	@ApiModelProperty("月度")
	private Integer  useMonth;

	@ApiModelProperty("物料编码(备用/映射字段)")
	private String matCode;

	@ApiModelProperty("上月底社会库存金额")
	private BigDecimal lastMonthMoney;

	@ApiModelProperty("所属车间")
	private String workshop;
}
