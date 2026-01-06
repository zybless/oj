package top.hcode.hoj.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2024/6/23 17:34
 */

@ApiModel(value = "TrainingCampTeamVO", description = "")
@Data
public class TrainingCampTeamVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String title;

    private String description;

    private List<DiscussionVO> relatedAnnouncement;

    private List<String> announcementCategory;

    private List<TrainingCampVO> trainingCampVOList;

}
