package top.hcode.hoj.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.hcode.hoj.annotation.AnonApi;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.pojo.entity.payment.Orders;
import top.hcode.hoj.service.payment.OrdersService;

/**
 * @Author: Minipax
 * @Date: 2023/10/10 13:01
 */
@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrdersController {

    @Autowired
    private OrdersService ordersService;

    @GetMapping("/get-orders-list")
    @RequiresAuthentication
    @RequiresRoles(value = {"root", "admin", "teacher"}, logical = Logical.OR)
    public CommonResult<IPage<Orders>> getOrdersList(@RequestParam(value = "limit", required = false) Integer limit,
                                                      @RequestParam(value = "currentPage", required = false) Integer currentPage) {
        return ordersService.getOrdersList(limit, currentPage);
    }

}
