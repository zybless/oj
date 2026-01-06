package top.hcode.hoj.dao.exam;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import top.hcode.hoj.pojo.entity.exam.Exam;
import top.hcode.hoj.pojo.vo.ExamVO;

/**
 * @Author: Minipax
 * @Date: 2024/3/25 19:43
 */
public interface ExamEntityService extends IService<Exam> {

    IPage<ExamVO> getExamListAdmin(Integer limit, Integer currentPage, String title);

    IPage<ExamVO> getExamList(Integer limit, Integer currentPage, String title);

    ExamVO getExamUser(Long examId);
}
