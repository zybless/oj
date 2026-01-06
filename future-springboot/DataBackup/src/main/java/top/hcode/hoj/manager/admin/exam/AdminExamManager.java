package top.hcode.hoj.manager.admin.exam;

import cn.hutool.core.io.FileUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import top.hcode.hoj.common.exception.StatusFailException;
import top.hcode.hoj.common.exception.StatusForbiddenException;
import top.hcode.hoj.dao.common.FileEntityService;
import top.hcode.hoj.dao.exam.ExamEntityService;
import top.hcode.hoj.dao.exam.ExamHistoryEntityService;
import top.hcode.hoj.dao.exam.ExamRepoEntityService;
import top.hcode.hoj.dao.user.UserInfoEntityService;
import top.hcode.hoj.pojo.dto.ExamRankDTO;
import top.hcode.hoj.pojo.entity.exam.Exam;
import top.hcode.hoj.pojo.entity.exam.ExamHistory;
import top.hcode.hoj.pojo.entity.exam.ExamProblem;
import top.hcode.hoj.pojo.entity.exam.ExamRepo;
import top.hcode.hoj.pojo.entity.user.UserInfo;
import top.hcode.hoj.pojo.vo.ExamHistoryVO;
import top.hcode.hoj.pojo.vo.ExamRankVO;
import top.hcode.hoj.pojo.vo.ExamRepoVO;
import top.hcode.hoj.pojo.vo.ExamVO;
import top.hcode.hoj.shiro.AccountProfile;
import top.hcode.hoj.utils.Constants;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.*;

/**
 * @Author: Minipax
 * @Date: 2024/3/25 19:39
 */

@Component
@RefreshScope
@Slf4j(topic = "hoj")
public class AdminExamManager {

    @Autowired
    private ExamEntityService examEntityService;

    @Autowired
    private ExamHistoryEntityService examHistoryEntityService;

    @Autowired
    private UserInfoEntityService userInfoEntityService;

    @Autowired
    private ExamRepoEntityService examRepoEntityService;

    @Autowired
    private FileEntityService fileEntityService;

    public IPage<ExamVO> getExamList(Integer limit, Integer currentPage, String title) {
        if (currentPage == null || currentPage < 1) currentPage = 1;
        if (limit == null || limit < 1) limit = 10;


        IPage<ExamVO> examList = examEntityService.getExamListAdmin(limit, currentPage, title);

        //太长了
//        for (ExamVO examVO : examList.getRecords()) {
//            QueryWrapper<ExamHistory> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("exam_id", examVO.getId());
//
//            ExamHistory examHistory = examHistoryEntityService.getOne(queryWrapper);
//            ExamHistoryVO examHistoryVO = examHistoryEntityService.getExamHistoryVO(examHistory.getId());
//            examVO.set
//        }


        return examList;
    }

    public List<ExamHistoryVO> getExamHistory(Long examId) {
        List<ExamHistoryVO> examHistoryVOList = new ArrayList<>();
        QueryWrapper<ExamHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("exam_id", examId);

        List<ExamHistory> examHistoryList = examHistoryEntityService.list(queryWrapper);

        for (ExamHistory examHistory : examHistoryList) {
            examHistoryVOList.add(examHistoryEntityService.getExamHistoryVO(examHistory.getId()));
        }
        return examHistoryVOList;
    }

