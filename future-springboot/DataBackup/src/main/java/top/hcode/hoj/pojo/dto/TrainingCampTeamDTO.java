package top.hcode.hoj.pojo.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2024/6/30 3:27
 */
@Data
public class TrainingCampTeamDTO {

    private String title;

    private String description;

    private List<String> relatedAnnouncement;

    private List<String> announcementCategory;

}
