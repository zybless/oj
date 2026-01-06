package top.hcode.hoj.dao.exam;

import com.baomidou.mybatisplus.extension.service.IService;
import top.hcode.hoj.pojo.entity.exam.ExamProblemAnswer;
import top.hcode.hoj.pojo.vo.ExamProblemAnswerVO;

import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/9 23:45
 */
public interface ExamProblemAnswerEntityService extends IService<ExamProblemAnswer> {
    public List<ExamProblemAnswer> getProblemAnswers(Long problemId);
}
