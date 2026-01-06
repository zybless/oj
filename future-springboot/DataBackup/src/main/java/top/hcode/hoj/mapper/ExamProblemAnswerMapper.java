package top.hcode.hoj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.hcode.hoj.pojo.entity.exam.ExamProblemAnswer;
import top.hcode.hoj.pojo.vo.ExamProblemAnswerVO;

import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/9 22:36
 */

@Mapper
@Repository
public interface ExamProblemAnswerMapper extends BaseMapper<ExamProblemAnswer> {

    public List<ExamProblemAnswer> getProblemAnswers(Long problemId);
}
