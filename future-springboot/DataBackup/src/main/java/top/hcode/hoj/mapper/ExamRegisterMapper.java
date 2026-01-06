package top.hcode.hoj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;
import top.hcode.hoj.pojo.entity.exam.ExamRegister;

/**
 * @Author: Minipax
 * @Date: 2024/8/30 15:05
 */
@Mapper
@Repository
public interface ExamRegisterMapper extends BaseMapper<ExamRegister> {
}
