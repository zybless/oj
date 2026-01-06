package top.hcode.hoj.service.admin.course;

import com.baomidou.mybatisplus.core.metadata.IPage;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.pojo.dto.AdminCourseDTO;
import top.hcode.hoj.pojo.vo.AdminCourseVO;

/**
 * @Author: Minipax
 * @Date: 2023/9/29 1:18
 */
public interface AdminCourseService {
    CommonResult<IPage<AdminCourseVO>> getCourseList(Integer limit, Integer currentPage, Integer type, String keyword);

    CommonResult<AdminCourseVO> getCourse(Long id);

    CommonResult<Void> addCourse(AdminCourseDTO adminCourseDTO);

    CommonResult<Void> updateCourse(AdminCourseDTO adminCourseDTO);

    CommonResult<Void> deleteCourse(Long id);


}
