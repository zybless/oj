package top.hcode.hoj.manager.admin.training;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;
import top.hcode.hoj.common.exception.StatusFailException;
import top.hcode.hoj.dao.training.TrainingCategoryEntityService;
import top.hcode.hoj.pojo.dto.trainingCategory.CategoryRankDTO;
import top.hcode.hoj.pojo.entity.training.TrainingCategory;
import top.hcode.hoj.shiro.AccountProfile;
import top.hcode.hoj.utils.LexoRankUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: Himit_ZH
 * @Date: 2022/3/9 19:29
 * @Description:
 */

@Component
@Slf4j(topic = "hoj")
public class AdminTrainingCategoryManager {

    @Resource
    private TrainingCategoryEntityService trainingCategoryEntityService;

    public TrainingCategory addTrainingCategory(TrainingCategory trainingCategory) throws StatusFailException {
        QueryWrapper<TrainingCategory> trainingCategoryQueryWrapper = new QueryWrapper<>();
        trainingCategoryQueryWrapper.eq(trainingCategory.getGid() != null, "gid", trainingCategory.getGid())
                .eq("name", trainingCategory.getName());
        TrainingCategory existedTrainingCategory = trainingCategoryEntityService.getOne(trainingCategoryQueryWrapper, false);
        if (existedTrainingCategory != null) {
            throw new StatusFailException("该分类名称已存在！请勿重复添加！");
        }

        // 查同组最后一个 lex_rank
        QueryWrapper<TrainingCategory> lastWrapper = new QueryWrapper<>();
        lastWrapper.eq(trainingCategory.getGid() != null, "gid", trainingCategory.getGid())
                .isNull(trainingCategory.getGid() == null, "gid")
                .orderByDesc("lex_rank")
                .last("LIMIT 1");
        TrainingCategory lastCategory = trainingCategoryEntityService.getOne(lastWrapper, false);

        // 生成新 lex_rank，排在最后
        String newLexRank = lastCategory != null
                ? LexoRankUtils.after(lastCategory.getLexRank())
                : "naaaa";
        trainingCategory.setLexRank(newLexRank);

        boolean isOk = trainingCategoryEntityService.save(trainingCategory);
        if (!isOk) {
            throw new StatusFailException("添加失败");
        }
        return trainingCategory;
    }

    public void updateTrainingCategory(TrainingCategory trainingCategory) throws StatusFailException {
        boolean isOk = trainingCategoryEntityService.updateById(trainingCategory);
        if (!isOk) {
            throw new StatusFailException("更新失败！");
        }
    }

    public void deleteTrainingCategory(Long cid) throws StatusFailException {
        boolean isOk = trainingCategoryEntityService.removeById(cid);
        if (!isOk) {
            throw new StatusFailException("删除失败！");
        }
        // 获取当前登录的用户
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();
        log.info("[{}],[{}],categoryId:[{}],operatorUid:[{}],operatorUsername:[{}]",
                "Admin_Training", "Delete_Category", cid, userRolesVo.getUid(), userRolesVo.getUsername());
    }

    public void updateCategoryRank(CategoryRankDTO rankDTO) throws StatusFailException {
        // 查拖拽项
        TrainingCategory dragItem = trainingCategoryEntityService.getById(rankDTO.getDragId());
        if (dragItem == null) {
            throw new StatusFailException("分类不存在");
        }

        // 查前后邻居的 rank 值
        String prevRank = null;
        String nextRank = null;

        if (rankDTO.getPrevId() != null) {
            TrainingCategory prev = trainingCategoryEntityService.getById(rankDTO.getPrevId());
            if (prev == null) throw new StatusFailException("前置分类不存在");
            prevRank = prev.getLexRank();
        }

        if (rankDTO.getNextId() != null) {
            TrainingCategory next = trainingCategoryEntityService.getById(rankDTO.getNextId());
            if (next == null) throw new StatusFailException("后置分类不存在");
            nextRank = next.getLexRank();
        }

        // 计算新 rank
        String newRank = LexoRankUtils.between(prevRank, nextRank);

        // 只更新一条记录
        boolean isOk = trainingCategoryEntityService.update(
                new LambdaUpdateWrapper<TrainingCategory>()
                        .eq(TrainingCategory::getId, rankDTO.getDragId())
                        .set(TrainingCategory::getLexRank, newRank)
        );

        if (!isOk) {
            throw new StatusFailException("排序更新失败");
        }
    }


}