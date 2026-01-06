package top.hcode.hoj.service.admin.exam;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Service;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.pojo.dto.ExamRankDTO;
import top.hcode.hoj.pojo.entity.exam.Exam;
import top.hcode.hoj.pojo.vo.ExamHistoryVO;
import top.hcode.hoj.pojo.vo.ExamRankVO;
import top.hcode.hoj.pojo.vo.ExamVO;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2024/3/25 14:08
 */
public interface AdminExamService {
    CommonResult<IPage<ExamVO>> getExamList(Integer limit, Integer currentPage, String title, Date startTime, Date endTime);

    CommonResult<List<ExamHistoryVO>> getExamHistory(Long examId);

    CommonResult<IPage<ExamRankVO>> getExamRank(ExamRankDTO examRankDTO);

    CommonResult<ExamVO> getExam(Long id);

    CommonResult<Void> deleteExam(Long id);

    CommonResult<Void> addExam(Exam exam);

    CommonResult<Void> updateExam(Exam exam);

    CommonResult<ExamHistoryVO> getExamHistoryDetail(Long historyId);

    CommonResult<Void> exportHistory(Long eid, HttpServletResponse response);
}
