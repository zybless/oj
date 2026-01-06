package top.hcode.hoj.controller.exam;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.pojo.dto.ExamSubmitDTO;
import top.hcode.hoj.pojo.vo.ExamHistoryVO;
import top.hcode.hoj.service.exam.ExamJudgeService;

/**
 * @Author: Minipax
 * @Date: 2023/9/20 16:55
 * @Description:
 */
@RestController
@RequestMapping("/api/exam-judge")
public class ExamJudgeController {

    @Autowired
    private ExamJudgeService examJudgeService;

    @GetMapping("/start_exercise")
    @RequiresAuthentication
    public CommonResult<Long> startExercise(@RequestParam Long repoId,
                                            @RequestParam Long trainingId) {
        return examJudgeService.startExercise(repoId, trainingId);
    }

    @PostMapping("/submit_exercise")
    @RequiresAuthentication
    public CommonResult<ExamHistoryVO> submitExercise(@RequestBody ExamSubmitDTO examSubmitDTO) {
        return examJudgeService.submitExercise(examSubmitDTO);
    }

    @GetMapping("/start_exam")
    @RequiresAuthentication
    public CommonResult<Long> startExam(@RequestParam Long examId,
                                        @RequestParam String password) {
        return examJudgeService.startExam(examId, password);
    }

    @PostMapping("/submit_exam")
    @RequiresAuthentication
    public CommonResult<ExamHistoryVO> submitExam(@RequestBody ExamSubmitDTO examSubmitDTO) {
        return examJudgeService.submitExam(examSubmitDTO);
    }

}
