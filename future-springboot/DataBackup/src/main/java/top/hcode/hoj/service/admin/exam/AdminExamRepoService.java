package top.hcode.hoj.service.admin.exam;

import com.baomidou.mybatisplus.core.metadata.IPage;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.pojo.entity.exam.ExamRepo;
import top.hcode.hoj.pojo.entity.exam.ExamRepoProblem;
import top.hcode.hoj.pojo.vo.ExamRepoVO;

import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/9 23:30
 */
public interface AdminExamRepoService {

    public CommonResult<IPage<ExamRepo>> getRepoList(Integer limit, Integer currentPage, String keyword);

    CommonResult<ExamRepoVO> getRepo(Long id);

    CommonResult<Void> deleteRepo(Long id);

    CommonResult<Void> addRepo(ExamRepo examRepo);

    CommonResult<Void> updateRepo(ExamRepo examRepo);

    CommonResult<Void> addProblemsToRepo(List<Long> problemIds, Long repoId, List<Float> grades);

    CommonResult<Void> removeProblemsFromRepo(List<Long> problemIds, Long repoId);

    CommonResult<Void> updateRepoProblems(List<ExamRepoProblem> examRepoProblemList, Long repoId);
}
