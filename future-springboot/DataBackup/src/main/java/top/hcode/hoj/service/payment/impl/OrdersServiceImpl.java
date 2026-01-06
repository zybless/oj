package top.hcode.hoj.service.payment.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.manager.admin.payment.AdminOrdersManager;
import top.hcode.hoj.pojo.entity.payment.Orders;
import top.hcode.hoj.service.payment.OrdersService;

/**
 * @Author: Minipax
 * @Date: 2023/10/10 13:00
 */
@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private AdminOrdersManager adminOrdersManager;

    @Override
    public CommonResult<IPage<Orders>> getOrdersList(Integer limit, Integer currentPage) {
        IPage<Orders> ordersList = adminOrdersManager.getOrdersList(limit, currentPage);
        return CommonResult.successResponse(ordersList);
    }
}
