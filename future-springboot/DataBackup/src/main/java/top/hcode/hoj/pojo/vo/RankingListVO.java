package top.hcode.hoj.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Minipax
 * @Date: 2024/7/29 10:20
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RankingListVO {

    private String username;

    @ApiModelProperty(value = "用户排名")
    private List<Integer> myRank = new ArrayList<>();

    private List<Integer> acNum;

    @ApiModelProperty(value = "时间区分的用户榜单")
    private List<List<RankingListDetailVO>> rankingListDetailVOList = new ArrayList<>();

}
