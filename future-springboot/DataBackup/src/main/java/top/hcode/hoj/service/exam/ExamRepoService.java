package top.hcode.hoj.service.exam;

import com.baomidou.mybatisplus.core.metadata.IPage;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.pojo.entity.exam.ExamRepo;
import top.hcode.hoj.pojo.vo.ExamRepoVO;

/**
 * @Author: Minipax
 * @Date: 2023/9/16 21:41
 * @Description:
 */
public interface ExamRepoService {
    CommonResult<IPage<ExamRepo>> getRepoList(Integer limit, Integer currentPage, String keyword, Integer auth);

    CommonResult<ExamRepoVO> getRepo(Long id);
}
