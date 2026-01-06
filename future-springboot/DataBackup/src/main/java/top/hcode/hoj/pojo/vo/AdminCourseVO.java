package top.hcode.hoj.pojo.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import top.hcode.hoj.pojo.entity.course.Course;

import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/29 1:29
 */
@Data
@ApiModel(value = "管理员课程查询对象AdminCourseVO", description = "")
public class AdminCourseVO {

    private Course course;

    private List<UserRolesVO> teacherList;

    private List<UserRolesVO> studentList;

}
