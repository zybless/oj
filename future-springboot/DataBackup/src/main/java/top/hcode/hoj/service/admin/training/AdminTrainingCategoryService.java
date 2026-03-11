package top.hcode.hoj.service.admin.training;

import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.pojo.dto.trainingCategory.CategoryRankDTO;
import top.hcode.hoj.pojo.entity.training.TrainingCategory;

import java.util.List;

/**
 * @Author: Himit_ZH
 * @Date: 2022/3/9 19:38
 * @Description:
 */
public interface AdminTrainingCategoryService {

    public CommonResult<TrainingCategory> addTrainingCategory(TrainingCategory trainingCategory);

    public CommonResult<Void> updateTrainingCategory(TrainingCategory trainingCategory);

    public CommonResult<Void> deleteTrainingCategory(Long cid);

    public CommonResult<Void> updateCategoryRank(CategoryRankDTO rankDTO);
}