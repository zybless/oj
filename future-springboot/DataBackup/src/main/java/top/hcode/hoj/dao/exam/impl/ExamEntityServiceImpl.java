package top.hcode.hoj.dao.exam.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hcode.hoj.dao.exam.ExamEntityService;
import top.hcode.hoj.mapper.ExamHistoryMapper;
import top.hcode.hoj.mapper.ExamMapper;
import top.hcode.hoj.pojo.entity.exam.Exam;
import top.hcode.hoj.pojo.vo.ExamHistoryVO;
import top.hcode.hoj.pojo.vo.ExamProblemVO;
import top.hcode.hoj.pojo.vo.ExamVO;

import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2024/3/25 19:43
 */
@Service
public class ExamEntityServiceImpl extends ServiceImpl<ExamMapper, Exam> implements ExamEntityService {

    @Autowired
    private ExamMapper examMapper;


    @Override
    public IPage<ExamVO> getExamListAdmin(Integer limit, Integer currentPage, String title) {
        Page<ExamVO> page = new Page<>(currentPage, limit);

        List<ExamVO> examList = examMapper.getExamListAdmin(page, title);

        return page.setRecords(examList);
    }

    @Override
    public IPage<ExamVO> getExamList(Integer limit, Integer currentPage, String title) {
        Page<ExamVO> page = new Page<>(currentPage, limit);

        List<ExamVO> examList = examMapper.getExamList(page, title);

        return page.setRecords(examList);
    }

    @Override
    public ExamVO getExamUser(Long examId) {
        return examMapper.getExamUser(examId);
    }
}
