package top.hcode.hoj.dao.course;

import com.baomidou.mybatisplus.extension.service.IService;
import top.hcode.hoj.pojo.entity.course.CourseTeacher;

import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/29 11:16
 */
public interface CourseTeacherEntityService extends IService<CourseTeacher> {
    public List<String> getTeacherIdsByCourseId(Long courseId);

    boolean updateBatchByCourseId(Long courseId, List<String> teacherIds);
}
