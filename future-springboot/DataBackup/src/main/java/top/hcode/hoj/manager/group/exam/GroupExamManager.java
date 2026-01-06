package top.hcode.hoj.manager.group.exam;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.hcode.hoj.common.exception.StatusFailException;
import top.hcode.hoj.dao.group.GroupExamEntityService;
import top.hcode.hoj.dao.training.TrainingExamEntityService;
import top.hcode.hoj.pojo.entity.group.GroupExam;
import top.hcode.hoj.pojo.entity.training.TrainingExam;

/**
 * @Author: Minipax
 * @Date: 2024/8/11 9:52
 */
@Component
public class GroupExamManager {

    @Autowired
    private GroupExamEntityService groupExamEntityService;

    @Autowired
    private TrainingExamEntityService trainingExamEntityService;

    public void addExam(Long gid, Long eid) throws StatusFailException {
        QueryWrapper<TrainingExam> trainingExamQueryWrapper = new QueryWrapper<>();
        trainingExamQueryWrapper.eq("gid", gid).eq("eid", eid);
        TrainingExam trainingExam = trainingExamEntityService.getOne(trainingExamQueryWrapper);

        if (trainingExam != null) {
            throw new StatusFailException("改考试已在当前班级中");
        }



    }

    public void removeExam(Long gid, Long eid) {

    }
}
