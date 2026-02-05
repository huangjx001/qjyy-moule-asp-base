package com.qjyy.base.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.qjyy.base.domain.vo.ProcessNodeSummaryVO;
import com.qjyy.base.domain.vo.ResourceDeviceSummaryVO;

@Mapper
public interface ProcessGraphQueryMapper {

	List<ProcessNodeSummaryVO> selectProcessNodeSummary(@Param("routeVersionId") Long routeVersionId);

	List<ResourceDeviceSummaryVO> selectResourceDeviceSummary(@Param("resourceRoomId") Long resourceRoomId);
}
