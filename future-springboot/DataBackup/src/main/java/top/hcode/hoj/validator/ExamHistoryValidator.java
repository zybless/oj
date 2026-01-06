package top.hcode.hoj.validator;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.hcode.hoj.common.exception.StatusFailException;
import top.hcode.hoj.common.exception.StatusForbiddenException;
import top.hcode.hoj.dao.exam.ExamHistoryEntityService;
import top.hcode.hoj.pojo.dto.ExamSubmitDTO;
import top.hcode.hoj.pojo.entity.exam.ExamHistory;
import top.hcode.hoj.shiro.AccountProfile;

/**
 * @Author: Minipax
 * @Date: 2023/9/20 21:38
 */
@Component
public class ExamHistoryValidator {

    @Autowired
    private ExamHistoryEntityService examHistoryEntityService;

    public void validateExamSubmit(ExamSubmitDTO examSubmitDTO) throws StatusFailException, StatusForbiddenException {
        String uid = ((AccountProfile) SecurityUtils.getSubject().getPrincipal()).getUid();
        ExamHistory examHistory = examHistoryEntityService.getById(examSubmitDTO.getHistoryId());

        if(examHistory == null || !examHistory.getUserId().equals(uid)) {
            throw new StatusForbiddenException("提交试卷ID错误！");
        }

        if(examSubmitDTO.getProblemIds().size() != examSubmitDTO.getProblemAnswers().size()) {
            throw new StatusFailException("题目数量和答案数量不匹配！");
        }
    }

}
