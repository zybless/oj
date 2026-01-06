package top.hcode.hoj.dao.exam.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.hcode.hoj.dao.exam.ExamRegisterEntityService;
import top.hcode.hoj.mapper.ExamRegisterMapper;
import top.hcode.hoj.pojo.entity.exam.ExamRegister;

/**
 * @Author: Minipax
 * @Date: 2024/8/30 15:06
 */
@Service
public class ExamRegisterEntityServiceImpl extends ServiceImpl<ExamRegisterMapper, ExamRegister> implements ExamRegisterEntityService {
}
