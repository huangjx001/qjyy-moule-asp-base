package com.qjyy.base.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qjyy.base.convert.RouteConvert;
import com.qjyy.base.domain.bo.ProcessRouteVersionCopyBo;
import com.qjyy.base.domain.bo.ProcessRouteVersionSaveBo;
import com.qjyy.base.domain.bo.ProcessRouteVersionUpdateBo;
import com.qjyy.base.domain.entity.DeviceMount;
import com.qjyy.base.domain.entity.ProcessEdge;
import com.qjyy.base.domain.entity.ProcessNode;
import com.qjyy.base.domain.entity.ProcessRoute;
import com.qjyy.base.domain.entity.ProcessRouteVersion;
import com.qjyy.base.domain.entity.ResourceRoom;
import com.qjyy.base.domain.entity.StepEdge;
import com.qjyy.base.domain.entity.StepNode;
import com.qjyy.base.domain.enums.RouteVersionStatusEnum;
import com.qjyy.base.domain.vo.GraphValidationErrorVO;
import com.qjyy.base.domain.vo.GraphValidationResultVO;
import com.qjyy.base.domain.vo.ProcessRouteVersionVO;
import com.qjyy.base.mapper.DeviceMountMapper;
import com.qjyy.base.mapper.ProcessEdgeMapper;
import com.qjyy.base.mapper.ProcessNodeMapper;
import com.qjyy.base.mapper.ProcessRouteMapper;
import com.qjyy.base.mapper.ProcessRouteVersionMapper;
import com.qjyy.base.mapper.ResourceRoomMapper;
import com.qjyy.base.mapper.StepEdgeMapper;
import com.qjyy.base.mapper.StepNodeMapper;
import com.qjyy.base.service.GraphValidationService;
import com.qjyy.base.service.ProcessRouteVersionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProcessRouteVersionServiceImpl implements ProcessRouteVersionService {

	private final ProcessRouteMapper processRouteMapper;
	private final ProcessRouteVersionMapper routeVersionMapper;
	private final ProcessNodeMapper processNodeMapper;
	private final ProcessEdgeMapper processEdgeMapper;
	private final ResourceRoomMapper resourceRoomMapper;
	private final StepNodeMapper stepNodeMapper;
	private final StepEdgeMapper stepEdgeMapper;
	private final DeviceMountMapper deviceMountMapper;
	private final RouteConvert routeConvert;
	private final GraphValidationService graphValidationService;

	@Override
	public Long create(ProcessRouteVersionSaveBo bo) {
		ProcessRoute route = processRouteMapper.selectById(bo.getRouteId());
		if (route == null) {
			return null;
		}
		ProcessRouteVersion entity = new ProcessRouteVersion();
		entity.setRouteId(bo.getRouteId());
		entity.setVersionCode(trimToNull(bo.getVersionCode()));
		entity.setVersionName(trimToNull(bo.getVersionName()));
		entity.setRemark(trimToNull(bo.getRemark()));
		entity.setStatus(RouteVersionStatusEnum.DRAFT.getCode());
		entity.setEnabled(1);
		routeVersionMapper.insert(entity);
		return entity.getId();
	}

	@Override
	public boolean update(Long id, ProcessRouteVersionUpdateBo bo) {
		ProcessRouteVersion entity = routeVersionMapper.selectById(id);
		if (entity == null || RouteVersionStatusEnum.RELEASED.getCode().equals(entity.getStatus())) {
			return false;
		}
		ProcessRouteVersion update = new ProcessRouteVersion();
		update.setId(id);
		update.setVersionName(trimToNull(bo.getVersionName()));
		update.setEnabled(bo.getEnabled());
		update.setRemark(trimToNull(bo.getRemark()));
		return routeVersionMapper.updateById(update) > 0;
	}

	@Override
	public ProcessRouteVersionVO get(Long id) {
		ProcessRouteVersion entity = routeVersionMapper.selectById(id);
		return entity == null ? null : routeConvert.toRouteVersionVO(entity);
	}

	@Override
	public List<ProcessRouteVersionVO> listByRouteId(Long routeId) {
		LambdaQueryWrapper<ProcessRouteVersion> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(ProcessRouteVersion::getRouteId, routeId).orderByDesc(ProcessRouteVersion::getId);
		return routeConvert.toRouteVersionVOList(routeVersionMapper.selectList(wrapper));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long copy(Long sourceVersionId, ProcessRouteVersionCopyBo bo) {
		ProcessRouteVersion source = routeVersionMapper.selectById(sourceVersionId);
		if (source == null) {
			return null;
		}
		ProcessRouteVersion target = new ProcessRouteVersion();
		target.setRouteId(source.getRouteId());
		target.setVersionCode(buildCopyValue(bo.getNewVersionCode(), source.getVersionCode(), "-COPY"));
		target.setVersionName(buildCopyValue(bo.getNewVersionName(), source.getVersionName(), "-副本"));
		target.setRemark(trimToNull(bo.getRemark()));
		target.setStatus(RouteVersionStatusEnum.DRAFT.getCode());
		target.setEnabled(1);
		routeVersionMapper.insert(target);

		Map<Long, Long> processNodeMap = new HashMap<>();
		List<ProcessNode> processNodes = listProcessNodes(sourceVersionId);
		for (ProcessNode node : processNodes) {
			ProcessNode copy = new ProcessNode();
			copy.setRouteVersionId(target.getId());
			copy.setNodeCode(node.getNodeCode());
			copy.setNodeName(node.getNodeName());
			copy.setNodeType(node.getNodeType());
			copy.setCriticalFlag(node.getCriticalFlag());
			copy.setDurationMinutes(node.getDurationMinutes());
			copy.setPositionX(node.getPositionX());
			copy.setPositionY(node.getPositionY());
			copy.setStatus(node.getStatus());
			processNodeMapper.insert(copy);
			processNodeMap.put(node.getId(), copy.getId());
		}
		List<ProcessEdge> processEdges = listProcessEdges(sourceVersionId);
		for (ProcessEdge edge : processEdges) {
			ProcessEdge copy = new ProcessEdge();
			copy.setRouteVersionId(target.getId());
			copy.setFromNodeId(processNodeMap.get(edge.getFromNodeId()));
			copy.setToNodeId(processNodeMap.get(edge.getToNodeId()));
			copy.setDependencyType(edge.getDependencyType());
			copy.setDependencyStrength(edge.getDependencyStrength());
			copy.setLagMinutes(edge.getLagMinutes());
			processEdgeMapper.insert(copy);
		}

		Map<Long, Long> resourceMap = new HashMap<>();
		List<ResourceRoom> resources = listResources(sourceVersionId);
		for (ResourceRoom resource : resources) {
			ResourceRoom copy = new ResourceRoom();
			copy.setRouteVersionId(target.getId());
			copy.setProcessNodeId(processNodeMap.get(resource.getProcessNodeId()));
			copy.setResourceCode(resource.getResourceCode());
			copy.setResourceName(resource.getResourceName());
			copy.setPriority(resource.getPriority());
			copy.setSetupMinutes(resource.getSetupMinutes());
			copy.setEnabled(resource.getEnabled());
			copy.setStatus(resource.getStatus());
			copy.setRemark(resource.getRemark());
			resourceRoomMapper.insert(copy);
			resourceMap.put(resource.getId(), copy.getId());
		}

		Map<Long, Long> stepNodeMap = new HashMap<>();
		List<StepNode> stepNodes = listStepNodes(sourceVersionId);
		for (StepNode node : stepNodes) {
			StepNode copy = new StepNode();
			copy.setRouteVersionId(target.getId());
			copy.setResourceRoomId(resourceMap.get(node.getResourceRoomId()));
			copy.setNodeCode(node.getNodeCode());
			copy.setNodeName(node.getNodeName());
			copy.setNodeType(node.getNodeType());
			copy.setQcFlag(node.getQcFlag());
			copy.setDurationMinutes(node.getDurationMinutes());
			copy.setPositionX(node.getPositionX());
			copy.setPositionY(node.getPositionY());
			copy.setStatus(node.getStatus());
			stepNodeMapper.insert(copy);
			stepNodeMap.put(node.getId(), copy.getId());
		}

		List<StepEdge> stepEdges = listStepEdges(sourceVersionId);
		for (StepEdge edge : stepEdges) {
			StepEdge copy = new StepEdge();
			copy.setRouteVersionId(target.getId());
			copy.setResourceRoomId(resourceMap.get(edge.getResourceRoomId()));
			copy.setFromNodeId(stepNodeMap.get(edge.getFromNodeId()));
			copy.setToNodeId(stepNodeMap.get(edge.getToNodeId()));
			copy.setDependencyType(edge.getDependencyType());
			copy.setDependencyStrength(edge.getDependencyStrength());
			copy.setLagMinutes(edge.getLagMinutes());
			stepEdgeMapper.insert(copy);
		}

		List<DeviceMount> mounts = listDeviceMounts(sourceVersionId);
		for (DeviceMount mount : mounts) {
			DeviceMount copy = new DeviceMount();
			copy.setRouteVersionId(target.getId());
			copy.setResourceRoomId(resourceMap.get(mount.getResourceRoomId()));
			copy.setStepNodeId(stepNodeMap.get(mount.getStepNodeId()));
			copy.setDeviceId(mount.getDeviceId());
			copy.setMountType(mount.getMountType());
			copy.setRemark(mount.getRemark());
			deviceMountMapper.insert(copy);
		}
		return target.getId();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public GraphValidationResultVO release(Long versionId) {
		ProcessRouteVersion version = routeVersionMapper.selectById(versionId);
		if (version == null) {
			return buildFail("VERSION_NOT_FOUND", "版本不存在");
		}
		if (RouteVersionStatusEnum.RELEASED.getCode().equals(version.getStatus())) {
			return buildFail("VERSION_RELEASED", "版本已发布");
		}
		List<ProcessNode> processNodes = listProcessNodes(versionId);
		List<ProcessEdge> processEdges = listProcessEdges(versionId);
		GraphValidationResultVO processResult = graphValidationService.validateProcessEntities(processNodes,
				processEdges);
		List<GraphValidationErrorVO> errors = new ArrayList<>();
		if (!processResult.isValid()) {
			errors.addAll(processResult.getErrors());
		}
		Map<Long, Integer> resourceCountMap = new HashMap<>();
		List<ResourceRoom> resources = listResources(versionId);
		for (ResourceRoom resource : resources) {
			resourceCountMap.put(resource.getProcessNodeId(),
					resourceCountMap.getOrDefault(resource.getProcessNodeId(), 0) + 1);
		}
		for (ProcessNode node : processNodes) {
			if (resourceCountMap.getOrDefault(node.getId(), 0) == 0) {
				GraphValidationErrorVO error = new GraphValidationErrorVO();
				error.setCode("RESOURCE_MISSING");
				error.setMessage("工序必须配置至少一个资源");
				error.setNodeId(node.getId());
				errors.add(error);
			}
		}
		Map<Long, List<StepNode>> stepNodeGroup = groupStepNodes(resources);
		Map<Long, List<StepEdge>> stepEdgeGroup = groupStepEdges(resources);
		for (ResourceRoom resource : resources) {
			List<StepNode> stepNodes = stepNodeGroup.get(resource.getId());
			List<StepEdge> stepEdges = stepEdgeGroup.get(resource.getId());
			GraphValidationResultVO stepResult = graphValidationService.validateStepEntities(stepNodes, stepEdges);
			if (!stepResult.isValid()) {
				errors.addAll(stepResult.getErrors());
			}
		}
		GraphValidationResultVO result = new GraphValidationResultVO();
		result.setErrors(errors);
		result.setValid(errors.isEmpty());
		if (result.isValid()) {
			ProcessRouteVersion update = new ProcessRouteVersion();
			update.setId(versionId);
			update.setStatus(RouteVersionStatusEnum.RELEASED.getCode());
			update.setReleasedAt(LocalDateTime.now());
			routeVersionMapper.updateById(update);
		}
		return result;
	}

	private GraphValidationResultVO buildFail(String code, String message) {
		GraphValidationErrorVO error = new GraphValidationErrorVO();
		error.setCode(code);
		error.setMessage(message);
		List<GraphValidationErrorVO> errors = new ArrayList<>();
		errors.add(error);
		GraphValidationResultVO result = new GraphValidationResultVO();
		result.setErrors(errors);
		result.setValid(false);
		return result;
	}

	private String trimToNull(String value) {
		return StringUtils.hasText(value) ? value.trim() : null;
	}

	private String buildCopyValue(String input, String source, String suffix) {
		String candidate = trimToNull(input);
		if (StringUtils.hasText(candidate)) {
			return candidate;
		}
		String base = trimToNull(source);
		return base == null ? null : base + suffix;
	}

	private List<ProcessNode> listProcessNodes(Long versionId) {
		LambdaQueryWrapper<ProcessNode> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(ProcessNode::getRouteVersionId, versionId);
		return processNodeMapper.selectList(wrapper);
	}

	private List<ProcessEdge> listProcessEdges(Long versionId) {
		LambdaQueryWrapper<ProcessEdge> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(ProcessEdge::getRouteVersionId, versionId);
		return processEdgeMapper.selectList(wrapper);
	}

	private List<ResourceRoom> listResources(Long versionId) {
		LambdaQueryWrapper<ResourceRoom> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(ResourceRoom::getRouteVersionId, versionId);
		return resourceRoomMapper.selectList(wrapper);
	}

	private List<StepNode> listStepNodes(Long versionId) {
		LambdaQueryWrapper<StepNode> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(StepNode::getRouteVersionId, versionId);
		return stepNodeMapper.selectList(wrapper);
	}

	private List<StepEdge> listStepEdges(Long versionId) {
		LambdaQueryWrapper<StepEdge> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(StepEdge::getRouteVersionId, versionId);
		return stepEdgeMapper.selectList(wrapper);
	}

	private List<DeviceMount> listDeviceMounts(Long versionId) {
		LambdaQueryWrapper<DeviceMount> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(DeviceMount::getRouteVersionId, versionId);
		return deviceMountMapper.selectList(wrapper);
	}

	private Map<Long, List<StepNode>> groupStepNodes(List<ResourceRoom> resources) {
		Map<Long, List<StepNode>> map = new HashMap<>();
		if (CollectionUtils.isEmpty(resources)) {
			return map;
		}
		List<Long> resourceIds = new ArrayList<>();
		for (ResourceRoom resource : resources) {
			resourceIds.add(resource.getId());
		}
		LambdaQueryWrapper<StepNode> wrapper = new LambdaQueryWrapper<>();
		wrapper.in(StepNode::getResourceRoomId, resourceIds);
		List<StepNode> list = stepNodeMapper.selectList(wrapper);
		for (StepNode node : list) {
			map.computeIfAbsent(node.getResourceRoomId(), key -> new ArrayList<>()).add(node);
		}
		return map;
	}

	private Map<Long, List<StepEdge>> groupStepEdges(List<ResourceRoom> resources) {
		Map<Long, List<StepEdge>> map = new HashMap<>();
		if (CollectionUtils.isEmpty(resources)) {
			return map;
		}
		List<Long> resourceIds = new ArrayList<>();
		for (ResourceRoom resource : resources) {
			resourceIds.add(resource.getId());
		}
		LambdaQueryWrapper<StepEdge> wrapper = new LambdaQueryWrapper<>();
		wrapper.in(StepEdge::getResourceRoomId, resourceIds);
		List<StepEdge> list = stepEdgeMapper.selectList(wrapper);
		for (StepEdge edge : list) {
			map.computeIfAbsent(edge.getResourceRoomId(), key -> new ArrayList<>()).add(edge);
		}
		return map;
	}
}
