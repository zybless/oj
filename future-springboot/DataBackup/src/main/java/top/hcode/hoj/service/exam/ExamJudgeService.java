package top.hcode.hoj.service.exam;

import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.pojo.dto.ExamSubmitDTO;
import top.hcode.hoj.pojo.vo.ExamHistoryVO;

/**
 * @Author: Minipax
 * @Date: 2023/9/20 19:32
 */
public interface ExamJudgeService {
    CommonResult<ExamHistoryVO> submitExercise(ExamSubmitDTO examSubmitDTO);

    CommonResult<Long> startExercise(Long repoId, Long tid);

    CommonResult<Long> startExam(Long examId, String password);

    CommonResult<ExamHistoryVO> submitExam(ExamSubmitDTO examSubmitDTO);
}
