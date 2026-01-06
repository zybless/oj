package top.hcode.hoj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.hcode.hoj.pojo.entity.exam.ExamHistory;
import top.hcode.hoj.pojo.entity.exam.ExamHistoryDetail;

import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/20 19:25
 */
@Mapper
@Repository
public interface ExamHistoryDetailMapper extends BaseMapper<ExamHistoryDetail> {
    List<ExamHistoryDetail> getHistoryDetails(Long historyId);
}
