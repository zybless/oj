package top.hcode.hoj.pojo.entity.payment;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: Minipax
 * @Date: 2023/10/8 16:06
 */
@Data
public class Orders implements Serializable {

    private String id;

    private String userId;

    private Integer goodsId;

    private String name;

    private BigDecimal price;

    private Integer quantity;

    private BigDecimal totalPrice;

    private Integer state;

    private Integer paymentMethod;

    private String alipayId;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

}
