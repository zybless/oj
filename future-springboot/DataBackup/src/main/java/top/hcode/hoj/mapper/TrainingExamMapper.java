package top.hcode.hoj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.hcode.hoj.pojo.entity.training.TrainingExam;

import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2024/7/15 14:09
 * @Description:
 */

@Mapper
@Repository
public interface TrainingExamMapper extends BaseMapper<TrainingExam> {
    List<Long> getTrainingExamIdList(@Param("tid") Long tid);
}
