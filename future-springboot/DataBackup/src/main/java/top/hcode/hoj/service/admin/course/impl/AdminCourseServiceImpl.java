package top.hcode.hoj.service.admin.course.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hcode.hoj.common.exception.StatusFailException;
import top.hcode.hoj.common.exception.StatusForbiddenException;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.common.result.ResultStatus;
import top.hcode.hoj.manager.admin.course.AdminCourseManager;
import top.hcode.hoj.pojo.dto.AdminCourseDTO;
import top.hcode.hoj.pojo.vo.AdminCourseVO;
import top.hcode.hoj.service.admin.course.AdminCourseService;

/**
 * @Author: Minipax
 * @Date: 2023/9/29 1:18
 */
@Service
public class AdminCourseServiceImpl implements AdminCourseService {

    @Autowired
    private AdminCourseManager adminCourseManager;

    @Override
    public CommonResult<IPage<AdminCourseVO>> getCourseList(Integer limit, Integer currentPage, Integer type, String keyword) {
        IPage<AdminCourseVO> courseVOList = adminCourseManager.getCourseList(limit, currentPage, type, keyword);
        return CommonResult.successResponse(courseVOList);
    }

    @Override
    public CommonResult<AdminCourseVO> getCourse(Long id) {
        try {
            AdminCourseVO adminCourseVO = adminCourseManager.getCourse(id);
            return CommonResult.successResponse(adminCourseVO);
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        }
    }

    @Override
    public CommonResult<Void> addCourse(AdminCourseDTO adminCourseDTO) {
        try {
            adminCourseManager.addCourse(adminCourseDTO);
            return CommonResult.successResponse();
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusForbiddenException e) {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<Void> updateCourse(AdminCourseDTO adminCourseDTO) {
        try {
            adminCourseManager.updateCourse(adminCourseDTO);
            return CommonResult.successResponse();
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusForbiddenException e) {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<Void> deleteCourse(Long id) {
        try {
            adminCourseManager.deleteCourse(id);
            return CommonResult.successResponse();
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusForbiddenException e) {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }
}
