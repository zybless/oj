package top.hcode.hoj.service.teacher;

import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.pojo.dto.HomeworkAssignmentDTO;

/**
 * @Author: Minipax
 * @Date: 2023/9/22 18:38
 */
public interface TeacherService {
    CommonResult<Void> assignHomework(HomeworkAssignmentDTO homeworkAssignmentDTO);

}
