package top.hcode.hoj.dao.exam.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hcode.hoj.dao.exam.ExamProblemEntityService;
import top.hcode.hoj.dao.exam.ExamRepoEntityService;
import top.hcode.hoj.dao.exam.ExamRepoProblemEntityService;
import top.hcode.hoj.mapper.ExamProblemMapper;
import top.hcode.hoj.mapper.ExamRepoMapper;
import top.hcode.hoj.mapper.ExamRepoProblemMapper;
import top.hcode.hoj.pojo.entity.exam.ExamProblem;
import top.hcode.hoj.pojo.entity.exam.ExamRepo;
import top.hcode.hoj.pojo.entity.exam.ExamRepoProblem;
import top.hcode.hoj.pojo.vo.ExamProblemVO;
import top.hcode.hoj.pojo.vo.ExamRepoVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/9 23:51
 */
@Service
public class ExamRepoProblemEntityServiceImpl extends ServiceImpl<ExamRepoProblemMapper, ExamRepoProblem> implements ExamRepoProblemEntityService {

    @Autowired
    private ExamRepoProblemMapper examRepoProblemMapper;

    @Autowired
    private ExamRepoMapper examRepoMapper;

    @Autowired
    private ExamProblemMapper examProblemMapper;

    @Override
    public List<ExamRepo> getProblemRepos(Long problemId) {
        List<Long> repoIds = examRepoProblemMapper.getProblemRepoIds(problemId);
        List<ExamRepo> examRepoList = new ArrayList<>();
        for (Long repoId : repoIds) {
            examRepoList.add(examRepoMapper.selectById(repoId));
        }

        return examRepoList;
    }

    @Override
    public List<ExamProblem> getRepoProblems(Long repoId) {
        List<Long> problemIds = examRepoProblemMapper.getRepoProblemIds(repoId);
        List<ExamProblem> examProblemList = new ArrayList<>();
        for (Long problemId : problemIds) {
            examProblemList.add(examProblemMapper.selectById(problemId));
        }

        return examProblemList;
    }

    @Override
    public Long getIdByRepoProblemId(Long repoId, Long problemId) {
        return examRepoProblemMapper.getIdByRepoProblemId(repoId, problemId);
    }

    @Override
    public List<Long> getIdByRepoId(Long repoId) {
        return examRepoProblemMapper.getIdByRepoId(repoId);
    }

    public List<ExamRepoProblem> getByRepoId(Long repoId) {
        return examRepoProblemMapper.getByRepoId(repoId);
    }

    @Override
    public HashMap<Long, Float> getProblemGradeMap(Long repoId) {
        List<ExamRepoProblem> examRepoProblems = this.getByRepoId(repoId);
        HashMap<Long, Float> problemGradeMap = new HashMap<>();
        for (ExamRepoProblem examRepoProblem : examRepoProblems) {
            problemGradeMap.put(examRepoProblem.getProblemId(), examRepoProblem.getGrade());
        }

        return problemGradeMap;
    }
}
