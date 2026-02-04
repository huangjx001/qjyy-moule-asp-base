package com.qjyy.base.domain.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("NC65 BOM树返回对象（多版本->子项->替代料）")
@Data
public class Nc65BomTreeVO {

    @ApiModelProperty(value = "根物料编码(mlId)", example = "P001")
    private String mlId;

    @ApiModelProperty(value = "BOM版本列表（nc65_bom_hdr）")
    private List<Nc65BomHdrVO> versions;
}
