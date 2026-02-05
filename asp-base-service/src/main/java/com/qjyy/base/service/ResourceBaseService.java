package com.qjyy.base.service;

import java.util.List;

import com.qjyy.base.domain.bo.ResourceBaseSaveBo;
import com.qjyy.base.domain.vo.ResourceBaseOptionVO;
import com.qjyy.base.domain.vo.ResourceBaseVO;

public interface ResourceBaseService {

	Long create(ResourceBaseSaveBo bo);

	boolean update(Long id, ResourceBaseSaveBo bo);

	boolean delete(Long id);

	ResourceBaseVO get(Long id);

	List<ResourceBaseVO> listAll();

	List<ResourceBaseOptionVO> listOptions(String keyword);
}
