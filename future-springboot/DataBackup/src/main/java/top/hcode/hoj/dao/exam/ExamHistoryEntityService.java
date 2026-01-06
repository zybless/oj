package top.hcode.hoj.dao.exam;

import com.baomidou.mybatisplus.extension.service.IService;
import top.hcode.hoj.pojo.entity.exam.ExamHistory;
import top.hcode.hoj.pojo.vo.ExamHistoryVO;

import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/20 19:22
 */
public interface ExamHistoryEntityService extends IService<ExamHistory> {
    ExamHistoryVO getExamHistoryVO(Long historyId);

    ExamHistoryVO getRecentExamHistoryVO(String uid, Long repoId);

}
