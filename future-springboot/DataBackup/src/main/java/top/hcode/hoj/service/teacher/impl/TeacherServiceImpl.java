package top.hcode.hoj.service.teacher.impl;

import org.springframework.stereotype.Service;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.pojo.dto.HomeworkAssignmentDTO;
import top.hcode.hoj.service.teacher.TeacherService;

/**
 * @Author: Minipax
 * @Date: 2023/9/22 18:38
 */
@Service
public class TeacherServiceImpl implements TeacherService {

    @Override
    public CommonResult<Void> assignHomework(HomeworkAssignmentDTO homeworkAssignmentDTO) {
        return null;
    }
}
