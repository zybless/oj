package top.hcode.hoj.pojo.vo;

import lombok.Data;
import top.hcode.hoj.pojo.entity.group.Group;

import java.util.HashMap;

/**
 * @Author: Minipax
 * @Date: 2023/10/12 14:34
 */
@Data
public class UserGroupProblemStatusVO {

    private HashMap<Long, HashMap<Long, HashMap<Long, Object>>> statusMap;

    private HashMap<Long, Group> groupDetail = new HashMap<>();

    private HashMap<Long, TrainingVO> trainingDetail = new HashMap<>();

    private HashMap<Long, ProblemVO> problemDetail = new HashMap<>();

}
