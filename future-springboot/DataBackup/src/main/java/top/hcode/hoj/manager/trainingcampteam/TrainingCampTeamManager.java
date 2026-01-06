package top.hcode.hoj.manager.trainingcampteam;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import top.hcode.hoj.common.exception.StatusForbiddenException;
import top.hcode.hoj.common.exception.StatusNotFoundException;
import top.hcode.hoj.dao.discussion.DiscussionEntityService;
import top.hcode.hoj.dao.trainingcampteam.TrainingCampEntityService;
import top.hcode.hoj.dao.trainingcampteam.TrainingCampTeamEntityService;
import top.hcode.hoj.exception.AccessException;
import top.hcode.hoj.manager.oj.DiscussionManager;
import top.hcode.hoj.pojo.entity.training.TrainingCamp;
import top.hcode.hoj.pojo.entity.training.TrainingCampTeam;
import top.hcode.hoj.pojo.vo.DiscussionVO;
import top.hcode.hoj.pojo.vo.TrainingCampTeamVO;
import top.hcode.hoj.pojo.vo.TrainingCampVO;
import top.hcode.hoj.service.trainingcampteam.TrainingCampTeamService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2024/6/23 18:42
 */

@Component
@RefreshScope
@Slf4j(topic = "hoj")
public class TrainingCampTeamManager {

    @Autowired
    private TrainingCampTeamEntityService trainingCampTeamEntityService;

    @Autowired
    private TrainingCampEntityService trainingCampEntityService;

    @Autowired
    private DiscussionManager discussionManager;

    public TrainingCampTeamVO getTrainingCampTeam() throws StatusForbiddenException, AccessException, StatusNotFoundException {
        QueryWrapper<TrainingCampTeam> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("id", 1);
        TrainingCampTeam trainingCampTeam = trainingCampTeamEntityService.getOne(queryWrapper1);

        QueryWrapper<TrainingCamp> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("training_camp_team_id", 1);
        List<TrainingCamp> trainingCampList = trainingCampEntityService.list(queryWrapper2);

        TrainingCampTeamVO trainingCampTeamVO = new TrainingCampTeamVO();

        trainingCampTeamVO.setId(trainingCampTeam.getId());
        trainingCampTeamVO.setDescription(trainingCampTeam.getDescription());
        trainingCampTeamVO.setTitle(trainingCampTeam.getTitle());

        List<String> idList = new ArrayList<>();
        List<String> categoryList = new ArrayList<>();

        String[] pairs = trainingCampTeam.getRelatedAnnouncement().split(",");
        for (String pair : pairs) {
            String[] elements = pair.split(":");
            idList.add(elements[0]);
            categoryList.add(elements[1]);
        }
        trainingCampTeamVO.setAnnouncementCategory(categoryList);

        List<DiscussionVO> discussionVOList = new ArrayList<>();
        for (String did : idList) {
            DiscussionVO discussion = discussionManager.getDiscussion(Integer.valueOf(did));
            discussionVOList.add(discussion);
        }

        trainingCampTeamVO.setRelatedAnnouncement(discussionVOList);

        List<TrainingCampVO> trainingCampVOList = new ArrayList<>();
        for (TrainingCamp trainingCamp : trainingCampList) {
            TrainingCampVO trainingCampVO = new TrainingCampVO();
            BeanUtils.copyProperties(trainingCamp, trainingCampVO);
            trainingCampVOList.add(trainingCampVO);
        }
        trainingCampTeamVO.setTrainingCampVOList(trainingCampVOList);
        return trainingCampTeamVO;
    }


    public TrainingCampVO getTrainingCamp(Long id) {
        QueryWrapper<TrainingCamp> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        TrainingCamp trainingCamp = trainingCampEntityService.getOne(queryWrapper);
        TrainingCampVO trainingCampVO = new TrainingCampVO();
        BeanUtils.copyProperties(trainingCamp, trainingCampVO);
        return trainingCampVO;
    }
}
