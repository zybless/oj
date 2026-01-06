package top.hcode.hoj.dao.exam;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import top.hcode.hoj.pojo.dto.ExamProblemDTO;
import top.hcode.hoj.pojo.entity.exam.ExamProblem;
import top.hcode.hoj.pojo.vo.ExamProblemVO;
import top.hcode.hoj.pojo.vo.ExamRepoProblemVO;

import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/4 14:52
 * @Description:
 */
public interface ExamProblemEntityService extends IService<ExamProblem> {

    Page<ExamProblemVO> getProblemList(Integer limit, Integer currentPage, Integer type, Integer difficulty, String keyword, Integer auth, Long repoId);

    boolean adminAddExamProblem(ExamProblemDTO examProblemDTO);

    boolean adminUpdateExamProblem(ExamProblemDTO examProblemDTO);


    IPage<ExamProblemVO> getProblemListAdmin(Integer limit, Integer currentPage, Integer type, Integer difficulty, String keyword, Integer auth, Long repoId);

    IPage<ExamProblemVO> getProblemListNotInRepo(Integer limit, Integer currentPage, Integer type, Integer difficulty, String keyword, Integer auth, Long repoId);

    IPage<ExamRepoProblemVO> getProblemListInRepo(Integer limit, Integer currentPage, Integer type, Integer difficulty, String keyword, Integer auth, Long repoId);

    IPage<ExamRepoProblemVO> getProblemListInRepoExam(Integer limit, Integer currentPage, Integer type, Integer difficulty, String keyword, Integer auth, Long repoId);

    IPage<ExamRepoProblemVO> getProblemListInRepoExamNoAnswer(Integer limit, Integer currentPage, Integer type, Integer difficulty, String keyword, Integer auth, Long repoId);


}
