package com.qjyy.base.convert;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.qjyy.base.domain.bo.ProcessBaseSaveBo;
import com.qjyy.base.domain.bo.ProductBaseSaveBo;
import com.qjyy.base.domain.bo.ResourceBaseSaveBo;
import com.qjyy.base.domain.bo.StepBaseSaveBo;
import com.qjyy.base.domain.entity.ProcessBase;
import com.qjyy.base.domain.entity.ProductBase;
import com.qjyy.base.domain.entity.ResourceBase;
import com.qjyy.base.domain.entity.StepBase;
import com.qjyy.base.domain.vo.ProcessBaseOptionVO;
import com.qjyy.base.domain.vo.ProcessBaseVO;
import com.qjyy.base.domain.vo.ProductBaseOptionVO;
import com.qjyy.base.domain.vo.ProductBaseVO;
import com.qjyy.base.domain.vo.ResourceBaseOptionVO;
import com.qjyy.base.domain.vo.ResourceBaseVO;
import com.qjyy.base.domain.vo.StepBaseOptionVO;
import com.qjyy.base.domain.vo.StepBaseVO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
		nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BaseDataConvert {

	ProcessBase toProcessBase(ProcessBaseSaveBo bo);

	ProductBase toProductBase(ProductBaseSaveBo bo);

	ResourceBase toResourceBase(ResourceBaseSaveBo bo);

	StepBase toStepBase(StepBaseSaveBo bo);

	ProcessBaseVO toProcessBaseVO(ProcessBase entity);

	ProductBaseVO toProductBaseVO(ProductBase entity);

	ResourceBaseVO toResourceBaseVO(ResourceBase entity);

	StepBaseVO toStepBaseVO(StepBase entity);

	List<ProcessBaseVO> toProcessBaseVOList(List<ProcessBase> list);

	List<ProductBaseVO> toProductBaseVOList(List<ProductBase> list);

	List<ResourceBaseVO> toResourceBaseVOList(List<ResourceBase> list);

	List<StepBaseVO> toStepBaseVOList(List<StepBase> list);

	List<ProcessBaseOptionVO> toProcessBaseOptionList(List<ProcessBase> list);

	List<ProductBaseOptionVO> toProductBaseOptionList(List<ProductBase> list);

	List<ResourceBaseOptionVO> toResourceBaseOptionList(List<ResourceBase> list);

	List<StepBaseOptionVO> toStepBaseOptionList(List<StepBase> list);
}
