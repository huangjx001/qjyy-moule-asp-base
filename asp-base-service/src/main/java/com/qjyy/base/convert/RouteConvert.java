package com.qjyy.base.convert;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.qjyy.base.domain.entity.ProcessEdge;
import com.qjyy.base.domain.entity.ProcessNode;
import com.qjyy.base.domain.entity.ProcessRoute;
import com.qjyy.base.domain.entity.ProcessRouteVersion;
import com.qjyy.base.domain.entity.ResourceRoom;
import com.qjyy.base.domain.entity.StepEdge;
import com.qjyy.base.domain.entity.StepNode;
import com.qjyy.base.domain.vo.ProcessEdgeVO;
import com.qjyy.base.domain.vo.ProcessNodeVO;
import com.qjyy.base.domain.vo.ProcessRouteVO;
import com.qjyy.base.domain.vo.ProcessRouteVersionVO;
import com.qjyy.base.domain.vo.ResourceRoomVO;
import com.qjyy.base.domain.vo.StepEdgeVO;
import com.qjyy.base.domain.vo.StepNodeVO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
		nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RouteConvert {

	ProcessRouteVO toRouteVO(ProcessRoute entity);

	List<ProcessRouteVO> toRouteVOList(List<ProcessRoute> list);

	ProcessRouteVersionVO toRouteVersionVO(ProcessRouteVersion entity);

	List<ProcessRouteVersionVO> toRouteVersionVOList(List<ProcessRouteVersion> list);

	List<ProcessNodeVO> toProcessNodeVOList(List<ProcessNode> list);

	List<ProcessEdgeVO> toProcessEdgeVOList(List<ProcessEdge> list);

	List<ResourceRoomVO> toResourceRoomVOList(List<ResourceRoom> list);

	List<StepNodeVO> toStepNodeVOList(List<StepNode> list);

	List<StepEdgeVO> toStepEdgeVOList(List<StepEdge> list);
}
