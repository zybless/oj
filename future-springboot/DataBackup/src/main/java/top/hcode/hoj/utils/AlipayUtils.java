package top.hcode.hoj.utils;

import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Component;
import top.hcode.hoj.config.AlipayConfig;
import top.hcode.hoj.pojo.entity.payment.Alipay;
import top.hcode.hoj.pojo.entity.payment.Orders;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @Author: Minipax
 * @Date: 2023/10/7 16:27
 */
@Component
@Slf4j(topic = "payment")
public class AlipayUtils {

    @Resource
    private AlipayConfig alipayConfig;

    //返回数据格式
    private static final String FORMAT = "json";
    //编码类型
    private static final String CHART_TYPE = "utf-8";
    //签名类型
    private static final String SIGN_TYPE = "RSA2";

    /*支付销售产品码,目前支付宝只支持FAST_INSTANT_TRADE_PAY*/
    public static final String PRODUCT_CODE = "FAST_INSTANT_TRADE_PAY";

    private static AlipayClient alipayClient = null;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private static final Random random = new Random();

    @PostConstruct
    public void init(){
        alipayClient = new DefaultAlipayClient(
                alipayConfig.getGateway(),
                alipayConfig.getAppId(),
                alipayConfig.getAppPrivateKey(),
                FORMAT,
                CHART_TYPE,
                alipayConfig.getAlipayPublicKey(),
                SIGN_TYPE);
    };

    /*================PC网页支付====================*/
    /**
     * 统一下单并调用支付页面接口
     * @param alipay
     * @return
     */
    public Map<String, Object> placeOrderAndPayForPCWeb(Alipay alipay){
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(alipayConfig.getNotifyUrl());
        request.setReturnUrl(alipayConfig.getReturnUrl());
        request.setBizContent(JSON.toJSONString(alipay));
        AlipayTradePagePayResponse response = null;
        try {
            response = alipayClient.pageExecute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("isSuccess", response.isSuccess());
        if(response.isSuccess()){
            log.info("调用成功");
            log.info(JSON.toJSONString(response));
            resultMap.put("body", response.getBody());
        } else {
            log.error("调用失败");
            log.error(response.getSubMsg());
            resultMap.put("subMsg", response.getSubMsg());
        }
        return resultMap;
    }

    /**
     * 交易订单查询
     * @param out_trade_no
     * @return
     */
    public Map<String, Object> tradeQueryForPCWeb(String out_trade_no){
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("trade_no", out_trade_no);
        request.setBizContent(bizContent.toString());
        AlipayTradeQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("isSuccess", response.isSuccess());
        if(response.isSuccess()){
            System.out.println("调用成功");
            System.out.println(JSON.toJSONString(response));
            resultMap.put("status", response.getTradeStatus());
        } else {
            System.out.println("调用失败");
            System.out.println(response.getSubMsg());
            resultMap.put("subMsg", response.getSubMsg());
        }
        return resultMap;
    }

    /**
     * 验证签名是否正确
     * @param sign
     * @param content
     * @return
     */
    public boolean CheckSignIn(String sign, String content){
        try {
            return AlipaySignature.rsaCheck(content, sign, alipayConfig.getAlipayPublicKey(), CHART_TYPE, SIGN_TYPE);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 将异步通知的参数转化为Map
     * @return
     */
//    public Map<String, String> paramstoMap(HttpServletRequest request) throws UnsupportedEncodingException {
//        Map<String, String> params = new HashMap<String, String>();
//        Map<String, String[]> requestParams = request.getParameterMap();
//        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
//            String name = (String) iter.next();
//            String[] values = (String[]) requestParams.get(name);
//            String valueStr = "";
//            for (int i = 0; i < values.length; i++) {
//                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
//            }
//            // 乱码解决，这段代码在出现乱码时使用。
////            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
//            params.put(name, valueStr);
//        }
//        return params;
//    }

    public boolean checkData(Map<String, String> params, Orders orders) {
        log.info("【请求开始-交易回调-订单确认】*********校验订单确认开始*********");

        //验证订单号是否准确，并且订单状态为待支付
        if(params.get("out_trade_no").equals(orders.getId()) && orders.getState().equals(0)){
            BigDecimal amount1 = new BigDecimal(params.get("total_amount"));
            BigDecimal amount2 = orders.getTotalPrice();
            //判断金额是否相等
            if(amount1.equals(amount2)){
                //验证收款商户id是否一致
                if(params.get("seller_id").equals(alipayConfig.getPid())){
                    //判断appid是否一致
                    if(params.get("app_id").equals(alipayConfig.getAppId())){
                        log.info("【成功：请求开始-交易回调-订单确认】*********校验订单确认成功*********");
                        return true;                    }
                }
            }
        }
        log.info("【失败：请求开始-交易回调-订单确认】*********校验订单确认失败*********");
        return false;
    }


    public String generateTradeNumber() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String strDate = formatter.format(date);
        String strRandom = RandomStringUtils.randomNumeric(8);
        return strDate + strRandom;
    }

}
