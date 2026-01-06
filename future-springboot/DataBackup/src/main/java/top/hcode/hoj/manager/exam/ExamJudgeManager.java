package top.hcode.hoj.manager.exam;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.hcode.hoj.common.exception.StatusAccessDeniedException;
import top.hcode.hoj.common.exception.StatusFailException;
import top.hcode.hoj.common.exception.StatusForbiddenException;
import top.hcode.hoj.dao.exam.*;
import top.hcode.hoj.dao.training.TrainingEntityService;
import top.hcode.hoj.dao.training.TrainingExamEntityService;
import top.hcode.hoj.dao.training.TrainingExamRecordEntityService;
import top.hcode.hoj.dao.training.TrainingRecordEntityService;
import top.hcode.hoj.pojo.dto.ExamSubmitDTO;
import top.hcode.hoj.pojo.entity.exam.*;
import top.hcode.hoj.pojo.entity.training.Training;
import top.hcode.hoj.pojo.entity.training.TrainingExam;
import top.hcode.hoj.pojo.entity.training.TrainingExamRecord;
import top.hcode.hoj.pojo.entity.training.TrainingRecord;
import top.hcode.hoj.pojo.vo.ExamHistoryVO;
import top.hcode.hoj.shiro.AccountProfile;
import top.hcode.hoj.validator.ExamHistoryValidator;
import top.hcode.hoj.validator.TrainingValidator;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/20 19:52
 */
@Component
@RefreshScope
@Slf4j(topic = "hoj")
public class ExamJudgeManager {

    @Autowired
    private ExamHistoryValidator examHistoryValidator;

    @Autowired
    private ExamHistoryEntityService examHistoryEntityService;

    @Autowired
    private ExamHistoryDetailEntityService examHistoryDetailEntityService;

    @Autowired
    private ExamProblemEntityService examProblemEntityService;

    @Autowired
    private ExamProblemAnswerEntityService examProblemAnswerEntityService;

    @Autowired
    private ExamRepoEntityService examRepoEntityService;

    @Autowired
    private ExamRepoProblemEntityService examRepoProblemEntityService;

    @Autowired
    private ExamEntityService examEntityService;

    @Autowired
    private TrainingExamEntityService trainingExamEntityService;

    @Autowired
    private TrainingEntityService trainingEntityService;

    @Autowired
    private TrainingExamRecordEntityService trainingExamRecordEntityService;

    @Autowired
    private TrainingValidator trainingValidator;

    @Autowired
    private ExamRegisterEntityService examRegisterEntityService;

    @Transactional(rollbackFor = Exception.class)
    public ExamHistoryVO submitExercise(ExamSubmitDTO examSubmitDTO) throws StatusForbiddenException, StatusFailException {
        examHistoryValidator.validateExamSubmit(examSubmitDTO);


        List<Long> problemIds = examSubmitDTO.getProblemIds();
        List<List<String>> problemAnswers = examSubmitDTO.getProblemAnswers();
        ExamHistory examHistory = examHistoryEntityService.getById(examSubmitDTO.getHistoryId());
        HashMap<Long, Float> problemGradeMap = examRepoProblemEntityService.getProblemGradeMap(examHistory.getRepoId());

        if(examHistory.getUserScore() != null) {
            throw new StatusFailException("该自由练习已提交过！");
        }

        List<ExamHistoryDetail> examHistoryDetailList = new ArrayList<>();
        Float userScore = 0F;
        for(int i = 0; i < problemIds.size(); ++i) {
            Long problemId = problemIds.get(i);
            List<String> problemAnswer = problemAnswers.get(i);

            ExamHistoryDetail examHistoryDetail = new ExamHistoryDetail();
            examHistoryDetail.setHistoryId(examSubmitDTO.getHistoryId());
            examHistoryDetail.setProblemId(problemId);
            examHistoryDetail.setUserAnswer(new Gson().toJson(problemAnswer));
            examHistoryDetail.setIsCorrect(checkAnswer(problemId, problemAnswer));

            examHistoryDetailList.add(examHistoryDetail);

            if(examHistoryDetail.getIsCorrect()) {
                userScore += problemGradeMap.get(problemId);
            }
        }

        boolean examHistoryDetailSaveResult = examHistoryDetailEntityService.saveBatch(examHistoryDetailList);

        examHistory.setUserScore(userScore);
        boolean examHistorySaveResult = examHistoryEntityService.saveOrUpdate(examHistory);

        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();
        TrainingExamRecord trainingExamRecord = new TrainingExamRecord();
        trainingExamRecord.setTid(examSubmitDTO.getTrainingId());
        trainingExamRecord.setUid(userRolesVo.getUid());
//        trainingExamRecord.setEid(examSubmitDTO.get)
        trainingExamRecord.setSubmitId(examSubmitDTO.getHistoryId());
        trainingExamRecordEntityService.save(trainingExamRecord);

        if(examHistoryDetailSaveResult && examHistorySaveResult) {
            ExamHistoryVO examHistoryVO = examHistoryEntityService.getExamHistoryVO(examHistory.getId());
            return examHistoryVO;
        } else {
            throw new StatusFailException("提交失败");
        }

    }

