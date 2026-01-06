package top.hcode.hoj.service.admin.training.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hcode.hoj.common.exception.StatusFailException;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.manager.admin.training.AdminTrainingProblemManager;
import top.hcode.hoj.pojo.dto.TrainingExamDTO;
import top.hcode.hoj.pojo.dto.TrainingProblemDTO;
import top.hcode.hoj.pojo.entity.exam.Exam;
import top.hcode.hoj.pojo.entity.exam.ExamRepo;
import top.hcode.hoj.pojo.entity.training.TrainingExam;
import top.hcode.hoj.pojo.entity.training.TrainingProblem;
import top.hcode.hoj.pojo.vo.ExamVO;
import top.hcode.hoj.service.admin.training.AdminTrainingProblemService;

import java.util.HashMap;

/**
 * @Author: Himit_ZH
 * @Date: 2022/3/9 20:53
 * @Description:
 */
@Service
public class AdminTrainingProblemServiceImpl implements AdminTrainingProblemService {

    @Autowired
    private AdminTrainingProblemManager adminTrainingProblemManager;

    @Override
    public CommonResult<HashMap<String, Object>> getProblemList(Integer limit, Integer currentPage, String keyword, Boolean queryExisted, Long tid) {
        HashMap<String, Object> problemMap = adminTrainingProblemManager.getProblemList(limit, currentPage, keyword, queryExisted, tid);
        return CommonResult.successResponse(problemMap);
    }

    @Override
    public CommonResult<Void> updateProblem(TrainingProblem trainingProblem) {
        try {
            adminTrainingProblemManager.updateProblem(trainingProblem);
            return CommonResult.successResponse();
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        }
    }

    @Override
    public CommonResult<Void> deleteProblem(Long pid, Long tid) {
        try {
            adminTrainingProblemManager.deleteProblem(pid, tid);
            return CommonResult.successResponse();
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        }
    }

    @Override
    public CommonResult<Void> addProblemFromPublic(TrainingProblemDTO trainingProblemDto) {
        try {
            adminTrainingProblemManager.addProblemFromPublic(trainingProblemDto);
            return CommonResult.successResponse();
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        }
    }

    @Override
    public CommonResult<Void> importTrainingRemoteOJProblem(String name, String problemId, Long tid) {
        try {
            adminTrainingProblemManager.importTrainingRemoteOJProblem(name, problemId, tid);
            return CommonResult.successResponse();
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        }
    }

    @Override
    public CommonResult<IPage<ExamRepo>> getExamList(Integer limit, Integer currentPage, String keyword, Long tid) {
        IPage<ExamRepo> examList = adminTrainingProblemManager.getExamList(limit, currentPage, keyword, tid);
        return CommonResult.successResponse(examList);
    }

    @Override
    public CommonResult<Void> updateExam(TrainingExam trainingExam) {
        try {
            adminTrainingProblemManager.updateExam(trainingExam);
            return CommonResult.successResponse();
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        }
    }

    @Override
    public CommonResult<Void> deleteExam(Long eid, Long tid) {
        try {
            adminTrainingProblemManager.deleteExam(eid, tid);
            return CommonResult.successResponse();
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        }
    }

    @Override
    public CommonResult<Void> addExamFromPublic(TrainingExamDTO trainingExamDTO) {
        try {
            adminTrainingProblemManager.addExamFromPublic(trainingExamDTO);
            return CommonResult.successResponse();
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        }
    }
}