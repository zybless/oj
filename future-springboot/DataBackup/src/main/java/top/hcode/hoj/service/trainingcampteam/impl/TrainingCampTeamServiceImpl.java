package top.hcode.hoj.service.trainingcampteam.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hcode.hoj.common.exception.StatusForbiddenException;
import top.hcode.hoj.common.exception.StatusNotFoundException;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.exception.AccessException;
import top.hcode.hoj.manager.trainingcampteam.TrainingCampTeamManager;
import top.hcode.hoj.pojo.vo.TrainingCampTeamVO;
import top.hcode.hoj.pojo.vo.TrainingCampVO;
import top.hcode.hoj.service.trainingcampteam.TrainingCampTeamService;

/**
 * @Author: Minipax
 * @Date: 2024/6/23 18:41
 */
@Service
public class TrainingCampTeamServiceImpl implements TrainingCampTeamService {

    @Autowired
    private TrainingCampTeamManager trainingCampTeamManager;

    @Override
    public CommonResult<TrainingCampTeamVO> getTrainingCampTeam() {
        try {
            return CommonResult.successResponse(trainingCampTeamManager.getTrainingCampTeam());
        } catch (StatusForbiddenException e) {
            throw new RuntimeException(e);
        } catch (AccessException e) {
            throw new RuntimeException(e);
        } catch (StatusNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CommonResult<TrainingCampVO> getTrainingCamp(Long id) {
        return CommonResult.successResponse(trainingCampTeamManager.getTrainingCamp(id));
    }
}
