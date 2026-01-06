package top.hcode.hoj.controller.course;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.hcode.hoj.annotation.AnonApi;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.pojo.vo.AdminCourseVO;
import top.hcode.hoj.pojo.vo.CourseVO;
import top.hcode.hoj.service.course.CourseService;

/**
 * @Author: Minipax
 * @Date: 2023/9/29 17:10
 */
@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/get-course-list")
    @AnonApi
    public CommonResult<IPage<CourseVO>> getCourseList(@RequestParam(value = "limit", required = false) Integer limit,
                                                       @RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                       @RequestParam(value = "type", required = false) Integer type,
                                                       @RequestParam(value = "keyword", required = false) String keyword) {
        return courseService.getCourseList(limit, currentPage, type, keyword);
    }

    @GetMapping("")
    @AnonApi
    public CommonResult<CourseVO> getCourse(@RequestParam("id") Long id) {
        return courseService.getCourse(id);
    }

}
