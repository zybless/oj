package top.hcode.hoj.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.hcode.hoj.common.exception.StatusFailException;
import top.hcode.hoj.dao.exam.ExamProblemEntityService;
import top.hcode.hoj.dao.exam.ExamRepoEntityService;
import top.hcode.hoj.pojo.entity.exam.ExamRepoProblem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/15 10:18
 */

@Component
public class ExamRepoValidator {

    @Autowired
    private ExamProblemEntityService examProblemEntityService;

    @Autowired
    private ExamRepoEntityService examRepoEntityService;

    public void validateAddExamProblemsRoExamRepo(List<Long> problemIds, List<Float> grades) throws StatusFailException {
        List<Long> validProblemIds = new ArrayList<>();

        if(problemIds.size() != grades.size()) {
            throw new StatusFailException("题目数量与分数数量不一致！");
        }

        for(int i = 0; i < problemIds.size(); ++i) {
            if(examProblemEntityService.getById(problemIds.get(i)) == null) {
                throw new StatusFailException("题目Id + " + problemIds.get(i) + "无效！");
            }

            if(grades.get(i) <= 0) {
                throw new StatusFailException("题目Id + " + problemIds.get(i) + "的分数无效！");
            }
        }

    }

    public void validateExamRepoProblemList(List<ExamRepoProblem> examRepoProblemList) throws StatusFailException {
        if(examRepoProblemList == null || examRepoProblemList.size() == 0) {
            throw new StatusFailException("内容为空！");
        }
        for (ExamRepoProblem examRepoProblem : examRepoProblemList) {
            if (examRepoProblem == null) {
                throw new StatusFailException("有题目为空！");
            }
            if (examRepoProblem.getProblemId() == null) {
                throw new StatusFailException("题目ID为空！");
            }

            if(examRepoProblem.getGrade() == null || examRepoProblem.getGrade() <= 0) {
                throw new StatusFailException("题目分数错误！");
            }

        }
    }

    public void validateRepoId(Long repoId) throws StatusFailException {
        if(examRepoEntityService.getById(repoId) == null) {
            throw new StatusFailException("题库无效！");
        }
    }
}
