package top.hcode.hoj.dao.training;

import com.baomidou.mybatisplus.extension.service.IService;
import top.hcode.hoj.pojo.entity.training.TrainingExamRecord;
import top.hcode.hoj.pojo.vo.TrainingExamRecordVO;

import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2024/8/15 11:02
 * @Description:
 */
public interface TrainingExamRecordEntityService extends IService<TrainingExamRecord> {

    public List<TrainingExamRecordVO> getTrainingRecord(Long tid);

}
