package top.hcode.hoj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.hcode.hoj.pojo.entity.contest.ContestStartStatus;

/**
 * @Author: Minipax
 * @Date: 2024/6/20 15:57
 */

@Mapper
@Repository
public interface ContestStartStatusMapper extends BaseMapper<ContestStartStatus> {



}
