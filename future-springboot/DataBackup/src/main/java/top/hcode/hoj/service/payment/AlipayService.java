package top.hcode.hoj.service.payment;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: Minipax
 * @Date: 2023/10/7 16:03
 */
public interface AlipayService {

    public String buyVip(Integer id);

    public String orderCallbackInAsync(HttpServletRequest request);



}
