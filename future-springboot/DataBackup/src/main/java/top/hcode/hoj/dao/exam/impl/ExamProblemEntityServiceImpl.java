package top.hcode.hoj.dao.exam.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.hcode.hoj.dao.exam.ExamProblemAnswerEntityService;
import top.hcode.hoj.dao.exam.ExamProblemEntityService;
import top.hcode.hoj.dao.exam.ExamRepoEntityService;
import top.hcode.hoj.dao.exam.ExamRepoProblemEntityService;
import top.hcode.hoj.exception.ProblemIDRepeatException;
import top.hcode.hoj.mapper.ExamProblemMapper;
import top.hcode.hoj.pojo.dto.ExamProblemDTO;
import top.hcode.hoj.pojo.entity.exam.ExamProblem;
import top.hcode.hoj.pojo.entity.exam.ExamProblemAnswer;
import top.hcode.hoj.pojo.entity.exam.ExamRepoProblem;
import top.hcode.hoj.pojo.vo.ExamProblemVO;
import top.hcode.hoj.pojo.vo.ExamRepoProblemVO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/4 16:41
 * @Description:
 */

@Service
public class ExamProblemEntityServiceImpl extends ServiceImpl<ExamProblemMapper, ExamProblem> implements ExamProblemEntityService {

    @Autowired
    private ExamProblemMapper examProblemMapper;

    @Autowired
    private ExamProblemAnswerEntityService examProblemAnswerEntityService;

    @Autowired
    private ExamRepoProblemEntityService examRepoProblemEntityService;

    @Autowired
    private ExamRepoEntityService examRepoEntityService;

    @Override
    public Page<ExamProblemVO> getProblemList(Integer limit, Integer currentPage, Integer type, Integer difficulty, String keyword, Integer auth, Long repoId) {
        //新建分页
        Page<ExamProblemVO> page = new Page<>(currentPage, limit);

        List<ExamProblemVO> problemList = examProblemMapper.getProblemList(page, keyword, type, difficulty, auth, repoId);

        for (ExamProblemVO examProblemVO : problemList) {
            examProblemVO.setExamProblemAnswerList(examProblemAnswerEntityService.getProblemAnswers(examProblemVO.getId()));
            examProblemVO.setExamRepoList(examRepoProblemEntityService.getProblemRepos(examProblemVO.getId()));
        }

        return page.setRecords(problemList);
    }

