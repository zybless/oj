package top.hcode.hoj.manager.exam;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import top.hcode.hoj.common.exception.StatusFailException;
import top.hcode.hoj.dao.exam.ExamEntityService;
import top.hcode.hoj.dao.exam.ExamHistoryEntityService;
import top.hcode.hoj.dao.exam.ExamRepoEntityService;
import top.hcode.hoj.dao.user.UserInfoEntityService;
import top.hcode.hoj.pojo.dto.ExamRankDTO;
import top.hcode.hoj.pojo.entity.exam.Exam;
import top.hcode.hoj.pojo.entity.exam.ExamHistory;
import top.hcode.hoj.pojo.entity.exam.ExamRepo;
import top.hcode.hoj.pojo.entity.user.UserInfo;
import top.hcode.hoj.pojo.vo.ExamHistoryVO;
import top.hcode.hoj.pojo.vo.ExamRankVO;
import top.hcode.hoj.pojo.vo.ExamVO;
import top.hcode.hoj.shiro.AccountProfile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2024/3/25 20:58
 */
@Component
public class ExamManager {

    @Autowired
    private ExamEntityService examEntityService;

    @Autowired
    private ExamHistoryEntityService examHistoryEntityService;

    @Autowired
    private ExamRepoEntityService examRepoEntityService;

    public IPage<ExamVO> getExamList(Integer limit, Integer currentPage, String title) {
        if (currentPage == null || currentPage < 1) currentPage = 1;
        if (limit == null || limit < 1) limit = 10;


        return examEntityService.getExamList(limit, currentPage, title);
    }

    public ExamVO getExam(Long examId) throws StatusFailException {
        ExamVO exam = examEntityService.getExamUser(examId);
        if (exam == null) {
            throw new StatusFailException("考试不存在!");
        }
        ExamRepo examRepo = examRepoEntityService.getById(exam.getExamRepoId());
        exam.setExamRepo(examRepo);
        return exam;
    }

    public ExamHistoryVO getMyExamResult(Long examId) throws StatusFailException {
        AccountProfile userInfo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();

        QueryWrapper<ExamHistory> wrapper = new QueryWrapper<>();
        wrapper.eq("exam_id", examId)
                .eq("user_id", userInfo.getUid())
                .isNotNull("submit_time")
                .last("LIMIT 1");

        ExamHistory examHistory = examHistoryEntityService.getOne(wrapper, false);
        if (examHistory == null) {
            throw new StatusFailException("暂无成绩，您尚未提交本次考试");
        }

        // 复用现有方法
        return examHistoryEntityService.getExamHistoryVO(examHistory.getId());
    }

}
