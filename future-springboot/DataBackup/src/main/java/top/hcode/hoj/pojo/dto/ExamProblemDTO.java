package top.hcode.hoj.pojo.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import top.hcode.hoj.pojo.entity.exam.ExamProblem;
import top.hcode.hoj.pojo.entity.exam.ExamProblemAnswer;

import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/5 22:39
 */

@Data
@Accessors(chain = true)
public class ExamProblemDTO {

    private ExamProblem examProblem;

    private List<ExamProblemAnswer> examProblemAnswers;

    private List<Long> repoIds;
}
