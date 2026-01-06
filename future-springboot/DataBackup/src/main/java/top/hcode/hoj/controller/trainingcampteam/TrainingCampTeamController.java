package top.hcode.hoj.controller.trainingcampteam;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.pojo.vo.TrainingCampTeamVO;
import top.hcode.hoj.pojo.vo.TrainingCampVO;
import top.hcode.hoj.service.trainingcampteam.TrainingCampTeamService;

/**
 * @Author: Minipax
 * @Date: 2024/6/23 18:39
 */
@RestController
@RequestMapping("/api/training-camp-team")
public class TrainingCampTeamController {

    @Autowired
    private TrainingCampTeamService trainingCampTeamService;

    @GetMapping("")
    public CommonResult<TrainingCampTeamVO> getTrainingCampTeam() {
        return trainingCampTeamService.getTrainingCampTeam();
    }

    @GetMapping("/training-camp")
    public CommonResult<TrainingCampVO> getTrainingCamp(@RequestParam("id") Long id) {
        return trainingCampTeamService.getTrainingCamp(id);
    }

}
