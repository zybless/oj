package top.hcode.hoj.dao.course.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hcode.hoj.dao.course.CourseEntityService;
import top.hcode.hoj.mapper.CourseMapper;
import top.hcode.hoj.mapper.CourseStudentMapper;
import top.hcode.hoj.mapper.CourseTeacherMapper;
import top.hcode.hoj.mapper.UserRoleMapper;
import top.hcode.hoj.pojo.entity.course.Course;
import top.hcode.hoj.pojo.entity.course.CourseStudent;
import top.hcode.hoj.pojo.entity.course.CourseTeacher;
import top.hcode.hoj.pojo.vo.AdminCourseVO;
import top.hcode.hoj.pojo.vo.CourseVO;
import top.hcode.hoj.pojo.vo.UserRolesVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/29 11:17
 */
@Service
public class CourseEntityServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseEntityService {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseTeacherMapper courseTeacherMapper;

    @Autowired
    private CourseStudentMapper courseStudentMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public IPage<AdminCourseVO> getCourseListAdmin(Integer limit, Integer currentPage, Integer type, String keyword) {
        Page<Course> page = new Page<>(currentPage, limit);

        QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<>();
        if(type != null) {
            courseQueryWrapper.eq("type", type);
        }

        if(keyword != null) {
            courseQueryWrapper.and(wrapper -> wrapper.like("title", keyword).or()
                    .like("content", keyword).or()
                    .like("faq", keyword).or()
                    .like("description", keyword));
        }

        List<Course> courseList = courseMapper.selectPage(page, courseQueryWrapper).getRecords();

        IPage<AdminCourseVO> adminCourseVOIPage = new Page<>(currentPage, limit);

        List<AdminCourseVO> adminCourseVOList = new ArrayList<>();
        for (Course course : courseList) {
            AdminCourseVO adminCourseVO = new AdminCourseVO();
            adminCourseVO.setCourse(course);

            QueryWrapper<CourseTeacher> courseTeacherQueryWrapper = new QueryWrapper<>();
            courseTeacherQueryWrapper.eq("course_id", course.getId());
            List<CourseTeacher> courseTeachers = courseTeacherMapper.selectList(courseTeacherQueryWrapper);

            List<UserRolesVO> teacherList = new ArrayList<>();
            for (CourseTeacher courseTeacher : courseTeachers) {
                UserRolesVO teacher = userRoleMapper.getUserRoles(courseTeacher.getTeacherId(), null);
                teacherList.add(teacher);
            }
            adminCourseVO.setTeacherList(teacherList);

            QueryWrapper<CourseStudent> courseStudentQueryWrapper = new QueryWrapper<>();
            courseStudentQueryWrapper.eq("course_id", course.getId());
            List<CourseStudent> courseStudents = courseStudentMapper.selectList(courseStudentQueryWrapper);

            List<UserRolesVO> studentList = new ArrayList<>();
            for (CourseStudent courseStudent : courseStudents) {
                UserRolesVO student = userRoleMapper.getUserRoles(courseStudent.getStudentId(), null);
                studentList.add(student);
            }
            adminCourseVO.setStudentList(studentList);

            adminCourseVOList.add(adminCourseVO);
        }

        adminCourseVOIPage.setRecords(adminCourseVOList);

        return adminCourseVOIPage;
    }

    @Override
    public IPage<CourseVO> getCourseList(Integer limit, Integer currentPage, Integer type, String keyword) {
        Page<Course> page = new Page<>(currentPage, limit);

        QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<>();
        if(type != null) {
            courseQueryWrapper.eq("type", type);
        }

        if(keyword != null) {
            courseQueryWrapper.and(wrapper -> wrapper.like("title", keyword).or()
                    .like("content", keyword).or()
                    .like("faq", keyword).or()
                    .like("description", keyword));
        }

        List<Course> courseList = courseMapper.selectPage(page, courseQueryWrapper).getRecords();

        IPage<CourseVO> courseVOPage = new Page<>(currentPage, limit);

        List<CourseVO> courseVOList = new ArrayList<>();
        for (Course course : courseList) {
            CourseVO courseVO = new CourseVO();
            courseVO.setCourse(course);

            QueryWrapper<CourseTeacher> courseTeacherQueryWrapper = new QueryWrapper<>();
            courseTeacherQueryWrapper.eq("course_id", course.getId());
            List<CourseTeacher> courseTeachers = courseTeacherMapper.selectList(courseTeacherQueryWrapper);

            List<UserRolesVO> teacherList = new ArrayList<>();
            for (CourseTeacher courseTeacher : courseTeachers) {
                UserRolesVO teacher = userRoleMapper.getUserRoles(courseTeacher.getTeacherId(), null);
                teacherList.add(teacher);
            }
            courseVO.setTeacherList(teacherList);

            //设置推荐课程
            //
            courseVOList.add(courseVO);
        }
        courseVOPage.setRecords(courseVOList);
        return courseVOPage;

    }
}
