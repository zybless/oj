package top.hcode.hoj.dao.trainingcampteam.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.hcode.hoj.dao.trainingcampteam.TrainingCampTeamEntityService;
import top.hcode.hoj.mapper.CourseMapper;
import top.hcode.hoj.mapper.TrainingCampTeamMapper;
import top.hcode.hoj.pojo.entity.course.Course;
import top.hcode.hoj.pojo.entity.training.TrainingCampTeam;

/**
 * @Author: Minipax
 * @Date: 2024/6/23 18:09
 */
@Service
public class TrainingCampTeamEntityServiceImpl extends ServiceImpl<TrainingCampTeamMapper, TrainingCampTeam> implements TrainingCampTeamEntityService {
}
