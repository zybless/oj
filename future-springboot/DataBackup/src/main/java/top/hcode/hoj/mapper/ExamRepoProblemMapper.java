package top.hcode.hoj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.hcode.hoj.pojo.entity.exam.ExamRepoProblem;

import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/9 23:33
 */
@Mapper
@Repository
public interface ExamRepoProblemMapper extends BaseMapper<ExamRepoProblem> {

    public List<Long> getProblemRepoIds(Long problemId);

    public List<Long> getRepoProblemIds(Long repoId);

    Long getIdByRepoProblemId(@Param("repoId") Long repoId,
                              @Param("problemId") Long problemId);

    List<Long> getIdByRepoId(Long repoId);

    List<ExamRepoProblem> getByRepoId(Long repoId);

}
