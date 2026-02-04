package com.qjyy.base.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.qjyy.base.convert.BaseConvert;
import com.qjyy.base.domain.bo.Nc65BomFlatRow;
import com.qjyy.base.domain.entity.Nc65BomBody;
import com.qjyy.base.domain.entity.Nc65BomHdr;
import com.qjyy.base.domain.entity.Nc65BomRepl;
import com.qjyy.base.domain.vo.Nc65BomBodyVO;
import com.qjyy.base.domain.vo.Nc65BomHdrVO;
import com.qjyy.base.domain.vo.Nc65BomReplVO;
import com.qjyy.base.domain.vo.Nc65BomTreeVO;
import com.qjyy.base.mapper.Nc65BomTreeQueryMapper;
import com.qjyy.base.service.Nc65BomQueryService;
import com.qjyy.base.service.Nc65MaterialService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Nc65BomQueryServiceImpl implements Nc65BomQueryService {

	private final Nc65BomTreeQueryMapper treeQueryMapper;

	private final Nc65MaterialService nc65MaterialService;

	private final BaseConvert baseConvert;

	@Override
	@Cacheable(cacheNames = "nc65:bom:tree", key = "'mlId:' + #mlId + ':onlyDefault:' + (#onlyDefault?:false) + ':includeRepl:' + (#includeRepl?:true)", unless = "#result == null || #result.versions == null || #result.versions.isEmpty()")
	public Nc65BomTreeVO queryBomTree(String mlId, Boolean onlyDefault, Boolean includeRepl) {

		List<Nc65BomFlatRow> rows = treeQueryMapper.selectBomFlatRows(mlId, onlyDefault, includeRepl);

		Nc65BomTreeVO tree = new Nc65BomTreeVO();
		tree.setMlId(mlId);
		tree.setVersions(new ArrayList<>());

		if (CollectionUtils.isEmpty(rows)) {
			return tree;
		}
		List<String> codes = rows.stream().map(Nc65BomFlatRow::getHdrMlId) // 获取主物料编码
				.map(StringUtils::trimToNull).filter(Objects::nonNull).distinct().collect(Collectors.toList());

		// 将子物料的编码（mlId）加入到批量查询中
		List<String> bodyCodes = rows.stream().map(Nc65BomFlatRow::getBodyMlId) // 获取子物料编码
				.map(StringUtils::trimToNull).filter(Objects::nonNull).distinct().collect(Collectors.toList());

		codes.addAll(bodyCodes); // 合并主物料和子物料的编码

		Map<String, String> nameMap = codes.isEmpty() ? new HashMap<>()
				: nc65MaterialService.getMaterialNameMapByProductCodes(codes);

		Map<String, Nc65BomHdrVO> hdrMap = new LinkedHashMap<>();
		Map<String, Map<String, Nc65BomBodyVO>> bodyMap = new HashMap<>();
		Map<String, Set<String>> replDedup = new HashMap<>();

		for (Nc65BomFlatRow row : rows) {

			// 1) hdr
			Nc65BomHdrVO hdrVO = hdrMap.get(row.getHdrId());
			if (hdrVO == null) {
				Nc65BomHdr hdrEntity = baseConvert.toBomHdrEntity(row);
				hdrVO = baseConvert.toBomHdrVO(hdrEntity);
				// ✅ 关键：填充物料名称
				String code = StringUtils.trimToNull(row.getHdrMlId());
				String materialName = code == null ? null : StringUtils.trimToNull(nameMap.get(code));
				hdrVO.setMaterialName(materialName); // 你 VO 里新增的字段
				hdrVO.setItems(new ArrayList<>());
				hdrMap.put(row.getHdrId(), hdrVO);
				bodyMap.put(row.getHdrId(), new LinkedHashMap<>());
			}

			// 2) body（可能为空）
			if (row.getBodyId() == null) {
				continue;
			}

			Map<String, Nc65BomBodyVO> bodyIdToVo = bodyMap.get(row.getHdrId());
			Nc65BomBodyVO bodyVO = bodyIdToVo.get(row.getBodyId());
			if (bodyVO == null) {
				Nc65BomBody bodyEntity = baseConvert.toBomBodyEntity(row);
				bodyVO = baseConvert.toBomBodyVO(bodyEntity);
				// ✅ 填充子物料的 materialName
				String bodyMaterialName = StringUtils.trimToNull(nameMap.get(row.getBodyMlId()));
				bodyVO.setMaterialName(bodyMaterialName); // 设置子物料名称
				bodyVO.setReplacements(new ArrayList<>());
				bodyIdToVo.put(row.getBodyId(), bodyVO);
				hdrVO.getItems().add(bodyVO);
			}

			// 3) repl（可能为空）
			if (row.getReplId() == null) {
				continue;
			}

			Set<String> replIds = replDedup.computeIfAbsent(row.getBodyId(), k -> new HashSet<>());
			if (!replIds.add(row.getReplId())) {
				continue;
			}

			Nc65BomRepl replEntity = baseConvert.toBomReplEntity(row);
			Nc65BomReplVO replVO = baseConvert.toBomReplVO(replEntity);
			bodyVO.getReplacements().add(replVO);
		}

		tree.setVersions(new ArrayList<>(hdrMap.values()));
		return tree;
	}
}
