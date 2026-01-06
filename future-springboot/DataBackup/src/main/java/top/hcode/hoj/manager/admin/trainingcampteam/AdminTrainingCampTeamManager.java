package top.hcode.hoj.manager.admin.trainingcampteam;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.hcode.hoj.common.exception.StatusFailException;
import top.hcode.hoj.common.exception.StatusForbiddenException;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.dao.discussion.DiscussionEntityService;
import top.hcode.hoj.dao.trainingcampteam.TrainingCampEntityService;
import top.hcode.hoj.dao.trainingcampteam.TrainingCampTeamEntityService;
import top.hcode.hoj.pojo.dto.TrainingCampTeamDTO;
import top.hcode.hoj.pojo.entity.discussion.Discussion;
import top.hcode.hoj.pojo.entity.training.TrainingCamp;
import top.hcode.hoj.pojo.entity.training.TrainingCampTeam;
import top.hcode.hoj.pojo.vo.AdminTrainingCampTeamVO;
import top.hcode.hoj.shiro.AccountProfile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Minipax
 * @Date: 2024/6/23 17:58
 */
@Component
@RefreshScope
@Slf4j(topic = "hoj")
public class AdminTrainingCampTeamManager {

    @Autowired
    private TrainingCampTeamEntityService trainingCampTeamEntityService;

    @Autowired
    private TrainingCampEntityService trainingCampEntityService;

    @Autowired
    private DiscussionEntityService discussionEntityService;

    public AdminTrainingCampTeamVO getTrainingCampTeam() throws StatusForbiddenException {
        boolean isRoot = SecurityUtils.getSubject().hasRole("root");
        boolean isAdmin = SecurityUtils.getSubject().hasRole("admin");

//        if (!isRoot && !isAdmin) {
//            throw new StatusForbiddenException("对不起，你无权查看集训队！");
//        }

        QueryWrapper<TrainingCampTeam> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("id", 1);
        TrainingCampTeam trainingCampTeam = trainingCampTeamEntityService.getOne(queryWrapper1);
        QueryWrapper<TrainingCamp> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("training_camp_team_id", 1);
        List<TrainingCamp> trainingCampList = trainingCampEntityService.list(queryWrapper2);

        AdminTrainingCampTeamVO adminTrainingCampTeamVO = new AdminTrainingCampTeamVO();
        BeanUtils.copyProperties(trainingCampTeam, adminTrainingCampTeamVO);

        adminTrainingCampTeamVO.setTrainingCampList(trainingCampList);

        List<String> idList = new ArrayList<>();
        List<String> categoryList = new ArrayList<>();

        String[] pairs = trainingCampTeam.getRelatedAnnouncement().split(",");
        for (String pair : pairs) {
            String[] elements = pair.split(":");
            idList.add(elements[0]);
            categoryList.add(elements[1]);
        }
        adminTrainingCampTeamVO.setAnnouncementCategory(categoryList);

        List<Integer> intList = idList.stream()
                .map(Integer::parseInt) // 将字符串解析为整数
                .collect(Collectors.toList());
        List<Discussion> discussionList = new ArrayList<>();

        for (Integer i : intList) {
            discussionList.add(discussionEntityService.getById(i));
        }

        adminTrainingCampTeamVO.setRelatedAnnouncement(discussionList);

        return adminTrainingCampTeamVO;
    }

    public TrainingCamp getTrainingCamp(Long id) throws StatusForbiddenException, StatusFailException {
        // 获取当前登录的用户
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();

        boolean isRoot = SecurityUtils.getSubject().hasRole("root");
        boolean isAdmin = SecurityUtils.getSubject().hasRole("admin");

//        if (!isRoot && !isAdmin) {
//            throw new StatusForbiddenException("对不起，你无权查看集训！");
//        }

        QueryWrapper<TrainingCamp> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        TrainingCamp trainingCamp = trainingCampEntityService.getOne(queryWrapper);
        if (trainingCamp == null) {
            throw new StatusFailException("id不存在");
        }

        return trainingCamp;
    }

    @Transactional(rollbackFor = Exception.class)
    public void addTrainingCamp(TrainingCamp trainingCamp) throws StatusForbiddenException, StatusFailException {
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();
        boolean isRoot = SecurityUtils.getSubject().hasRole("root");
        boolean isAdmin = SecurityUtils.getSubject().hasRole("admin");

//        if (!isRoot && !isAdmin) {
//            throw new StatusForbiddenException("对不起，你无权增加集训！");
//        }
        trainingCamp.setTrainingCampTeamId(1L);
        trainingCamp.setModifiedUser(userRolesVo.getUsername());
        boolean save = trainingCampEntityService.save(trainingCamp);
        if (!save) {
            throw new StatusFailException("添加失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateTrainingCampTeam(TrainingCampTeamDTO trainingCampTeamDTO) throws StatusFailException, StatusForbiddenException {
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();
        boolean isRoot = SecurityUtils.getSubject().hasRole("root");
        boolean isAdmin = SecurityUtils.getSubject().hasRole("admin");

//        if (!isRoot && !isAdmin) {
//            throw new StatusForbiddenException("对不起，你无权修改集训队！");
//        }

        TrainingCampTeam trainingCampTeam = new TrainingCampTeam();
        BeanUtils.copyProperties(trainingCampTeamDTO, trainingCampTeam);
        trainingCampTeam.setId(1L);
        trainingCampTeam.setModifiedUser(userRolesVo.getUsername());

        List<String> idList = trainingCampTeamDTO.getRelatedAnnouncement();
        List<String> categoryList = trainingCampTeamDTO.getAnnouncementCategory();

        if (idList.size() != categoryList.size()) {
            throw new StatusFailException("类别与公告数量不统一！");
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < idList.size(); i++) {
            sb.append(idList.get(i)).append(":").append(categoryList.get(i));
            if (i < idList.size() - 1) {
                sb.append(","); // 用逗号分隔不同的元素对
            }
        }

        trainingCampTeam.setRelatedAnnouncement(sb.toString());
        boolean update = trainingCampTeamEntityService.updateById(trainingCampTeam);
        if (!update) {
            throw new StatusFailException("修改失败");
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void updateTrainingCamp(TrainingCamp trainingCamp) throws StatusForbiddenException, StatusFailException {
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();
        boolean isRoot = SecurityUtils.getSubject().hasRole("root");
        boolean isAdmin = SecurityUtils.getSubject().hasRole("admin");

//        if (!isRoot && !isAdmin) {
//            throw new StatusForbiddenException("对不起，你无权修改集训！");
//        }
        trainingCamp.setTrainingCampTeamId(1L);
        trainingCamp.setModifiedUser(userRolesVo.getUsername());
        boolean update = trainingCampEntityService.updateById(trainingCamp);
        if (!update) {
            throw new StatusFailException("修改失败");
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteTrainingCamp(Long id) throws StatusForbiddenException, StatusFailException {
        boolean isRoot = SecurityUtils.getSubject().hasRole("root");
        boolean isAdmin = SecurityUtils.getSubject().hasRole("admin");

//        if (!isRoot && !isAdmin) {
//            throw new StatusForbiddenException("对不起，你无权删除集训！");
//        }
        boolean del = trainingCampEntityService.removeById(id);
        if (!del) {
            throw new StatusFailException("删除失败");
        }

    }
}
