package top.hcode.hoj.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2024/7/30 8:53
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthRankingVO {

    private String name;

    private List<MonthRankingDetailVO> MonthRankingDetailVOList = new ArrayList<>();

}
