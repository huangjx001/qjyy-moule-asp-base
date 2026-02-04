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
@ApiModel("NC65 销售出库年度汇总（M4cStat）")
@TableName("nc65_m4c_stat_year")
public class Nc65M4cStatYear {

	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty("主键")
	private Long id;

	@ApiModelProperty("年度")
	private Integer year;

	@ApiModelProperty("成品物料编号（wlcode）")
	private String wlCode;

	@ApiModelProperty("销售主数量（zcnnum）")
	private BigDecimal zcnNum;

	@ApiModelProperty("销售金额（zcnmny）")
	private BigDecimal zcnMny;

	@ApiModelProperty("赠品主数量（zpnnum）")
	private BigDecimal zpnNum;

	@ApiModelProperty("赠品金额（zpnmny）")
	private BigDecimal zpnMny;

	@ApiModelProperty("创建时间")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	@ApiModelProperty("更新时间（可当最后同步时间）")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
}
