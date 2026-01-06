package top.hcode.hoj.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.pojo.dto.AdminCourseDTO;
import top.hcode.hoj.pojo.dto.TrainingCampTeamDTO;
import top.hcode.hoj.pojo.entity.training.TrainingCamp;
import top.hcode.hoj.pojo.entity.training.TrainingCampTeam;
import top.hcode.hoj.pojo.vo.AdminCourseVO;
import top.hcode.hoj.pojo.vo.AdminTrainingCampTeamVO;
import top.hcode.hoj.service.admin.course.AdminCourseService;
import top.hcode.hoj.service.admin.trainingcampteam.AdminTrainingCampTeamService;

/**
 * @Author: Minipax
 * @Date: 2024/6/23 17:41
 */

@RestController
@RequestMapping("/api/admin/training-camp-team")
public class AdminTrainingCampTeamController {

    @Autowired
    private AdminTrainingCampTeamService adminTrainingCampTeamService;

    @GetMapping("")
    @RequiresAuthentication
    @RequiresRoles(value = {"root", "teacher"}, logical = Logical.OR)
    public CommonResult<AdminTrainingCampTeamVO> getTrainingCampTeam() {
        return adminTrainingCampTeamService.getTrainingCampTeam();
    }

    @GetMapping("/training-camp")
    @RequiresAuthentication
    @RequiresRoles(value = {"root", "teacher"}, logical = Logical.OR)
    public CommonResult<TrainingCamp> getTrainingCamp(@RequestParam("id") Long id) {
        return adminTrainingCampTeamService.getTrainingCamp(id);
    }

    @PostMapping("/training-camp")
    @RequiresAuthentication
    @RequiresRoles(value = {"root", "teacher"}, logical = Logical.OR)
    public CommonResult<Void> addTrainingCamp(@RequestBody TrainingCamp trainingCamp) {
        return adminTrainingCampTeamService.addTrainingCamp(trainingCamp);
    }

    @PutMapping("")
    @RequiresAuthentication
    @RequiresRoles(value = {"root", "teacher"}, logical = Logical.OR)
    public CommonResult<Void> updateTrainingCampTeam(@RequestBody TrainingCampTeamDTO trainingCampTeamDTO) {
        return adminTrainingCampTeamService.updateTrainingCampTeam(trainingCampTeamDTO);
    }

    @PutMapping("/training-camp")
    @RequiresAuthentication
    @RequiresRoles(value = {"root", "teacher"}, logical = Logical.OR)
    public CommonResult<Void> updateTrainingCamp(@RequestBody TrainingCamp trainingCamp) {
        return adminTrainingCampTeamService.updateTrainingCamp(trainingCamp);
    }

    @DeleteMapping("/training-camp")
    @RequiresAuthentication
    @RequiresRoles(value = {"root", "teacher"}, logical = Logical.OR)
    public CommonResult<Void> deleteTrainingCamp(@RequestParam("id") Long id) {
        return adminTrainingCampTeamService.deleteTrainingCamp(id);
    }

}
