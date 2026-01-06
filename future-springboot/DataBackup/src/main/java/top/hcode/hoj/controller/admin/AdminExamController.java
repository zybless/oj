package top.hcode.hoj.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.pojo.dto.ExamRankDTO;
import top.hcode.hoj.pojo.entity.exam.Exam;
import top.hcode.hoj.pojo.entity.exam.ExamRepo;
import top.hcode.hoj.pojo.vo.*;
import top.hcode.hoj.service.admin.exam.AdminExamService;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2024/3/25 14:06
 */
@RestController
@RequestMapping("/api/admin/exam")
public class AdminExamController {

    @Autowired
    private AdminExamService adminExamService;

    @GetMapping("/get-exam-list")
    @RequiresAuthentication
    @RequiresRoles(value = {"root", "admin", "teacher"}, logical = Logical.OR)
    public CommonResult<IPage<ExamVO>> getExamList(@RequestParam(value = "limit", required = false) Integer limit,
                                                   @RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                   @RequestParam(value = "title", required = false) String title,
                                                   @RequestParam(value = "startTime", required = false) Date startTime,
                                                   @RequestParam(value = "endTime", required = false) Date endTime) {
        return adminExamService.getExamList(limit, currentPage, title, startTime, endTime);
    }

    @GetMapping("/get-exam-history")
    @RequiresAuthentication
    @RequiresRoles(value = {"root", "admin", "teacher"}, logical = Logical.OR)
    public CommonResult<List<ExamHistoryVO>> getExamHistory(@RequestParam Long id) {
        return adminExamService.getExamHistory(id);
    }

    @GetMapping("/get-exam-history-detail")
    @RequiresAuthentication
    @RequiresRoles(value = {"root", "admin", "teacher"}, logical = Logical.OR)
    public CommonResult<ExamHistoryVO> getExamHistoryDetail(@RequestParam Long historyId) {
        return adminExamService.getExamHistoryDetail(historyId);
    }

    @GetMapping("/get-exam-rank")
    @RequiresAuthentication
    @RequiresRoles(value = {"root", "admin", "teacher"}, logical = Logical.OR)
    public CommonResult<IPage<ExamRankVO>> getExamRank(ExamRankDTO examRankDTO) {
        return adminExamService.getExamRank(examRankDTO);
    }

    @GetMapping("")
    @RequiresAuthentication
    @RequiresRoles(value = {"root", "admin", "teacher"}, logical = Logical.OR)
    public CommonResult<ExamVO> getRepo(@RequestParam("id") Long id) {
        return adminExamService.getExam(id);
    }

    @DeleteMapping("")
    @RequiresAuthentication
    @RequiresRoles(value = {"root", "teacher", "admin"}, logical = Logical.OR)
    public CommonResult<Void> deleteExam(@RequestParam("id") Long id) {
        return adminExamService.deleteExam(id);
    }

    @PostMapping("")
    @RequiresAuthentication
    @RequiresRoles(value = {"root", "admin", "teacher"}, logical = Logical.OR)
    public CommonResult<Void> addExam(@RequestBody Exam exam) {
        return adminExamService.addExam(exam);
    }

    @PutMapping("")
    @RequiresAuthentication
    @RequiresRoles(value = {"root", "admin", "teacher"}, logical = Logical.OR)
    public CommonResult<Void> updateExam(@RequestBody Exam Exam) {
        return adminExamService.updateExam(Exam);
    }

    @GetMapping("/export-history")
    @RequiresAuthentication
    @RequiresRoles(value = {"root", "admin", "teacher"}, logical = Logical.OR)
    public CommonResult<Void> exportHistory(@RequestParam("eid") Long eid, HttpServletResponse response) {
        return adminExamService.exportHistory(eid, response);
    }


}
