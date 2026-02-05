package com.qjyy.base.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qjyy.base.domain.bo.DeviceMountBo;
import com.qjyy.base.domain.entity.DeviceMount;
import com.qjyy.base.domain.entity.ProcessRouteVersion;
import com.qjyy.base.domain.entity.ResourceRoom;
import com.qjyy.base.domain.entity.StepNode;
import com.qjyy.base.domain.enums.RouteVersionStatusEnum;
import com.qjyy.base.domain.vo.ResourceDeviceSummaryVO;
import com.qjyy.base.mapper.DeviceMountMapper;
import com.qjyy.base.mapper.ProcessGraphQueryMapper;
import com.qjyy.base.mapper.ProcessRouteVersionMapper;
import com.qjyy.base.mapper.ResourceRoomMapper;
import com.qjyy.base.mapper.StepNodeMapper;
import com.qjyy.base.service.DeviceMountService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceMountServiceImpl implements DeviceMountService {

	private final DeviceMountMapper deviceMountMapper;
	private final ResourceRoomMapper resourceRoomMapper;
	private final StepNodeMapper stepNodeMapper;
	private final ProcessRouteVersionMapper routeVersionMapper;
	private final ProcessGraphQueryMapper graphQueryMapper;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long addResourceMount(DeviceMountBo bo) {
		// 资源级设备挂载
		ResourceRoom resource = resourceRoomMapper.selectById(bo.getResourceRoomId());
		if (resource == null) {
			log.warn("资源挂载失败, 资源不存在, resourceRoomId={}", bo.getResourceRoomId());
			return null;
		}
		ProcessRouteVersion version = routeVersionMapper.selectById(resource.getRouteVersionId());
		if (version == null || RouteVersionStatusEnum.RELEASED.getCode().equals(version.getStatus())) {
			log.warn("资源挂载失败, 版本不可编辑, resourceRoomId={}", bo.getResourceRoomId());
			return null;
		}
		DeviceMount mount = new DeviceMount();
		mount.setRouteVersionId(resource.getRouteVersionId());
		mount.setResourceRoomId(resource.getId());
		mount.setDeviceId(bo.getDeviceId());
		mount.setMountType("RESOURCE");
		deviceMountMapper.insert(mount);
		log.info("资源挂载成功, mountId={}, resourceRoomId={}, deviceId={}", mount.getId(), mount.getResourceRoomId(),
				mount.getDeviceId());
		return mount.getId();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long addStepMount(DeviceMountBo bo) {
		// 工步级设备挂载
		StepNode step = stepNodeMapper.selectById(bo.getStepNodeId());
		if (step == null) {
			log.warn("工步挂载失败, 工步不存在, stepNodeId={}", bo.getStepNodeId());
			return null;
		}
		ProcessRouteVersion version = routeVersionMapper.selectById(step.getRouteVersionId());
		if (version == null || RouteVersionStatusEnum.RELEASED.getCode().equals(version.getStatus())) {
			log.warn("工步挂载失败, 版本不可编辑, stepNodeId={}", bo.getStepNodeId());
			return null;
		}
		DeviceMount mount = new DeviceMount();
		mount.setRouteVersionId(step.getRouteVersionId());
		mount.setResourceRoomId(step.getResourceRoomId());
		mount.setStepNodeId(step.getId());
		mount.setDeviceId(bo.getDeviceId());
		mount.setMountType("STEP");
		deviceMountMapper.insert(mount);
		log.info("工步挂载成功, mountId={}, stepNodeId={}, deviceId={}", mount.getId(), mount.getStepNodeId(),
				mount.getDeviceId());
		return mount.getId();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteMount(Long mountId) {
		// 删除挂载
		DeviceMount mount = deviceMountMapper.selectById(mountId);
		if (mount == null) {
			log.warn("删除挂载失败, 挂载不存在, mountId={}", mountId);
			return false;
		}
		ProcessRouteVersion version = routeVersionMapper.selectById(mount.getRouteVersionId());
		if (version == null || RouteVersionStatusEnum.RELEASED.getCode().equals(version.getStatus())) {
			log.warn("删除挂载失败, 版本不可编辑, mountId={}", mountId);
			return false;
		}
		boolean deleted = deviceMountMapper.deleteById(mountId) > 0;
		log.info("删除挂载, mountId={}, deleted={}", mountId, deleted);
		return deleted;
	}

	@Override
	public List<ResourceDeviceSummaryVO> listResourceDevices(Long resourceRoomId) {
		// 资源设备汇总
		return graphQueryMapper.selectResourceDeviceSummary(resourceRoomId);
	}
}
