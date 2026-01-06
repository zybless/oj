package top.hcode.hoj.pojo.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import top.hcode.hoj.pojo.entity.exam.ExamHistory;
import top.hcode.hoj.pojo.entity.exam.ExamHistoryDetail;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/20 19:48
 */
@ApiModel(value = "考试历史查询对象ExamHistoryVO", description = "")
@Data
public class ExamHistoryVO implements Serializable {

    private ExamHistory examHistory;

    private List<ExamHistoryDetail> examHistoryDetailList;

    private List<ExamRepoProblemVO> examRepoProblemVOList;

}
