package com.qjyy.base.domain.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("nc65_material")
@ApiModel("NC65 物料实体")
public class Nc65Material {
	@TableId(value = "id", type = IdType.INPUT)
	@ApiModelProperty("物料主键")
	private String id;
	
	@ApiModelProperty("类型编号")
	private String typeNo;

	@ApiModelProperty("存货大类（物料分类）")
	private String typeId;

	@ApiModelProperty("物料名称")
	private String materialName;

	@ApiModelProperty("产品类型（物料大类）")
	private String iscomsume;

	@ApiModelProperty("通用名称（简称）")
	private String commonName;

	@ApiModelProperty("生产计量单位")
	private String productUnitId;

	@ApiModelProperty("生产车间（生产部门）")
	private String workshopId;

	@ApiModelProperty("规格")
	private String specification;

	@ApiModelProperty("是否批次管理（1：是，0：否）")
	private Integer isbatch;

	@ApiModelProperty("型号")
	private String model;

	@ApiModelProperty("执行标准（批准文号）")
	private String extStandard;

	@ApiModelProperty("产品代码（物料编号）")
	private String productCode;

	@ApiModelProperty("状态（启用状态）(1=未启用，2=已启用，3=已停用)")
	private String status;

	@ApiModelProperty("安全库存量")
	private String securityInvNum;

	@ApiModelProperty("最小安全库存量")
	private String minSecurityInvNum;

	@ApiModelProperty("最大安全库存量")
	private String maxSecurityInvNum;

	@ApiModelProperty("系列名称")
	private String seriesName;

	@ApiModelProperty("生产模式（1：流程生产，2：离散生产）")
	private String productModel;

	@ApiModelProperty("仓储地点（主仓库编号）")
	private String storeId;

	@ApiModelProperty("英克ID（产成品才有）")
	private String crmId;

	@ApiModelProperty("默认库存单位")
	private String stordocUnit;

	@ApiModelProperty("默认库存换算率")
	private String stordocVchange;

	@ApiModelProperty("默认生产单位")
	private String prodUnit;

	@ApiModelProperty("默认生产换算率")
	private String prodVchange;

	@ApiModelProperty("时间戳")
	private String ts;

	@ApiModelProperty("新品规格标识")
	private String newspec;

	@ApiModelProperty("记录创建时间")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date creationTime;
}
