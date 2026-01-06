package top.hcode.hoj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.hcode.hoj.pojo.entity.exam.Exam;
import top.hcode.hoj.pojo.vo.ExamVO;

import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2024/3/25 19:40
 */
@Mapper
@Repository
public interface ExamMapper extends BaseMapper<Exam> {

    List<ExamVO> getExamList(IPage page,
                             @Param("title") String title);

    List<ExamVO> getExamListAdmin(IPage<ExamVO> page,
                                  @Param("title") String title);

    ExamVO getExamUser(@Param("examId") Long examId);

}
