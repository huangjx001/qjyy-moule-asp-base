package com.qjyy.base.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qjyy.base.convert.RouteConvert;
import com.qjyy.base.domain.bo.ProcessRouteProductSaveBo;
import com.qjyy.base.domain.entity.ProcessRoute;
import com.qjyy.base.domain.entity.ProcessRouteProduct;
import com.qjyy.base.domain.vo.ProcessRouteProductVO;
import com.qjyy.base.domain.vo.ProcessRouteVO;
import com.qjyy.base.mapper.ProcessRouteMapper;
import com.qjyy.base.mapper.ProcessRouteProductMapper;
import com.qjyy.base.service.ProcessRouteProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessRouteProductServiceImpl implements ProcessRouteProductService {

	private final ProcessRouteProductMapper routeProductMapper;
	private final ProcessRouteMapper routeMapper;
	private final RouteConvert routeConvert;

	@Override
	public Long create(ProcessRouteProductSaveBo bo) {
		ProcessRouteProduct entity = routeConvert.toRouteProduct(bo);
		normalize(entity);
		routeProductMapper.insert(entity);
		log.info("创建路线关联, id={}, routeId={}", entity.getId(), entity.getRouteId());
		return entity.getId();
	}

	@Override
	public boolean update(Long id, ProcessRouteProductSaveBo bo) {
		ProcessRouteProduct entity = routeConvert.toRouteProduct(bo);
		normalize(entity);
		entity.setId(id);
		boolean updated = routeProductMapper.updateById(entity) > 0;
		log.info("更新路线关联, id={}, updated={}", id, updated);
		return updated;
	}

	@Override
	public boolean delete(Long id) {
		boolean deleted = routeProductMapper.deleteById(id) > 0;
		log.info("删除路线关联, id={}, deleted={}", id, deleted);
		return deleted;
	}

	@Override
	public List<ProcessRouteProductVO> list(String productCode) {
		LambdaQueryWrapper<ProcessRouteProduct> wrapper = new LambdaQueryWrapper<>();
		if (StringUtils.hasText(productCode)) {
			wrapper.eq(ProcessRouteProduct::getProductCode, productCode);
		}
		wrapper.orderByDesc(ProcessRouteProduct::getId);
		return routeConvert.toRouteProductVOList(routeProductMapper.selectList(wrapper));
	}

	@Override
	public ProcessRouteVO resolveRoute(String productCode) {
		ProcessRouteProduct mapping = findProductMapping(productCode);
		if (mapping == null) {
			mapping = findDefaultMapping();
		}
		if (mapping == null) {
			return null;
		}
		ProcessRoute route = routeMapper.selectById(mapping.getRouteId());
		return route == null ? null : routeConvert.toRouteVO(route);
	}

	private ProcessRouteProduct findProductMapping(String productCode) {
		if (!StringUtils.hasText(productCode)) {
			return null;
		}
		LambdaQueryWrapper<ProcessRouteProduct> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(ProcessRouteProduct::getProductCode, productCode)
				.eq(ProcessRouteProduct::getEnabled, 1)
				.orderByDesc(ProcessRouteProduct::getId)
				.last("LIMIT 1");
		return routeProductMapper.selectOne(wrapper);
	}

	private ProcessRouteProduct findDefaultMapping() {
		LambdaQueryWrapper<ProcessRouteProduct> wrapper = new LambdaQueryWrapper<>();
		wrapper.isNull(ProcessRouteProduct::getProductCode)
				.eq(ProcessRouteProduct::getIsDefault, 1)
				.eq(ProcessRouteProduct::getEnabled, 1)
				.orderByDesc(ProcessRouteProduct::getId)
				.last("LIMIT 1");
		return routeProductMapper.selectOne(wrapper);
	}

	private void normalize(ProcessRouteProduct entity) {
		entity.setProductCode(trimToNull(entity.getProductCode()));
		entity.setRemark(trimToNull(entity.getRemark()));
	}

	private String trimToNull(String value) {
		return StringUtils.hasText(value) ? value.trim() : null;
	}
}
