package top.hcode.hoj.validator;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Component;
import top.hcode.hoj.common.exception.StatusFailException;
import top.hcode.hoj.dao.exam.ExamRepoEntityService;
import top.hcode.hoj.pojo.dto.ExamProblemDTO;
import top.hcode.hoj.pojo.entity.exam.ExamProblem;
import top.hcode.hoj.pojo.entity.exam.ExamProblemAnswer;
import top.hcode.hoj.pojo.entity.exam.ExamRepo;
import top.hcode.hoj.pojo.entity.problem.Problem;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/5 22:50
 */

@Component
public class ExamProblemValidator {

    @Resource
    private CommonValidator commonValidator;

    private ExamRepoEntityService examRepoEntityService;

    public void validateExamProblem(ExamProblem examProblem) throws StatusFailException {
        if (examProblem == null) {
            throw new StatusFailException("题目的配置项不能为空！");
        }

        if(examProblem.getContent() == null) {
            throw new StatusFailException("题目的内容不能为空！");
        }

        if(examProblem.getType() == null) {
            throw new StatusFailException("题目的类型不能为空！");
        }


    }

    public void validateExamProblemAnswer(List<ExamProblemAnswer> examProblemAnswers) throws StatusFailException {
        if (examProblemAnswers == null) {
            throw new StatusFailException("答案或者选项不能为空！");
        }

        for (ExamProblemAnswer examProblemAnswer : examProblemAnswers) {
            if (examProblemAnswer.getContent() == null) {
                throw new StatusFailException("答案或者选项的内容不能为空！");
            }

            if (examProblemAnswer.getIsCorrect() == null) {
                throw new StatusFailException("答案或者选项的是否正确不能为空！");
            }
        }
    }

    public void validateExamProblemRepo(List<Long> repoIds) throws StatusFailException {
        if(repoIds == null) {
            return;
        }

        for (Long repoId : repoIds) {
            QueryWrapper<ExamRepo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", repoId);
            ExamRepo examRepo = examRepoEntityService.getOne(queryWrapper);
            if (examRepo == null) {
                throw new StatusFailException("该题目的题库不存在，请更换！");
            }
        }
    }

    public void validateExamProblemDTO(ExamProblemDTO examProblemDTO) throws StatusFailException {

        validateExamProblem(examProblemDTO.getExamProblem());

        validateExamProblemAnswer(examProblemDTO.getExamProblemAnswers());

        validateExamProblemRepo(examProblemDTO.getRepoIds());
    }

}
