package top.hcode.hoj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;
import top.hcode.hoj.pojo.entity.group.GroupExam;

/**
 * @Author: Minipax
 * @Date: 2024/8/11 10:02
 */
@Mapper
@Repository
public interface GroupExamMapper extends BaseMapper<GroupExam> {



}
