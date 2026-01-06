package top.hcode.hoj.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: Minipax
 * @Date: 2023/9/9 22:40
 */

@ApiModel(value = "考试题目答案列表查询对象ExamProblemAnswerVO", description = "")
@Data
public class ExamProblemAnswerVO {

    @ApiModelProperty(value = "答案id")
    private Long id;

    @ApiModelProperty(value = "题目id")
    private String problemId;

    @ApiModelProperty(value = "选项内容")
    private String content;

    @ApiModelProperty(value = "是否是正确选项")
    private Boolean isCorrect;

    @ApiModelProperty(value = "选项分析")
    private String analysis;

}
