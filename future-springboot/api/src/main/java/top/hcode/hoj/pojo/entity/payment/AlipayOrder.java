package top.hcode.hoj.pojo.entity.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: Minipax
 * @Date: 2023/10/8 16:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlipayOrder {

    private String charset;

    private String outTradeNo;

    private String method;

    private BigDecimal totalAmount;

    private String sign;

    private String tradeNo;

    private String authAppId;

    private String version;

    private String appId;

    private String signType;

    private String sellerId;

    private Date timestamp;

}
