package top.hcode.hoj.dao.training.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hcode.hoj.dao.training.TrainingExamRecordEntityService;
import top.hcode.hoj.mapper.TrainingExamRecordMapper;
import top.hcode.hoj.pojo.entity.training.TrainingExamRecord;
import top.hcode.hoj.pojo.vo.TrainingExamRecordVO;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2024/8/15 11:04
 * @Description:
 */
@Service
public class TrainingExamRecordEntityServiceImpl extends ServiceImpl<TrainingExamRecordMapper, TrainingExamRecord> implements TrainingExamRecordEntityService {

    @Autowired
    private TrainingExamRecordMapper trainingExamRecordMapper;

    @Override
    public List<TrainingExamRecordVO> getTrainingRecord(Long tid){
        return trainingExamRecordMapper.getTrainingRecord(tid);
    }

}
