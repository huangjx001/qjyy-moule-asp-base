package com.qjyy.base.domain.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("英克销售出库年度汇总视图同步数据表")
@TableName("yk_nmrms_yjjxs_fhjdp_v")
public class YkNmrmsYjjxsFhjdpV {
	
    @ApiModelProperty("主键ID")
    @TableId(value = "id", type = IdType.AUTO)  // 主键设置为自增
    private Long id;  // 主键字段，数据类型使用 Long 或 Integer 取决于你的数据库表设计

	@ApiModelProperty("年度")
	private Integer year;

	@ApiModelProperty("物料编码")
	private String goodsId;

	@ApiModelProperty("币种名称")
	private String currencyName;

	@ApiModelProperty("生产类型")
	private String productionType;

	@ApiModelProperty("销售计划数量")
	private BigDecimal salePlanQty;

	@ApiModelProperty("销售计划金额")
	private BigDecimal salePlanValue;

	@ApiModelProperty("赠品计划数量")
	private BigDecimal giftPlanQty;

	@ApiModelProperty("赠品计划金额")
	private BigDecimal giftPlanValue;

	@ApiModelProperty("本年实际发货数量")
	private BigDecimal bnsjfQty;

	@ApiModelProperty("本年实际发货金额")
	private BigDecimal bnsjfValue;

	@ApiModelProperty("本年实际赠品发货数量")
	private BigDecimal bnsjzqQty;

	@ApiModelProperty("本年实际赠品发货金额")
	private BigDecimal bnsjzqValue;

	@ApiModelProperty("发货进度")
	private String deliveryStatus;

	@ApiModelProperty("当前库存数量")
	private BigDecimal currentQty;

	@ApiModelProperty("当前库存金额")
	private BigDecimal currentValue;

	@ApiModelProperty("数据创建时间")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	@ApiModelProperty("数据更新时间")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	@ApiModelProperty("计划完成率")
	private String jhwcl;

	@ApiModelProperty("本年累计计划完成率")
	private BigDecimal bnljjhfhl;

	@ApiModelProperty("日计完成率")
	private String daycntwcl;

	@ApiModelProperty("销售数量")
	private BigDecimal xcs;

	@ApiModelProperty("当月销售数量")
	private BigDecimal dysl;

	@ApiModelProperty("发货进度预警")
	private String fhjdyj;

	@ApiModelProperty("上年同期发货数量")
	private BigDecimal sntqfhqty;

	@ApiModelProperty("上年同期发货金额")
	private BigDecimal sntqfhje;

	@ApiModelProperty("发货增速率")
	private String fhzzl;

	@ApiModelProperty("本年累计销售数量")
	private BigDecimal bncxqty;

	@ApiModelProperty("本年累计销售金额")
	private BigDecimal bncxje;

	@ApiModelProperty("上一期库存数量")
	private BigDecimal syqmkcqty;

	@ApiModelProperty("上一期库存金额")
	private BigDecimal syqmkcje;

	@ApiModelProperty("库存增速率")
	private String sykczzl;
}