    public IPage<ExamRankVO> getExamRank(ExamRankDTO examRankDTO) throws StatusFailException {
        Long examId = examRankDTO.getExamId();
        Integer currentPage = examRankDTO.getCurrentPage();
        Integer limit = examRankDTO.getLimit();

        if (examId == null) {
            throw new StatusFailException("错误：examId不能为空");
        }
        // 页数，每页题数若为空，设置默认值
        if (currentPage == null || currentPage < 1) currentPage = 1;
        if (limit == null || limit < 1) limit = 50;

//        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();

        QueryWrapper<ExamHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("exam_id", examId);

        List<ExamHistory> examHistoryList = examHistoryEntityService.list(queryWrapper);

        if (examHistoryList == null || examHistoryList.size() == 0) {
            throw new StatusFailException("没有考试历史");
        }

        Page<ExamRankVO> page = new Page<>(currentPage, limit);

        List<ExamRankVO> examRankVOList = new ArrayList<>();

        for (ExamHistory examHistory : examHistoryList) {
            String userId = examHistory.getUserId();
            Float userScore = examHistory.getUserScore();

            UserInfo userInfo = userInfoEntityService.getById(userId);

            ExamRankVO examRankVO = new ExamRankVO();
            examRankVO.setUid(userId);

            if (userScore == null) {
                examRankVO.setUserScore(0F);
            } else {
                examRankVO.setUserScore(userScore);
            }

            if (userInfo != null) {
                examRankVO.setUsername(userInfo.getUsername());
                examRankVO.setNickname(userInfo.getNickname());
                examRankVO.setRealname(userInfo.getRealname());
                examRankVO.setGender(userInfo.getGender());
                examRankVO.setAvatar(userInfo.getAvatar());
                examRankVO.setSchool(userInfo.getSchool());
                examRankVO.setHistoryId(examHistory.getId());

                examRankVOList.add(examRankVO);
            }

        }

        Collections.sort(examRankVOList);

        Integer curRank = 1, preRank = 1;
        Float curScore = examRankVOList.get(0).getUserScore();
        for (ExamRankVO examRankVO : examRankVOList) {
            if (Objects.equals(examRankVO.getUserScore(), curScore)) {
                examRankVO.setRank(preRank);
            } else {
                examRankVO.setRank(curRank);
                preRank = curRank;
                curScore = examRankVO.getUserScore();
            }
            curRank++;
        }


        page.setTotal(examRankVOList.size());

        if (examRankVOList.size() > limit) {
            int startIndex = (currentPage - 1) * limit, endIndex = Math.min(currentPage * limit, examRankVOList.size());
            page.setRecords(examRankVOList.subList(startIndex, endIndex));
        } else {
            page.setRecords(examRankVOList);
        }

        return page;
    }

    public void deleteExam(Long id) throws StatusFailException, StatusForbiddenException {
        Exam exam = examEntityService.getById(id);
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();

        boolean isRoot = SecurityUtils.getSubject().hasRole("root");
        boolean isTeacher = SecurityUtils.getSubject().hasRole("teacher");

        if (!isRoot && !isTeacher && !userRolesVo.getUsername().equals(exam.getAuthor())) {
            throw new StatusForbiddenException("对不起，你无权限删除选拔赛！");
        }

        boolean isOk = examEntityService.removeById(id);

        //TODO 历史记录删除

        if (isOk) { // 删除成功
            log.info("[{}],[{}],pid:[{}],operatorUid:[{}],operatorUsername:[{}]",
                    "Admin_Exam_Repo", "Delete", id, userRolesVo.getUid(), userRolesVo.getUsername());
        } else {
            throw new StatusFailException("删除失败！");
        }
    }

    public void addExam(Exam exam) throws StatusForbiddenException, StatusFailException {
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();

        boolean isRoot = SecurityUtils.getSubject().hasRole("root");
        boolean isAdmin = SecurityUtils.getSubject().hasRole("admin");
        // 只有超级管理员和题目管理员、题目创建者才能操作
//        if (!isRoot && !isAdmin) {
//            throw new StatusForbiddenException("对不起，你无权限增加选拔赛！");
//        }

//        // 判断权重是否正确
//        String weightStr = exam.getWeight();
//        double[] weight = Arrays.stream(weightStr.split(","))
//                .mapToDouble(Double::parseDouble)
//                .toArray();
//        if(weight.length != 5) {
//            throw new StatusFailException("权重数量错误");
//        }
//
//        double sum = 0;
//        for (double w : weight) {
//            sum += w;
//        }
//
//        if(sum != 100.0) {
//            throw new StatusFailException("权重和错误");
//        }

        exam.setModifiedUser(userRolesVo.getUsername());
        exam.setAuthor(userRolesVo.getUsername());
        boolean isOk = examEntityService.saveOrUpdate(exam);
        if (!isOk) {
            throw new StatusFailException("添加失败");
        }
    }