    public Boolean checkAnswer(Long problemId, List<String> userAnswers) throws StatusFailException {
        ExamProblem examProblem = examProblemEntityService.getById(problemId);
        if(examProblem == null) {
            throw new StatusFailException("题目ID不存在！");
        }

        List<ExamProblemAnswer> problemAnswers = examProblemAnswerEntityService.getProblemAnswers(problemId);
        if(problemAnswers == null || problemAnswers.size() == 0) {
            throw new StatusFailException("题目选项答案不存在！");
        }

        Integer type = examProblem.getType();
        List<ExamProblemAnswer> correctProblemAnswers;
        if(type == 3) {
            correctProblemAnswers = problemAnswers;
        } else {
            correctProblemAnswers = new ArrayList<>();
            for (ExamProblemAnswer problemAnswer : problemAnswers) {
                if (problemAnswer.getIsCorrect()) {
                    correctProblemAnswers.add(problemAnswer);
                }
            }
        }

        if(userAnswers == null || userAnswers.size() != correctProblemAnswers.size()) {
            return Boolean.FALSE;
        }

        Boolean result = Boolean.TRUE;

        if(type == 3) {
            for(int i = 0; i < correctProblemAnswers.size(); ++i) {
                if(!correctProblemAnswers.get(i).getContent().equals(userAnswers.get(i))) {
                    result = Boolean.FALSE;
                }
            }
        } else {
            for(int i = 0; i < correctProblemAnswers.size(); ++i) {
                ExamProblemAnswer examProblemAnswer = correctProblemAnswers.get(i);
                if(!userAnswers.contains(examProblemAnswer.getId().toString())) {
                    result = Boolean.FALSE;
                }
            }
        }

        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long startExercise(Long repoId, Long tid) throws StatusFailException, StatusForbiddenException, StatusAccessDeniedException {
        ExamHistory examHistory = new ExamHistory();

        ExamRepo examRepo = examRepoEntityService.getById(repoId);
        if(examRepo == null) {
            throw new StatusFailException("题库不存在！");
        }

        boolean isRoot = SecurityUtils.getSubject().hasRole("root");
        if (!isRoot) {
            checkTrainingAuth(repoId, tid);
        }

        examHistory.setRepoId(repoId);
        examHistory.setExamId(0L);
        examHistory.setTotalScore(examRepo.getTotalScore());
        examHistory.setTotalScore(examRepoEntityService.getById(repoId).getTotalScore());
        examHistory.setType(1);
        examHistory.setUserId(((AccountProfile) SecurityUtils.getSubject().getPrincipal()).getUid());


        boolean save = examHistoryEntityService.save(examHistory);
        if(!save) {
            throw new StatusFailException("练习失败！");
        }
        return examHistory.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long startExam(Long examId, String password) throws StatusForbiddenException, StatusFailException {
        Exam exam = examEntityService.getById(examId);
        if ((exam.getPassword() != null || exam.getPassword() != "") && !exam.getPassword().equals(password)) {
            throw new StatusForbiddenException("密码错误！");
        }

        if (exam.getStartTime().after(new Date())) {
            throw new StatusForbiddenException("还未到考试时间！");
        }

        if (exam.getEndTime().before(new Date())) {
            throw new StatusForbiddenException("考试已经结束！");
        }

        String uid = ((AccountProfile) SecurityUtils.getSubject().getPrincipal()).getUid();

        QueryWrapper<ExamHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("exam_id", examId).eq("user_id", uid);
        // 判断是否参加过该考试
        ExamHistory examHistory = examHistoryEntityService.getOne(queryWrapper);
        if (examHistory == null) {
            examHistory = new ExamHistory();

            ExamRepo examRepo = examRepoEntityService.getById(exam.getExamRepoId());
            if(examRepo == null) {
                throw new StatusFailException("题库不存在！");
            }

            examHistory.setRepoId(exam.getExamRepoId());
            examHistory.setExamId(examId);
            examHistory.setTotalScore(examRepo.getTotalScore());
            examHistory.setTotalScore(examRepoEntityService.getById(exam.getExamRepoId()).getTotalScore());
            examHistory.setType(2);
            examHistory.setUserId(uid);


            boolean saveExamHistory = examHistoryEntityService.save(examHistory);

            ExamRegister examRegister = new ExamRegister();
            examRegister.setEid(examId);
            examRegister.setUid(uid);
            examRegister.setStatus(0);
            boolean saveExamRegister = examRegisterEntityService.save(examRegister);


            if(!saveExamHistory || !saveExamRegister) {
                throw new StatusFailException("考试进入失败！");
            }

        } else if(examHistory.getUserScore() != null) {
            throw new StatusFailException("该考试已提交过！");
        }

        return examHistory.getId();

    }

    @Transactional(rollbackFor = Exception.class)
    public ExamHistoryVO submitExam(ExamSubmitDTO examSubmitDTO) throws StatusForbiddenException, StatusFailException {
        examHistoryValidator.validateExamSubmit(examSubmitDTO);

        List<Long> problemIds = examSubmitDTO.getProblemIds();
        List<List<String>> problemAnswers = examSubmitDTO.getProblemAnswers();
        ExamHistory examHistory = examHistoryEntityService.getById(examSubmitDTO.getHistoryId());
        HashMap<Long, Float> problemGradeMap = examRepoProblemEntityService.getProblemGradeMap(examHistory.getRepoId());

        if(examHistory.getUserScore() != null) {
            throw new StatusFailException("该考试已提交过！");
        }

        Instant now = Instant.now();

        Instant twoMinutesAgo = now.minus(2, ChronoUnit.MINUTES);

        Date twoMinutesAgoDate = Date.from(twoMinutesAgo);

        Long examId = examHistory.getExamId();
        Exam exam = examEntityService.getById(examId);
        if (exam.getEndTime().before(twoMinutesAgoDate)) {
            throw new StatusFailException("已超时，无法提交");
        }

        List<ExamHistoryDetail> examHistoryDetailList = new ArrayList<>();
        Float userScore = 0F;
        for(int i = 0; i < problemIds.size(); ++i) {
            Long problemId = problemIds.get(i);
            List<String> problemAnswer = problemAnswers.get(i);

            ExamHistoryDetail examHistoryDetail = new ExamHistoryDetail();
            examHistoryDetail.setHistoryId(examSubmitDTO.getHistoryId());
            examHistoryDetail.setProblemId(problemId);
            examHistoryDetail.setUserAnswer(new Gson().toJson(problemAnswer));
            examHistoryDetail.setIsCorrect(checkAnswer(problemId, problemAnswer));

            examHistoryDetailList.add(examHistoryDetail);

            if(examHistoryDetail.getIsCorrect()) {
                userScore += problemGradeMap.get(problemId);
            }
        }

        boolean examHistoryDetailSaveResult = examHistoryDetailEntityService.saveBatch(examHistoryDetailList);

        examHistory.setUserScore(userScore);
        boolean examHistorySaveResult = examHistoryEntityService.saveOrUpdate(examHistory);

        if(examHistoryDetailSaveResult && examHistorySaveResult) {
            ExamHistoryVO examHistoryVO = examHistoryEntityService.getExamHistoryVO(examHistory.getId());
//            return examHistoryVO;
            return null;
        } else {
            throw new StatusFailException("提交失败");
        }
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
}
