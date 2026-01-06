package top.hcode.hoj.pojo.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.hcode.hoj.pojo.entity.exam.Exam;
import top.hcode.hoj.pojo.entity.exam.ExamProblem;
import top.hcode.hoj.pojo.entity.exam.ExamRepo;

import java.util.Date;
import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2024/3/25 14:11
 */
@ApiModel(value = "考试查询对象ExamVO", description = "")
@Data
public class ExamVO {

    @ApiModelProperty(value = "考试id")
    private Long id;

    @ApiModelProperty(value = "考试名字")
    private String title;

    @ApiModelProperty(value = "考试密码")
    private String password;

    @ApiModelProperty(value = "考试简介")
    private String remark;

    @ApiModelProperty(value = "题库id")
    private Long examRepoId;

    @ApiModelProperty(value = "编程题比赛Id")
    private Long contestId;

    @ApiModelProperty(value = "作者")
    private String author;

    @ApiModelProperty(value = "权重")
    private double[] weight;

    @ApiModelProperty(value = "考试开始时间")
    private Date startTime;

    @ApiModelProperty(value = "考试结束时间")
    private Date endTime;

    @ApiModelProperty(value = "修改题目的管理员用户名")
    private String modifiedUser;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    @ApiModelProperty(value = "考试历史详情")
    private List<ExamHistoryVO> examHistoryVOList;

    @ApiModelProperty(value = "考试repo")
    private ExamRepo examRepo;

    public ExamVO() {

    }

    public ExamVO(Exam exam) {
        this.title = exam.getTitle();
        this.remark = exam.getRemark();
        this.password = exam.getPassword();
        this.examRepoId = exam.getExamRepoId();
        this.startTime = exam.getStartTime();
        this.endTime = exam.getEndTime();
        this.modifiedUser = exam.getModifiedUser();
        this.gmtCreate = exam.getGmtCreate();
        this.gmtModified = exam.getGmtModified();
    }
}
