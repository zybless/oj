package top.hcode.hoj.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.pojo.dto.AdminCourseDTO;
import top.hcode.hoj.pojo.vo.AdminCourseVO;
import top.hcode.hoj.service.admin.course.AdminCourseService;

/**
 * @Author: Minipax
 * @Date: 2023/9/29 1:17
 */

@RestController
@RequestMapping("/api/admin/course")
public class AdminCourseController {

    @Autowired
    private AdminCourseService adminCourseService;

    @GetMapping("/get-course-list")
    @RequiresAuthentication
    @RequiresRoles(value = {"root", "admin", "course_admin", "teacher"}, logical = Logical.OR)
    public CommonResult<IPage<AdminCourseVO>> getCourseList(@RequestParam(value = "limit", required = false) Integer limit,
                                                             @RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                             @RequestParam(value = "type", required = false) Integer type,
                                                             @RequestParam(value = "keyword", required = false) String keyword) {
        return adminCourseService.getCourseList(limit, currentPage, type, keyword);
    }

    @GetMapping("")
    @RequiresAuthentication
    @RequiresRoles(value = {"root", "admin", "course_admin", "teacher"}, logical = Logical.OR)
    public CommonResult<AdminCourseVO> getCourse(@RequestParam("id") Long id) {
        return adminCourseService.getCourse(id);
    }

    @PostMapping("")
    @RequiresAuthentication
    @RequiresRoles(value = {"root", "admin", "course_admin", "teacher"}, logical = Logical.OR)
    public CommonResult<Void> addCourse(@RequestBody AdminCourseDTO adminCourseDTO) {
        return adminCourseService.addCourse(adminCourseDTO);
    }

    @PutMapping("")
    @RequiresAuthentication
    @RequiresRoles(value = {"root", "admin", "course_admin", "teacher"}, logical = Logical.OR)
    public CommonResult<Void> updateCourse(@RequestBody AdminCourseDTO adminCourseDTO) {
        return adminCourseService.updateCourse(adminCourseDTO);
    }

    @DeleteMapping("")
    @RequiresAuthentication
    @RequiresRoles(value = {"root", "admin", "course_admin", "teacher"}, logical = Logical.OR)
    public CommonResult<Void> deleteCourse(@RequestParam("id") Long id) {
        return adminCourseService.deleteCourse(id);
    }


}
