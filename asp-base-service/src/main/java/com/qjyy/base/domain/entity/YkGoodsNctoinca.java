package com.qjyy.base.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("英克物料信息表")
@TableName("yk_zx_goods_nctoinca")
public class YkGoodsNctoinca {

	@TableId
	@ApiModelProperty("物料编码")
	private String code;

	@ApiModelProperty("物料短名称")
	private String materialShortName;

	@ApiModelProperty("物料规格")
	private String materialSpec;

	@ApiModelProperty("DEF2 字段")
	private String def2;

	@ApiModelProperty("商品短名称")
	private String goodsShortName;

	@ApiModelProperty("商品类型")
	private String goodsType;

	@ApiModelProperty("时间戳")
	private String ts;

	@ApiModelProperty("生产区域")
	private String prodArea;

	@ApiModelProperty("物料 MNE 编码")
	private String materialMneCode;

	@ApiModelProperty("物料名称")
	private String name;
}
