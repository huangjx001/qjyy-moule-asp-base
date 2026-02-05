package com.qjyy.base.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qjyy.base.convert.BaseDataConvert;
import com.qjyy.base.domain.bo.ResourceBaseSaveBo;
import com.qjyy.base.domain.entity.ResourceBase;
import com.qjyy.base.domain.vo.ResourceBaseOptionVO;
import com.qjyy.base.domain.vo.ResourceBaseVO;
import com.qjyy.base.mapper.ResourceBaseMapper;
import com.qjyy.base.service.ResourceBaseService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceBaseServiceImpl implements ResourceBaseService {

	private final ResourceBaseMapper resourceBaseMapper;
	private final BaseDataConvert baseDataConvert;

	@Override
	public Long create(ResourceBaseSaveBo bo) {
		ResourceBase entity = baseDataConvert.toResourceBase(bo);
		normalize(entity);
		resourceBaseMapper.insert(entity);
		log.info("创建资源基础数据成功, id={}, code={}", entity.getId(), entity.getResourceCode());
		return entity.getId();
	}

	@Override
	public boolean update(Long id, ResourceBaseSaveBo bo) {
		ResourceBase entity = baseDataConvert.toResourceBase(bo);
		normalize(entity);
		entity.setId(id);
		boolean updated = resourceBaseMapper.updateById(entity) > 0;
		log.info("更新资源基础数据, id={}, updated={}", id, updated);
		return updated;
	}

	@Override
	public boolean delete(Long id) {
		boolean deleted = resourceBaseMapper.deleteById(id) > 0;
		log.info("删除资源基础数据, id={}, deleted={}", id, deleted);
		return deleted;
	}

	@Override
	public ResourceBaseVO get(Long id) {
		ResourceBase entity = resourceBaseMapper.selectById(id);
		return entity == null ? null : baseDataConvert.toResourceBaseVO(entity);
	}

	@Override
	public List<ResourceBaseVO> listAll() {
		LambdaQueryWrapper<ResourceBase> wrapper = new LambdaQueryWrapper<>();
		wrapper.orderByDesc(ResourceBase::getId);
		return baseDataConvert.toResourceBaseVOList(resourceBaseMapper.selectList(wrapper));
	}

	@Override
	public List<ResourceBaseOptionVO> listOptions(String keyword) {
		LambdaQueryWrapper<ResourceBase> wrapper = buildKeywordWrapper(keyword);
		wrapper.eq(ResourceBase::getEnabled, 1);
		wrapper.orderByDesc(ResourceBase::getId);
		return baseDataConvert.toResourceBaseOptionList(resourceBaseMapper.selectList(wrapper));
	}

	private void normalize(ResourceBase entity) {
		entity.setResourceCode(trimToNull(entity.getResourceCode()));
		entity.setResourceName(trimToNull(entity.getResourceName()));
		entity.setResourceType(trimToNull(entity.getResourceType()));
		entity.setCapacityDesc(trimToNull(entity.getCapacityDesc()));
		entity.setRemark(trimToNull(entity.getRemark()));
	}

	private LambdaQueryWrapper<ResourceBase> buildKeywordWrapper(String keyword) {
		LambdaQueryWrapper<ResourceBase> wrapper = new LambdaQueryWrapper<>();
		if (StringUtils.hasText(keyword)) {
			String like = "%" + keyword.trim() + "%";
			wrapper.and(q -> q.like(ResourceBase::getResourceCode, like).or()
					.like(ResourceBase::getResourceName, like).or()
					.like(ResourceBase::getResourceType, like));
		}
		return wrapper;
	}

	private String trimToNull(String value) {
		return StringUtils.hasText(value) ? value.trim() : null;
	}
}
