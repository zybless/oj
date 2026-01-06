package top.hcode.hoj.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: Minipax
 * @Date: 2024/7/15 13:35
 * @Description:
 */
@Data
public class TrainingExamDTO {

    @NotBlank(message = "试卷id不能为空")
    private Long eid;

    @NotBlank(message = "训练id不能为空")
    private Long tid;

//    @NotBlank(message = "题目在训练中的展示id不能为空")
    private String displayId;
}