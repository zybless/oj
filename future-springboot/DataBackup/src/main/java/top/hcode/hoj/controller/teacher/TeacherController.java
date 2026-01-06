package top.hcode.hoj.controller.teacher;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.pojo.dto.HomeworkAssignmentDTO;
import top.hcode.hoj.service.teacher.TeacherService;


/**
 * @Author: Minipax
 * @Date: 2023/9/22 11:16
 */
@RestController
@RequestMapping("/api/teacher")
public class TeacherController {


    @Autowired
    private TeacherService teacherService;

    @GetMapping("/exercise")
    @RequiresAuthentication
    public CommonResult<Void> assignHomework(@RequestBody HomeworkAssignmentDTO homeworkAssignmentDTO) {
        return teacherService.assignHomework(homeworkAssignmentDTO);
    }

}
