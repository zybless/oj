package top.hcode.hoj.service.admin.trainingcampteam.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;
import top.hcode.hoj.common.exception.StatusFailException;
import top.hcode.hoj.common.exception.StatusForbiddenException;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.common.result.ResultStatus;
import top.hcode.hoj.manager.admin.trainingcampteam.AdminTrainingCampTeamManager;
import top.hcode.hoj.pojo.dto.TrainingCampTeamDTO;
import top.hcode.hoj.pojo.entity.training.TrainingCamp;
import top.hcode.hoj.pojo.entity.training.TrainingCampTeam;
import top.hcode.hoj.pojo.vo.AdminTrainingCampTeamVO;
import top.hcode.hoj.service.admin.trainingcampteam.AdminTrainingCampTeamService;

/**
 * @Author: Minipax
 * @Date: 2024/6/23 17:51
 */
@Service
public class AdminTrainingCampTeamServiceImpl implements AdminTrainingCampTeamService {

    @Autowired
    private AdminTrainingCampTeamManager adminTrainingCampTeamManager;

    @Override
    public CommonResult<AdminTrainingCampTeamVO> getTrainingCampTeam() {
        try {
            return CommonResult.successResponse(adminTrainingCampTeamManager.getTrainingCampTeam());
        } catch (StatusForbiddenException e) {
            return CommonResult.errorResponse(e.getMessage());
        }
    }

    @Override
    public CommonResult<TrainingCamp> getTrainingCamp(Long id) {
        try {
            return CommonResult.successResponse(adminTrainingCampTeamManager.getTrainingCamp(id));
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusForbiddenException e) {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<Void> addTrainingCamp(TrainingCamp trainingCamp) {
        try {
            adminTrainingCampTeamManager.addTrainingCamp(trainingCamp);
            return CommonResult.successResponse();
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusForbiddenException e) {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<Void> updateTrainingCampTeam(TrainingCampTeamDTO trainingCampTeamDTO) {
        try {
            adminTrainingCampTeamManager.updateTrainingCampTeam(trainingCampTeamDTO);
            return CommonResult.successResponse();
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusForbiddenException e) {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<Void> updateTrainingCamp(TrainingCamp trainingCamp) {
        try {
            adminTrainingCampTeamManager.updateTrainingCamp(trainingCamp);
            return CommonResult.successResponse();
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusForbiddenException e) {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<Void> deleteTrainingCamp(Long id) {
        try {
            adminTrainingCampTeamManager.deleteTrainingCamp(id);
            return CommonResult.successResponse();
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusForbiddenException e) {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }
}
