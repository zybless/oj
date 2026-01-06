package top.hcode.hoj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.hcode.hoj.pojo.entity.exam.ExamHistory;
import top.hcode.hoj.pojo.entity.exam.ExamProblemAnswer;
import top.hcode.hoj.pojo.vo.ExamHistoryVO;

import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/20 19:24
 */
@Mapper
@Repository
public interface ExamHistoryMapper extends BaseMapper<ExamHistory> {


    List<Long> getIDByUid(@Param("uid")String uid,
                          @Param("type")Integer type,
                          @Param("repoId") Long repoId);

    Long getRecentID(@Param("uid")String uid,
                     @Param("type")Integer type,
                     @Param("repoId") Long repoId);

}
