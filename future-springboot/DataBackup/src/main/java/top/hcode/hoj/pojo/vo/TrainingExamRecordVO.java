package top.hcode.hoj.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: Minipax
 * @Date: 2024/8/15 10:58
 * @Description:
 */
@Data
@ApiModel(value="用户在训练考试的记录", description="")
public class TrainingExamRecordVO {

    private Long id;

    @ApiModelProperty(value = "训练id")
    private Long tid;

    @ApiModelProperty(value = "训练考试id")
    private Long teid;

    @ApiModelProperty(value = "用户id")
    private String uid;

    @ApiModelProperty(value = "考试id")
    private Long eid;

    @ApiModelProperty(value = "提交id")
    private Long submitId;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "学校")
    private String school;

    @ApiModelProperty(value = "性别")
    private String gender;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "真实姓名")
    private String realname;

    @ApiModelProperty(value = "昵称")
    private String nickname;

//    @ApiModelProperty(value = "提交结果状态码")
//    private Integer status;
//
//    @ApiModelProperty(value = "OI得分")
//    private Integer score;
//
//    @ApiModelProperty(value = "提交耗时")
//    private Integer useTime;

}
