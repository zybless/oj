package top.hcode.hoj.pojo.entity.payment;

import lombok.Data;

/**
 * @Author: Minipax
 * @Date: 2023/10/8 0:52
 */
@Data
public class Alipay {

    //商户订单号
    private String out_trade_no;
    //订单名称
    private String subject;
    //金额
    private String total_amount;
    //商品描述
    private String body;
    //超时时间参数
    private String timeout_express = "50m";
    //产品编号
    private String product_code = "FAST_INSTANT_TRADE_PAY";

}
