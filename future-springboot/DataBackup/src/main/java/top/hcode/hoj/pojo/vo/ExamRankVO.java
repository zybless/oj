package top.hcode.hoj.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import oshi.driver.mac.net.NetStat;

/**
 * @Author: Minipax
 * @Date: 2024/3/26 19:25
 */
@Data
public class ExamRankVO implements Comparable<ExamRankVO> {

    @ApiModelProperty(value = "排名,排名为-1则为打星队伍")
    private Integer rank;

    @ApiModelProperty(value = "用户id")
    private String uid;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "用户真实姓名")
    private String realname;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "学校")
    private String school;

    @ApiModelProperty(value = "性别")
    private String gender;

    @ApiModelProperty(value = "头像")
    private String avatar;

    private Float userScore;

    private Long historyId;

    @ApiModelProperty(value = "提交总时")
    private Long totalTime;

    @Override
    public int compareTo(ExamRankVO examRankVO) {
        if (examRankVO.getUserScore() - this.userScore > 0) {
            return 1;
        } else if (examRankVO.getUserScore() - this.userScore < 0){
            return -1;
        } else {
            return 0;
        }
    }

}
