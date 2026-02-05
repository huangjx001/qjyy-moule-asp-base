package com.qjyy.base.service;

import java.util.List;

import com.qjyy.base.domain.bo.ResourceRoomSaveBo;
import com.qjyy.base.domain.vo.ResourceRoomVO;

public interface ResourceRoomService {

	Long create(ResourceRoomSaveBo bo);

	boolean update(Long id, ResourceRoomSaveBo bo);

	boolean delete(Long id);

	List<ResourceRoomVO> listByProcessNode(Long processNodeId);
}
