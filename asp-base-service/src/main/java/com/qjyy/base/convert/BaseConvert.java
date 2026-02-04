package com.qjyy.base.convert;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.qjyy.base.domain.bo.Nc65BomFlatRow;
import com.qjyy.base.domain.entity.Nc65BomBody;
import com.qjyy.base.domain.entity.Nc65BomHdr;
import com.qjyy.base.domain.entity.Nc65BomRepl;
import com.qjyy.base.domain.entity.Nc65Jydw;
import com.qjyy.base.domain.entity.Nc65Marbasclass;
import com.qjyy.base.domain.entity.Nc65Material;
import com.qjyy.base.domain.entity.YkMonthPlanStat;
import com.qjyy.base.domain.vo.Nc65BomBodyVO;
import com.qjyy.base.domain.vo.Nc65BomHdrVO;
import com.qjyy.base.domain.vo.Nc65BomReplVO;
import com.qjyy.base.domain.vo.Nc65JydwVO;
import com.qjyy.base.domain.vo.Nc65MarbasclassTreeVO;
import com.qjyy.base.domain.vo.Nc65MaterialBriefVO;
import com.qjyy.base.domain.vo.YkMonthPlanStatVO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BaseConvert {

	BaseConvert INSTANCE = Mappers.getMapper(BaseConvert.class);

	@Mapping(target = "children", ignore = true) // children 由组树逻辑填充
	@Mapping(target = "materialCount", ignore = true)
	@Mapping(target = "totalMaterialCount", ignore = true)
	Nc65MarbasclassTreeVO toMarbasclassTreeVO(Nc65Marbasclass entity);

	List<Nc65MarbasclassTreeVO> toMarbasclassTreeVOList(List<Nc65Marbasclass> list);

	/**
	 * Nc65Material -> Nc65MaterialBriefVO
	 */
	Nc65MaterialBriefVO toMaterialBriefVO(Nc65Material e);

	/**
	 * List<Nc65Material> -> List<Nc65MaterialBriefVO>
	 */
	List<Nc65MaterialBriefVO> toMaterialBriefVOList(List<Nc65Material> list);

	/**
	 * hdr -> hdrVO items 由组树逻辑填充，不在 MapStruct 做
	 */
	@Mapping(target = "items", ignore = true)
	Nc65BomHdrVO toBomHdrVO(Nc65BomHdr entity);

	List<Nc65BomHdrVO> toBomHdrVOList(List<Nc65BomHdr> list);

	/**
	 * body -> bodyVO replacements 由组树逻辑填充，不在 MapStruct 做
	 */
	@Mapping(target = "replacements", ignore = true)
	Nc65BomBodyVO toBomBodyVO(Nc65BomBody entity);

	List<Nc65BomBodyVO> toBomBodyVOList(List<Nc65BomBody> list);

	/**
	 * repl -> replVO
	 */
	Nc65BomReplVO toBomReplVO(Nc65BomRepl entity);

	List<Nc65BomReplVO> toBomReplVOList(List<Nc65BomRepl> list);
	
	
	
    // Row -> Hdr Entity
    @Mapping(target = "id",        source = "hdrId")
    @Mapping(target = "mlId",      source = "hdrMlId")
    @Mapping(target = "unit",      source = "hdrUnit")
    @Mapping(target = "nnum",      source = "hdrNnum")
    @Mapping(target = "nastunit",  source = "hdrNastunit")
    @Mapping(target = "nastnum",   source = "hdrNastnum")
    @Mapping(target = "rate",      source = "hdrRate")
    @Mapping(target = "version",   source = "hdrVersion")
    @Mapping(target = "hbdefault", source = "hdrHbdefault")
    Nc65BomHdr toBomHdrEntity(Nc65BomFlatRow row);

    // Row -> Body Entity
    @Mapping(target = "id",          source = "bodyId")
    @Mapping(target = "parentBomId", source = "bodyParentBomId")
    @Mapping(target = "mlId",        source = "bodyMlId")
    @Mapping(target = "version",     source = "bodyVersion")
    @Mapping(target = "unit",        source = "bodyUnit")
    @Mapping(target = "baseNum",     source = "bodyBaseNum")
    @Mapping(target = "nastunit",    source = "bodyNastunit")
    @Mapping(target = "useNum",      source = "bodyUseNum")
    @Mapping(target = "convertRate", source = "bodyConvertRate")
    @Mapping(target = "bcanreplace", source = "bodyBcanreplace")
    @Mapping(target = "freplacetype",source = "bodyFreplacetype")
    Nc65BomBody toBomBodyEntity(Nc65BomFlatRow row);

    // Row -> Repl Entity
    @Mapping(target = "id",           source = "replId")
    @Mapping(target = "cbomBid",      source = "replCbomBid")
    @Mapping(target = "replRowno",    source = "replRowno")
    @Mapping(target = "replWlcode",   source = "replWlcode")
    @Mapping(target = "replWlname",   source = "replWlname")
    @Mapping(target = "vreplaceindex",source = "replVreplaceindex")
    Nc65BomRepl toBomReplEntity(Nc65BomFlatRow row);
    
    Nc65JydwVO toJydwVO(Nc65Jydw e);

	List<Nc65JydwVO> toJydwVOList(List<Nc65Jydw> list);

	/**
	 * 月度计划统计 -> 视图
	 */
	YkMonthPlanStatVO toMonthPlanStatVO(YkMonthPlanStat entity);

	List<YkMonthPlanStatVO> toMonthPlanStatVOList(List<YkMonthPlanStat> list);

	// ================== 类型小转换（hbdefault 常见） ==================
	/**
	 * hbdefault: tinyint(1)/Integer -> Boolean 如果你的 entity 里 hbdefault 是
	 * Integer/Short，就会用到这个
	 */
	default Boolean toBool(Integer v) {
		if (v == null) {
			return null;
		}
		return v == 1;
	}
}
