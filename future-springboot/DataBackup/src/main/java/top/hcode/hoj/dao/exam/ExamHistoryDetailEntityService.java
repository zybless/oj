package top.hcode.hoj.dao.exam;

import com.baomidou.mybatisplus.extension.service.IService;
import top.hcode.hoj.pojo.entity.exam.ExamHistoryDetail;

import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/20 19:28
 */
public interface ExamHistoryDetailEntityService extends IService<ExamHistoryDetail> {
    List<ExamHistoryDetail> getHistoryDetails(Long historyId);

}
