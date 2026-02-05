package com.qjyy.base.service;

import java.util.List;

import com.qjyy.base.domain.bo.StepBaseSaveBo;
import com.qjyy.base.domain.vo.StepBaseOptionVO;
import com.qjyy.base.domain.vo.StepBaseVO;

public interface StepBaseService {

	Long create(StepBaseSaveBo bo);

	boolean update(Long id, StepBaseSaveBo bo);

	boolean delete(Long id);

	StepBaseVO get(Long id);

	List<StepBaseVO> listAll();

	List<StepBaseOptionVO> listOptions(String keyword);
}
