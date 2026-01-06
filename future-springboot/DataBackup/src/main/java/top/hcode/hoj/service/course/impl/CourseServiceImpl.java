package top.hcode.hoj.service.course.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hcode.hoj.common.exception.StatusFailException;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.manager.course.CourseManager;
import top.hcode.hoj.pojo.vo.CourseVO;
import top.hcode.hoj.service.course.CourseService;

/**
 * @Author: Minipax
 * @Date: 2023/9/29 17:22
 */
@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseManager courseManager;

    @Override
    public CommonResult<IPage<CourseVO>> getCourseList(Integer limit, Integer currentPage, Integer type, String keyword) {
        return CommonResult.successResponse(courseManager.getCourseList(limit, currentPage, type, keyword));
    }

    @Override
    public CommonResult<CourseVO> getCourse(Long id) {
        try {
            CourseVO courseVO = courseManager.getCourse(id);
            return CommonResult.successResponse(courseVO);
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        }
    }
}
