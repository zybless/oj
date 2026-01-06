package top.hcode.hoj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.hcode.hoj.pojo.entity.training.TrainingCamp;

/**
 * @Author: Minipax
 * @Date: 2024/6/23 18:06
 */
@Mapper
@Repository
public interface TrainingCampMapper extends BaseMapper<TrainingCamp> {
}
