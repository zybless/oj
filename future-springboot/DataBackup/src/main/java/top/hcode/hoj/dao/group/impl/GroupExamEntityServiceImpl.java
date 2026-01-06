package top.hcode.hoj.dao.group.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.hcode.hoj.dao.group.GroupExamEntityService;
import top.hcode.hoj.mapper.GroupExamMapper;
import top.hcode.hoj.pojo.entity.group.GroupExam;

/**
 * @Author: Minipax
 * @Date: 2024/8/11 10:04
 */
@Service
public class GroupExamEntityServiceImpl extends ServiceImpl<GroupExamMapper, GroupExam> implements GroupExamEntityService {
}
