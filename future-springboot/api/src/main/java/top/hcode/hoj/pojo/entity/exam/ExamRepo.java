package top.hcode.hoj.pojo.entity.exam;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

/**
 * @Author: Minipax
 * @Date: 2023/9/9 22:19
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ExamRepo对象", description="")
public class ExamRepo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String title;

    private Integer problemCount;

    private String remark;

    private String author;

    private Integer timeLimit;

    private Float totalScore;

    private String modifiedUser;

    @ApiModelProperty(value = "0公开，（待确定）")
    private Integer auth;

    private Date gmtCreate;

    private Date gmtModified;

}
