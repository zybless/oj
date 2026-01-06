package top.hcode.hoj.service.exam.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hcode.hoj.common.exception.StatusFailException;
import top.hcode.hoj.common.exception.StatusForbiddenException;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.common.result.ResultStatus;
import top.hcode.hoj.manager.exam.ExamHistoryManager;
import top.hcode.hoj.pojo.entity.exam.ExamHistory;
import top.hcode.hoj.pojo.vo.ExamHistoryVO;
import top.hcode.hoj.service.exam.ExamHistoryService;

import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/20 19:21
 */
@Service
public class ExamHistoryServiceImpl implements ExamHistoryService {

    @Autowired
    private ExamHistoryManager examHistoryManager;

    @Override
    public CommonResult<List<ExamHistory>> getExerciseHistory(Long repoId) {
        return CommonResult.successResponse(examHistoryManager.getExerciseHistory(repoId));
    }

    @Override
    public CommonResult<ExamHistoryVO> getExerciseHistoryDetail(Long historyId, Long repoId) {
        try {
            return CommonResult.successResponse(examHistoryManager.getExerciseHistoryDetail(historyId, repoId));
        } catch (StatusForbiddenException e) {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        }

    }
}
