package top.hcode.hoj.controller.exam;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.hcode.hoj.annotation.AnonApi;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.pojo.dto.ExamRankDTO;
import top.hcode.hoj.pojo.dto.ExamSubmitDTO;
import top.hcode.hoj.pojo.entity.exam.Exam;
import top.hcode.hoj.pojo.entity.exam.ExamRepo;
import top.hcode.hoj.pojo.vo.ExamProblemVO;
import top.hcode.hoj.pojo.vo.ExamVO;
import top.hcode.hoj.service.exam.ExamService;

/**
 * @Author: Minipax
 * @Date: 2023/9/18 15:06
 * @Description:
 */
@RestController
@RequestMapping("/api/exam")
public class ExamController {

    @Autowired
    private ExamService examService;

    @GetMapping("/get-exam-list")
    @AnonApi
    public CommonResult<IPage<ExamVO>> getExamList(@RequestParam(value = "limit", required = false) Integer limit,
                                                   @RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                   @RequestParam(value = "title", required = false) String title) {
        return examService.getExamList(limit, currentPage, title);
    }

    @GetMapping("")
    @AnonApi
    public CommonResult<ExamVO> getExam(@RequestParam Long examId) {
        return examService.getExam(examId);
    }

    @GetMapping("/get-problem-list")
    @RequiresAuthentication
    public CommonResult<IPage<ExamProblemVO>> submitExercise(@RequestBody ExamSubmitDTO examSubmitDTO) {
        return examService.submitExercise(examSubmitDTO);
    }



}
