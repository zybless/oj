package top.hcode.hoj.manager.admin.course;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import top.hcode.hoj.common.exception.StatusFailException;
import top.hcode.hoj.common.exception.StatusForbiddenException;
import top.hcode.hoj.dao.course.CourseEntityService;
import top.hcode.hoj.dao.course.CourseStudentEntityService;
import top.hcode.hoj.dao.course.CourseTeacherEntityService;
import top.hcode.hoj.dao.user.UserRoleEntityService;
import top.hcode.hoj.pojo.dto.AdminCourseDTO;
import top.hcode.hoj.pojo.entity.course.Course;
import top.hcode.hoj.pojo.entity.course.CourseStudent;
import top.hcode.hoj.pojo.entity.course.CourseTeacher;
import top.hcode.hoj.pojo.vo.AdminCourseVO;
import top.hcode.hoj.pojo.vo.UserRolesVO;
import top.hcode.hoj.shiro.AccountProfile;
import top.hcode.hoj.validator.CourseValidator;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/29 11:14
 */
@Component
@RefreshScope
@Slf4j(topic = "hoj")
public class AdminCourseManager {

    @Autowired
    private CourseEntityService courseEntityService;

    @Autowired
    public CourseTeacherEntityService courseTeacherEntityService;

    @Autowired
    private CourseStudentEntityService courseStudentEntityService;

    @Autowired
    private UserRoleEntityService userRoleEntityService;

    @Autowired
    private CourseValidator courseValidator;


    public IPage<AdminCourseVO> getCourseList(Integer limit, Integer currentPage, Integer type, String keyword) {
        if (currentPage == null || currentPage < 1) currentPage = 1;
        if (limit == null || limit < 1) limit = 10;

        if (!StringUtils.isEmpty(keyword)) {
            keyword = keyword.trim();
        }

        return courseEntityService.getCourseListAdmin(limit, currentPage, type, keyword);

    }

    public AdminCourseVO getCourse(Long id) throws StatusFailException {
        AdminCourseVO adminCourseVO = new AdminCourseVO();

        Course course = courseEntityService.getById(id);
        if(course == null) {
            throw new StatusFailException("课程不存在");
        }

        adminCourseVO.setCourse(course);

        QueryWrapper<CourseTeacher> courseTeacherQueryWrapper = new QueryWrapper<>();
        courseTeacherQueryWrapper.eq("course_id", course.getId());
        List<CourseTeacher> courseTeachers = courseTeacherEntityService.list(courseTeacherQueryWrapper);

        List<UserRolesVO> teacherList = new ArrayList<>();
        for (CourseTeacher courseTeacher : courseTeachers) {
            UserRolesVO teacher = userRoleEntityService.getUserRoles(courseTeacher.getTeacherId(), null);
            teacherList.add(teacher);
        }
        adminCourseVO.setTeacherList(teacherList);

        QueryWrapper<CourseStudent> courseStudentQueryWrapper = new QueryWrapper<>();
        courseStudentQueryWrapper.eq("course_id", course.getId());
        List<CourseStudent> courseStudents = courseStudentEntityService.list(courseStudentQueryWrapper);

        List<UserRolesVO> studentList = new ArrayList<>();
        for (CourseStudent courseStudent : courseStudents) {
            UserRolesVO student = userRoleEntityService.getUserRoles(courseStudent.getStudentId(), null);
            studentList.add(student);
        }
        adminCourseVO.setStudentList(studentList);

        return adminCourseVO;
    }

    @Transactional(rollbackFor = Exception.class)
    public void addCourse(AdminCourseDTO adminCourseDTO) throws StatusFailException, StatusForbiddenException {
        courseValidator.validateAdminCourseDTO(adminCourseDTO);

        // 获取当前登录的用户
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();

        boolean isRoot = SecurityUtils.getSubject().hasRole("root");
        boolean isAdmin = SecurityUtils.getSubject().hasRole("admin");

//        if (!isRoot && !isAdmin) {
//            throw new StatusForbiddenException("对不起，你无权限增加课程！");
//        }

        adminCourseDTO.getCourse().setModifiedUser(userRolesVo.getUsername());

        Course course = adminCourseDTO.getCourse();
        boolean courseSaveResult = courseEntityService.save(course);
        Long courseId = course.getId();

        List<String> teacherIds = adminCourseDTO.getTeacherIds();
        List<CourseTeacher> courseTeacherList = new ArrayList<>();
        for (String teacherId : teacherIds) {
            CourseTeacher courseTeacher = new CourseTeacher();
            courseTeacher.setCourseId(courseId);
            courseTeacher.setTeacherId(teacherId);
            courseTeacherList.add(courseTeacher);
        }
        boolean courseTeacherSaveResult = courseTeacherEntityService.saveBatch(courseTeacherList);

        List<String> studentIds = adminCourseDTO.getStudentIds();
        List<CourseStudent> courseStudentList = new ArrayList<>();
        for (String studentId : studentIds) {
            CourseStudent courseStudent = new CourseStudent();
            courseStudent.setCourseId(courseId);
            courseStudent.setStudentId(studentId);
            courseStudentList.add(courseStudent);
        }
        boolean courseStudentSaveResult = courseStudentEntityService.saveBatch(courseStudentList);

        if (!courseSaveResult || !courseTeacherSaveResult || !courseStudentSaveResult) {
            throw new StatusFailException("添加失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateCourse(AdminCourseDTO adminCourseDTO) throws StatusFailException, StatusForbiddenException {
        courseValidator.validateAdminCourseDTO(adminCourseDTO);

        // 获取当前登录的用户
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();

        boolean isRoot = SecurityUtils.getSubject().hasRole("root");
        boolean isAdmin = SecurityUtils.getSubject().hasRole("admin");

//        if (!isRoot && !isAdmin) {
//            throw new StatusForbiddenException("对不起，你无权限更新课程！");
//        }

        Course course = adminCourseDTO.getCourse();
        Long courseId = course.getId();
        Course courseDB = courseEntityService.getById(courseId);
        if (courseDB == null) {
            throw new StatusFailException("课程不存在！");
        }


        course.setModifiedUser(userRolesVo.getUsername());
        boolean updateCourseResult = courseEntityService.updateById(course);

        List<String> teacherIds = adminCourseDTO.getTeacherIds();
        boolean updateCourseTeacherResult = courseTeacherEntityService.updateBatchByCourseId(courseId, teacherIds);

        List<String> studentIds = adminCourseDTO.getStudentIds();
        boolean updateCourseStudentResult = courseStudentEntityService.updateBatchByCourseId(courseId, studentIds);

        if (!updateCourseResult || !updateCourseTeacherResult || !updateCourseStudentResult) {
            throw new StatusFailException("更新失败！");
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteCourse(Long courseId) throws StatusForbiddenException, StatusFailException {
        // 获取当前登录的用户
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();

        boolean isRoot = SecurityUtils.getSubject().hasRole("root");
        boolean isAdmin = SecurityUtils.getSubject().hasRole("admin");

//        if (!isRoot && !isAdmin) {
//            throw new StatusForbiddenException("对不起，你无权删除课程！");
//        }

        Course courseDB = courseEntityService.getById(courseId);
        if (courseDB == null) {
            throw new StatusFailException("课程不存在！");
        }

        boolean removeResult = courseEntityService.removeById(courseId);

        if (!removeResult) {
            throw new StatusFailException("删除失败！");
        }

    }
}