    public void updateExam(Exam exam) throws StatusForbiddenException, StatusFailException {
        Exam dbExam = examEntityService.getById(exam.getId());
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();

        boolean isRoot = SecurityUtils.getSubject().hasRole("root");
        boolean isTeacher = SecurityUtils.getSubject().hasRole("teacher");

        if (!isRoot && !isTeacher && !userRolesVo.getUsername().equals(dbExam.getAuthor())) {
            throw new StatusForbiddenException("对不起，你无权限修改选拔赛！");
        }

        if(dbExam == null) {
            throw new StatusFailException("修改失败，ID不存在");
        }
        dbExam.setTitle(exam.getTitle());
        dbExam.setRemark(exam.getRemark());
        dbExam.setPassword(exam.getPassword());
        dbExam.setStartTime(exam.getStartTime());
        dbExam.setEndTime(exam.getEndTime());
        dbExam.setExamRepoId(exam.getExamRepoId());

        dbExam.setModifiedUser(userRolesVo.getUsername());
        boolean isOk = examEntityService.saveOrUpdate(dbExam);
        if (!isOk) {
            throw new StatusFailException("修改失败");
        }
    }

    public ExamVO getExam(Long id) throws StatusForbiddenException, StatusFailException {
        Exam exam = examEntityService.getById(id);

        if (exam != null) { // 查询成功
            boolean isRoot = SecurityUtils.getSubject().hasRole("root");
            boolean isAdmin = SecurityUtils.getSubject().hasRole("admin");
            // 只有超级管理员和题目管理员、题目创建者才能操作
//            if (!isRoot && !isAdmin) {
//                throw new StatusForbiddenException("对不起，你无权限增加题库！");
//            }

            ExamVO examVO = new ExamVO(exam);
            examVO.setId(id);
            return examVO;
        } else {
            throw new StatusFailException("查询失败！");
        }
    }

    public ExamHistoryVO getExamHistoryDetail(Long historyId) throws StatusFailException {
        ExamHistoryVO examHistoryVO = examHistoryEntityService.getExamHistoryVO(historyId);
        if (examHistoryVO == null) {
            throw new StatusFailException("改历史成绩不存在！");
        }
        return examHistoryVO;
    }

    public void exportHistory(Long eid, HttpServletResponse response) throws StatusFailException, IOException {
        Exam exam = examEntityService.getById(eid);
        if (exam == null) {
            throw new StatusFailException("错误：该选拔赛不存在！");
        }
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        String fileName = URLEncoder.encode("exam_" + exam.getId() + "_rank", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        response.setHeader("Content-Type", "application/xlsx");

        QueryWrapper<ExamHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("exam_id", eid);

        List<ExamHistory> examHistoryList = examHistoryEntityService.list(queryWrapper);

        if (examHistoryList == null || examHistoryList.size() == 0) {
            throw new StatusFailException("没有考试历史");
        }

        List<ExamRankVO> examRankVOList = new ArrayList<>();

        for (ExamHistory examHistory : examHistoryList) {
            String userId = examHistory.getUserId();
            Float userScore = examHistory.getUserScore();

            UserInfo userInfo = userInfoEntityService.getById(userId);

            ExamRankVO examRankVO = new ExamRankVO();
            examRankVO.setUid(userId);

            if (userScore == null) {
                examRankVO.setUserScore(0F);
            } else {
                examRankVO.setUserScore(userScore);
            }

            if (userInfo != null) {
                examRankVO.setUsername(userInfo.getUsername());
                examRankVO.setNickname(userInfo.getNickname());
                examRankVO.setRealname(userInfo.getRealname());
                examRankVO.setGender(userInfo.getGender());
                examRankVO.setAvatar(userInfo.getAvatar());
                examRankVO.setSchool(userInfo.getSchool());
                examRankVO.setHistoryId(examHistory.getId());

                examRankVOList.add(examRankVO);
            }

        }

        Collections.sort(examRankVOList);

        Integer curRank = 1, preRank = 1;
        Float curScore = examRankVOList.get(0).getUserScore();
        for (ExamRankVO examRankVO : examRankVOList) {
            if (Objects.equals(examRankVO.getUserScore(), curScore)) {
                examRankVO.setRank(preRank);
            } else {
                examRankVO.setRank(curRank);
                preRank = curRank;
                curScore = examRankVO.getUserScore();
            }
            curRank++;
        }

        ExamRepo examRepo = examRepoEntityService.getById(exam.getExamRepoId());

        EasyExcel.write(response.getOutputStream())
                .head(fileEntityService.getExamRankExcelHead(examRepo.getProblemCount()))
                .sheet("排名")
                .doWrite(fileEntityService.changeExamRankToExcelRowList(examRankVOList));
    }
}
