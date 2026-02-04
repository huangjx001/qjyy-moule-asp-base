package com.qjyy.base.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("年度需求视图结构")
@Data
public class AnnualDemandVO {

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

	@ApiModelProperty(value = "年度计划销售数量 (盒)", example = "10000", notes = "计划销售数量在年度计划中定义，用于衡量预期的销售总量。")
	private Integer plannedSalesQuantity;

	@ApiModelProperty(value = "年度计划ZP数量 (盒)", example = "5000", notes = "年度计划 ZP 数量，通常指某种特定商品的销售数量。")
	private Integer plannedZPQuantity;

	@ApiModelProperty(value = "本年实际正常发货数量 (盒)", example = "8000", notes = "本年度实际正常发货数量，指的是实际发货但不包含异常或特殊发货的数量。")
	private Integer actualNormalDeliveryQuantity;

	@ApiModelProperty(value = "本年实际ZP发货数量 (盒)", example = "4000", notes = "本年度实际 ZP 发货数量，通常指特定类型商品的发货数量。")
	private Integer actualZPDeliveryQuantity;

	@ApiModelProperty(value = "计划完成率", example = "80", notes = "计划完成率 = (实际发货数量 / 年度计划销售数量) * 100，衡量销售计划的完成情况。")
	private Double completionRate;

	@ApiModelProperty(value = "发货进度预警", example = "true", notes = "发货进度预警，若发货进度滞后或有异常，则触发预警。")
	private Boolean deliveryProgressWarning;

	@ApiModelProperty(value = "去年同期发货数量 (盒)", example = "7000", notes = "去年同期发货数量，作为发货增长率的参考。")
	private Integer lastYearSamePeriodDeliveryQuantity;

	@ApiModelProperty(value = "发货增长率", example = "14.29", notes = "发货增长率 = ((本年实际发货数量 - 去年同期发货数量) / 去年同期发货数量) * 100，用于衡量今年发货相较于去年增长的比例。")
	private Double deliveryGrowthRate;

	@ApiModelProperty(value = "期初年度计划总数量 (盒)", example = "15000", notes = "期初年度计划总数量，用于比较年度计划目标与实际发货之间的差距。")
	private Integer initialAnnualPlanTotalQuantity;

	@ApiModelProperty(value = "本年度实际发货总数量 (盒)", example = "12000", notes = "本年度实际发货总数量，衡量实际完成的发货总量。")
	private Integer actualTotalDeliveryQuantity;

	@ApiModelProperty(value = "年度计划修正差值 (盒)", example = "500", notes = "年度计划修正差值，表示与初期计划相比所做的修正。")
	private Integer annualPlanAdjustmentDifference;

	@ApiModelProperty(value = "修正后年度计划总数量 (盒)", example = "15500", notes = "修正后年度计划总数量，表示在修正后的新年度计划目标总数。")
	private Integer adjustedAnnualPlanTotalQuantity;

	@ApiModelProperty(value = "修正后剩余未发货数量 (盒)", example = "3500", notes = "修正后剩余未发货数量，表示修正后的发货进度尚未完成的数量。")
	private Integer adjustedRemainingUnshippedQuantity;

	@ApiModelProperty(value = "年度计划完成率修正值", example = "85", notes = "年度计划完成率修正值，用于衡量修正后年度计划的完成情况。")
	private Double adjustedAnnualPlanCompletionRate;

}