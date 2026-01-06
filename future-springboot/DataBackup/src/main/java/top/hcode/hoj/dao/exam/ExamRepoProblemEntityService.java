package top.hcode.hoj.dao.exam;

import com.baomidou.mybatisplus.extension.service.IService;
import top.hcode.hoj.pojo.entity.exam.ExamProblem;
import top.hcode.hoj.pojo.entity.exam.ExamRepo;
import top.hcode.hoj.pojo.entity.exam.ExamRepoProblem;

import java.util.HashMap;
import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/9 23:51
 */
public interface ExamRepoProblemEntityService extends IService<ExamRepoProblem> {

    public List<ExamRepo> getProblemRepos(Long problemId);

    public List<ExamProblem> getRepoProblems(Long repoId);

    Long getIdByRepoProblemId(Long repoId, Long problemId);

    List<Long> getIdByRepoId(Long repoId);

    public List<ExamRepoProblem> getByRepoId(Long repoId);

    public HashMap<Long, Float> getProblemGradeMap(Long repoId);
}
