package com.qjyy.base.service.impl;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qjyy.base.convert.BaseConvert;
import com.qjyy.base.domain.entity.Nc65Marbasclass;
import com.qjyy.base.domain.vo.Nc65MarbasclassTreeVO;
import com.qjyy.base.mapper.Nc65MarbasclassMapper;
import com.qjyy.base.mapper.Nc65MaterialMapper;
import com.qjyy.base.service.Nc65MarbasclassService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Nc65MarbasclassServiceImpl extends ServiceImpl<Nc65MarbasclassMapper, Nc65Marbasclass>
		implements Nc65MarbasclassService {

	private final BaseConvert baseConvert;
	private final Nc65MaterialMapper nc65MaterialMapper;

	@Override
	@Cacheable(cacheNames = "nc65:marbasclass:tree", key = "'all'", unless = "#result == null || #result.isEmpty()")
	public List<Nc65MarbasclassTreeVO> tree() {
		List<Nc65Marbasclass> classes = this.list();
		if (classes == null || classes.isEmpty()) {
			return Collections.emptyList();
		}
		// 1) entity -> vo（convert）
		List<Nc65MarbasclassTreeVO> vos = baseConvert.toMarbasclassTreeVOList(classes);
		if (vos == null || vos.isEmpty()) {
			return Collections.emptyList();
		}
		// 2) code -> node（清洗 + 初始化 children）
		Map<String, Nc65MarbasclassTreeVO> codeMap = new LinkedHashMap<>();
		for (Nc65MarbasclassTreeVO n : vos) {
			if (n == null)
				continue;

			String code = StringUtils.trimToNull(n.getCode());
			if (code == null)
				continue;

			n.setCode(code);
			n.setParentCode(StringUtils.trimToNull(n.getParentCode()));
			if (n.getChildren() == null)
				n.setChildren(new ArrayList<>());
			if (n.getMaterialCount() == null)
				n.setMaterialCount(0);
			if (n.getTotalMaterialCount() == null)
				n.setTotalMaterialCount(0);
			// 假设 code 唯一；若不唯一可改成 merge 策略
			codeMap.put(code, n);
		}

		if (codeMap.isEmpty()) {
			return Collections.emptyList();
		}

		// 3) 统计物料数量(typeNo -> count)，回填到节点 materialCount
		Map<String, Integer> countMap = Optional.ofNullable(nc65MaterialMapper.countByTypeNo())
				.orElse(Collections.emptyList()).stream().filter(x -> StringUtils.isNotBlank(x.getTypeNo()))
				.collect(Collectors.toMap(x -> x.getTypeNo().trim(),
						x -> x.getCnt() == null ? 0 : x.getCnt().intValue(), (a, b) -> a));

		for (Nc65MarbasclassTreeVO node : codeMap.values()) {
			node.setMaterialCount(countMap.getOrDefault(node.getCode(), 0));
		}

		// 4) 组树（parentCode 为空为根，多根）
		List<Nc65MarbasclassTreeVO> roots = new ArrayList<>();
		for (Nc65MarbasclassTreeVO node : codeMap.values()) {
			String parentCode = node.getParentCode();
			if (parentCode == null) {
				roots.add(node);
				continue;
			}
			Nc65MarbasclassTreeVO parent = codeMap.get(parentCode);
			if (parent == null || parent == node) {
				roots.add(node);
			} else {
				parent.getChildren().add(node);
			}
		}

		// 5) 计算含子孙总数
		for (Nc65MarbasclassTreeVO r : roots) {
			fillTotalCount(r);
		}

		// 6) 排序（可选）
		sortRecursivelyByCode(roots);

		return roots;
	}

	private int fillTotalCount(Nc65MarbasclassTreeVO node) {
		int total = node.getMaterialCount() == null ? 0 : node.getMaterialCount();
		List<Nc65MarbasclassTreeVO> children = node.getChildren();
		if (children != null) {
			for (Nc65MarbasclassTreeVO c : children) {
				total += fillTotalCount(c);
			}
		}
		node.setTotalMaterialCount(total);
		return total;
	}

	private void sortRecursivelyByCode(List<Nc65MarbasclassTreeVO> nodes) {
		if (nodes == null || nodes.isEmpty())
			return;

		nodes.sort(Comparator.comparing(n -> StringUtils.defaultString(n.getCode())));
		for (Nc65MarbasclassTreeVO n : nodes) {
			sortRecursivelyByCode(n.getChildren());
		}
	}

	@Override
	@Cacheable(cacheNames = "nc65:marbasclass:descCodes", key = "#rootCode", unless = "#result == null || #result.isEmpty()")
	public List<String> selfAndDescendantCodes(String rootCode) {
		String root = StringUtils.trimToNull(rootCode);
		if (root == null) {
			return Collections.emptyList();
		}
		// 只查构建树需要的字段即可
		List<Nc65Marbasclass> all = this.list(new LambdaQueryWrapper<Nc65Marbasclass>().select(Nc65Marbasclass::getCode,
				Nc65Marbasclass::getParentCode));

		if (all == null || all.isEmpty()) {
			return Collections.singletonList(root);
		}
		// parentCode -> childrenCodes
		Map<String, List<String>> childrenMap = new HashMap<>();
		for (Nc65Marbasclass e : all) {
			if (e == null)
				continue;
			String code = StringUtils.trimToNull(e.getCode());
			if (code == null)
				continue;
			String parent = StringUtils.trimToNull(e.getParentCode());
			childrenMap.computeIfAbsent(parent, k -> new ArrayList<>()).add(code);
		}
		// BFS 收集后代（含自身）
		Set<String> result = new LinkedHashSet<>();
		Deque<String> q = new ArrayDeque<>();
		q.add(root);
		while (!q.isEmpty()) {
			String cur = q.poll();
			if (!result.add(cur))
				continue; // 去重 + 防环
			List<String> kids = childrenMap.get(cur);
			if (kids != null) {
				for (String k : kids) {
					if (StringUtils.isNotBlank(k))
						q.add(k);
				}
			}
		}

		return new ArrayList<>(result);
	}
}
