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
		ResourceRoom resource = resourceRoomMapper.selectById(bo.getResourceRoomId());
		if (resource == null) {
			return null;
		}
		ProcessRouteVersion version = routeVersionMapper.selectById(resource.getRouteVersionId());
		if (version == null || RouteVersionStatusEnum.RELEASED.getCode().equals(version.getStatus())) {
			return null;
		}
		DeviceMount mount = new DeviceMount();
		mount.setRouteVersionId(resource.getRouteVersionId());
		mount.setResourceRoomId(resource.getId());
		mount.setDeviceId(bo.getDeviceId());
		mount.setMountType("RESOURCE");
		deviceMountMapper.insert(mount);
		return mount.getId();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long addStepMount(DeviceMountBo bo) {
		StepNode step = stepNodeMapper.selectById(bo.getStepNodeId());
		if (step == null) {
			return null;
		}
		ProcessRouteVersion version = routeVersionMapper.selectById(step.getRouteVersionId());
		if (version == null || RouteVersionStatusEnum.RELEASED.getCode().equals(version.getStatus())) {
			return null;
		}
		DeviceMount mount = new DeviceMount();
		mount.setRouteVersionId(step.getRouteVersionId());
		mount.setResourceRoomId(step.getResourceRoomId());
		mount.setStepNodeId(step.getId());
		mount.setDeviceId(bo.getDeviceId());
		mount.setMountType("STEP");
		deviceMountMapper.insert(mount);
		return mount.getId();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteMount(Long mountId) {
		DeviceMount mount = deviceMountMapper.selectById(mountId);
		if (mount == null) {
			return false;
		}
		ProcessRouteVersion version = routeVersionMapper.selectById(mount.getRouteVersionId());
		if (version == null || RouteVersionStatusEnum.RELEASED.getCode().equals(version.getStatus())) {
			return false;
		}
		return deviceMountMapper.deleteById(mountId) > 0;
	}

	@Override
	public List<ResourceDeviceSummaryVO> listResourceDevices(Long resourceRoomId) {
		return graphQueryMapper.selectResourceDeviceSummary(resourceRoomId);
	}
}
