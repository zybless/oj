package top.hcode.hoj.service.admin.exam;

import com.baomidou.mybatisplus.core.metadata.IPage;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.pojo.dto.ExamProblemDTO;
import top.hcode.hoj.pojo.vo.ExamProblemVO;
import top.hcode.hoj.pojo.vo.ExamRepoProblemVO;

public interface AdminExamProblemService {
    public CommonResult<IPage<ExamProblemVO>> getProblemList(Integer limit, Integer currentPage, Integer type, Integer difficulty, String keyword, Integer auth, Long repoId);

    public CommonResult<ExamProblemVO> getProblem(Long id);

    public CommonResult<Void> deleteProblem(Long id);

    public CommonResult<Void> addProblem(ExamProblemDTO examProblemDTO);

    public CommonResult<Void> updateProblem(ExamProblemDTO examProblemDTO);

    CommonResult<IPage<ExamProblemVO>> getProblemListNotInRepo(Integer limit, Integer currentPage, Integer type, Integer difficulty, String keyword, Integer auth, Long repoId);

    CommonResult<IPage<ExamRepoProblemVO>> getProblemListInRepo(Integer limit, Integer currentPage, Integer type, Integer difficulty, String keyword, Integer auth, Long repoId);
}
