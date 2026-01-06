package top.hcode.hoj.controller.admin;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.pojo.entity.user.Session;
import top.hcode.hoj.pojo.vo.MonthRankingVO;
import top.hcode.hoj.service.admin.system.DashboardService;


import java.util.Map;

/**
 * @Author: Himit_ZH
 * @Date: 2020/12/6 15:10
 * @Description:
 */
@RestController
@RequestMapping("/api/admin/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;


    @PostMapping("/get-sessions")
    @RequiresAuthentication
    @RequiresRoles(value = {"root","admin","problem_admin", "teacher"},logical = Logical.OR)
    public CommonResult<Session> getRecentSession(){

        return dashboardService.getRecentSession();
    }

    @GetMapping("/get-dashboard-info")
    @RequiresAuthentication
    @RequiresRoles(value = {"root","admin","problem_admin", "teacher"},logical = Logical.OR)
    public CommonResult<Map<Object,Object>> getDashboardInfo(){

        return dashboardService.getDashboardInfo();
    }

    @PostMapping("/update-ranking-list")
    @RequiresAuthentication
    @RequiresRoles(value = {"root","admin", "teacher"},logical = Logical.OR)
    public CommonResult<Void> updateRankingList() {
        return dashboardService.updateRankingList();
    }

    @PostMapping("/update-month-ranking")
    @RequiresAuthentication
    @RequiresRoles(value = {"root", "teacher"},logical = Logical.OR)
    public CommonResult<Void> updateMonthRanking(@RequestBody MonthRankingVO monthRankingVO) {
        return dashboardService.updateMonthRanking(monthRankingVO);
    }

    @PostMapping("/update-ac")
    @RequiresAuthentication
    @RequiresRoles(value = {"root","admin", "teacher"},logical = Logical.OR)
    public CommonResult<Void> updateAc() {
        return dashboardService.updateAc();
    }
}