package top.hcode.hoj.service.group.exam;

import com.baomidou.mybatisplus.core.metadata.IPage;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.pojo.vo.ExamRepoVO;

/**
 * @Author: Minipax
 * @Date: 2024/8/10 21:45
 */
public interface GroupExamService {

//    CommonResult<IPage<ExamRepoVO>> getExamList(Integer limit, Integer currentPage, Long gid);

    CommonResult<Void> addExam(Long gid, Long eid);

    CommonResult<Void> removeExam(Long gid, Long eid);
}
