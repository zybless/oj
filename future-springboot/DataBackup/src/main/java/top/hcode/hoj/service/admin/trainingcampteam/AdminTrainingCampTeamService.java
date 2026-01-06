package top.hcode.hoj.service.admin.trainingcampteam;

import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.pojo.dto.TrainingCampTeamDTO;
import top.hcode.hoj.pojo.entity.training.TrainingCamp;
import top.hcode.hoj.pojo.entity.training.TrainingCampTeam;
import top.hcode.hoj.pojo.vo.AdminTrainingCampTeamVO;

/**
 * @Author: Minipax
 * @Date: 2024/6/23 17:50
 */
public interface AdminTrainingCampTeamService {


    CommonResult<AdminTrainingCampTeamVO> getTrainingCampTeam();

    CommonResult<TrainingCamp> getTrainingCamp(Long id);

    CommonResult<Void> addTrainingCamp(TrainingCamp trainingCamp);

    CommonResult<Void> updateTrainingCampTeam(TrainingCampTeamDTO trainingCampTeamDTO);

    CommonResult<Void> updateTrainingCamp(TrainingCamp trainingCamp);

    CommonResult<Void> deleteTrainingCamp(Long id);
}
