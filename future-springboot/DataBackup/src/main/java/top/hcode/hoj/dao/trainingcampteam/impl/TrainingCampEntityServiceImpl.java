package top.hcode.hoj.dao.trainingcampteam.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.hcode.hoj.dao.trainingcampteam.TrainingCampEntityService;
import top.hcode.hoj.mapper.TrainingCampMapper;
import top.hcode.hoj.pojo.entity.training.TrainingCamp;

/**
 * @Author: Minipax
 * @Date: 2024/6/23 18:10
 */
@Service
public class TrainingCampEntityServiceImpl extends ServiceImpl<TrainingCampMapper, TrainingCamp> implements TrainingCampEntityService {
}
