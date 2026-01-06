package top.hcode.hoj.pojo.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: Minipax
 * @Date: 2024/6/23 17:36
 */

@ApiModel(value = "TrainingCampVO", description = "")
@Data
public class TrainingCampVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long trainingCampTeamId;

    private String title;

    private String duration;

    private String intensity;

    private String image;

    private String content;

    private String faq;

    private BigDecimal price;

    private String description;

    private Integer limitNumber;

    private Date startTime;

    private Date endTime;

}
