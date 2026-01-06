package top.hcode.hoj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.hcode.hoj.pojo.entity.course.CourseStudent;

/**
 * @Author: Minipax
 * @Date: 2023/9/29 11:20
 */
@Mapper
@Repository
public interface CourseStudentMapper extends BaseMapper<CourseStudent> {
}
