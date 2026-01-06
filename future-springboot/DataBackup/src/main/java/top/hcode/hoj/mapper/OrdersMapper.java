package top.hcode.hoj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.hcode.hoj.pojo.entity.payment.Orders;

/**
 * @Author: Minipax
 * @Date: 2023/10/8 16:10
 */
@Mapper
@Repository
public interface OrdersMapper extends BaseMapper<Orders> {



}
