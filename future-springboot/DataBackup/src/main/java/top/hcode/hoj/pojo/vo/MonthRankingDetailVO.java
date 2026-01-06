package top.hcode.hoj.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.hcode.hoj.pojo.entity.user.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2024/7/30 8:55
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthRankingDetailVO {

    private String name;

    private List<UserInfo> userInfoList = new ArrayList<>();

}
