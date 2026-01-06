package top.hcode.hoj.pojo.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.hcode.hoj.pojo.entity.exam.ExamProblem;
import top.hcode.hoj.pojo.entity.exam.ExamProblemAnswer;
import top.hcode.hoj.pojo.entity.exam.ExamRepo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/5 15:51
 */

@ApiModel(value = "考试题目列表查询对象ExamProblemVO", description = "")
@Data
public class ExamProblemVO implements Serializable {

    @ApiModelProperty(value = "题目id")
    private Long id;

    @ApiModelProperty(value = "题目难度")
    private Integer difficulty;

    @ApiModelProperty(value = "题目类型")
    private Integer type;

    @ApiModelProperty(value = "题目内容")
    private String content;

    @ApiModelProperty(value = "解析")
    private String analysis;

    @ApiModelProperty(value = "默认为1公开，2为私有，3为比赛中")
    private Integer auth;

    @ApiModelProperty(value = "作者")
    private String author;

    @ApiModelProperty(value = "修改题目的管理员用户名")
    private String modifiedUser;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    @ApiModelProperty(value = "题目选项和答案")
    private List<ExamProblemAnswer> examProblemAnswerList;

    @ApiModelProperty(value = "题目从属的题库")
    private List<ExamRepo> examRepoList;

    public ExamProblemVO() {}

    public ExamProblemVO(ExamProblem examProblem) {
        if(examProblem == null) {
            return;
        }
        this.id = examProblem.getId();
        this.difficulty = examProblem.getDifficulty();
        this.type = examProblem.getType();
        this.content = examProblem.getContent();
        this.analysis = examProblem.getAnalysis();
        this.auth = examProblem.getAuth();
        this.modifiedUser = examProblem.getModifiedUser();
        this.gmtCreate = examProblem.getGmtCreate();
        this.gmtModified = examProblem.getGmtModified();
    }

    // 以下为题目做题情况

//    @ApiModelProperty(value = "该题总提交数")
//    private Integer total = 0;
//
//    @ApiModelProperty(value = "该题总正确提交数")
//    private Integer correct = 0;

}
