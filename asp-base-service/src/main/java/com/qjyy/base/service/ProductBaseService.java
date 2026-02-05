package com.qjyy.base.service;

import java.util.List;

import com.qjyy.base.domain.bo.ProductBaseSaveBo;
import com.qjyy.base.domain.vo.ProductBaseOptionVO;
import com.qjyy.base.domain.vo.ProductBaseVO;

public interface ProductBaseService {

	Long create(ProductBaseSaveBo bo);

	boolean update(Long id, ProductBaseSaveBo bo);

	boolean delete(Long id);

	ProductBaseVO get(Long id);

	List<ProductBaseVO> listAll();

	List<ProductBaseOptionVO> listOptions(String keyword);
}
