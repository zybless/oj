package top.hcode.hoj.controller.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.service.admin.livestream.AdminLiveStreamService;

/**
 * @Author: Minipax
 * @Date: 2024/4/25 11:22
 */
@RestController
@RequestMapping("/api/livestream")
public class LiveStreamController {

    @Autowired
    private AdminLiveStreamService adminLiveStreamService;

    @GetMapping("")
    public CommonResult<String> getLiveStream() {
        return adminLiveStreamService.getLiveStream();
    }

}
