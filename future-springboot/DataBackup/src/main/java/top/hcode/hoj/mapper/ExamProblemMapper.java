package top.hcode.hoj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.hcode.hoj.pojo.entity.exam.ExamProblem;
import top.hcode.hoj.pojo.entity.problem.Problem;
import top.hcode.hoj.pojo.vo.ExamProblemVO;
import top.hcode.hoj.pojo.vo.ExamRepoProblemVO;
import top.hcode.hoj.pojo.vo.ProblemVO;

import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/4 16:42
 * @Description:
 */
@Mapper
@Repository
public interface ExamProblemMapper extends BaseMapper<ExamProblem> {
    List<ExamProblemVO> getProblemList(IPage page,
                                       @Param("keyword") String keyword,
                                       @Param("type") Integer type,
                                       @Param("difficulty") Integer difficulty,
                                       @Param("auth") Integer auth,
                                       @Param("repoId") Long repoId);

    List<ExamProblemVO> getProblemListAdmin(IPage page,
                                            @Param("keyword") String keyword,
                                            @Param("type") Integer type,
                                            @Param("difficulty") Integer difficulty,
                                            @Param("auth") Integer auth,
                                            @Param("repoId") Long repoId);

    List<ExamProblemVO> getProblemListNotInRepo(IPage page,
                                                @Param("keyword") String keyword,
                                                @Param("type") Integer type,
                                                @Param("difficulty") Integer difficulty,
                                                @Param("auth") Integer auth,
                                                @Param("repoId") Long repoId);

    List<ExamRepoProblemVO> getProblemListInRepo(IPage page,
                                                 @Param("keyword") String keyword,
                                                 @Param("type") Integer type,
                                                 @Param("difficulty") Integer difficulty,
                                                 @Param("auth") Integer auth,
                                                 @Param("repoId") Long repoId);

    List<ExamRepoProblemVO> getProblemsByRepoId(Long repoId);
}