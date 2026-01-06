package top.hcode.hoj.dao.exam.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hcode.hoj.dao.exam.ExamHistoryDetailEntityService;
import top.hcode.hoj.mapper.ExamHistoryDetailMapper;
import top.hcode.hoj.pojo.entity.exam.ExamHistoryDetail;

import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/20 19:28
 */
@Service
public class ExamHistoryDetailEntityServiceImpl extends ServiceImpl<ExamHistoryDetailMapper, ExamHistoryDetail> implements ExamHistoryDetailEntityService {

    @Autowired
    ExamHistoryDetailMapper examHistoryDetailMapper;

    @Override
    public List<ExamHistoryDetail> getHistoryDetails(Long historyId) {
        return examHistoryDetailMapper.getHistoryDetails(historyId);
    }
}
