package top.hcode.hoj.service.exam.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hcode.hoj.common.exception.StatusAccessDeniedException;
import top.hcode.hoj.common.exception.StatusFailException;
import top.hcode.hoj.common.exception.StatusForbiddenException;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.common.result.ResultStatus;
import top.hcode.hoj.manager.exam.ExamJudgeManager;
import top.hcode.hoj.pojo.dto.ExamSubmitDTO;
import top.hcode.hoj.pojo.vo.ExamHistoryVO;
import top.hcode.hoj.service.exam.ExamJudgeService;

/**
 * @Author: Minipax
 * @Date: 2023/9/20 19:33
 */
@Service
public class ExamJudgeServiceImpl implements ExamJudgeService {

    @Autowired
    private ExamJudgeManager examJudgeManager;

    @Override
    public CommonResult<ExamHistoryVO> submitExercise(ExamSubmitDTO examSubmitDTO) {
        try {
            return CommonResult.successResponse(examJudgeManager.submitExercise(examSubmitDTO));
        } catch (StatusForbiddenException e) {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        }
    }

    @Override
    public CommonResult<Long> startExercise(Long repoId, Long tid) {
        try {
            return CommonResult.successResponse(examJudgeManager.startExercise(repoId, tid));
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusForbiddenException e) {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusAccessDeniedException e) {
            return CommonResult.errorResponse(e.getMessage());
        }
    }

    @Override
    public CommonResult<Long> startExam(Long examId, String password) {
        try {
            return CommonResult.successResponse(examJudgeManager.startExam(examId, password));
        } catch (StatusForbiddenException e) {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        }
    }

    @Override
    public CommonResult<ExamHistoryVO> submitExam(ExamSubmitDTO examSubmitDTO) {
        try {
            return CommonResult.successResponse(examJudgeManager.submitExam(examSubmitDTO));
        } catch (StatusForbiddenException e) {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        }
    }
}
