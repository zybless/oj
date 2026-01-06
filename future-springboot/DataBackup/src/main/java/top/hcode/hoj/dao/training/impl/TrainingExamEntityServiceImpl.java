package top.hcode.hoj.dao.training.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hcode.hoj.dao.training.TrainingExamEntityService;
import top.hcode.hoj.mapper.TrainingExamMapper;
import top.hcode.hoj.pojo.entity.training.TrainingExam;

import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2024/7/15 14:09
 * @Description:
 */
@Service
public class TrainingExamEntityServiceImpl extends ServiceImpl<TrainingExamMapper, TrainingExam> implements TrainingExamEntityService {

    @Autowired
    private TrainingExamMapper trainingExamMapper;

    @Override
    public List<Long> getTrainingExamIdList(Long tid) {
        return trainingExamMapper.getTrainingExamIdList(tid);
    }

}
