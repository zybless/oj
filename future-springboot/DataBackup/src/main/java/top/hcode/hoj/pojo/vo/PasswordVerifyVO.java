package top.hcode.hoj.pojo.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Himit_ZH
 * @Date: 2022/3/11 17:00
 * @Description: 密码验证结果视图对象
 */
@Data
public class PasswordVerifyVO implements Serializable {
    
    /**
     * 验证是否成功
     */
    private Boolean success;
    
    /**
     * 验证消息
     */
    private String message;
    
    public PasswordVerifyVO(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}