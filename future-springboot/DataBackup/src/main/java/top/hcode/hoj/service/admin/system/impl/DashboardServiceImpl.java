package top.hcode.hoj.service.admin.system.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.manager.admin.system.DashboardManager;
import top.hcode.hoj.pojo.entity.user.Session;
import top.hcode.hoj.pojo.vo.MonthRankingVO;
import top.hcode.hoj.service.admin.system.DashboardService;

import java.util.Map;

/**
 * @Author: Himit_ZH
 * @Date: 2022/3/9 22:20
 * @Description:
 */
@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private DashboardManager dashboardManager;

    @Override
    public CommonResult<Session> getRecentSession() {
        return CommonResult.successResponse(dashboardManager.getRecentSession());
    }

    @Override
    public CommonResult<Map<Object, Object>> getDashboardInfo() {
        return CommonResult.successResponse(dashboardManager.getDashboardInfo());
    }

    @Override
    public CommonResult<Void> updateRankingList() {
        dashboardManager.updateRankingList();
        return CommonResult.successResponse();
    }

    @Override
    public CommonResult<Void> updateMonthRanking(MonthRankingVO monthRankingVO) {
        dashboardManager.updateMonthRanking(monthRankingVO);
        return CommonResult.successResponse();
    }

    @Override
    public CommonResult<Void> updateAc() {
        dashboardManager.updateAc();
        return CommonResult.successResponse();
    }
}