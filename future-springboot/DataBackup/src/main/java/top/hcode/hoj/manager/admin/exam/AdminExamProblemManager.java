package top.hcode.hoj.manager.admin.exam;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import top.hcode.hoj.common.exception.StatusFailException;
import top.hcode.hoj.common.exception.StatusForbiddenException;
import top.hcode.hoj.dao.exam.ExamProblemAnswerEntityService;
import top.hcode.hoj.dao.exam.ExamProblemEntityService;
import top.hcode.hoj.dao.exam.ExamRepoEntityService;
import top.hcode.hoj.dao.exam.ExamRepoProblemEntityService;
import top.hcode.hoj.dao.judge.JudgeEntityService;
import top.hcode.hoj.pojo.dto.ExamProblemDTO;
import top.hcode.hoj.pojo.entity.exam.ExamProblem;
import top.hcode.hoj.pojo.entity.exam.ExamRepo;
import top.hcode.hoj.pojo.entity.judge.Judge;
import top.hcode.hoj.pojo.vo.ExamProblemVO;
import top.hcode.hoj.pojo.vo.ExamRepoProblemVO;
import top.hcode.hoj.shiro.AccountProfile;
import top.hcode.hoj.utils.Constants;
import top.hcode.hoj.validator.ExamProblemValidator;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/4 14:43
 * @Description:
 */

@Component
@RefreshScope
@Slf4j(topic = "hoj")
public class AdminExamProblemManager {

    @Autowired
    private ExamProblemEntityService examProblemEntityService;

    @Autowired
    private ExamProblemAnswerEntityService examProblemAnswerEntityService;

    @Autowired
    private ExamRepoProblemEntityService examRepoProblemEntityService;

    @Autowired
    private ExamRepoEntityService examRepoEntityService;

    @Resource
    private ExamProblemValidator examProblemValidator;

    public IPage<ExamProblemVO> getProblemList(Integer limit, Integer currentPage, Integer type, Integer difficulty, String keyword, Integer auth, Long repoId) {
        if (currentPage == null || currentPage < 1) currentPage = 1;
        if (limit == null || limit < 1) limit = 10;
//        IPage<ExamProblem> iPage = new Page<>(currentPage, limit);
//        IPage<ExamProblem> problemList;
//
//        QueryWrapper<ExamProblem> queryWrapper = new QueryWrapper<>();
////        queryWrapper.eq("is_group", false)
////                .orderByDesc("id");
//
////        if (auth != null && auth != 0) {
////            queryWrapper.eq("auth", auth);
////        }
//
//        if(repoId != null) {
//            queryWrapper.and(wrapper -> wrapper.eq("repo", key))
//        }
//
//        if (!StringUtils.isEmpty(keyword)) {
//            final String key = keyword.trim();
//            queryWrapper.and(wrapper -> wrapper.like("content", key));
//            problemList = examProblemEntityService.page(iPage, queryWrapper);
//        } else {
//            problemList = examProblemEntityService.page(iPage, queryWrapper);
//        }

        if (!StringUtils.isEmpty(keyword)) {
            keyword = keyword.trim();
        }
        return examProblemEntityService.getProblemListAdmin(limit, currentPage, type, difficulty, keyword, auth, repoId);
    }

    public ExamProblemVO getProblem(Long id) throws StatusForbiddenException, StatusFailException {
        ExamProblem examProblem = examProblemEntityService.getById(id);

        if (examProblem != null) { // 查询成功
            boolean isRoot = SecurityUtils.getSubject().hasRole("root");
            boolean isAdmin = SecurityUtils.getSubject().hasRole("admin");
            // 只有超级管理员和题目管理员、题目创建者才能操作
//            if (!isRoot && !isAdmin) {
//                throw new StatusForbiddenException("对不起，你无权限增加题库！");
//            }
            ExamProblemVO examProblemVO = new ExamProblemVO(examProblem);

            examProblemVO.setExamProblemAnswerList(examProblemAnswerEntityService.getProblemAnswers(id));
            examProblemVO.setExamRepoList(examRepoProblemEntityService.getProblemRepos(id));

            return examProblemVO;
        } else {
            throw new StatusFailException("查询失败！");
        }
    }

