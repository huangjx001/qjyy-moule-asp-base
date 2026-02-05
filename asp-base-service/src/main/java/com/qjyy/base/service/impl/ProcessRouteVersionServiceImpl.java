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
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
		// 创建版本
		ProcessRoute route = processRouteMapper.selectById(bo.getRouteId());
		if (route == null) {
			log.warn("创建版本失败, routeId不存在, routeId={}", bo.getRouteId());
			return null;
		}
		ProcessRouteVersion entity = routeConvert.toProcessRouteVersion(bo);
		entity.setRouteId(bo.getRouteId());
		entity.setVersionCode(trimToNull(entity.getVersionCode()));
		entity.setVersionName(trimToNull(entity.getVersionName()));
		entity.setRemark(trimToNull(entity.getRemark()));
		entity.setStatus(RouteVersionStatusEnum.DRAFT.getCode());
		entity.setEnabled(1);
		routeVersionMapper.insert(entity);
		log.info("创建版本成功, versionId={}, routeId={}, versionCode={}", entity.getId(), entity.getRouteId(),
				entity.getVersionCode());
		return entity.getId();
	}

	@Override
	public boolean update(Long id, ProcessRouteVersionUpdateBo bo) {
		// 更新版本信息
		ProcessRouteVersion entity = routeVersionMapper.selectById(id);
		if (entity == null || RouteVersionStatusEnum.RELEASED.getCode().equals(entity.getStatus())) {
			log.warn("更新版本失败, versionId={}, status={}", id, entity == null ? null : entity.getStatus());
			return false;
		}
		ProcessRouteVersion update = new ProcessRouteVersion();
		update.setId(id);
		update.setVersionName(trimToNull(bo.getVersionName()));
		update.setEnabled(bo.getEnabled());
		update.setRemark(trimToNull(bo.getRemark()));
		boolean updated = routeVersionMapper.updateById(update) > 0;
		log.info("更新版本, versionId={}, updated={}", id, updated);
		return updated;
	}

	@Override
	public ProcessRouteVersionVO get(Long id) {
		// 查询版本详情
		ProcessRouteVersion entity = routeVersionMapper.selectById(id);
		return entity == null ? null : routeConvert.toRouteVersionVO(entity);
	}

	@Override
	public List<ProcessRouteVersionVO> listByRouteId(Long routeId) {
		// 查询路线下的版本列表
		LambdaQueryWrapper<ProcessRouteVersion> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(ProcessRouteVersion::getRouteId, routeId).orderByDesc(ProcessRouteVersion::getId);
		return routeConvert.toRouteVersionVOList(routeVersionMapper.selectList(wrapper));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long copy(Long sourceVersionId, ProcessRouteVersionCopyBo bo) {
		// 复制版本：完整复制工序图、资源、工步图、设备挂载到新版本
		ProcessRouteVersion source = routeVersionMapper.selectById(sourceVersionId);
		if (source == null) {
			log.warn("复制版本失败, 源版本不存在, sourceVersionId={}", sourceVersionId);
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
		log.info("开始复制版本, sourceVersionId={}, targetVersionId={}", sourceVersionId, target.getId());

		List<ProcessNode> processNodes = listProcessNodes(sourceVersionId);
		Map<Long, Long> processNodeMap = copyProcessNodes(target.getId(), processNodes);
		List<ProcessEdge> processEdges = listProcessEdges(sourceVersionId);
		copyProcessEdges(target.getId(), processNodeMap, processEdges);

		List<ResourceRoom> resources = listResources(sourceVersionId);
		Map<Long, Long> resourceMap = copyResources(target.getId(), processNodeMap, resources);

		List<StepNode> stepNodes = listStepNodes(sourceVersionId);
		Map<Long, Long> stepNodeMap = copyStepNodes(target.getId(), resourceMap, stepNodes);
		List<StepEdge> stepEdges = listStepEdges(sourceVersionId);
		copyStepEdges(target.getId(), resourceMap, stepNodeMap, stepEdges);

		List<DeviceMount> mounts = listDeviceMounts(sourceVersionId);
		copyDeviceMounts(target.getId(), resourceMap, stepNodeMap, mounts);
		log.info("复制版本完成, sourceVersionId={}, targetVersionId={}", sourceVersionId, target.getId());
		return target.getId();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public GraphValidationResultVO release(Long versionId) {
		// 发布版本，强制校验（工序图 + 资源 + 工步图）
		ProcessRouteVersion version = routeVersionMapper.selectById(versionId);
		if (version == null) {
			log.warn("发布失败, 版本不存在, versionId={}", versionId);
			return buildFail("VERSION_NOT_FOUND", "版本不存在");
		}
		if (RouteVersionStatusEnum.RELEASED.getCode().equals(version.getStatus())) {
			log.warn("发布失败, 版本已发布, versionId={}", versionId);
			return buildFail("VERSION_RELEASED", "版本已发布");
		}
		List<ProcessNode> processNodes = listProcessNodes(versionId);
		List<ProcessEdge> processEdges = listProcessEdges(versionId);
		List<ResourceRoom> resources = listResources(versionId);
		List<GraphValidationErrorVO> errors = new ArrayList<>();
		appendGraphErrors(errors, graphValidationService.validateProcessEntities(processNodes, processEdges));
		appendResourceErrors(errors, processNodes, resources);
		appendStepGraphErrors(errors, resources);
		GraphValidationResultVO result = new GraphValidationResultVO();
		result.setErrors(errors);
		result.setValid(errors.isEmpty());
		if (result.isValid()) {
			ProcessRouteVersion update = new ProcessRouteVersion();
			update.setId(versionId);
			update.setStatus(RouteVersionStatusEnum.RELEASED.getCode());
			update.setReleasedAt(LocalDateTime.now());
			routeVersionMapper.updateById(update);
			log.info("发布成功, versionId={}", versionId);
		} else {
			log.warn("发布失败, versionId={}, errorCount={}", versionId, errors.size());
		}
		return result;
	}

	private Map<Long, Long> copyProcessNodes(Long targetVersionId, List<ProcessNode> processNodes) {
		// 复制工序节点并返回旧新ID映射
		Map<Long, Long> processNodeMap = new HashMap<>();
		for (ProcessNode node : processNodes) {
			ProcessNode copy = routeConvert.toProcessNode(node);
			copy.setId(null);
			copy.setRouteVersionId(targetVersionId);
			processNodeMapper.insert(copy);
			processNodeMap.put(node.getId(), copy.getId());
		}
		return processNodeMap;
	}

	private void copyProcessEdges(Long targetVersionId, Map<Long, Long> processNodeMap,
			List<ProcessEdge> processEdges) {
		// 复制工序依赖边
		for (ProcessEdge edge : processEdges) {
			ProcessEdge copy = routeConvert.toProcessEdge(edge);
			copy.setId(null);
			copy.setRouteVersionId(targetVersionId);
			copy.setFromNodeId(processNodeMap.get(edge.getFromNodeId()));
			copy.setToNodeId(processNodeMap.get(edge.getToNodeId()));
			processEdgeMapper.insert(copy);
		}
	}

	private Map<Long, Long> copyResources(Long targetVersionId, Map<Long, Long> processNodeMap,
			List<ResourceRoom> resources) {
		// 复制资源并返回旧新ID映射
		Map<Long, Long> resourceMap = new HashMap<>();
		for (ResourceRoom resource : resources) {
			ResourceRoom copy = routeConvert.toResourceRoom(resource);
			copy.setId(null);
			copy.setRouteVersionId(targetVersionId);
			copy.setProcessNodeId(processNodeMap.get(resource.getProcessNodeId()));
			resourceRoomMapper.insert(copy);
			resourceMap.put(resource.getId(), copy.getId());
		}
		return resourceMap;
	}

	private Map<Long, Long> copyStepNodes(Long targetVersionId, Map<Long, Long> resourceMap,
			List<StepNode> stepNodes) {
		// 复制工步节点并返回旧新ID映射
		Map<Long, Long> stepNodeMap = new HashMap<>();
		for (StepNode node : stepNodes) {
			StepNode copy = routeConvert.toStepNode(node);
			copy.setId(null);
			copy.setRouteVersionId(targetVersionId);
			copy.setResourceRoomId(resourceMap.get(node.getResourceRoomId()));
			stepNodeMapper.insert(copy);
			stepNodeMap.put(node.getId(), copy.getId());
		}
		return stepNodeMap;
	}

	private void copyStepEdges(Long targetVersionId, Map<Long, Long> resourceMap, Map<Long, Long> stepNodeMap,
			List<StepEdge> stepEdges) {
		// 复制工步依赖边
		for (StepEdge edge : stepEdges) {
			StepEdge copy = routeConvert.toStepEdge(edge);
			copy.setId(null);
			copy.setRouteVersionId(targetVersionId);
			copy.setResourceRoomId(resourceMap.get(edge.getResourceRoomId()));
			copy.setFromNodeId(stepNodeMap.get(edge.getFromNodeId()));
			copy.setToNodeId(stepNodeMap.get(edge.getToNodeId()));
			stepEdgeMapper.insert(copy);
		}
	}

	private void copyDeviceMounts(Long targetVersionId, Map<Long, Long> resourceMap, Map<Long, Long> stepNodeMap,
			List<DeviceMount> mounts) {
		// 复制设备挂载
		for (DeviceMount mount : mounts) {
			DeviceMount copy = routeConvert.toDeviceMount(mount);
			copy.setId(null);
			copy.setRouteVersionId(targetVersionId);
			copy.setResourceRoomId(resourceMap.get(mount.getResourceRoomId()));
			copy.setStepNodeId(stepNodeMap.get(mount.getStepNodeId()));
			deviceMountMapper.insert(copy);
		}
	}

	private void appendGraphErrors(List<GraphValidationErrorVO> errors, GraphValidationResultVO result) {
		// 追加图校验错误
		if (result != null && !result.isValid() && !CollectionUtils.isEmpty(result.getErrors())) {
			errors.addAll(result.getErrors());
		}
	}

	private void appendResourceErrors(List<GraphValidationErrorVO> errors, List<ProcessNode> processNodes,
			List<ResourceRoom> resources) {
		// 追加资源配置错误
		Map<Long, Integer> resourceCountMap = new HashMap<>();
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
	}

	private void appendStepGraphErrors(List<GraphValidationErrorVO> errors, List<ResourceRoom> resources) {
		// 追加工步图校验错误
		Map<Long, List<StepNode>> stepNodeGroup = groupStepNodes(resources);
		Map<Long, List<StepEdge>> stepEdgeGroup = groupStepEdges(resources);
		for (ResourceRoom resource : resources) {
			List<StepNode> stepNodes = stepNodeGroup.get(resource.getId());
			List<StepEdge> stepEdges = stepEdgeGroup.get(resource.getId());
			GraphValidationResultVO stepResult = graphValidationService.validateStepEntities(stepNodes, stepEdges);
			if (stepResult != null && !stepResult.isValid() && !CollectionUtils.isEmpty(stepResult.getErrors())) {
				errors.addAll(stepResult.getErrors());
			}
		}
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
