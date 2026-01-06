package top.hcode.hoj.pojo.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import top.hcode.hoj.pojo.entity.course.Course;

import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/29 1:23
 */
@Data
@Accessors(chain = true)
public class AdminCourseDTO {

    private Course course;

    private List<String> teacherIds;

    private List<String> studentIds;

}
