package top.hcode.hoj.pojo.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import top.hcode.hoj.pojo.entity.discussion.Discussion;
import top.hcode.hoj.pojo.entity.training.TrainingCamp;
import top.hcode.hoj.pojo.entity.training.TrainingCampTeam;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2024/6/23 17:39
 */
@ApiModel(value = "AdminTrainingCampTeamVO", description = "")
@Data
public class AdminTrainingCampTeamVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String title;

    private String description;

    private List<Discussion> relatedAnnouncement;

    private List<String> announcementCategory;

    private String modifiedUser;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    private List<TrainingCamp> trainingCampList;

}