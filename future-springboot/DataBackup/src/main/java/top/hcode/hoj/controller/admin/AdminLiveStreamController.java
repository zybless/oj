package top.hcode.hoj.controller.admin;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.service.admin.livestream.AdminLiveStreamService;

/**
 * @Author: Minipax
 * @Date: 2023/12/1 14:50
 */
@RestController
@RequestMapping("/api/admin/livestream")
public class AdminLiveStreamController {

    @Autowired
    private AdminLiveStreamService adminLiveStreamService;

    @GetMapping("")
    public CommonResult<String> getLiveStream() {
        return adminLiveStreamService.getLiveStream();
    }

    @PutMapping("")
    @RequiresAuthentication
    @RequiresRoles(value = {"root", "admin", "teacher"}, logical = Logical.OR)
    public CommonResult<Void> updateLiveStream(@RequestParam String url) {
        return adminLiveStreamService.updateLiveStream(url);
    }

}
