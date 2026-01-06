package top.hcode.hoj.pojo.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/17 14:11
 * @Description:
 */

@Data
@Accessors(chain = true)
public class RemoveProblemsFromRepoDTO {

    private List<Long> problemIds;

    private Long repoId;

}