    public void deleteProblem(Long id) throws StatusFailException, StatusForbiddenException {
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();

        ExamProblem examProblem = examProblemEntityService.getById(id);
        boolean isRoot = SecurityUtils.getSubject().hasRole("root");
        boolean isTeacher = SecurityUtils.getSubject().hasRole("teacher");
        // 只有超级管理员和题目管理员、题目创建者才能操作
        if (!isRoot && !isTeacher && !userRolesVo.getUsername().equals(examProblem.getAuthor())) {
            throw new StatusForbiddenException("对不起，你无权删除题目！");
        }
        List<ExamRepo> problemRepos = examRepoProblemEntityService.getProblemRepos(id);
        List<Long> repoIds = new ArrayList<>();
        for (ExamRepo problemRepo : problemRepos) {
            repoIds.add(problemRepo.getId());
        }

        boolean isOk = examProblemEntityService.removeById(id);

        if(!repoIds.isEmpty()){
            examRepoEntityService.removeProblemFromReposBatch(repoIds);
        }

        /*
        problem的id为其他表的外键的表中的对应数据都会被一起删除！
         */
        if (isOk) { // 删除成功
            log.info("[{}],[{}],pid:[{}],operatorUid:[{}],operatorUsername:[{}]",
                    "Admin_Exam_Problem", "Delete", id, userRolesVo.getUid(), userRolesVo.getUsername());
        } else {
            throw new StatusFailException("删除失败！");
        }
    }

    public void addProblem(ExamProblemDTO examProblemDTO) throws StatusFailException, StatusForbiddenException {

        examProblemValidator.validateExamProblemDTO(examProblemDTO);

        // 获取当前登录的用户
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();

        boolean isRoot = SecurityUtils.getSubject().hasRole("root");
        boolean isAdmin = SecurityUtils.getSubject().hasRole("admin");
        // 只有超级管理员和题目管理员、题目创建者才能操作
//        if (!isRoot && !isAdmin) {
//            throw new StatusForbiddenException("对不起，你无权限增加题目！");
//        }

        // 记录修改题目的用户
        examProblemDTO.getExamProblem().setModifiedUser(userRolesVo.getUsername());
        examProblemDTO.getExamProblem().setAuthor(userRolesVo.getUsername());

        boolean isOk = examProblemEntityService.adminAddExamProblem(examProblemDTO);
        if (!isOk) {
            throw new StatusFailException("添加失败");
        }
    }

    public void updateProblem(ExamProblemDTO examProblemDTO) throws StatusForbiddenException, StatusFailException {

        examProblemValidator.validateExamProblemDTO(examProblemDTO);
        ExamProblem examProblem = examProblemEntityService.getById(examProblemDTO.getExamProblem().getId());

        // 获取当前登录的用户
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();

        boolean isRoot = SecurityUtils.getSubject().hasRole("root");
        boolean isTeacher = SecurityUtils.getSubject().hasRole("teacher");
        // 只有超级管理员和题目管理员、题目创建者才能操作
        if (!isRoot && !isTeacher && !userRolesVo.getUsername().equals(examProblem.getAuthor())) {
            throw new StatusForbiddenException("对不起，你无权限修改题目！");
        }

        // 记录修改题目的用户
        examProblemDTO.getExamProblem().setModifiedUser(userRolesVo.getUsername());

        boolean result = examProblemEntityService.adminUpdateExamProblem(examProblemDTO);
        if (!result) {
            throw new StatusFailException("修改失败");
        }
    }

    public IPage<ExamProblemVO> getProblemListNotInRepo(Integer limit, Integer currentPage, Integer type, Integer difficulty, String keyword, Integer auth, Long repoId) {
        if (currentPage == null || currentPage < 1) currentPage = 1;
        if (limit == null || limit < 1) limit = 10;

        if (!StringUtils.isEmpty(keyword)) {
            keyword = keyword.trim();
        }
        return examProblemEntityService.getProblemListNotInRepo(limit, currentPage, type, difficulty, keyword, auth, repoId);
    }

    public IPage<ExamRepoProblemVO> getProblemListInRepo(Integer limit, Integer currentPage, Integer type, Integer difficulty, String keyword, Integer auth, Long repoId) {
        if (currentPage == null || currentPage < 1) currentPage = 1;
        if (limit == null || limit < 1) limit = 10;

        if (!StringUtils.isEmpty(keyword)) {
            keyword = keyword.trim();
        }
        return examProblemEntityService.getProblemListInRepo(limit, currentPage, type, difficulty, keyword, auth, repoId);

    }
}
