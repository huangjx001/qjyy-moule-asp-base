package com.qjyy.base.domain.bo;

import java.util.List;

import com.qjyy.common.core.web.page.PageDomain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel(description = "NC65 物料分页查询条件")
@Data
@EqualsAndHashCode(callSuper = true)
public class Nc65MaterialQueryBo extends PageDomain {

	@ApiModelProperty(value = "分类code")
	private String classCode;

	@ApiModelProperty(value = "是否包含子类型(默认true)", example = "true")
	private Boolean includeChildren = Boolean.TRUE;

	@ApiModelProperty(value = "关键字(匹配物料名称/物料编号)")
	private String keyword;

	@ApiModelProperty(value = "状态(可多选). 1=未启用,2=已启用,3=已停用")
	private List<String> status;

	@ApiModelProperty(value = "物料编号(productCode)精确匹配")
	private String productCode;

	@ApiModelProperty(value = "物料名称(materialName)模糊匹配")
	private String materialName;

	@ApiModelProperty(value = "规格(specification)模糊匹配")
	private String specification;

	@ApiModelProperty(value = "产品类型(iscomsume)精确匹配")
	private String iscomsume;

	@ApiModelProperty(value = "车间(workshopId)精确匹配")
	private String workshopId;

	@ApiModelProperty(value = "是否批次管理 1/0")
	private Integer isbatch;

	@ApiModelProperty("新品规格标识")
	private String newspec;
}
