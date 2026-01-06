package top.hcode.hoj.service.exam.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Service;
import top.hcode.hoj.common.exception.StatusAccessDeniedException;
import top.hcode.hoj.common.exception.StatusFailException;
import top.hcode.hoj.common.exception.StatusForbiddenException;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.common.result.ResultStatus;
import top.hcode.hoj.manager.exam.ExamProblemManager;
import top.hcode.hoj.pojo.vo.ExamProblemVO;
import top.hcode.hoj.pojo.vo.ExamRepoProblemVO;
import top.hcode.hoj.service.exam.ExamProblemService;

import javax.annotation.Resource;

/**
 * @Author: Minipax
 * @Date: 2023/9/16 19:14
 * @Description:
 */
@Service
public class ExamProblemServiceImpl implements ExamProblemService {

    @Resource
    private ExamProblemManager examProblemManager;

    @Override
    public CommonResult<IPage<ExamProblemVO>> getProblemList(Integer limit, Integer currentPage, Integer type, Integer difficulty, String keyword, Integer auth, Long repoId) {
        return CommonResult.successResponse(examProblemManager.getProblemList(limit, currentPage, type, difficulty, keyword, auth, repoId));
    }

    @Override
    public CommonResult<IPage<ExamRepoProblemVO>> getProblemListInRepo(Integer limit, Integer currentPage, Integer type, Integer difficulty, String keyword, Integer auth, Long repoId) {
        return CommonResult.successResponse(examProblemManager.getProblemListInRepo(limit, currentPage, type, difficulty, keyword, auth, repoId));
    }

    @Override
    public CommonResult<IPage<ExamRepoProblemVO>> getProblemListInRepoExam(Integer limit, Integer currentPage, Integer type, Integer difficulty, String keyword, Integer auth, Long repoId, Long tid, Long eid) {
        try {
            return CommonResult.successResponse(examProblemManager.getProblemListInRepoExam(limit, currentPage, type, difficulty, keyword, auth, repoId, tid, eid));
        } catch (StatusAccessDeniedException e) {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.ACCESS_DENIED);
        } catch (StatusForbiddenException e) {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }

    }

    @Override
    public CommonResult<IPage<ExamRepoProblemVO>> getProblemListInRepoExamNoAnswer(Integer limit, Integer currentPage, Integer type, Integer difficulty, String keyword, Integer auth, Long repoId) {
        return CommonResult.successResponse(examProblemManager.getProblemListInRepoExamNoAnswer(limit, currentPage, type, difficulty, keyword, auth, repoId));

    }
}
