package top.hcode.hoj.service.exam.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hcode.hoj.common.exception.StatusFailException;
import top.hcode.hoj.common.exception.StatusForbiddenException;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.common.result.ResultStatus;
import top.hcode.hoj.manager.exam.ExamManager;
import top.hcode.hoj.pojo.dto.ExamRankDTO;
import top.hcode.hoj.pojo.dto.ExamSubmitDTO;
import top.hcode.hoj.pojo.vo.ExamProblemVO;
import top.hcode.hoj.pojo.vo.ExamVO;
import top.hcode.hoj.service.exam.ExamService;

/**
 * @Author: Minipax
 * @Date: 2023/9/18 15:07
 * @Description:
 */
@Service
public class ExamServiceImpl implements ExamService {

    @Autowired
    private ExamManager examManager;

    @Override
    public CommonResult<IPage<ExamVO>> getExamList(Integer limit, Integer currentPage, String title) {
        return CommonResult.successResponse(examManager.getExamList(limit, currentPage, title));
    }

    @Override
    public CommonResult<ExamVO> getExam(Long examId) {
        try {
            return CommonResult.successResponse(examManager.getExam(examId));
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        }
    }

    @Override
    public CommonResult<IPage<ExamProblemVO>> submitExercise(ExamSubmitDTO examSubmitDTO) {
        return null;
    }

}
