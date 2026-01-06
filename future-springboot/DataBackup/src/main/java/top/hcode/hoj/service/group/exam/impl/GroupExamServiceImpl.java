package top.hcode.hoj.service.group.exam.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hcode.hoj.common.exception.StatusFailException;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.manager.group.exam.GroupExamManager;
import top.hcode.hoj.pojo.entity.exam.ExamRepo;
import top.hcode.hoj.pojo.vo.ExamRepoVO;
import top.hcode.hoj.service.group.exam.GroupExamService;

/**
 * @Author: Minipax
 * @Date: 2024/8/10 21:45
 */
@Service
public class GroupExamServiceImpl implements GroupExamService {

    @Autowired
    private GroupExamManager groupExamManager;

//    @Override
//    public CommonResult<IPage<ExamRepoVO>> getExamList(Integer limit, Integer currentPage, Long gid) {
//        IPage<ExamRepo> examList = groupExamManager.getExamList(limit, currentPage, gid);
//        return CommonResult.successResponse(examList);
//    }

    @Override
    public CommonResult<Void> addExam(Long gid, Long eid) {
        try {
            groupExamManager.addExam(gid, eid);
            return CommonResult.successResponse();
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        }
    }

    @Override
    public CommonResult<Void> removeExam(Long gid, Long eid) {
//        try {
            groupExamManager.removeExam(gid, eid);
            return CommonResult.successResponse();
//        }
    }
}
