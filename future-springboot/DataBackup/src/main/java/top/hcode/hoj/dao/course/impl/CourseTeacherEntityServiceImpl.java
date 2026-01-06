package top.hcode.hoj.dao.course.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hcode.hoj.dao.course.CourseTeacherEntityService;
import top.hcode.hoj.mapper.CourseTeacherMapper;
import top.hcode.hoj.pojo.entity.course.CourseTeacher;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/29 11:17
 */
@Service
public class CourseTeacherEntityServiceImpl extends ServiceImpl<CourseTeacherMapper, CourseTeacher> implements CourseTeacherEntityService {

    @Autowired
    private CourseTeacherMapper courseTeacherMapper;

    @Override
    public List<String> getTeacherIdsByCourseId(Long courseId) {
        QueryWrapper<CourseTeacher> courseTeacherQueryWrapper = new QueryWrapper<>();
        courseTeacherQueryWrapper.eq("course_id", courseId);
        List<CourseTeacher> courseTeachers = courseTeacherMapper.selectList(courseTeacherQueryWrapper);

        List<String> teacherIds = new ArrayList<>();
        for (CourseTeacher courseTeacher : courseTeachers) {
            teacherIds.add(courseTeacher.getTeacherId());
        }

        return teacherIds;
    }

    @Override
    public boolean updateBatchByCourseId(Long courseId, List<String> teacherIds) {
        QueryWrapper<CourseTeacher> courseTeacherQueryWrapper = new QueryWrapper<>();
        courseTeacherQueryWrapper.eq("course_id", courseId);

        boolean removeResult = this.remove(courseTeacherQueryWrapper);

        List<CourseTeacher> courseTeachers = new ArrayList<>();
        for (String teacherId : teacherIds) {
            CourseTeacher courseTeacher = new CourseTeacher();
            courseTeacher.setTeacherId(teacherId);
            courseTeacher.setCourseId(courseId);
            courseTeachers.add(courseTeacher);
        }

        boolean saveResult = this.saveBatch(courseTeachers);

        return true;
    }
}
