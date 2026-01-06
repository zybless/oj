package top.hcode.hoj.controller.exam;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.models.auth.In;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.hcode.hoj.annotation.AnonApi;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.pojo.entity.exam.ExamRepo;
import top.hcode.hoj.pojo.vo.ExamProblemVO;
import top.hcode.hoj.pojo.vo.ExamRepoProblemVO;
import top.hcode.hoj.pojo.vo.ExamRepoVO;
import top.hcode.hoj.service.exam.ExamProblemService;

/**
 * @Author: Minipax
 * @Date: 2023/9/16 19:12
 * @Description:
 */

@RestController
@RequestMapping("/api/exam-problem")
public class ExamProblemController {

    @Autowired
    private ExamProblemService examProblemService;

    @GetMapping("/get-problem-list")
    @AnonApi
    public CommonResult<IPage<ExamProblemVO>> getProblemList(@RequestParam(value = "limit", required = false) Integer limit,
                                                             @RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                             @RequestParam(value = "type", required = false) Integer type,
                                                             @RequestParam(value = "difficulty", required = false) Integer difficulty,
                                                             @RequestParam(value = "keyword", required = false) String keyword,
                                                             @RequestParam(value = "auth", required = false) Integer auth,
                                                             @RequestParam(value = "repoId", required = false) Long repoId) {
        return examProblemService.getProblemList(limit, currentPage, type, difficulty, keyword, auth, repoId);
    }

    @GetMapping("/get-problem-list-in-repo")
    @AnonApi
    public CommonResult<IPage<ExamRepoProblemVO>> getProblemListInRepo(@RequestParam(value = "limit", required = false) Integer limit,
                                                                       @RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                                       @RequestParam(value = "type", required = false) Integer type,
                                                                       @RequestParam(value = "difficulty", required = false) Integer difficulty,
                                                                       @RequestParam(value = "keyword", required = false) String keyword,
                                                                       @RequestParam(value = "auth", required = false) Integer auth,
                                                                       @RequestParam(value = "repoId", required = false) Long repoId) {
        return examProblemService.getProblemListInRepo(limit, currentPage, type, difficulty, keyword, auth, repoId);
    }

    @GetMapping("/get-problem-list-in-repo-exam")
    @RequiresAuthentication
    public CommonResult<IPage<ExamRepoProblemVO>> getProblemListInRepoExam(@RequestParam(value = "limit", required = false) Integer limit,
                                                                       @RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                                       @RequestParam(value = "type", required = false) Integer type,
                                                                       @RequestParam(value = "difficulty", required = false) Integer difficulty,
                                                                       @RequestParam(value = "keyword", required = false) String keyword,
                                                                       @RequestParam(value = "auth", required = false) Integer auth,
                                                                       @RequestParam(value = "repoId", required = false) Long repoId,
                                                                           @RequestParam(value = "trainingId", required = false) Long trainingId,
                                                                           @RequestParam(value = "examId", required = false)Long examId) {
        return examProblemService.getProblemListInRepoExam(limit, currentPage, type, difficulty, keyword, auth, repoId, trainingId, examId);
    }

//    @GetMapping("/get-problem-list-in-repo-exam-na")
//    @RequiresAuthentication
//    public CommonResult<IPage<ExamRepoProblemVO>> getProblemListInRepoExamNoAnswer(@RequestParam(value = "limit", required = false) Integer limit,
//                                                                           @RequestParam(value = "currentPage", required = false) Integer currentPage,
//                                                                           @RequestParam(value = "type", required = false) Integer type,
//                                                                           @RequestParam(value = "difficulty", required = false) Integer difficulty,
//                                                                           @RequestParam(value = "keyword", required = false) String keyword,
//                                                                           @RequestParam(value = "auth", required = false) Integer auth,
//                                                                           @RequestParam(value = "repoId", required = false) Long repoId) {
//        return examProblemService.getProblemListInRepoExamNoAnswer(limit, currentPage, type, difficulty, keyword, auth, repoId);
//    }

}
