package top.hcode.hoj.service.admin.exam.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hcode.hoj.common.exception.StatusFailException;
import top.hcode.hoj.common.exception.StatusForbiddenException;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.common.result.ResultStatus;
import top.hcode.hoj.manager.admin.exam.AdminExamManager;
import top.hcode.hoj.pojo.dto.ExamRankDTO;
import top.hcode.hoj.pojo.entity.exam.Exam;
import top.hcode.hoj.pojo.vo.ExamHistoryVO;
import top.hcode.hoj.pojo.vo.ExamRankVO;
import top.hcode.hoj.pojo.vo.ExamRepoVO;
import top.hcode.hoj.pojo.vo.ExamVO;
import top.hcode.hoj.service.admin.exam.AdminExamService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2024/3/25 14:08
 */
@Service
public class AdminExamServiceImpl implements AdminExamService {

    @Autowired
    private AdminExamManager adminExamManager;

    @Override
    public CommonResult<IPage<ExamVO>> getExamList(Integer limit, Integer currentPage, String title, Date startTime, Date endTime) {
        IPage<ExamVO> examList = adminExamManager.getExamList(limit, currentPage, title);
        return CommonResult.successResponse(examList);
    }

    @Override
    public CommonResult<List<ExamHistoryVO>> getExamHistory(Long examId) {
        List<ExamHistoryVO> examHistoryList = adminExamManager.getExamHistory(examId);
        return CommonResult.successResponse(examHistoryList);
    }

    @Override
    public CommonResult<IPage<ExamRankVO>> getExamRank(ExamRankDTO examRankDTO) {
        try {
            return CommonResult.successResponse(adminExamManager.getExamRank(examRankDTO));
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        }
    }

    @Override
    public CommonResult<ExamVO> getExam(Long id) {
        try {
            ExamVO examVO = adminExamManager.getExam(id);
            return CommonResult.successResponse(examVO);
        } catch (StatusForbiddenException e) {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        }
    }

    @Override
    public CommonResult<Void> deleteExam(Long id) {
        try {
            adminExamManager.deleteExam(id);
            return CommonResult.successResponse();
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusForbiddenException e) {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<Void> addExam(Exam exam) {
        try {
            adminExamManager.addExam(exam);
            return CommonResult.successResponse();
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusForbiddenException e) {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<Void> updateExam(Exam exam) {
        try {
            adminExamManager.updateExam(exam);
            return CommonResult.successResponse();
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusForbiddenException e) {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<ExamHistoryVO> getExamHistoryDetail(Long historyId) {
        try {
            return CommonResult.successResponse(adminExamManager.getExamHistoryDetail(historyId));
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        }
    }

    @Override
    public CommonResult<Void> exportHistory(Long eid, HttpServletResponse response) {
        try {
            adminExamManager.exportHistory(eid, response);
            return CommonResult.successResponse();
        } catch (StatusFailException | IOException e) {
            return CommonResult.errorResponse(e.getMessage());
        }
    }
}