    @Override
    public IPage<ExamProblemVO> getProblemListAdmin(Integer limit, Integer currentPage, Integer type, Integer difficulty, String keyword, Integer auth, Long repoId) {
        Page<ExamProblemVO> page = new Page<>(currentPage, limit);

        List<ExamProblemVO> problemList = examProblemMapper.getProblemListAdmin(page, keyword, type, difficulty, auth, repoId);

        for (ExamProblemVO examProblemVO : problemList) {
            examProblemVO.setExamProblemAnswerList(examProblemAnswerEntityService.getProblemAnswers(examProblemVO.getId()));
            examProblemVO.setExamRepoList(examRepoProblemEntityService.getProblemRepos(examProblemVO.getId()));
        }

        return page.setRecords(problemList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean adminAddExamProblem(ExamProblemDTO examProblemDTO) {
        ExamProblem examProblem = examProblemDTO.getExamProblem();
        List<ExamProblemAnswer> examProblemAnswers = examProblemDTO.getExamProblemAnswers();
        List<Long> repoIds = examProblemDTO.getRepoIds();

        // 添加题目
        examProblemMapper.insert(examProblem);

        Long pid = examProblem.getId();
        if (pid == null) {
            throw new ProblemIDRepeatException("Problem insert failed!");
        }

        // 添加题目答案
        for (ExamProblemAnswer examProblemAnswer : examProblemAnswers) {
            examProblemAnswer.setProblemId(pid);
        }
        boolean examProblemAnswerSaveResult = examProblemAnswerEntityService.saveOrUpdateBatch(examProblemAnswers);

        // 添加题库信息
        boolean examRepoProblemSaveResult = true;
        boolean examRepoUpdateResult = true;

        if(repoIds != null && repoIds.size() > 0) {
            List<ExamRepoProblem> examRepoProblems = new ArrayList<>();
            for (Long repoId : examProblemDTO.getRepoIds()) {
                ExamRepoProblem examRepoProblem = new ExamRepoProblem();
                examRepoProblem.setRepoId(repoId);
                examRepoProblem.setProblemId(pid);
                examRepoProblems.add(examRepoProblem);
            }

            examRepoProblemSaveResult = examRepoProblemEntityService.saveOrUpdateBatch(examRepoProblems);
            // 题库加1数量
            examRepoUpdateResult = examRepoEntityService.addProblemToReposBatch(examProblemDTO.getRepoIds());
        }

        if(examProblemAnswerSaveResult && examRepoProblemSaveResult && examRepoUpdateResult) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean adminUpdateExamProblem(ExamProblemDTO examProblemDTO) {
        ExamProblem examProblem = examProblemDTO.getExamProblem();
        List<ExamProblemAnswer> examProblemAnswers = examProblemDTO.getExamProblemAnswers();
        List<Long> repoIds = examProblemDTO.getRepoIds();

        Long pid = examProblem.getId();
        if (pid == null) {
            throw new ProblemIDRepeatException("Problem update failed!");
        }

        // 更新题目
        boolean examProblemUpdateResult = this.updateById(examProblem);

        // 更新答案
        for (ExamProblemAnswer examProblemAnswer : examProblemAnswers) {
            examProblemAnswer.setProblemId(pid);
        }
        boolean examProblemAnswerUpdateResult = examProblemAnswerEntityService.saveOrUpdateBatch(examProblemAnswers);
        HashSet<Long> examProblemAnswerIds = new HashSet<>();
        for (ExamProblemAnswer examProblemAnswer : examProblemAnswers) {
            examProblemAnswerIds.add(examProblemAnswer.getId());
        }
        // 删除
        List<ExamProblemAnswer> problemAnswers = examProblemAnswerEntityService.getProblemAnswers(examProblem.getId());

        List<Long> examProblemAnswerRemoveIdList = new ArrayList<>();
        for (ExamProblemAnswer problemAnswer : problemAnswers) {
            if(!examProblemAnswerIds.contains(problemAnswer.getId())) {
                examProblemAnswerRemoveIdList.add(problemAnswer.getId());
            }
        }

        boolean examProblemAnswerRemoveResult = true;
        if(examProblemAnswerRemoveIdList.size() != 0) {
            examProblemAnswerRemoveResult = examProblemAnswerEntityService.removeByIds(examProblemAnswerRemoveIdList);
        }

        // 更新题库
//        if(repoIds == null) {
////            repoIds = new ArrayList<>();
////        }
////
////        List<ExamRepo> problemRepos = examRepoProblemEntityService.getProblemRepos(examProblem.getId());
////        List<Long> examRepoProblemRemoveIdList = new ArrayList<>();
////        for (ExamRepo problemRepo : problemRepos) {
////            if(!repoIds.contains(problemRepo.getId())) {
////                examRepoProblemRemoveIdList.add(problemRepo.getId());
////            }
////        }
////
////        boolean examRepoProblemRemoveResult = true;
////        if(examRepoProblemRemoveIdList.size() != 0) {
////            examRepoProblemRemoveResult = examRepoProblemEntityService.removeByIds(examRepoProblemRemoveIdList);
////        }

        if(examProblemUpdateResult && examProblemAnswerUpdateResult && examProblemAnswerRemoveResult) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public Page<ExamProblemVO> getProblemListNotInRepo(Integer limit, Integer currentPage, Integer type, Integer difficulty, String keyword, Integer auth, Long repoId) {
        //新建分页
        Page<ExamProblemVO> page = new Page<>(currentPage, limit);

        List<ExamProblemVO> problemList = examProblemMapper.getProblemListNotInRepo(page, keyword, type, difficulty, auth, repoId);

        for (ExamProblemVO examProblemVO : problemList) {
            examProblemVO.setExamProblemAnswerList(examProblemAnswerEntityService.getProblemAnswers(examProblemVO.getId()));
            examProblemVO.setExamRepoList(examRepoProblemEntityService.getProblemRepos(examProblemVO.getId()));
        }

        return page.setRecords(problemList);
    }

    @Override
    public IPage<ExamRepoProblemVO> getProblemListInRepo(Integer limit, Integer currentPage, Integer type, Integer difficulty, String keyword, Integer auth, Long repoId) {
        Page<ExamRepoProblemVO> page = new Page<>(currentPage, limit);

        List<ExamRepoProblemVO> problemList = examProblemMapper.getProblemListInRepo(page, keyword, type, difficulty, auth, repoId);

        for (ExamRepoProblemVO examRepoProblemVO : problemList) {
            examRepoProblemVO.setExamProblemAnswerList(examProblemAnswerEntityService.getProblemAnswers(examRepoProblemVO.getId()));
        }

        return page.setRecords(problemList);
    }

    @Override
    public IPage<ExamRepoProblemVO> getProblemListInRepoExam(Integer limit, Integer currentPage, Integer type, Integer difficulty, String keyword, Integer auth, Long repoId) {
        Page<ExamRepoProblemVO> page = new Page<>(currentPage, limit);

        List<ExamRepoProblemVO> problemList = examProblemMapper.getProblemListInRepo(page, keyword, type, difficulty, auth, repoId);

        for (ExamRepoProblemVO examRepoProblemVO : problemList) {
            List<ExamProblemAnswer> problemAnswers = examProblemAnswerEntityService.getProblemAnswers(examRepoProblemVO.getId());
            for (ExamProblemAnswer problemAnswer : problemAnswers) {
                if(examRepoProblemVO.getType().equals(3)) {
                    problemAnswer.setContent(null);
                }
                problemAnswer.setIsCorrect(null);
                problemAnswer.setAnalysis(null);
            }
            examRepoProblemVO.setExamProblemAnswerList(problemAnswers);

        }

        return page.setRecords(problemList);
    }

    @Override
    public IPage<ExamRepoProblemVO> getProblemListInRepoExamNoAnswer(Integer limit, Integer currentPage, Integer type, Integer difficulty, String keyword, Integer auth, Long repoId) {
        Page<ExamRepoProblemVO> page = new Page<>(currentPage, limit);

        List<ExamRepoProblemVO> problemList = examProblemMapper.getProblemListInRepo(page, keyword, type, difficulty, auth, repoId);

        for (ExamRepoProblemVO examRepoProblemVO : problemList) {
            List<ExamProblemAnswer> problemAnswers = examProblemAnswerEntityService.getProblemAnswers(examRepoProblemVO.getId());
            for (ExamProblemAnswer problemAnswer : problemAnswers) {
                if(examRepoProblemVO.getType().equals(3)) {
                    problemAnswer.setContent(null);
                }
                problemAnswer.setIsCorrect(null);
            }
            examRepoProblemVO.setExamProblemAnswerList(problemAnswers);

        }

        return page.setRecords(problemList);
    }
}
