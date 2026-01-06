package top.hcode.hoj.service.admin.exam.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hcode.hoj.common.exception.StatusFailException;
import top.hcode.hoj.common.exception.StatusForbiddenException;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.common.result.ResultStatus;
import top.hcode.hoj.manager.admin.exam.AdminExamRepoManager;
import top.hcode.hoj.pojo.entity.exam.ExamRepo;
import top.hcode.hoj.pojo.entity.exam.ExamRepoProblem;
import top.hcode.hoj.pojo.vo.ExamRepoVO;
import top.hcode.hoj.service.admin.exam.AdminExamRepoService;

import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/9 23:31
 */
@Service
public class AdminExamRepoServiceImpl implements AdminExamRepoService {

    @Autowired
    private AdminExamRepoManager adminExamRepoManager;

    @Override
    public CommonResult<IPage<ExamRepo>> getRepoList(Integer limit, Integer currentPage, String keyword) {
        IPage<ExamRepo> examRepoList = adminExamRepoManager.getRepoList(limit, currentPage, keyword);
        return CommonResult.successResponse(examRepoList);
    }

    @Override
    public CommonResult<ExamRepoVO> getRepo(Long id) {
        try {
            ExamRepoVO examRepoVO = adminExamRepoManager.getRepo(id);
            return CommonResult.successResponse(examRepoVO);
        } catch (StatusForbiddenException e) {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        }
    }

    @Override
    public CommonResult<Void> deleteRepo(Long id) {
        try {
            adminExamRepoManager.deleteRepo(id);
            return CommonResult.successResponse();
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusForbiddenException e) {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<Void> addRepo(ExamRepo examRepo) {
        try {
            adminExamRepoManager.addRepo(examRepo);
            return CommonResult.successResponse();
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusForbiddenException e) {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<Void> updateRepo(ExamRepo examRepo) {
        try {
            adminExamRepoManager.updateRepo(examRepo);
            return CommonResult.successResponse();
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusForbiddenException e) {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<Void> addProblemsToRepo(List<Long> problemIds, Long repoId, List<Float> grades) {
        try {
            adminExamRepoManager.addProblemsToRepo(problemIds, repoId, grades);
            return CommonResult.successResponse();
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusForbiddenException e) {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<Void> removeProblemsFromRepo(List<Long> problemIds, Long repoId) {
        try {
            adminExamRepoManager.removeProblemsFromRepo(problemIds, repoId);
            return CommonResult.successResponse();
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusForbiddenException e) {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<Void> updateRepoProblems(List<ExamRepoProblem> examRepoProblemList, Long repoId) {
        try {
            adminExamRepoManager.updateRepoProblems(examRepoProblemList, repoId);
            return CommonResult.successResponse();
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusForbiddenException e) {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }
}
