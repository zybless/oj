package top.hcode.hoj.controller.payment;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.hcode.hoj.annotation.AnonApi;
import top.hcode.hoj.config.AlipayConfig;
import top.hcode.hoj.pojo.entity.payment.AlipayOrder;
import top.hcode.hoj.service.payment.AlipayService;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Minipax
 * @Date: 2023/10/5 15:47
 * @Description:
 */
@RestController
@RequestMapping("/api/alipay")
public class AlipayController {

    @Autowired
    private AlipayService alipayService;

    @Autowired
    private AlipayConfig alipayConfig;

    @GetMapping("/buy-vip")
    @RequiresAuthentication
    public String buyVIP(@RequestParam(value = "id") Integer id) {
        return alipayService.buyVip(id);
    }

    @PostMapping("/notify-url")
    @AnonApi
    public String orderCallbackInAsync(HttpServletRequest request) {

        return alipayService.orderCallbackInAsync(request);
    }

    @GetMapping(value = "/return")
    @AnonApi
    public String returnPage(@RequestParam(value = "charset", required = false) String charset,
                             @RequestParam(value = "out_trade_no", required = false) String outTradeNo,
                             @RequestParam(value = "method", required = false) String method,
                             @RequestParam(value = "total_amount", required = false) BigDecimal totalAmount,
                             @RequestParam(value = "sign", required = false) String sign,
                             @RequestParam(value = "trade_no", required = false) String tradeNo,
                             @RequestParam(value = "auth_app_id", required = false) String authAppId,
                             @RequestParam(value = "version", required = false) String version,
                             @RequestParam(value = "app_id", required = false) String appId,
                             @RequestParam(value = "sign_type", required = false) String signType,
                             @RequestParam(value = "seller_id", required = false) String sellerId,
                             @RequestParam(value = "timestamp", required = false) String timestamp) throws ParseException {
        AlipayOrder alipayOrder = new AlipayOrder(charset, outTradeNo, method, totalAmount, sign, tradeNo, authAppId, version, appId, signType, sellerId, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timestamp));
        //模拟支付界面
        return "success";
    }



}
