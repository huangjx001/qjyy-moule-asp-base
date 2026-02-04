package com.qjyy.base.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qjyy.base.convert.RouteConvert;
import com.qjyy.base.domain.bo.ResourceRoomSaveBo;
import com.qjyy.base.domain.entity.DeviceMount;
import com.qjyy.base.domain.entity.ProcessNode;
import com.qjyy.base.domain.entity.ProcessRouteVersion;
import com.qjyy.base.domain.entity.ResourceRoom;
import com.qjyy.base.domain.entity.StepEdge;
import com.qjyy.base.domain.entity.StepNode;
import com.qjyy.base.domain.enums.RouteVersionStatusEnum;
import com.qjyy.base.domain.vo.ResourceRoomVO;
import com.qjyy.base.mapper.DeviceMountMapper;
import com.qjyy.base.mapper.ProcessNodeMapper;
import com.qjyy.base.mapper.ProcessRouteVersionMapper;
import com.qjyy.base.mapper.ResourceRoomMapper;
import com.qjyy.base.mapper.StepEdgeMapper;
import com.qjyy.base.mapper.StepNodeMapper;
import com.qjyy.base.service.ResourceRoomService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResourceRoomServiceImpl implements ResourceRoomService {

	private final ResourceRoomMapper resourceRoomMapper;
	private final ProcessRouteVersionMapper routeVersionMapper;
	private final StepNodeMapper stepNodeMapper;
	private final StepEdgeMapper stepEdgeMapper;
	private final DeviceMountMapper deviceMountMapper;
	private final ProcessNodeMapper processNodeMapper;
	private final RouteConvert routeConvert;

	@Override
	public Long create(ResourceRoomSaveBo bo) {
		ProcessRouteVersion version = routeVersionMapper.selectById(bo.getRouteVersionId());
		if (version == null || RouteVersionStatusEnum.RELEASED.getCode().equals(version.getStatus())) {
			return null;
		}
		ProcessNode node = processNodeMapper.selectById(bo.getProcessNodeId());
		if (node == null || !bo.getRouteVersionId().equals(node.getRouteVersionId())) {
			return null;
		}
		ResourceRoom entity = routeConvert.toResourceRoom(bo);
		resourceRoomMapper.insert(entity);
		return entity.getId();
	}

	@Override
	public boolean update(Long id, ResourceRoomSaveBo bo) {
		ResourceRoom existing = resourceRoomMapper.selectById(id);
		if (existing == null) {
			return false;
		}
		ProcessRouteVersion version = routeVersionMapper.selectById(existing.getRouteVersionId());
		if (version == null || RouteVersionStatusEnum.RELEASED.getCode().equals(version.getStatus())) {
			return false;
		}
		ProcessNode node = processNodeMapper.selectById(bo.getProcessNodeId());
		if (node == null || !existing.getRouteVersionId().equals(node.getRouteVersionId())) {
			return false;
		}
		ResourceRoom entity = routeConvert.toResourceRoom(bo);
		entity.setId(id);
		entity.setRouteVersionId(existing.getRouteVersionId());
		return resourceRoomMapper.updateById(entity) > 0;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean delete(Long id) {
		ResourceRoom resource = resourceRoomMapper.selectById(id);
		if (resource == null) {
			return false;
		}
		ProcessRouteVersion version = routeVersionMapper.selectById(resource.getRouteVersionId());
		if (version == null || RouteVersionStatusEnum.RELEASED.getCode().equals(version.getStatus())) {
			return false;
		}
		LambdaQueryWrapper<StepEdge> edgeWrapper = new LambdaQueryWrapper<>();
		edgeWrapper.eq(StepEdge::getResourceRoomId, id);
		stepEdgeMapper.delete(edgeWrapper);
		LambdaQueryWrapper<StepNode> nodeWrapper = new LambdaQueryWrapper<>();
		nodeWrapper.eq(StepNode::getResourceRoomId, id);
		stepNodeMapper.delete(nodeWrapper);
		LambdaQueryWrapper<DeviceMount> mountWrapper = new LambdaQueryWrapper<>();
		mountWrapper.eq(DeviceMount::getResourceRoomId, id);
		deviceMountMapper.delete(mountWrapper);
		return resourceRoomMapper.deleteById(id) > 0;
	}

	@Override
	public List<ResourceRoomVO> listByProcessNode(Long processNodeId) {
		LambdaQueryWrapper<ResourceRoom> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(ResourceRoom::getProcessNodeId, processNodeId);
		return routeConvert.toResourceRoomVOList(resourceRoomMapper.selectList(wrapper));
	}

}
