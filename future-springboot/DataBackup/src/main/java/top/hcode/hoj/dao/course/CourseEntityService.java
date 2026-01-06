package top.hcode.hoj.dao.course;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import top.hcode.hoj.pojo.entity.course.Course;
import top.hcode.hoj.pojo.vo.AdminCourseVO;
import top.hcode.hoj.pojo.vo.CourseVO;

/**
 * @Author: Minipax
 * @Date: 2023/9/29 11:16
 */
public interface CourseEntityService extends IService<Course> {


    IPage<AdminCourseVO> getCourseListAdmin(Integer limit, Integer currentPage, Integer type, String keyword);

    IPage<CourseVO> getCourseList(Integer limit, Integer currentPage, Integer type, String keyword);

}
