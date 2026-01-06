package top.hcode.hoj.dao.exam.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hcode.hoj.dao.exam.ExamProblemAnswerEntityService;
import top.hcode.hoj.mapper.ExamProblemAnswerMapper;
import top.hcode.hoj.pojo.entity.exam.ExamProblemAnswer;
import top.hcode.hoj.pojo.vo.ExamProblemAnswerVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/9 23:45
 */
@Service
public class ExamProblemAnswerEntityServiceImpl extends ServiceImpl<ExamProblemAnswerMapper, ExamProblemAnswer> implements ExamProblemAnswerEntityService {

    @Autowired
    private ExamProblemAnswerMapper examProblemAnswerMapper;

    @Override
    public List<ExamProblemAnswer> getProblemAnswers(Long problemId) {
        return examProblemAnswerMapper.getProblemAnswers(problemId);
    }
}
