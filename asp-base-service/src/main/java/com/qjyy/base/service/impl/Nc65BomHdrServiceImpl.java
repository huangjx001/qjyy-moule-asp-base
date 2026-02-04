package com.qjyy.base.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qjyy.base.domain.bo.Nc65BomHdrCountBo;
import com.qjyy.base.domain.entity.Nc65BomHdr;
import com.qjyy.base.mapper.Nc65BomHdrMapper;
import com.qjyy.base.service.Nc65BomHdrService;

@Service
public class Nc65BomHdrServiceImpl extends ServiceImpl<Nc65BomHdrMapper, Nc65BomHdr> implements Nc65BomHdrService {

	@Override
	public Map<String, Long> countBomHdrByMlIds(List<String> mlIds) {
		if (CollectionUtils.isEmpty(mlIds)) {
			return Collections.emptyMap();
		}
		List<String> ids = mlIds.stream().map(StringUtils::trimToNull).filter(Objects::nonNull).distinct()
				.collect(Collectors.toList());
		if (ids.isEmpty()) {
			return Collections.emptyMap();
		}
		List<Nc65BomHdrCountBo> rows = this.baseMapper.countByMlIds(ids);
		if (CollectionUtils.isEmpty(rows)) {
			return Collections.emptyMap();
		}
		return rows.stream().filter(Objects::nonNull).filter(r -> StringUtils.isNotBlank(r.getMlId())).collect(
				Collectors.toMap(Nc65BomHdrCountBo::getMlId, r -> r.getCnt() == null ? 0L : r.getCnt(), (a, b) -> a // 理论上不会重复，防御一下
				));
	}
}
