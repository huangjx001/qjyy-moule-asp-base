package com.qjyy.base.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qjyy.base.domain.entity.Device;

@Mapper
public interface DeviceMapper extends BaseMapper<Device> {
}
