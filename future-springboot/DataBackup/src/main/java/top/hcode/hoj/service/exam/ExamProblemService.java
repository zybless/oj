package top.hcode.hoj.service.exam;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.models.auth.In;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.pojo.vo.ExamProblemVO;
import top.hcode.hoj.pojo.vo.ExamRepoProblemVO;

/**
 * @Author: Minipax
 * @Date: 2023/9/16 19:14
 * @Description:
 */
public interface ExamProblemService {
    public CommonResult<IPage<ExamProblemVO>> getProblemList(Integer limit, Integer currentPage, Integer type, Integer difficulty, String keyword, Integer auth, Long repoId);

    public CommonResult<IPage<ExamRepoProblemVO>> getProblemListInRepo(Integer limit, Integer currentPage, Integer type, Integer difficulty, String keyword, Integer auth, Long repoId);

    CommonResult<IPage<ExamRepoProblemVO>> getProblemListInRepoExam(Integer limit, Integer currentPage, Integer type, Integer difficulty, String keyword, Integer auth, Long repoId, Long tid, Long eid);

    CommonResult<IPage<ExamRepoProblemVO>> getProblemListInRepoExamNoAnswer(Integer limit, Integer currentPage, Integer type, Integer difficulty, String keyword, Integer auth, Long repoId);

}
