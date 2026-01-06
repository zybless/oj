package top.hcode.hoj.config;

import lombok.Data;
import top.hcode.hoj.utils.IpUtils;

/**
 * @Author Himit_ZH
 * @Date 2022/10/26
 */
@Data
public class WebConfig {

    // 邮箱配置
    private String emailUsername;

    private String emailPassword;

    private String emailHost;

    private Integer emailPort;

    private Boolean emailSsl = true;

    private String emailBGImg = "https://noip.wlbbai.com/api/public/img/logo.jpg";

    // 网站前端显示配置
    private String baseUrl = "http://" + IpUtils.getServiceIp();

    private String name = "蔚来新赛道用户注册验证码";

    private String shortName = "蔚来新赛道";

    private String description;

    private Boolean register = true;

    private String recordName;

    private String recordUrl;

    private String projectName = "蔚来新赛道";

    private String projectUrl = "https://noip.wlbbai.com/api";
}
