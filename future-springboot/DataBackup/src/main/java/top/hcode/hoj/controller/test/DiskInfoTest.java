package top.hcode.hoj.controller.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.manager.email.EmailManager;
import top.hcode.hoj.pojo.entity.monitor.DiskInfo;

/**
 * @Author: Minipax
 * @Date: 2025/8/18 16:00
 * @Description:
 */
@RestController
@RequestMapping("/api/test")
public class DiskInfoTest {

    @Autowired
    private EmailManager emailManager;

    @GetMapping("/diskinfo")
    public CommonResult<Void> sendDiskInfo() {
        DiskInfo diskInfo = new DiskInfo();
        diskInfo.setFreeDisk(100D);
        diskInfo.setTotalDisk(200D);
        diskInfo.setUsagePercentage(0.5);
        emailManager.sendDiskInfo(diskInfo, "127.0.0.1", "test");
        return CommonResult.successResponse();
    }

}
