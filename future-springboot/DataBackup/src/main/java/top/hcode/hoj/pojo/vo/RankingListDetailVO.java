package top.hcode.hoj.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: Minipax
 * @Date: 2024/7/29 10:21
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RankingListDetailVO implements Comparable<RankingListDetailVO>{

    @ApiModelProperty(value = "用户id")
    private String uid;

    @ApiModelProperty(value = "用户名称")
    private String username;

    @ApiModelProperty(value = "ac数量")
    private Integer acNum;

    @Override
    public int compareTo(RankingListDetailVO other) {
        return Integer.compare(this.acNum, other.acNum); // 按照年龄排序
    }
}
