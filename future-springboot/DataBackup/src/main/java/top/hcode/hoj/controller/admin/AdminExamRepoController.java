package top.hcode.hoj.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.pojo.dto.AddProblemsToRepoDTO;
import top.hcode.hoj.pojo.dto.RemoveProblemsFromRepoDTO;
import top.hcode.hoj.pojo.entity.exam.ExamRepo;
import top.hcode.hoj.pojo.entity.exam.ExamRepoProblem;
import top.hcode.hoj.pojo.vo.ExamRepoVO;
import top.hcode.hoj.service.admin.exam.AdminExamRepoService;

import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/13 12:30
 */

@RestController
@RequestMapping("/api/admin/exam-repo")
public class AdminExamRepoController {

    @Autowired
    private AdminExamRepoService adminExamRepoService;

    @GetMapping("/get-repo-list")
    @RequiresAuthentication
    @RequiresRoles(value = {"root", "admin", "problem_admin", "teacher"}, logical = Logical.OR)
    public CommonResult<IPage<ExamRepo>> getRepoList(@RequestParam(value = "limit", required = false) Integer limit,
                                                        @RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                        @RequestParam(value = "keyword", required = false) String keyword) {
        return adminExamRepoService.getRepoList(limit, currentPage, keyword);
    }

    @GetMapping("")
    @RequiresAuthentication
    @RequiresRoles(value = {"root", "admin", "problem_admin", "teacher"}, logical = Logical.OR)
    public CommonResult<ExamRepoVO> getRepo(@RequestParam("id") Long id) {
        return adminExamRepoService.getRepo(id);
    }

    @DeleteMapping("")
    @RequiresAuthentication
    @RequiresRoles(value = {"root", "teacher", "admin"}, logical = Logical.OR)
    public CommonResult<Void> deleteRepo(@RequestParam("id") Long id) {
        return adminExamRepoService.deleteRepo(id);
    }

    @PostMapping("")
    @RequiresAuthentication
    @RequiresRoles(value = {"root", "admin", "problem_admin", "teacher"}, logical = Logical.OR)
    public CommonResult<Void> addRepo(@RequestBody ExamRepo examRepo) {
        return adminExamRepoService.addRepo(examRepo);
    }

    @PutMapping("")
    @RequiresAuthentication
    @RequiresRoles(value = {"root", "admin", "problem_admin", "teacher"}, logical = Logical.OR)
    public CommonResult<Void> updateRepo(@RequestBody ExamRepo examRepo) {
        return adminExamRepoService.updateRepo(examRepo);
    }

//    @PutMapping("add-problems")
//    @RequiresAuthentication
//    @RequiresRoles(value = {"root", "admin", "problem_admin"}, logical = Logical.OR)
//    public CommonResult<Void> addProblemsToRepo(@RequestBody AddProblemsToRepoDTO addProblemsToRepoDTO) {
//        return adminExamRepoService.addProblemsToRepo(addProblemsToRepoDTO.getProblemIds(), addProblemsToRepoDTO.getRepoId(), addProblemsToRepoDTO.getGrades());
//    }
//
//    @PutMapping("remove-problems")
//    @RequiresAuthentication
//    @RequiresRoles(value = {"root", "admin", "problem_admin"}, logical = Logical.OR)
//    public CommonResult<Void> removeProblemsFromRepo(@RequestBody RemoveProblemsFromRepoDTO removeProblemsFromRepoDTO) {
//        return adminExamRepoService.removeProblemsFromRepo(removeProblemsFromRepoDTO.getProblemIds(), removeProblemsFromRepoDTO.getRepoId());
//    }

    @PutMapping("update-problems")
    @RequiresAuthentication
    @RequiresRoles(value = {"root", "admin", "problem_admin", "teacher"}, logical = Logical.OR)
    public CommonResult<Void> updateRepoProblems(@RequestBody List<ExamRepoProblem> examRepoProblemList, @RequestParam Long repoId) {
        return adminExamRepoService.updateRepoProblems(examRepoProblemList, repoId);
    }

}
