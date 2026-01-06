package top.hcode.hoj.dao.exam.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hcode.hoj.dao.exam.ExamHistoryEntityService;
import top.hcode.hoj.mapper.ExamHistoryDetailMapper;
import top.hcode.hoj.mapper.ExamHistoryMapper;
import top.hcode.hoj.mapper.ExamProblemAnswerMapper;
import top.hcode.hoj.mapper.ExamProblemMapper;
import top.hcode.hoj.pojo.entity.exam.ExamHistory;
import top.hcode.hoj.pojo.entity.exam.ExamHistoryDetail;
import top.hcode.hoj.pojo.entity.exam.ExamProblem;
import top.hcode.hoj.pojo.vo.ExamHistoryVO;
import top.hcode.hoj.pojo.vo.ExamProblemVO;
import top.hcode.hoj.pojo.vo.ExamRepoProblemVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/20 19:22
 */
@Service
public class ExamHistoryEntityServiceImpl extends ServiceImpl<ExamHistoryMapper, ExamHistory> implements ExamHistoryEntityService {

    @Autowired
    private ExamHistoryMapper examHistoryMapper;

    @Autowired
    private ExamHistoryDetailMapper examHistoryDetailMapper;

    @Autowired
    private ExamProblemMapper examProblemMapper;

    @Autowired
    private ExamProblemAnswerMapper examProblemAnswerMapper;

    @Override
    public ExamHistoryVO getExamHistoryVO(Long historyId) {
        ExamHistoryVO examHistoryVO = new ExamHistoryVO();
        examHistoryVO.setExamHistory(examHistoryMapper.selectById(historyId));
        examHistoryVO.setExamHistoryDetailList(examHistoryDetailMapper.getHistoryDetails(historyId));
        examHistoryVO.setExamRepoProblemVOList(examProblemMapper.getProblemsByRepoId(examHistoryVO.getExamHistory().getRepoId()));

        for (ExamRepoProblemVO examRepoProblemVO : examHistoryVO.getExamRepoProblemVOList()) {
            examRepoProblemVO.setExamProblemAnswerList(examProblemAnswerMapper.getProblemAnswers(examRepoProblemVO.getId()));
        }
        return examHistoryVO;
    }

    @Override
    public ExamHistoryVO getRecentExamHistoryVO(String uid, Long repoId) {
        ExamHistoryVO examHistoryVO = new ExamHistoryVO();
        Long historyId = examHistoryMapper.getRecentID(uid, 1, repoId);

        if(historyId == null) {
            return null;
        }

        examHistoryVO.setExamHistory(examHistoryMapper.selectById(historyId));
        examHistoryVO.setExamHistoryDetailList(examHistoryDetailMapper.getHistoryDetails(historyId));
        examHistoryVO.setExamRepoProblemVOList(examProblemMapper.getProblemsByRepoId(examHistoryVO.getExamHistory().getRepoId()));

        for (ExamRepoProblemVO examRepoProblemVO : examHistoryVO.getExamRepoProblemVOList()) {
            examRepoProblemVO.setExamProblemAnswerList(examProblemAnswerMapper.getProblemAnswers(examRepoProblemVO.getId()));
        }
        return examHistoryVO;
    }

}
