package com.qjyy.base.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qjyy.base.domain.entity.Nc65M4cStatYear;
import com.qjyy.base.mapper.Nc65M4cStatYearMapper;
import com.qjyy.base.service.Nc65M4cStatYearService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class Nc65M4cStatYearServiceImpl extends ServiceImpl<Nc65M4cStatYearMapper, Nc65M4cStatYear>
		implements Nc65M4cStatYearService {
}
