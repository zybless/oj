package top.hcode.hoj.dao.course;

import com.baomidou.mybatisplus.extension.service.IService;
import top.hcode.hoj.pojo.entity.course.CourseStudent;

import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/29 11:16
 */
public interface CourseStudentEntityService extends IService<CourseStudent> {
    boolean updateBatchByCourseId(Long courseId, List<String> studentIds);

}
