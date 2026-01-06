package top.hcode.hoj.pojo.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.hcode.hoj.pojo.entity.exam.ExamProblem;
import top.hcode.hoj.pojo.entity.exam.ExamRepo;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/9 22:50
 */

@ApiModel(value = "考试题库查询对象ExamRepoVO", description = "")
@Data
public class ExamRepoVO {

    @ApiModelProperty(value = "题目id")
    private Long id;

    @ApiModelProperty(value = "题库名字")
    private String title;

    @ApiModelProperty(value = "题目总数")
    private Integer problemCount;

    @ApiModelProperty(value = "题库描述")
    private String remark;

    @ApiModelProperty(value = "作者")
    private String author;

    @ApiModelProperty(value = "题库限时")
    private Integer timeLimit;

    @ApiModelProperty(value = "总分")
    private Float totalScore;

    @ApiModelProperty(value = "默认为1公开，2为私有，3为比赛中")
    private Integer auth;

    @ApiModelProperty(value = "修改题目的管理员用户名")
    private String modifiedUser;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    @ApiModelProperty(value = "题库题目")
    private List<ExamProblem> examProblemList;

    @ApiModelProperty(value = "用户得分")
    private Float userScore;

    public ExamRepoVO() {}

    public ExamRepoVO(ExamRepo examRepo) {
        if(examRepo == null) {
            return;
        }
        this.id = examRepo.getId();
        this.title = examRepo.getTitle();
        this.problemCount = examRepo.getProblemCount();
        this.remark = examRepo.getRemark();
        this.timeLimit = examRepo.getTimeLimit();
        this.totalScore = examRepo.getTotalScore();
        this.auth = examRepo.getAuth();
        this.modifiedUser = examRepo.getModifiedUser();
        this.gmtCreate = examRepo.getGmtCreate();
        this.gmtModified = examRepo.getGmtModified();
    }
}
