package top.hcode.hoj.dao.exam.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hcode.hoj.dao.exam.ExamRepoEntityService;
import top.hcode.hoj.mapper.ExamRepoMapper;
import top.hcode.hoj.mapper.ExamRepoProblemMapper;
import top.hcode.hoj.pojo.entity.exam.ExamRepo;
import top.hcode.hoj.pojo.entity.exam.ExamRepoProblem;
import top.hcode.hoj.pojo.vo.ExamProblemVO;
import top.hcode.hoj.pojo.vo.ExamRepoVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Minipax
 * @Date: 2023/9/10 0:02
 */
@Service
public class ExamRepoEntityServiceImpl extends ServiceImpl<ExamRepoMapper, ExamRepo> implements ExamRepoEntityService {

    @Autowired
    private ExamRepoMapper examRepoMapper;


    @Override
    public boolean addProblemToReposBatch(List<Long> repoIds) {
        return examRepoMapper.addProblemToRepoBatch(repoIds);
    }

    @Override
    public boolean removeProblemFromReposBatch(List<Long> repoIds) {
        return examRepoMapper.removeProblemFromRepoBatch(repoIds);
    }

    @Override
    public boolean addProblemsToRepoBatch(Long repoId, Integer size) {
        return examRepoMapper.addProblemsToRepoBatch(repoId, size);
    }

    @Override
    public boolean removeProblemsFromRepoBatch(Long repoId, int size) {
        return examRepoMapper.removeProblemsFromRepoBatch(repoId, size);
    }

    @Override
    public IPage<ExamRepo> getRepoList(Integer limit, Integer currentPage, String keyword, Integer auth) {
        Page<ExamRepo> page = new Page<>(currentPage, limit);

        List<ExamRepo> repoList = examRepoMapper.getRepoList(page, keyword, auth);

        return page.setRecords(repoList);
    }
}
