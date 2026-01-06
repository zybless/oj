package top.hcode.hoj.controller.exam;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.hcode.hoj.annotation.AnonApi;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.pojo.entity.exam.ExamRepo;
import top.hcode.hoj.pojo.vo.ExamRepoVO;
import top.hcode.hoj.service.exam.ExamRepoService;

/**
 * @Author: Minipax
 * @Date: 2023/9/16 21:41
 * @Description:
 */

@RestController
@RequestMapping("/api/exam-repo")
public class ExamRepoController {

    @Autowired
    private ExamRepoService examRepoService;

    @GetMapping("/get-repo-list")
    @AnonApi
    public CommonResult<IPage<ExamRepo>> getRepoList(@RequestParam(value = "limit", required = false) Integer limit,
                                                     @RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                     @RequestParam(value = "keyword", required = false) String keyword,
                                                     @RequestParam(value = "auth", required = false) Integer auth) {
        return examRepoService.getRepoList(limit, currentPage, keyword, auth);
    }

    @GetMapping("")
    @AnonApi
    public CommonResult<ExamRepoVO> getRepo(@RequestParam("id") Long id) {
        return examRepoService.getRepo(id);
    }

}
