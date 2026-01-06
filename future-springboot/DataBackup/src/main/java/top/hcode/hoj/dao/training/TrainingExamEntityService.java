package top.hcode.hoj.dao.training;

import com.baomidou.mybatisplus.extension.service.IService;
import top.hcode.hoj.pojo.entity.training.TrainingExam;
import top.hcode.hoj.pojo.entity.training.TrainingProblem;

import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2024/7/15 14:07
 * @Description:
 */


public interface TrainingExamEntityService extends IService<TrainingExam> {


    List<Long> getTrainingExamIdList(Long id);
}
