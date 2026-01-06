package top.hcode.hoj.service.exam.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.manager.exam.ExamRepoManager;
import top.hcode.hoj.pojo.entity.exam.ExamRepo;
import top.hcode.hoj.pojo.vo.ExamRepoVO;
import top.hcode.hoj.service.exam.ExamRepoService;

/**
 * @Author: Minipax
 * @Date: 2023/9/16 21:41
 * @Description:
 */
@Service
public class ExamRepoServiceImpl implements ExamRepoService {

    @Autowired
    private ExamRepoManager examRepoManager;

    @Override
    public CommonResult<IPage<ExamRepo>> getRepoList(Integer limit, Integer currentPage, String keyword, Integer auth) {
        return CommonResult.successResponse(examRepoManager.getRepoList(limit, currentPage, keyword, auth));
    }

    @Override
    public CommonResult<ExamRepoVO> getRepo(Long id) {
        return CommonResult.successResponse(examRepoManager.getRepo(id));
    }
}
