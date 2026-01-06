package top.hcode.hoj.service.exam;

import com.baomidou.mybatisplus.core.metadata.IPage;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.pojo.dto.ExamRankDTO;
import top.hcode.hoj.pojo.dto.ExamSubmitDTO;
import top.hcode.hoj.pojo.entity.exam.Exam;
import top.hcode.hoj.pojo.vo.ExamProblemVO;
import top.hcode.hoj.pojo.vo.ExamVO;

/**
 * @Author: Minipax
 * @Date: 2023/9/18 15:07
 * @Description:
 */
public interface ExamService {
    CommonResult<IPage<ExamVO>> getExamList(Integer limit, Integer currentPage, String title);

    CommonResult<IPage<ExamProblemVO>> submitExercise(ExamSubmitDTO examSubmitDTO);

    CommonResult<ExamVO> getExam(Long examId);
}
