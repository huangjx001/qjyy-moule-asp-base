package com.qjyy.base.service;

import java.util.List;

import com.qjyy.base.domain.bo.DeviceMountBo;
import com.qjyy.base.domain.vo.ResourceDeviceSummaryVO;

public interface DeviceMountService {

	Long addResourceMount(DeviceMountBo bo);

	Long addStepMount(DeviceMountBo bo);

	boolean deleteMount(Long mountId);

	List<ResourceDeviceSummaryVO> listResourceDevices(Long resourceRoomId);
}
