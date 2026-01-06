package top.hcode.hoj.service.payment.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.hcode.hoj.common.exception.StatusFailException;
import top.hcode.hoj.common.exception.StatusForbiddenException;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.common.result.ResultStatus;
import top.hcode.hoj.config.AlipayConfig;
import top.hcode.hoj.dao.payment.OrdersEntityService;
import top.hcode.hoj.dao.payment.VipInfoEntityService;
import top.hcode.hoj.dao.user.UserInfoEntityService;
import top.hcode.hoj.manager.payment.AlipayManager;
import top.hcode.hoj.pojo.entity.payment.Alipay;
import top.hcode.hoj.pojo.entity.payment.Orders;
import top.hcode.hoj.pojo.entity.payment.VipInfo;
import top.hcode.hoj.pojo.entity.user.UserInfo;
import top.hcode.hoj.service.payment.AlipayService;
import top.hcode.hoj.shiro.AccountProfile;
import top.hcode.hoj.utils.AlipayUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Minipax
 * @Date: 2023/10/7 16:03
 */
@Service
@Slf4j(topic = "payment")
public class AlipayServiceImpl implements AlipayService {

    @Autowired
    private AlipayManager alipayManager;

    @Override
    public String buyVip(Integer id) {
        try {
            return alipayManager.buyVip(id);
        } catch (StatusFailException e) {
            return e.getMessage();
        }
    }

//    public void orderCallbackInSync() {
//        try {
//            OutputStream outputStream = response.getOutputStream();
//            //通过设置响应头控制浏览器以UTF-8的编码显示数据，如果不加这句话，那么浏览器显示的将是乱码
//            response.setHeader("content-type", "text/html;charset=UTF-8");
//            String outputData = "支付成功，请返回网站并刷新页面。";
//
//            /**
//             * data.getBytes()是一个将字符转换成字节数组的过程，这个过程中一定会去查码表，
//             * 如果是中文的操作系统环境，默认就是查找查GB2312的码表，
//             */
//            byte[] dataByteArr = outputData.getBytes("UTF-8");//将字符转换成字节数组，指定以UTF-8编码进行转换
//            outputStream.write(dataByteArr);//使用OutputStream流向客户端输出字节数组
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @Override
    public String orderCallbackInAsync(HttpServletRequest request) {
        try {
            return alipayManager.orderCallbackInAsync(request);
        } catch (StatusFailException e) {
            return e.getMessage();
        }
    }


}
