package top.hcode.hoj.service.admin.livestream;

import org.springframework.stereotype.Service;
import top.hcode.hoj.common.result.CommonResult;

/**
 * @Author: Minipax
 * @Date: 2023/12/1 14:55
 */
public interface AdminLiveStreamService {

    CommonResult<String> getLiveStream();

    CommonResult<Void> updateLiveStream(String url);
}
