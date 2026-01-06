package top.hcode.hoj.service.course;

import com.baomidou.mybatisplus.core.metadata.IPage;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.pojo.vo.CourseVO;

/**
 * @Author: Minipax
 * @Date: 2023/9/29 17:22
 */
public interface CourseService {
    CommonResult<IPage<CourseVO>> getCourseList(Integer limit, Integer currentPage, Integer type, String keyword);

    CommonResult<CourseVO> getCourse(Long id);

}
