package top.hcode.hoj.manager.admin.exam;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import top.hcode.hoj.common.exception.StatusFailException;
import top.hcode.hoj.common.exception.StatusForbiddenException;
import top.hcode.hoj.dao.exam.ExamRepoEntityService;
import top.hcode.hoj.dao.exam.ExamRepoProblemEntityService;
import top.hcode.hoj.pojo.entity.exam.ExamProblem;
import top.hcode.hoj.pojo.entity.exam.ExamRepo;
import top.hcode.hoj.pojo.entity.exam.ExamRepoProblem;
import top.hcode.hoj.pojo.vo.ExamRepoVO;
import top.hcode.hoj.shiro.AccountProfile;
import top.hcode.hoj.utils.Constants;
import top.hcode.hoj.validator.ExamRepoValidator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/13 14:07
 */

@Component
@RefreshScope
@Slf4j(topic = "hoj")
public class AdminExamRepoManager {

    @Autowired
    private ExamRepoEntityService examRepoEntityService;

    @Autowired
    private ExamRepoProblemEntityService examRepoProblemEntityService;

    @Autowired
    private ExamRepoValidator examRepoValidator;

    public IPage<ExamRepo> getRepoList(Integer limit, Integer currentPage, String keyword) {
        if (currentPage == null || currentPage < 1) currentPage = 1;
        if (limit == null || limit < 1) limit = 10;

        IPage<ExamRepo> iPage = new Page<>(currentPage, limit);
        IPage<ExamRepo> repoList;

        QueryWrapper<ExamRepo> queryWrapper = new QueryWrapper<>();

        if (!StringUtils.isEmpty(keyword)) {
            final String key = keyword.trim();
            queryWrapper.and(wrapper -> wrapper.like("title", key));
            repoList = examRepoEntityService.page(iPage, queryWrapper);
        } else {
            repoList = examRepoEntityService.page(iPage, queryWrapper);
        }
        return repoList;
    }

    public ExamRepoVO getRepo(Long id) throws StatusForbiddenException, StatusFailException {
        ExamRepo examRepo = examRepoEntityService.getById(id);

        if (examRepo != null) { // 查询成功
//            boolean isRoot = SecurityUtils.getSubject().hasRole("root");
//            boolean isAdmin = SecurityUtils.getSubject().hasRole("admin");
//            // 只有超级管理员和题目管理员、题目创建者才能操作
//            if (!isRoot && !isAdmin) {
//                throw new StatusForbiddenException("对不起，你无权限增加题库！");
//            }

            ExamRepoVO examRepoVO = new ExamRepoVO(examRepo);
            List<ExamProblem> examProblemList = examRepoProblemEntityService.getRepoProblems(id);
            examRepoVO.setExamProblemList(examProblemList);

            return examRepoVO;
        } else {
            throw new StatusFailException("查询失败！");
        }
    }

    public void deleteRepo(Long id) throws StatusFailException, StatusForbiddenException {
        ExamRepo examRepo = examRepoEntityService.getById(id);
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();

        boolean isRoot = SecurityUtils.getSubject().hasRole("root");
        boolean isTeacher = SecurityUtils.getSubject().hasRole("teacher");

        if (!isRoot && !isTeacher && !userRolesVo.getUsername().equals(examRepo.getAuthor())) {
            throw new StatusForbiddenException("对不起，你无权限修改题库！");
        }
        boolean isOk = examRepoEntityService.removeById(id);

        if (isOk) { // 删除成功
            log.info("[{}],[{}],pid:[{}],operatorUid:[{}],operatorUsername:[{}]",
                    "Admin_Exam_Repo", "Delete", id, userRolesVo.getUid(), userRolesVo.getUsername());
        } else {
            throw new StatusFailException("删除失败！");
        }
    }


    public void addRepo(ExamRepo examRepo) throws StatusForbiddenException, StatusFailException {
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();

        boolean isRoot = SecurityUtils.getSubject().hasRole("root");
        boolean isAdmin = SecurityUtils.getSubject().hasRole("admin");
        // 只有超级管理员和题目管理员、题目创建者才能操作
//        if (!isRoot && !isAdmin) {
//            throw new StatusForbiddenException("对不起，你无权限增加题库！");
//        }

        examRepo.setModifiedUser(userRolesVo.getUsername());
        examRepo.setAuthor(userRolesVo.getUsername());
        examRepo.setProblemCount(0);
        examRepo.setTotalScore(0F);
        boolean isOk = examRepoEntityService.saveOrUpdate(examRepo);
        if (!isOk) {
            throw new StatusFailException("添加失败");
        }
    }

