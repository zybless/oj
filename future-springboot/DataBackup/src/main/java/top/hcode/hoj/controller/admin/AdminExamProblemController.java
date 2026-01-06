package top.hcode.hoj.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.pojo.dto.ExamProblemDTO;
import top.hcode.hoj.pojo.vo.ExamProblemVO;
import top.hcode.hoj.pojo.vo.ExamRepoProblemVO;
import top.hcode.hoj.service.admin.exam.AdminExamProblemService;

@RestController
@RequestMapping("/api/admin/exam-problem")
public class AdminExamProblemController {

    @Autowired
    private AdminExamProblemService adminExamProblemService;

    @GetMapping("/get-problem-list")
    @RequiresAuthentication
    @RequiresRoles(value = {"root", "admin", "teacher"}, logical = Logical.OR)
    public CommonResult<IPage<ExamProblemVO>> getProblemList(@RequestParam(value = "limit", required = false) Integer limit,
                                                           @RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                           @RequestParam(value = "type", required = false) Integer type,
                                                           @RequestParam(value = "difficulty", required = false) Integer difficulty,
                                                           @RequestParam(value = "keyword", required = false) String keyword,
                                                           @RequestParam(value = "auth", required = false) Integer auth,
                                                           @RequestParam(value = "repoId", required = false) Long repoId) {
        return adminExamProblemService.getProblemList(limit, currentPage, type, difficulty, keyword, auth, repoId);
    }

    @GetMapping("")
    @RequiresAuthentication
    @RequiresRoles(value = {"root", "admin", "teacher"}, logical = Logical.OR)
    public CommonResult<ExamProblemVO> getProblem(@RequestParam("id") Long id) {
        return adminExamProblemService.getProblem(id);
    }

    @DeleteMapping("")
    @RequiresAuthentication
    @RequiresRoles(value = {"root", "teacher", "admin"}, logical = Logical.OR)
    public CommonResult<Void> deleteProblem(@RequestParam("id") Long id) {
        return adminExamProblemService.deleteProblem(id);
    }

    @PostMapping("")
    @RequiresAuthentication
    @RequiresRoles(value = {"root", "admin", "teacher"}, logical = Logical.OR)
    public CommonResult<Void> addProblem(@RequestBody ExamProblemDTO examProblemDTO) {
        return adminExamProblemService.addProblem(examProblemDTO);
    }

    @PutMapping("")
    @RequiresAuthentication
    @RequiresRoles(value = {"root", "admin", "teacher"}, logical = Logical.OR)
    public CommonResult<Void> updateProblem(@RequestBody ExamProblemDTO examProblemDTO) {
        return adminExamProblemService.updateProblem(examProblemDTO);
    }

    @GetMapping("/get-problem-list-not-in-repo")
    @RequiresAuthentication
    @RequiresRoles(value = {"root", "admin", "exam_problem_admin", "teacher"}, logical = Logical.OR)
    public CommonResult<IPage<ExamProblemVO>> getProblemListNotInRepo(@RequestParam(value = "limit", required = false) Integer limit,
                                                             @RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                             @RequestParam(value = "type", required = false) Integer type,
                                                             @RequestParam(value = "difficulty", required = false) Integer difficulty,
                                                             @RequestParam(value = "keyword", required = false) String keyword,
                                                             @RequestParam(value = "auth", required = false) Integer auth,
                                                             @RequestParam(value = "repoId", required = false) Long repoId) {
        return adminExamProblemService.getProblemListNotInRepo(limit, currentPage, type, difficulty, keyword, auth, repoId);
    }

    @GetMapping("/get-problem-list-in-repo")
    @RequiresAuthentication
    @RequiresRoles(value = {"root", "admin", "exam_problem_admin", "teacher"}, logical = Logical.OR)
    public CommonResult<IPage<ExamRepoProblemVO>> getProblemListInRepo(@RequestParam(value = "limit", required = false) Integer limit,
                                                                       @RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                                       @RequestParam(value = "type", required = false) Integer type,
                                                                       @RequestParam(value = "difficulty", required = false) Integer difficulty,
                                                                       @RequestParam(value = "keyword", required = false) String keyword,
                                                                       @RequestParam(value = "auth", required = false) Integer auth,
                                                                       @RequestParam(value = "repoId", required = false) Long repoId) {
        return adminExamProblemService.getProblemListInRepo(limit, currentPage, type, difficulty, keyword, auth, repoId);
    }

}
