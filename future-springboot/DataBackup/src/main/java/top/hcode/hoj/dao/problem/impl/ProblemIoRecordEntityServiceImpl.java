package top.hcode.hoj.dao.problem.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.hcode.hoj.dao.problem.ProblemIoRecordEntityService;
import top.hcode.hoj.mapper.ProblemIoRecordMapper;
import top.hcode.hoj.pojo.entity.problem.ProblemIoRecord;

/**
 * @Author: Minipax
 * @Date: 2024/8/15 21:10
 * @Description:
 */
@Service
public class ProblemIoRecordEntityServiceImpl extends ServiceImpl<ProblemIoRecordMapper, ProblemIoRecord> implements ProblemIoRecordEntityService {
}
