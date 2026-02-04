package com.qjyy.base.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qjyy.base.convert.BaseConvert;
import com.qjyy.base.domain.bo.Nc65MaterialQueryBo;
import com.qjyy.base.domain.entity.Nc65Jydw;
import com.qjyy.base.domain.entity.Nc65Material;
import com.qjyy.base.domain.vo.Nc65JydwVO;
import com.qjyy.base.domain.vo.Nc65MaterialBriefVO;
import com.qjyy.base.mapper.Nc65MaterialMapper;
import com.qjyy.base.service.Nc65BomHdrService;
import com.qjyy.base.service.Nc65JydwService;
import com.qjyy.base.service.Nc65MarbasclassService;
import com.qjyy.base.service.Nc65MaterialService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class Nc65MaterialServiceImpl extends ServiceImpl<Nc65MaterialMapper, Nc65Material>
		implements Nc65MaterialService {

	private final Nc65MarbasclassService nc65MarbasclassService;

	private final Nc65BomHdrService nc65BomHdrService;

	private final Nc65JydwService nc65JydwService;

	private final BaseConvert baseConvert;

	@Override
	public Page<Nc65MaterialBriefVO> pageByBo(Nc65MaterialQueryBo bo) {
		// 获取分类代码和子类代码（如果需要）
		String rootCode = StringUtils.trimToNull(bo.getClassCode());
		List<String> codes = resolveCodesIfNeed(rootCode, bo.getIncludeChildren());
		log.info("解析后的分类代码: {}", codes);
		// 执行分页查询
		Page<Nc65Material> page = this.page(new Page<>(bo.getPageNum(), bo.getPageSize()), buildWrapper(bo, codes));
		// 如果没有记录，直接返回空的分页结果
		if (page.getTotal() == 0 || page.getRecords().isEmpty()) {
			return new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
		}
		IPage<Nc65MaterialBriefVO> iVoPage = page.convert(baseConvert::toMaterialBriefVO);
		Page<Nc65MaterialBriefVO> voPage = new Page<>(iVoPage.getCurrent(), iVoPage.getSize(), iVoPage.getTotal());
		voPage.setRecords(iVoPage.getRecords());
		enrichMaterialBriefList(voPage.getRecords());
		return voPage;

	}

	@Override
	public String getMaterialNameByProductCode(String productCode) {
		String code = trimToNull(productCode);
		if (code == null) {
			return "未知物料";
		}

		String name = this.lambdaQuery().select(Nc65Material::getMaterialName).eq(Nc65Material::getProductCode, code)
				.last("LIMIT 1").oneOpt().map(Nc65Material::getMaterialName).map(this::trimToNull).orElse(null);

		return name == null ? "未知物料" : name;
	}

	@Override
	public Map<String, String> getMaterialNameMapByProductCodes(List<String> productCodes) {
		List<String> codes = distinctTrimmed(productCodes);
		if (codes.isEmpty()) {
			return Collections.emptyMap();
		}

		return this.lambdaQuery().select(Nc65Material::getProductCode, Nc65Material::getMaterialName)
				.in(Nc65Material::getProductCode, codes).list().stream()
				.filter(m -> trimToNull(m.getProductCode()) != null)
				.filter(m -> trimToNull(m.getMaterialName()) != null).collect(Collectors
						.toMap(m -> trimToNull(m.getProductCode()), m -> trimToNull(m.getMaterialName()), (a, b) -> a));
	}

	private List<String> distinctTrimmed(List<String> list) {
		if (list == null || list.isEmpty()) {
			return Collections.emptyList();
		}
		return list.stream().map(this::trimToNull).filter(Objects::nonNull).distinct().collect(Collectors.toList());
	}

	private void enrichMaterialBriefList(List<Nc65MaterialBriefVO> voList) {
		if (voList == null || voList.isEmpty()) {
			return;
		}

		// 1) 收集当前页需要的 key（去空、去重）
		List<String> productCodes = voList.stream().map(Nc65MaterialBriefVO::getProductCode)
				.map(StringUtils::trimToNull).filter(Objects::nonNull).distinct().collect(Collectors.toList());

		List<String> materialIds = voList.stream().map(Nc65MaterialBriefVO::getId).map(StringUtils::trimToNull)
				.filter(Objects::nonNull).distinct().collect(Collectors.toList());

		// 2) 批量查 BOM（mlId -> cnt）
		Map<String, Long> bomCntMap = productCodes.isEmpty() ? Collections.emptyMap()
				: nc65BomHdrService.countBomHdrByMlIds(productCodes);

		// 3) 批量查计量单位（materialId -> List<Nc65JydwVO>）
		Map<String, List<Nc65JydwVO>> jydwMap = buildJydwMap(materialIds);

		final List<Nc65JydwVO> emptyJydw = Collections.emptyList();

		voList.forEach(vo -> {
			String code = StringUtils.trimToNull(vo.getProductCode());
			vo.setHasBom(code != null && bomCntMap.getOrDefault(code, 0L) > 0);

			String mid = StringUtils.trimToNull(vo.getId());
			vo.setJydwList(mid == null ? emptyJydw : jydwMap.getOrDefault(mid, emptyJydw));
		});
	}

	private Map<String, List<Nc65JydwVO>> buildJydwMap(List<String> materialIds) {
		if (materialIds == null || materialIds.isEmpty()) {
			return Collections.emptyMap();
		}

		List<Nc65Jydw> entities = nc65JydwService
				.list(new LambdaQueryWrapper<Nc65Jydw>().in(Nc65Jydw::getMaterialId, materialIds)
						.orderByAsc(Nc65Jydw::getMaterialId).orderByAsc(Nc65Jydw::getProname));

		if (entities == null || entities.isEmpty()) {
			return Collections.emptyMap();
		}

		List<Nc65JydwVO> voList = baseConvert.toJydwVOList(entities);
		if (voList == null || voList.isEmpty()) {
			return Collections.emptyMap();
		}

		return voList.stream().filter(Objects::nonNull).filter(x -> StringUtils.isNotBlank(x.getMaterialId()))
				.collect(Collectors.groupingBy(Nc65JydwVO::getMaterialId));
	}

	/**
	 * 仅在传了 classCode 时解析 codes；不传则返回 null 表示“不过滤分类”
	 */
	private List<String> resolveCodesIfNeed(String rootCode, Boolean includeChildren) {
		if (rootCode == null) {
			return null; // ✅ 关键：null 表示查全部
		}

		boolean withChildren = includeChildren == null || Boolean.TRUE.equals(includeChildren);
		if (!withChildren) {
			return Collections.singletonList(rootCode);
		}

		List<String> codes = nc65MarbasclassService.selfAndDescendantCodes(rootCode);
		if (codes == null || codes.isEmpty()) {
			return Collections.singletonList(rootCode);
		}

		codes.removeIf(StringUtils::isBlank);
		return codes.isEmpty() ? Collections.singletonList(rootCode) : codes;
	}

	/**
	 * 统一构建查询条件（后续加条件只改这里） codes: - null：不按分类过滤（查全部） - 非空：typeNo in (codes) -
	 * 空集合：强制查不到（防误查）
	 */
	private LambdaQueryWrapper<Nc65Material> buildWrapper(Nc65MaterialQueryBo bo, List<String> codes) {
		LambdaQueryWrapper<Nc65Material> qw = new LambdaQueryWrapper<>();
		// ✅ 分类过滤（可选）
		if (codes != null) {
			if (codes.isEmpty()) {
				// 传了 classCode 但解析不到任何code：避免全表扫，直接查不到
				qw.eq(Nc65Material::getTypeNo, "__NULL__");
				return qw;
			}
			qw.in(Nc65Material::getTypeNo, codes);
		}

		// ====== 其他条件（统一 trim）======
		String productCode = trimToNull(bo.getProductCode());
		if (productCode != null) {
			qw.eq(Nc65Material::getProductCode, productCode);
		}

		String materialName = trimToNull(bo.getMaterialName());
		if (materialName != null) {
			qw.like(Nc65Material::getMaterialName, materialName);
		}

		String specification = trimToNull(bo.getSpecification());
		if (specification != null) {
			qw.like(Nc65Material::getSpecification, specification);
		}

		String iscomsume = trimToNull(bo.getIscomsume());
		if (iscomsume != null) {
			qw.eq(Nc65Material::getIscomsume, iscomsume);
		}

		String workshopId = trimToNull(bo.getWorkshopId());
		if (workshopId != null) {
			qw.eq(Nc65Material::getWorkshopId, workshopId);
		}

		if (bo.getIsbatch() != null) {
			qw.eq(Nc65Material::getIsbatch, bo.getIsbatch());
		}

		if (bo.getStatus() != null && !bo.getStatus().isEmpty()) {
			bo.getStatus().removeIf(StringUtils::isBlank);
			if (!bo.getStatus().isEmpty()) {
				qw.in(Nc65Material::getStatus, bo.getStatus());
			}
		}

		// keyword：名称/编号联合搜索
		String keyword = trimToNull(bo.getKeyword());
		if (keyword != null) {
			qw.and(w -> w.like(Nc65Material::getMaterialName, keyword).or().like(Nc65Material::getProductCode,
					keyword));
		}

		// 新增的新品规格标识 (newspec)
		String newspec = trimToNull(bo.getNewspec());
		if (newspec != null) {
			qw.eq(Nc65Material::getNewspec, newspec);
		}

		return qw;
	}

	private String trimToNull(String s) {
		return StringUtils.trimToNull(s);
	}

}
