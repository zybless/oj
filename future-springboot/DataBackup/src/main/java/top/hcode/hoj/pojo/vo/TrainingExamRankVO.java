package top.hcode.hoj.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import top.hcode.hoj.pojo.entity.exam.ExamHistory;

import java.util.HashMap;
import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2024/8/15 11:09
 * @Description:
 */
@Data
@Accessors(chain = true)
public class TrainingExamRankVO {

    @ApiModelProperty(value = "用户id")
    private String uid;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "用户真实姓名")
    private String realname;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "学校")
    private String school;

    @ApiModelProperty(value = "性别")
    private String gender;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "ac题目数")
    private Integer ac;

    @ApiModelProperty(value = "有提交的试卷的提交详情")
    private HashMap<Long, ExamHistory> examSubmissionInfo;

}
