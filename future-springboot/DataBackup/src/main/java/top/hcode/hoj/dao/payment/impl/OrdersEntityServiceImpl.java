package top.hcode.hoj.dao.payment.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.hcode.hoj.dao.payment.OrdersEntityService;
import top.hcode.hoj.mapper.OrdersMapper;
import top.hcode.hoj.pojo.entity.payment.Orders;

/**
 * @Author: Minipax
 * @Date: 2023/10/8 16:09
 */
@Service
public class OrdersEntityServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersEntityService {

}
