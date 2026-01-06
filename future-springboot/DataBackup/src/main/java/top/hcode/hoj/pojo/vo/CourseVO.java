package top.hcode.hoj.pojo.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import top.hcode.hoj.pojo.entity.course.Course;

import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/30 11:26
 */
@Data
@ApiModel(value = "课程查询对象CourseVO", description = "")
public class CourseVO {

    private Course course;

    private List<UserRolesVO> teacherList;

    private List<Course> recommendCourseList;

}
