package top.hcode.hoj.pojo.entity.exam;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Minipax
 * @Date: 2024/3/25 13:59
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Exam对象", description="")
public class Exam implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String title;

    private String password;

    private String remark;

    private String author;

    private Long examRepoId;

    private Long contestId;

    private String weight;

    private Date startTime;

    private Date endTime;

    private String modifiedUser;

    private Date gmtCreate;

    private Date gmtModified;

}
