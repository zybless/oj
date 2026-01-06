package top.hcode.hoj.controller.exam;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.pojo.entity.exam.ExamHistory;
import top.hcode.hoj.pojo.vo.ExamHistoryVO;
import top.hcode.hoj.service.exam.ExamHistoryService;

import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/21 11:47
 */
@RestController
@RequestMapping("/api/exam-history")
public class ExamHistoryController {

    @Autowired
    private ExamHistoryService examHistoryService;

    @GetMapping("/exercise")
    @RequiresAuthentication
    public CommonResult<List<ExamHistory>> getExerciseHistory(@RequestParam Long repoId) {
        return examHistoryService.getExerciseHistory(repoId);
    }

    @GetMapping("/exercise-detail")
    @RequiresAuthentication
    public CommonResult<ExamHistoryVO> getExerciseHistoryDetail(@RequestParam(value = "historyId", required = false) Long historyId,
                                                                @RequestParam Long repoId) {
        return examHistoryService.getExerciseHistoryDetail(historyId, repoId);
    }


}
