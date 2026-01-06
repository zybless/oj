package top.hcode.hoj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.hcode.hoj.pojo.entity.course.Course;

import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/29 11:19
 */
@Mapper
@Repository
public interface CourseMapper extends BaseMapper<Course> {


}
