package top.hcode.hoj.manager.admin.payment;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import top.hcode.hoj.dao.payment.OrdersEntityService;
import top.hcode.hoj.pojo.entity.payment.Orders;

/**
 * @Author: Minipax
 * @Date: 2023/10/10 13:02
 */
@Component
@RefreshScope
@Slf4j(topic = "hoj")
public class AdminOrdersManager {

    @Autowired
    private OrdersEntityService ordersEntityService;

    public IPage<Orders> getOrdersList(Integer limit, Integer currentPage) {
        if (currentPage == null || currentPage < 1) currentPage = 1;
        if (limit == null || limit < 1) limit = 10;
        IPage<Orders> iPage = new Page<>(currentPage, limit);
//        if (!StringUtils.isEmpty(keyword)) {
//            keyword = keyword.trim();
//        }

        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();

        return ordersEntityService.page(iPage, queryWrapper);
    }
}
