package top.hcode.hoj.service.exam;

import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.pojo.entity.exam.ExamHistory;
import top.hcode.hoj.pojo.vo.ExamHistoryVO;

import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/20 19:21
 */
public interface ExamHistoryService {

    CommonResult<List<ExamHistory>> getExerciseHistory(Long repoId);

    CommonResult<ExamHistoryVO> getExerciseHistoryDetail(Long historyId, Long repoId);
}
