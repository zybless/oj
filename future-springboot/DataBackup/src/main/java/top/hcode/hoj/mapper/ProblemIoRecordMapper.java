package top.hcode.hoj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;
import top.hcode.hoj.pojo.entity.problem.ProblemIoRecord;

/**
 * @Author: Minipax
 * @Date: 2024/8/15 21:08
 * @Description:
 */
@Mapper
@Repository
public interface ProblemIoRecordMapper extends BaseMapper<ProblemIoRecord> {
}
