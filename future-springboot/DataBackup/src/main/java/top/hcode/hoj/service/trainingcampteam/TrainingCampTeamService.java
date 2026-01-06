package top.hcode.hoj.service.trainingcampteam;

import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.pojo.vo.TrainingCampTeamVO;
import top.hcode.hoj.pojo.vo.TrainingCampVO;

/**
 * @Author: Minipax
 * @Date: 2024/6/23 18:41
 */
public interface TrainingCampTeamService {

    CommonResult<TrainingCampTeamVO> getTrainingCampTeam();

    CommonResult<TrainingCampVO> getTrainingCamp(Long id);
}
