package top.hcode.hoj.controller.group;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.pojo.entity.problem.Problem;
import top.hcode.hoj.pojo.vo.ExamRepoVO;
import top.hcode.hoj.service.group.exam.GroupExamService;

/**
 * @Author: Minipax
 * @Date: 2024/8/10 21:27
 */
@RestController
@RequiresAuthentication
@RequestMapping("/api/group")
public class GroupExamController {

    @Autowired
    private GroupExamService groupExamService;

//    @GetMapping("/get-exam-list")
//    public CommonResult<IPage<ExamRepoVO>> getExamList(@RequestParam(value = "limit", required = false) Integer limit,
//                                                       @RequestParam(value = "currentPage", required = false) Integer currentPage,
//                                                       @RequestParam(value = "gid", required = true) Long gid) {
//        return groupExamService.getExamList(limit, currentPage, gid);
//    }

//    @GetMapping("/get-admin-exam-list")
//    public CommonResult<IPage<Problem>> getAdminExamList(@RequestParam(value = "limit", required = false) Integer limit,
//                                                            @RequestParam(value = "currentPage", required = false) Integer currentPage,
//                                                            @RequestParam(value = "gid", required = true) Long gid) {
//        return groupExamService.getAdminExamList(limit, currentPage, gid);
//    }

    @GetMapping("/add-exam")
    @RequiresRoles(value = {"root", "admin", "teacher"}, logical = Logical.OR)
    public CommonResult<Void> addExam(@RequestParam(value = "gid", required = true) Long gid,
                                      @RequestParam(value = "eid", required = true) Long eid) {
        return groupExamService.addExam(gid, eid);
    }

    @GetMapping("/remove-exam")
    @RequiresRoles(value = {"root", "admin", "teacher"}, logical = Logical.OR)
    public CommonResult<Void> removeExam(@RequestParam(value = "gid", required = true) Long gid,
                                         @RequestParam(value = "eid", required = true) Long eid) {
        return groupExamService.removeExam(gid, eid);
    }

}
