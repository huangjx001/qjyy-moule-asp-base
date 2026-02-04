package com.qjyy.base.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@TableName("nc65_jydw")
@Data
@ApiModel("物料计量单位")
public class Nc65Jydw {

	@TableId(value = "id", type = IdType.ASSIGN_UUID)
	@ApiModelProperty("物料主键")
	private String id;

	@ApiModelProperty("关联物料主表ID")
	private String materialId;

	@ApiModelProperty("时间戳")
	private String ts;

	@ApiModelProperty("计量单位名称")
	private String proname;

	@ApiModelProperty("换算率")
	private String prochange;

	@ApiModelProperty("单位包装主键")
	private String dwpk;

	@ApiModelProperty("解析后的换算率（分子部分）")
	private String prochangeValue; // 新增字段，存储解析后的值
}
