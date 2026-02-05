package com.qjyy.base.service;

import java.util.List;

import com.qjyy.base.domain.bo.ProcessBaseSaveBo;
import com.qjyy.base.domain.vo.ProcessBaseOptionVO;
import com.qjyy.base.domain.vo.ProcessBaseVO;

public interface ProcessBaseService {

	Long create(ProcessBaseSaveBo bo);

	boolean update(Long id, ProcessBaseSaveBo bo);

	boolean delete(Long id);

	ProcessBaseVO get(Long id);

	List<ProcessBaseVO> listAll();

	List<ProcessBaseOptionVO> listOptions(String keyword);
}
