package top.hcode.hoj.pojo.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/16 16:29
 */

@Data
@Accessors(chain = true)
public class AddProblemsToRepoDTO {

    private List<Long> problemIds;

    private Long repoId;

    private List<Float> grades;

}
