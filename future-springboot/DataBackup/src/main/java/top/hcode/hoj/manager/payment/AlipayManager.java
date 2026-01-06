package top.hcode.hoj.manager.payment;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.hcode.hoj.common.exception.StatusFailException;
import top.hcode.hoj.config.AlipayConfig;
import top.hcode.hoj.dao.payment.OrdersEntityService;
import top.hcode.hoj.dao.payment.VipInfoEntityService;
import top.hcode.hoj.dao.user.UserInfoEntityService;
import top.hcode.hoj.dao.user.UserRoleEntityService;
import top.hcode.hoj.pojo.entity.payment.Alipay;
import top.hcode.hoj.pojo.entity.payment.Orders;
import top.hcode.hoj.pojo.entity.payment.VipInfo;
import top.hcode.hoj.pojo.entity.user.UserInfo;
import top.hcode.hoj.pojo.entity.user.UserRole;
import top.hcode.hoj.shiro.AccountProfile;
import top.hcode.hoj.utils.AlipayUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Minipax
 * @Date: 2023/10/9 20:10
 */
@Component
@Slf4j(topic = "payment")
public class AlipayManager {

    @Autowired
    private AlipayUtils alipayUtils;

    @Autowired
    private AlipayConfig alipayConfig;

    @Autowired
    private OrdersEntityService ordersEntityService;

    @Autowired
    private UserInfoEntityService userInfoEntityService;

    @Autowired
    private VipInfoEntityService vipInfoEntityService;

    @Autowired
    private UserRoleEntityService userRoleEntityService;

    @Transactional(rollbackFor = Exception.class)
    public String buyVip(Integer id) throws StatusFailException {
        log.info("【请求开始-在线购买-交易创建】*********统一下单开始*********");

        VipInfo vipInfo = vipInfoEntityService.getById(id);

        if (vipInfo == null) {
            throw new StatusFailException("此商品不存在！");
        }

        Alipay alipay = new Alipay();

        String tradeNo = alipayUtils.generateTradeNumber();
        alipay.setOut_trade_no(tradeNo);
        alipay.setSubject(vipInfo.getName());
        alipay.setTotal_amount(String.valueOf(vipInfo.getPrice()));


        Map<String, Object> map = alipayUtils.placeOrderAndPayForPCWeb(alipay);

        if (Boolean.parseBoolean(String.valueOf(map.get("isSuccess")))) {
            AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();

            log.info("【请求开始-在线购买-交易创建】统一下单成功，开始保存订单数据");

            //保存订单信息
            // 添加你自己的业务逻辑，主要是保存订单数据

            Orders orders = new Orders();
            orders.setId(tradeNo);
            orders.setUserId(userRolesVo.getUid());
            orders.setName(vipInfo.getName());
            orders.setState(0);
            orders.setPrice(vipInfo.getPrice());
            orders.setQuantity(1);
            orders.setTotalPrice(vipInfo.getPrice());
            orders.setGoodsId(id);

            ordersEntityService.save(orders);

            log.info("【请求成功-在线购买-交易创建】*********统一下单结束*********");
            return (String)map.get("body");
        }else{
            log.info("【失败：请求失败-在线购买-交易创建】*********统一下单结束*********");
            return (String)map.get("subMsg");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public String orderCallbackInAsync(HttpServletRequest request) throws StatusFailException {
        try {
            if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
                System.out.println("=========支付宝异步回调========");

                Map<String, String> params = new HashMap<>();
                Map<String, String[]> requestParams = request.getParameterMap();
                for (String name : requestParams.keySet()) {
                    params.put(name, request.getParameter(name));
                    // System.out.println(name + " = " + request.getParameter(name));
                }

                String outTradeNo = params.get("out_trade_no");
                String gmtPayment = params.get("gmt_payment");
                String alipayTradeNo = params.get("trade_no");

                String sign = params.get("sign");
                String content = AlipaySignature.getSignCheckContentV1(params);
                boolean checkSignature = AlipaySignature.rsa256CheckContent(content, sign, alipayConfig.getAlipayPublicKey(), "UTF-8"); // 验证签名
                // 支付宝验签
                if (checkSignature) {
                    // 验签通过
                    System.out.println("交易名称: " + params.get("subject"));
                    System.out.println("交易状态: " + params.get("trade_status"));
                    System.out.println("支付宝交易凭证号: " + params.get("trade_no"));
                    System.out.println("商户订单号: " + params.get("out_trade_no"));
                    System.out.println("交易金额: " + params.get("total_amount"));
                    System.out.println("买家在支付宝唯一id: " + params.get("buyer_id"));
                    System.out.println("买家付款时间: " + params.get("gmt_payment"));
                    System.out.println("买家付款金额: " + params.get("buyer_pay_amount"));

                    // 查询订单
                    QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", outTradeNo);
                    Orders orders = ordersEntityService.getOne(queryWrapper);

                    if (orders != null && alipayUtils.checkData(params, orders)) {
                        orders.setState(1);
                        orders.setAlipayId(params.get("trade_no"));
                    } else {
                        throw new StatusFailException("付款异常");
                    }
                    VipInfo vipInfo = vipInfoEntityService.getById(orders.getGoodsId());

                    ordersEntityService.updateById(orders);

                    QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
                    UserInfo user = userInfoEntityService.getById(orders.getUserId());
                    if(user != null) {
                        if(user.getIsVip()) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(user.getVipExpiredTime());
                            calendar.add(Calendar.DAY_OF_YEAR, vipInfo.getLastTime());
                            user.setVipExpiredTime(calendar.getTime());
                        } else {
                            user.setIsVip(true);
                            userRoleEntityService.save(new UserRole().setRoleId(1010L).setUid(user.getUuid()));
                            Calendar calendar = Calendar.getInstance();
                            calendar.add(Calendar.DAY_OF_YEAR, vipInfo.getLastTime());
                            user.setVipExpiredTime(calendar.getTime());
                        }
                    }
                    userInfoEntityService.updateById(user);
                }
            }
            return "支付完成";
        } catch (AlipayApiException e) {
            log.error("AlipayApiException: ", e);
            throw new StatusFailException(e.getMessage());
        }
    }

}
