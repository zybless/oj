package top.hcode.hoj.manager.course;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import top.hcode.hoj.common.exception.StatusFailException;
import top.hcode.hoj.dao.course.CourseEntityService;
import top.hcode.hoj.dao.course.CourseStudentEntityService;
import top.hcode.hoj.dao.course.CourseTeacherEntityService;
import top.hcode.hoj.dao.user.UserRoleEntityService;
import top.hcode.hoj.pojo.entity.course.Course;
import top.hcode.hoj.pojo.entity.course.CourseTeacher;
import top.hcode.hoj.pojo.vo.CourseVO;
import top.hcode.hoj.pojo.vo.UserRolesVO;
import top.hcode.hoj.validator.CourseValidator;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/30 12:56
 */
@Component
@RefreshScope
@Slf4j(topic = "hoj")
public class CourseManager {

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

    public IPage<CourseVO> getCourseList(Integer limit, Integer currentPage, Integer type, String keyword) {
        if (currentPage == null || currentPage < 1) currentPage = 1;
        if (limit == null || limit < 1) limit = 10;

        if (!StringUtils.isEmpty(keyword)) {
            keyword = keyword.trim();
        }

        return courseEntityService.getCourseList(limit, currentPage, type, keyword);
    }

    public CourseVO getCourse(Long id) throws StatusFailException {
        CourseVO courseVO = new CourseVO();

        Course course = courseEntityService.getById(id);
        if(course == null) {
            throw new StatusFailException("课程不存在");
        }

        courseVO.setCourse(course);

        QueryWrapper<CourseTeacher> courseTeacherQueryWrapper = new QueryWrapper<>();
        courseTeacherQueryWrapper.eq("course_id", course.getId());
        List<CourseTeacher> courseTeachers = courseTeacherEntityService.list(courseTeacherQueryWrapper);

        List<UserRolesVO> teacherList = new ArrayList<>();
        for (CourseTeacher courseTeacher : courseTeachers) {
            UserRolesVO teacher = userRoleEntityService.getUserRoles(courseTeacher.getTeacherId(), null);
            teacherList.add(teacher);
        }
        courseVO.setTeacherList(teacherList);

        //推荐课程

        //
        return courseVO;
    }
}