    public void updateRepo(ExamRepo examRepo) throws StatusForbiddenException, StatusFailException {
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();

        boolean isRoot = SecurityUtils.getSubject().hasRole("root");
        boolean isTeacher = SecurityUtils.getSubject().hasRole("teacher");

        ExamRepo examRepoDataBase = examRepoEntityService.getById(examRepo.getId());

        if (!isRoot && !isTeacher && !userRolesVo.getUsername().equals(examRepoDataBase.getAuthor())) {
            throw new StatusForbiddenException("对不起，你无权限修改题库！");
        }

        ExamRepo dbExamRepo = examRepoEntityService.getById(examRepo.getId());
        if(dbExamRepo == null) {
            throw new StatusFailException("修改失败，ID不存在");
        }
        dbExamRepo.setTitle(examRepo.getTitle());
        dbExamRepo.setAuth(examRepo.getAuth());
        dbExamRepo.setRemark(examRepo.getRemark());
        dbExamRepo.setTimeLimit(examRepo.getTimeLimit());

        dbExamRepo.setModifiedUser(userRolesVo.getUsername());
        boolean isOk = examRepoEntityService.saveOrUpdate(dbExamRepo);
        if (!isOk) {
            throw new StatusFailException("修改失败");
        }
    }

    public void addProblemsToRepo(List<Long> problemIds, Long repoId, List<Float> grades) throws StatusForbiddenException, StatusFailException {
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();

        boolean isRoot = SecurityUtils.getSubject().hasRole("root");
        boolean isAdmin = SecurityUtils.getSubject().hasRole("admin");
        // 只有超级管理员和题目管理员、题目创建者才能操作
//        if (!isRoot && !isAdmin) {
//            throw new StatusForbiddenException("对不起，你无权限增加题库！");
//        }

        examRepoValidator.validateAddExamProblemsRoExamRepo(problemIds, grades);

        List<ExamRepoProblem> examRepoProblemList = new ArrayList<>();
        for(int i = 0; i < problemIds.size(); ++i) {
            examRepoProblemList.add(new ExamRepoProblem(repoId, problemIds.get(i), grades.get(i)));
        }

        boolean saveResult = examRepoProblemEntityService.saveBatch(examRepoProblemList);

        boolean updateResult = examRepoEntityService.addProblemsToRepoBatch(repoId, problemIds.size());

        if (!saveResult || !updateResult) {
            throw new StatusFailException("增加失败");
        }
    }

    public void removeProblemsFromRepo(List<Long> problemIds, Long repoId) throws StatusForbiddenException, StatusFailException {
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();

        boolean isRoot = SecurityUtils.getSubject().hasRole("root");
        boolean isAdmin = SecurityUtils.getSubject().hasRole("admin");
        // 只有超级管理员和题目管理员、题目创建者才能操作
//        if (!isRoot && !isAdmin) {
//            throw new StatusForbiddenException("对不起，你无权限增加题库！");
//        }

        List<Long> examRepoProblemIdList = new ArrayList<>();
        for (Long problemId : problemIds) {
            examRepoProblemIdList.add(examRepoProblemEntityService.getIdByRepoProblemId(repoId, problemId));
        }

        boolean removeResult = examRepoProblemEntityService.removeByIds(examRepoProblemIdList);

        boolean updateResult = examRepoEntityService.removeProblemsFromRepoBatch(repoId, problemIds.size());

        if (!removeResult || !updateResult) {
            throw new StatusFailException("删除失败");
        }

    }

    public void updateRepoProblems(List<ExamRepoProblem> examRepoProblemList, Long repoId) throws StatusForbiddenException, StatusFailException {
        examRepoValidator.validateExamRepoProblemList(examRepoProblemList);
        examRepoValidator.validateRepoId(repoId);
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();

        boolean isRoot = SecurityUtils.getSubject().hasRole("root");
        boolean isAdmin = SecurityUtils.getSubject().hasRole("admin");
        // 只有超级管理员和题目管理员、题目创建者才能操作
//        if (!isRoot && !isAdmin) {
//            throw new StatusForbiddenException("对不起，你无权限增加题库！");
//        }

        List<Long> examRepoProblemIdList = examRepoProblemEntityService.getIdByRepoId(repoId);


        for (ExamRepoProblem examRepoProblem : examRepoProblemList) {
            examRepoProblem.setRepoId(repoId);
        }

        boolean updateResult = examRepoProblemEntityService.saveOrUpdateBatch(examRepoProblemList);
        HashSet<Long> updateIDList = new HashSet<>();
        Float totalScore = 0F;
        for (ExamRepoProblem examRepoProblem : examRepoProblemList) {
            updateIDList.add(examRepoProblem.getId());
            totalScore += examRepoProblem.getGrade();
        }

        List<Long> examRepoProblemRemoveIdList = new ArrayList<>();

        for (Long ID : examRepoProblemIdList) {
            if(!updateIDList.contains(ID)) {
                examRepoProblemRemoveIdList.add(ID);
            }
        }

        boolean removeResult = true;
        if(examRepoProblemRemoveIdList.size() > 0) {
            removeResult = examRepoProblemEntityService.removeByIds(examRepoProblemRemoveIdList);
        }

        Integer problemCount = examRepoProblemList.size() - examRepoProblemIdList.size();
        boolean repoProblemCountUpdateResult = examRepoEntityService.addProblemsToRepoBatch(repoId, problemCount);

        ExamRepo examRepo = examRepoEntityService.getById(repoId);
        examRepo.setTotalScore(totalScore);
        boolean examRepoUpdateResult = examRepoEntityService.saveOrUpdate(examRepo);

        if (!removeResult || !updateResult || !repoProblemCountUpdateResult || !examRepoUpdateResult) {
            throw new StatusFailException("更新失败");
        }
    }
}
