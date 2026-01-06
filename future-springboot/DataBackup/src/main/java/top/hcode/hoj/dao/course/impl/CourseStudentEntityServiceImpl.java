package top.hcode.hoj.dao.course.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.hcode.hoj.dao.course.CourseStudentEntityService;
import top.hcode.hoj.mapper.CourseStudentMapper;
import top.hcode.hoj.pojo.entity.course.CourseStudent;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/29 11:17
 */
@Service
public class CourseStudentEntityServiceImpl extends ServiceImpl<CourseStudentMapper, CourseStudent> implements CourseStudentEntityService {

    @Override
    public boolean updateBatchByCourseId(Long courseId, List<String> studentIds) {
        QueryWrapper<CourseStudent> courseStudentQueryWrapper = new QueryWrapper<>();
        courseStudentQueryWrapper.eq("course_id", courseId);

        boolean removeResult = this.remove(courseStudentQueryWrapper);

        List<CourseStudent> courseStudents = new ArrayList<>();
        for (String studentId : studentIds) {
            CourseStudent courseStudent = new CourseStudent();
            courseStudent.setStudentId(studentId);
            courseStudent.setCourseId(courseId);
            courseStudents.add(courseStudent);
        }

        boolean saveResult = this.saveBatch(courseStudents);

        return true;
    }
}
