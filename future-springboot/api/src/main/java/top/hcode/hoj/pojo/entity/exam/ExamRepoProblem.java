package top.hcode.hoj.pojo.entity.exam;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author: Minipax
 * @Date: 2023/9/9 23:23
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ExamRepoProblem对象", description="")
public class ExamRepoProblem implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long repoId;

    private Long problemId;

    private Float grade;

    public ExamRepoProblem () {}

    public ExamRepoProblem (Long repoId, Long problemId, Float grade) {
        this.repoId = repoId;
        this.problemId = problemId;
        this.grade = grade;
    }

}
