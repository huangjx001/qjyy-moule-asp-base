package com.qjyy.base.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qjyy.base.convert.RouteConvert;
import com.qjyy.base.domain.bo.StepEdgeBo;
import com.qjyy.base.domain.bo.StepGraphSaveBo;
import com.qjyy.base.domain.bo.StepNodeBo;
import com.qjyy.base.domain.entity.ProcessRouteVersion;
import com.qjyy.base.domain.entity.ResourceRoom;
import com.qjyy.base.domain.entity.StepEdge;
import com.qjyy.base.domain.entity.StepNode;
import com.qjyy.base.domain.enums.RouteVersionStatusEnum;
import com.qjyy.base.domain.vo.GraphValidationResultVO;
import com.qjyy.base.domain.vo.StepGraphVO;
import com.qjyy.base.mapper.ProcessRouteVersionMapper;
import com.qjyy.base.mapper.ResourceRoomMapper;
import com.qjyy.base.mapper.StepEdgeMapper;
import com.qjyy.base.mapper.StepNodeMapper;
import com.qjyy.base.service.GraphValidationService;
import com.qjyy.base.service.StepGraphService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StepGraphServiceImpl implements StepGraphService {

	private final ResourceRoomMapper resourceRoomMapper;
	private final ProcessRouteVersionMapper routeVersionMapper;
	private final StepNodeMapper stepNodeMapper;
	private final StepEdgeMapper stepEdgeMapper;
	private final GraphValidationService graphValidationService;
	private final RouteConvert routeConvert;

	@Override
	public StepGraphVO getGraph(Long resourceRoomId) {
		StepGraphVO vo = new StepGraphVO();
		vo.setResourceRoomId(resourceRoomId);
		LambdaQueryWrapper<StepNode> nodeWrapper = new LambdaQueryWrapper<>();
		nodeWrapper.eq(StepNode::getResourceRoomId, resourceRoomId);
		LambdaQueryWrapper<StepEdge> edgeWrapper = new LambdaQueryWrapper<>();
		edgeWrapper.eq(StepEdge::getResourceRoomId, resourceRoomId);
		vo.setNodes(routeConvert.toStepNodeVOList(stepNodeMapper.selectList(nodeWrapper)));
		vo.setEdges(routeConvert.toStepEdgeVOList(stepEdgeMapper.selectList(edgeWrapper)));
		return vo;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean saveGraph(StepGraphSaveBo bo) {
		if (bo == null || bo.getResourceRoomId() == null) {
			return false;
		}
		ResourceRoom resource = resourceRoomMapper.selectById(bo.getResourceRoomId());
		if (resource == null) {
			return false;
		}
		ProcessRouteVersion version = routeVersionMapper.selectById(resource.getRouteVersionId());
		if (version == null || RouteVersionStatusEnum.RELEASED.getCode().equals(version.getStatus())) {
			return false;
		}
		LambdaQueryWrapper<StepEdge> edgeWrapper = new LambdaQueryWrapper<>();
		edgeWrapper.eq(StepEdge::getResourceRoomId, bo.getResourceRoomId());
		stepEdgeMapper.delete(edgeWrapper);
		LambdaQueryWrapper<StepNode> nodeWrapper = new LambdaQueryWrapper<>();
		nodeWrapper.eq(StepNode::getResourceRoomId, bo.getResourceRoomId());
		stepNodeMapper.delete(nodeWrapper);
		if (!CollectionUtils.isEmpty(bo.getNodes())) {
			for (StepNodeBo nodeBo : bo.getNodes()) {
				StepNode node = new StepNode();
				node.setId(nodeBo.getId());
				node.setRouteVersionId(resource.getRouteVersionId());
				node.setResourceRoomId(bo.getResourceRoomId());
				node.setNodeCode(nodeBo.getNodeCode());
				node.setNodeName(nodeBo.getNodeName());
				node.setNodeType(nodeBo.getNodeType());
				node.setQcFlag(nodeBo.getQcFlag());
				node.setDurationMinutes(nodeBo.getDurationMinutes());
				node.setPositionX(nodeBo.getPositionX());
				node.setPositionY(nodeBo.getPositionY());
				node.setStatus(nodeBo.getStatus());
				stepNodeMapper.insert(node);
			}
		}
		if (!CollectionUtils.isEmpty(bo.getEdges())) {
			for (StepEdgeBo edgeBo : bo.getEdges()) {
				StepEdge edge = new StepEdge();
				edge.setId(edgeBo.getId());
				edge.setRouteVersionId(resource.getRouteVersionId());
				edge.setResourceRoomId(bo.getResourceRoomId());
				edge.setFromNodeId(edgeBo.getFromNodeId());
				edge.setToNodeId(edgeBo.getToNodeId());
				edge.setDependencyType(edgeBo.getDependencyType());
				edge.setDependencyStrength(edgeBo.getDependencyStrength());
				edge.setLagMinutes(edgeBo.getLagMinutes());
				stepEdgeMapper.insert(edge);
			}
		}
		return true;
	}

	@Override
	public GraphValidationResultVO validateGraph(StepGraphSaveBo bo) {
		return graphValidationService.validateStepGraph(bo.getNodes(), bo.getEdges());
	}
}
