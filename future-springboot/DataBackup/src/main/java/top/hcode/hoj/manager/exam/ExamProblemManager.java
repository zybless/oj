package top.hcode.hoj.manager.exam;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import top.hcode.hoj.common.exception.StatusAccessDeniedException;
import top.hcode.hoj.common.exception.StatusFailException;
import top.hcode.hoj.common.exception.StatusForbiddenException;
import top.hcode.hoj.dao.exam.ExamHistoryEntityService;
import top.hcode.hoj.dao.exam.ExamProblemEntityService;
import top.hcode.hoj.dao.exam.ExamRegisterEntityService;
import top.hcode.hoj.dao.training.TrainingEntityService;
import top.hcode.hoj.dao.training.TrainingExamEntityService;
import top.hcode.hoj.pojo.entity.exam.ExamHistory;
import top.hcode.hoj.pojo.entity.exam.ExamRegister;
import top.hcode.hoj.pojo.entity.training.Training;
import top.hcode.hoj.pojo.entity.training.TrainingExam;
import top.hcode.hoj.pojo.vo.ExamProblemVO;
import top.hcode.hoj.pojo.vo.ExamRepoProblemVO;
import top.hcode.hoj.shiro.AccountProfile;
import top.hcode.hoj.utils.Constants;
import top.hcode.hoj.validator.TrainingValidator;

import javax.annotation.Resource;

/**
 * @Author: Minipax
 * @Date: 2023/9/16 19:25
 * @Description:
 */
@Component
@RefreshScope
@Slf4j(topic = "hoj")
public class ExamProblemManager {

    @Autowired
    private ExamProblemEntityService examProblemEntityService;

    @Autowired
    private ExamHistoryEntityService examHistoryEntityService;

    @Autowired
    private TrainingExamEntityService trainingExamEntityService;

    @Autowired
    private TrainingValidator trainingValidator;

    @Autowired
    private TrainingEntityService trainingEntityService;

    @Autowired
    private ExamRegisterEntityService examRegisterEntityService;

    public IPage<ExamProblemVO> getProblemList(Integer limit, Integer currentPage, Integer type, Integer difficulty, String keyword, Integer auth, Long repoId) {
        if (currentPage == null || currentPage < 1) currentPage = 1;
        if (limit == null || limit < 1) limit = 10;

        // 关键词查询不为空
        if (!StringUtils.isEmpty(keyword)) {
            keyword = keyword.trim();
        }

        return examProblemEntityService.getProblemList(limit, currentPage, type, difficulty, keyword, auth, repoId);

    }

    public IPage<ExamRepoProblemVO> getProblemListInRepo(Integer limit, Integer currentPage, Integer type, Integer difficulty, String keyword, Integer auth, Long repoId) {
        if (currentPage == null || currentPage < 1) currentPage = 1;
        if (limit == null || limit < 1) limit = 10;

        if (!StringUtils.isEmpty(keyword)) {
            keyword = keyword.trim();
        }
        return examProblemEntityService.getProblemListInRepo(limit, currentPage, type, difficulty, keyword, auth, repoId);
    }

    public IPage<ExamRepoProblemVO> getProblemListInRepoExam(Integer limit, Integer currentPage, Integer type, Integer difficulty, String keyword, Integer auth, Long repoId, Long tid, Long eid) throws StatusForbiddenException, StatusAccessDeniedException {
        // 鉴权
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();
        boolean isRoot = SecurityUtils.getSubject().hasRole("root");
        boolean isAdmin = SecurityUtils.getSubject().hasRole("admin");
        boolean isTeacher = SecurityUtils.getSubject().hasRole("teacher");
        if (!isRoot || !isAdmin || !isTeacher) {
            if(tid != null) {
                checkTrainingAuth(repoId, tid);
            } else if(eid != null) {
                checkExamAuth(eid, userRolesVo.getUid());
            } else {
                throw new StatusAccessDeniedException("未指明考试类型");
            }
        }

        if (currentPage == null || currentPage < 1) currentPage = 1;
        if (limit == null || limit < 1) limit = 10;

        if (!StringUtils.isEmpty(keyword)) {
            keyword = keyword.trim();
        }
        return examProblemEntityService.getProblemListInRepoExam(limit, currentPage, type, difficulty, keyword, auth, repoId);
    }

    public IPage<ExamRepoProblemVO> getProblemListInRepoExamNoAnswer(Integer limit, Integer currentPage, Integer type, Integer difficulty, String keyword, Integer auth, Long repoId) {
        if (currentPage == null || currentPage < 1) currentPage = 1;
        if (limit == null || limit < 1) limit = 10;

        if (!StringUtils.isEmpty(keyword)) {
            keyword = keyword.trim();
        }
        return examProblemEntityService.getProblemListInRepoExamNoAnswer(limit, currentPage, type, difficulty, keyword, auth, repoId);
    }

    private void checkTrainingAuth(Long repoId, Long tid) throws StatusForbiddenException, StatusAccessDeniedException {
        QueryWrapper<TrainingExam> trainingExamQueryWrapper = new QueryWrapper<>();
        trainingExamQueryWrapper.eq("eid", repoId).eq("tid", tid);
        TrainingExam trainingExam = trainingExamEntityService.getOne(trainingExamQueryWrapper);
        if (trainingExam == null) {
            throw new StatusAccessDeniedException("错误的试卷ID");
        }

        Training training = trainingEntityService.getById(tid);
        trainingValidator.validateTrainingAuth(training);
    }

    private void checkExamAuth(Long eid, String uid) throws StatusForbiddenException, StatusAccessDeniedException {
        QueryWrapper<ExamRegister> examRegisterQueryWrapper = new QueryWrapper<>();
        examRegisterQueryWrapper.eq("eid", eid).eq("uid", uid);
        ExamRegister examRegister = examRegisterEntityService.getOne(examRegisterQueryWrapper);
        if (examRegister == null) {
            throw new StatusAccessDeniedException("尚未输入密码验证");
        }
    }
}
