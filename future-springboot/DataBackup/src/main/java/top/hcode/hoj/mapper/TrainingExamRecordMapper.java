package top.hcode.hoj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.hcode.hoj.pojo.entity.training.TrainingExamRecord;
import top.hcode.hoj.pojo.vo.TrainingExamRecordVO;

import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2024/8/15 10:56
 * @Description:
 */
@Mapper
@Repository
public interface TrainingExamRecordMapper extends BaseMapper<TrainingExamRecord> {

    public List<TrainingExamRecordVO> getTrainingRecord(@Param("tid") Long tid);

}
