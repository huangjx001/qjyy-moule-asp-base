package com.qjyy.base.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qjyy.base.convert.BaseDataConvert;
import com.qjyy.base.domain.bo.ProductBaseSaveBo;
import com.qjyy.base.domain.entity.ProductBase;
import com.qjyy.base.domain.vo.ProductBaseOptionVO;
import com.qjyy.base.domain.vo.ProductBaseVO;
import com.qjyy.base.mapper.ProductBaseMapper;
import com.qjyy.base.service.ProductBaseService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductBaseServiceImpl implements ProductBaseService {

	private final ProductBaseMapper productBaseMapper;
	private final BaseDataConvert baseDataConvert;

	@Override
	public Long create(ProductBaseSaveBo bo) {
		ProductBase entity = baseDataConvert.toProductBase(bo);
		normalize(entity);
		productBaseMapper.insert(entity);
		log.info("创建产品基础数据成功, id={}, code={}", entity.getId(), entity.getProductCode());
		return entity.getId();
	}

	@Override
	public boolean update(Long id, ProductBaseSaveBo bo) {
		ProductBase entity = baseDataConvert.toProductBase(bo);
		normalize(entity);
		entity.setId(id);
		boolean updated = productBaseMapper.updateById(entity) > 0;
		log.info("更新产品基础数据, id={}, updated={}", id, updated);
		return updated;
	}

	@Override
	public boolean delete(Long id) {
		boolean deleted = productBaseMapper.deleteById(id) > 0;
		log.info("删除产品基础数据, id={}, deleted={}", id, deleted);
		return deleted;
	}

	@Override
	public ProductBaseVO get(Long id) {
		ProductBase entity = productBaseMapper.selectById(id);
		return entity == null ? null : baseDataConvert.toProductBaseVO(entity);
	}

	@Override
	public List<ProductBaseVO> listAll() {
		LambdaQueryWrapper<ProductBase> wrapper = new LambdaQueryWrapper<>();
		wrapper.orderByDesc(ProductBase::getId);
		return baseDataConvert.toProductBaseVOList(productBaseMapper.selectList(wrapper));
	}

	@Override
	public List<ProductBaseOptionVO> listOptions(String keyword) {
		LambdaQueryWrapper<ProductBase> wrapper = buildKeywordWrapper(keyword);
		wrapper.eq(ProductBase::getEnabled, 1);
		wrapper.orderByDesc(ProductBase::getId);
		return baseDataConvert.toProductBaseOptionList(productBaseMapper.selectList(wrapper));
	}

	private void normalize(ProductBase entity) {
		entity.setProductCode(trimToNull(entity.getProductCode()));
		entity.setProductName(trimToNull(entity.getProductName()));
		entity.setProductType(trimToNull(entity.getProductType()));
		entity.setDosageForm(trimToNull(entity.getDosageForm()));
		entity.setRemark(trimToNull(entity.getRemark()));
	}

	private LambdaQueryWrapper<ProductBase> buildKeywordWrapper(String keyword) {
		LambdaQueryWrapper<ProductBase> wrapper = new LambdaQueryWrapper<>();
		if (StringUtils.hasText(keyword)) {
			String like = "%" + keyword.trim() + "%";
			wrapper.and(q -> q.like(ProductBase::getProductCode, like).or()
					.like(ProductBase::getProductName, like).or()
					.like(ProductBase::getProductType, like));
		}
		return wrapper;
	}

	private String trimToNull(String value) {
		return StringUtils.hasText(value) ? value.trim() : null;
	}
}
