package com.qjyy.base.service.impl;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qjyy.base.convert.BaseConvert;
import com.qjyy.base.domain.bo.YkMonthPlanStatPageQuery;
import com.qjyy.base.domain.entity.YkMonthPlanStat;
import com.qjyy.base.domain.vo.YkMonthPlanStatVO;
import com.qjyy.base.mapper.YkMonthPlanStatMapper;
import com.qjyy.base.service.YkMonthPlanStatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class YkMonthPlanStatServiceImpl extends ServiceImpl<YkMonthPlanStatMapper, YkMonthPlanStat>
		implements YkMonthPlanStatService {

	private final BaseConvert baseConvert;

	@Override
	public IPage<YkMonthPlanStatVO> page(YkMonthPlanStatPageQuery bo) {
		normalize(bo);

		long pageNum = bo.getPageNum() == null || bo.getPageNum() < 1 ? 1L : bo.getPageNum();
		long pageSize = bo.getPageSize() == null || bo.getPageSize() < 1 ? 20L : bo.getPageSize();

		Page<YkMonthPlanStat> page = new Page<>(pageNum, pageSize);
		IPage<YkMonthPlanStat> entityPage = this.page(page, buildWrapper(bo));
		return entityPage.convert(this::toVo);
	}

	@Override
	public List<YkMonthPlanStatVO> list(YkMonthPlanStatPageQuery bo) {
		normalize(bo);
		return this.list(buildWrapper(bo)).stream().map(this::toVo).collect(Collectors.toList());
	}

	private void normalize(YkMonthPlanStatPageQuery bo) {
		// 未传年份时默认当前年；未传月份则不过滤月份
		if (bo.getUseYear() == null) {
			bo.setUseYear(YearMonth.now().getYear());
		}

		bo.setCode(trimToNull(bo.getCode()));
		bo.setMaterialShortName(trimToNull(bo.getMaterialShortName()));
		bo.setCurrencyName(trimToNull(bo.getCurrencyName()));
		bo.setPgdw(trimToNull(bo.getPgdw()));
		bo.setWorkshop(trimToNull(bo.getWorkshop()));
	}

	private LambdaQueryWrapper<YkMonthPlanStat> buildWrapper(YkMonthPlanStatPageQuery bo) {
		LambdaQueryWrapper<YkMonthPlanStat> qw = new LambdaQueryWrapper<>();

		qw.eq(Objects.nonNull(bo.getUseYear()), YkMonthPlanStat::getUseYear, bo.getUseYear());
		qw.eq(Objects.nonNull(bo.getUseMonth()), YkMonthPlanStat::getUseMonth, bo.getUseMonth());

		qw.like(StringUtils.hasText(bo.getCode()), YkMonthPlanStat::getCode, bo.getCode());
		qw.like(StringUtils.hasText(bo.getMaterialShortName()), YkMonthPlanStat::getMaterialShortName, bo.getMaterialShortName());
		qw.like(StringUtils.hasText(bo.getCurrencyName()), YkMonthPlanStat::getCurrencyName, bo.getCurrencyName());
		qw.like(StringUtils.hasText(bo.getPgdw()), YkMonthPlanStat::getPgdw, bo.getPgdw());
		qw.like(StringUtils.hasText(bo.getWorkshop()), YkMonthPlanStat::getWorkshop, bo.getWorkshop());

		qw.ge(Objects.nonNull(bo.getKclMin()), YkMonthPlanStat::getKcl, bo.getKclMin());
		qw.le(Objects.nonNull(bo.getKclMax()), YkMonthPlanStat::getKcl, bo.getKclMax());

		// 只看有计划数据：销售计划或发货计划任一不为空
		if (Boolean.TRUE.equals(bo.getOnlyHasPlan())) {
			qw.and(wrapper -> wrapper.isNotNull(YkMonthPlanStat::getSalePlanNum)
					.or()
					.isNotNull(YkMonthPlanStat::getDeliverPlanNum));
		}

		return qw;
	}

	private String trimToNull(String s) {
		return StringUtils.hasText(s) ? s.trim() : null;
	}

	private YkMonthPlanStatVO toVo(YkMonthPlanStat entity) {
		if (entity == null) {
			return null;
		}

		YkMonthPlanStatVO vo = baseConvert.toMonthPlanStatVO(entity);

		YearMonth base = buildBaseMonth(entity.getUseYear(), entity.getUseMonth());
		vo.setNextMonth(buildNextMonth(base == null ? null : base.plusMonths(1),
				entity.getDeliverPlanNumNextMonth(), entity.getDeliverPlanCostNextMonth()));
		vo.setNextTwoMonth(buildNextMonth(base == null ? null : base.plusMonths(2),
				entity.getDeliverPlanNumNextTwoMonth(), entity.getDeliverPlanCostNextTwoMonth()));

		return vo;
	}

	private YearMonth buildBaseMonth(Integer year, Integer month) {
		if (year == null || month == null) {
			return null;
		}
		return YearMonth.of(year, month);
	}

	private YkMonthPlanStatVO.MonthPlan buildNextMonth(YearMonth month, BigDecimal num, BigDecimal cost) {
		if (month == null) {
			return null;
		}
		YkMonthPlanStatVO.MonthPlan plan = new YkMonthPlanStatVO.MonthPlan();
		plan.setYear(month.getYear());
		plan.setMonth(month.getMonthValue());
		plan.setDeliverPlanNum(num);
		plan.setDeliverPlanCost(cost);
		return plan;
	}
}
