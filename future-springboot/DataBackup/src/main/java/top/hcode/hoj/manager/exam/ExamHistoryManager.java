package top.hcode.hoj.manager.exam;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.hcode.hoj.common.exception.StatusFailException;
import top.hcode.hoj.common.exception.StatusForbiddenException;
import top.hcode.hoj.dao.exam.ExamHistoryEntityService;
import top.hcode.hoj.pojo.entity.exam.ExamHistory;
import top.hcode.hoj.pojo.vo.ExamHistoryVO;
import top.hcode.hoj.shiro.AccountProfile;

import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/21 12:23
 */
@Component
public class ExamHistoryManager {

    @Autowired
    private ExamHistoryEntityService examHistoryEntityService;

    public List<ExamHistory> getExerciseHistory(Long repoId) {
        String uid = ((AccountProfile) SecurityUtils.getSubject().getPrincipal()).getUid();
        QueryWrapper<ExamHistory> examHistoryQueryWrapper = new QueryWrapper<>();
        examHistoryQueryWrapper.eq("user_id", uid).eq("type", 1).eq("repo_id", repoId);
        return examHistoryEntityService.list(examHistoryQueryWrapper);
    }

    public ExamHistoryVO getExerciseHistoryDetail(Long historyId, Long repoId) throws StatusForbiddenException, StatusFailException {
        String uid = ((AccountProfile) SecurityUtils.getSubject().getPrincipal()).getUid();
        if(historyId == null) {
            return examHistoryEntityService.getRecentExamHistoryVO(uid, repoId);
        }

        ExamHistory examHistory = examHistoryEntityService.getById(historyId);

        boolean isRoot = SecurityUtils.getSubject().hasRole("root");
        boolean isAdmin = SecurityUtils.getSubject().hasRole("admin");
        boolean isTeacher = SecurityUtils.getSubject().hasRole("teacher");

        if(!isRoot && !isAdmin && !isTeacher && !examHistory.getUserId().equals(uid)) {
            throw new StatusForbiddenException("不是当前用户的历史记录！");
        }

        if(examHistory.getType() != 1) {
            throw new StatusFailException("不是练习的历史记录！");
        }

        return examHistoryEntityService.getExamHistoryVO(historyId);
    }
}
