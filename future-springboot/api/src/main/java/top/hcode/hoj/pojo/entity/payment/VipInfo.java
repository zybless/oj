package top.hcode.hoj.pojo.entity.payment;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: Minipax
 * @Date: 2023/10/9 20:00
 */
@Data
public class VipInfo {

    private Integer id;

    private String name;

    private BigDecimal price;

    private Integer lastTime;

    private String modifiedUser;

    private String description;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

}
