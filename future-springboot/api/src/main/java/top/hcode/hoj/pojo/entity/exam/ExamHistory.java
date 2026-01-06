package top.hcode.hoj.pojo.entity.exam;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Minipax
 * @Date: 2023/9/20 18:46
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ExamHistory对象", description="")
public class ExamHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long repoId;

    private Long examId;

    private String userId;

    private Float userScore;

    private Float totalScore;

    private Integer type;

    @TableField(fill = FieldFill.INSERT)
    private Date startTime;

    @TableField(fill = FieldFill.UPDATE)
    private Date submitTime;

}
