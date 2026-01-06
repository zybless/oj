package top.hcode.hoj.service.payment;

import com.baomidou.mybatisplus.core.metadata.IPage;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.pojo.entity.payment.Orders;

/**
 * @Author: Minipax
 * @Date: 2023/10/10 13:00
 */
public interface OrdersService {
    CommonResult<IPage<Orders>> getOrdersList(Integer limit, Integer currentPage);
}
