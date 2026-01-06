package top.hcode.hoj.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.hcode.hoj.common.exception.StatusFailException;
import top.hcode.hoj.dao.course.CourseEntityService;
import top.hcode.hoj.dao.user.UserInfoEntityService;
import top.hcode.hoj.pojo.dto.AdminCourseDTO;
import top.hcode.hoj.pojo.entity.course.Course;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/29 12:21
 */
@Component
public class CourseValidator {

    @Autowired
    private CourseEntityService courseEntityService;

    @Autowired
    private UserInfoEntityService userInfoEntityService;

    public void validateAdminCourseDTO(AdminCourseDTO adminCourseDTO) throws StatusFailException {
        if (adminCourseDTO == null) {
            throw new StatusFailException("课程不能为空");
        }

        Course course = adminCourseDTO.getCourse();
        if (adminCourseDTO.getTeacherIds() == null) {
            adminCourseDTO.setTeacherIds(new ArrayList<>());
        }

        if (adminCourseDTO.getStudentIds() == null) {
            adminCourseDTO.setStudentIds(new ArrayList<>());
        }
        List<String> teacherIds = adminCourseDTO.getTeacherIds();
        List<String> studentIds = adminCourseDTO.getStudentIds();

        this.validateCourse(course);

        for (String teacherId : teacherIds) {
            if(userInfoEntityService.getById(teacherId) == null) {
                throw new StatusFailException("ID为" + teacherId + "的教师不存在。");
            }
        }

        for (String studentId : studentIds) {
            if(userInfoEntityService.getById(studentId) == null) {
                throw new StatusFailException("ID为" + studentId + "的学生不存在。");
            }
        }

        if (studentIds.size() > course.getLimitNumber()) {
            throw new StatusFailException("学生人数大于上限人数");
        }

    }

    private void validateCourse(Course course) throws StatusFailException {

        if(course.getId() != null) {
            if (courseEntityService.getById(course.getId()) == null) {
                throw new StatusFailException("此ID的课程不存在");
            }
        }

        if(course.getTitle() == null || course.getTitle().equals("")) {
            throw new StatusFailException("课程的标题不能为空");
        }

        if (course.getPrice() == null || course.getPrice().compareTo(BigDecimal.valueOf(0)) == -1) {
            throw new StatusFailException("价格设置错误（未设置或者小于0）");
        }

        if (course.getLimitNumber() == null || course.getLimitNumber() < 0) {
            throw new StatusFailException("人数上限错误");
        }

        if (course.getStartTime() == null) {
            throw new StatusFailException("未设置开始时间");
        }

        if (course.getEndTime() == null) {
            throw new StatusFailException("未设置结束时间");
        }

        if (course.getStartTime().after(course.getEndTime())) {
            throw new StatusFailException("开始时间在结束时间之后");
        }
    }
}
