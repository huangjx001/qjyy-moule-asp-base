package com.qjyy.base.domain.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("物料BOM主表计数行（按ml_id分组）")
@Data
public class Nc65BomHdrCountBo {

	@ApiModelProperty("物料编码(ml_id)")
	private String mlId;

	@ApiModelProperty("BOM主表行数（通常=版本数）")
	private Long cnt;
}
