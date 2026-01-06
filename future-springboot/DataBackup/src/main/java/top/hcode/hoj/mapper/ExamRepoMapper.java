package top.hcode.hoj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.hcode.hoj.pojo.entity.exam.ExamProblem;
import top.hcode.hoj.pojo.entity.exam.ExamRepo;

import java.util.List;
import java.util.Map;

/**
 * @Author: Minipax
 * @Date: 2023/9/9 22:49
 */
@Mapper
@Repository
public interface ExamRepoMapper extends BaseMapper<ExamRepo> {

    boolean addProblemToRepoBatch(List<Long> repoIds);

    boolean removeProblemFromRepoBatch(List<Long> repoIds);

    boolean addProblemsToRepoBatch(@Param("repoId") Long repoId,
                                   @Param("size") Integer size);

    boolean removeProblemsFromRepoBatch(@Param("repoId") Long repoId,
                                        @Param("size") Integer size);

    List<ExamRepo> getRepoList(IPage page,
                               @Param("keyword") String keyword,
                               @Param("auth") Integer auth);

}
