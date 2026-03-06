package top.hcode.hoj.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Author: Himit_ZH
 * @Date: 2022/3/11 17:00
 * @Description: 密码验证数据传输对象
 */
@Data
public class PasswordVerifyDTO implements Serializable {
    
    @NotBlank(message = "密码不能为空")
    private String password;
}