package top.hcode.hoj.pojo.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/18 15:10
 * @Description:
 */
@Data
@Accessors(chain = true)
public class ExamSubmitDTO {

    private Long historyId;

    private Long trainingId;

    private List<Long> problemIds;

    private List<List<String>> problemAnswers;

}
