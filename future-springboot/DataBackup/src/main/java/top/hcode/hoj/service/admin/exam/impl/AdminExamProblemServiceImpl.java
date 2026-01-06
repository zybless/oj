package top.hcode.hoj.service.admin.exam.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hcode.hoj.common.exception.StatusFailException;
import top.hcode.hoj.common.exception.StatusForbiddenException;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.common.result.ResultStatus;
import top.hcode.hoj.manager.admin.exam.AdminExamProblemManager;
import top.hcode.hoj.pojo.dto.ExamProblemDTO;
import top.hcode.hoj.pojo.entity.exam.ExamProblem;
import top.hcode.hoj.pojo.vo.ExamProblemVO;
import top.hcode.hoj.pojo.vo.ExamRepoProblemVO;
import top.hcode.hoj.service.admin.exam.AdminExamProblemService;

@Service
public class AdminExamProblemServiceImpl implements AdminExamProblemService {

    @Autowired
    private AdminExamProblemManager adminExamProblemManager;

    @Override
    public CommonResult<IPage<ExamProblemVO>> getProblemList(Integer limit, Integer currentPage, Integer type, Integer difficulty, String keyword, Integer auth, Long repoId) {
        IPage<ExamProblemVO> problemList = adminExamProblemManager.getProblemList(limit, currentPage, type, difficulty, keyword, auth, repoId);
        return CommonResult.successResponse(problemList);
    }

    @Override
    public CommonResult<ExamProblemVO> getProblem(Long id) {
        try {
            ExamProblemVO examProblemVO = adminExamProblemManager.getProblem(id);
            return CommonResult.successResponse(examProblemVO);
        } catch (StatusForbiddenException e) {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        }
    }

    @Override
    public CommonResult<Void> deleteProblem(Long id) {
        try {
            adminExamProblemManager.deleteProblem(id);
            return CommonResult.successResponse();
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusForbiddenException e) {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<Void> addProblem(ExamProblemDTO examProblemDTO) {
        try {
            adminExamProblemManager.addProblem(examProblemDTO);
            return CommonResult.successResponse();
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusForbiddenException e) {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<Void> updateProblem(ExamProblemDTO examProblemDTO) {
        try {
            adminExamProblemManager.updateProblem(examProblemDTO);
            return CommonResult.successResponse();
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusForbiddenException e) {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<IPage<ExamProblemVO>> getProblemListNotInRepo(Integer limit, Integer currentPage, Integer type, Integer difficulty, String keyword, Integer auth, Long repoId) {
        IPage<ExamProblemVO> problemList = adminExamProblemManager.getProblemListNotInRepo(limit, currentPage, type, difficulty, keyword, auth, repoId);
        return CommonResult.successResponse(problemList);
    }

    @Override
    public CommonResult<IPage<ExamRepoProblemVO>> getProblemListInRepo(Integer limit, Integer currentPage, Integer type, Integer difficulty, String keyword, Integer auth, Long repoId) {
        IPage<ExamRepoProblemVO> problemList = adminExamProblemManager.getProblemListInRepo(limit, currentPage, type, difficulty, keyword, auth, repoId);
        return CommonResult.successResponse(problemList);
    }
}
