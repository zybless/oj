package top.hcode.hoj.service.admin.training;

import com.baomidou.mybatisplus.core.metadata.IPage;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.pojo.dto.TrainingExamDTO;
import top.hcode.hoj.pojo.dto.TrainingProblemDTO;
import top.hcode.hoj.pojo.entity.exam.Exam;
import top.hcode.hoj.pojo.entity.exam.ExamRepo;
import top.hcode.hoj.pojo.entity.training.TrainingExam;
import top.hcode.hoj.pojo.entity.training.TrainingProblem;
import top.hcode.hoj.pojo.vo.ExamVO;

import java.util.HashMap;

public interface AdminTrainingProblemService {

    public CommonResult<HashMap<String, Object>> getProblemList(Integer limit, Integer currentPage, String keyword, Boolean queryExisted, Long tid);

    public CommonResult<Void> updateProblem(TrainingProblem trainingProblem);

    public CommonResult<Void> deleteProblem(Long pid,Long tid);

    public CommonResult<Void> addProblemFromPublic(TrainingProblemDTO trainingProblemDto);

    public CommonResult<Void> importTrainingRemoteOJProblem(String name, String problemId, Long tid);

    public CommonResult<IPage<ExamRepo>> getExamList(Integer limit, Integer currentPage, String keyword, Long tid);

    public CommonResult<Void> updateExam(TrainingExam trainingExamProblem);

    public CommonResult<Void> deleteExam(Long eid, Long tid);

    public CommonResult<Void> addExamFromPublic(TrainingExamDTO trainingExamDTO);
}
