package top.hcode.hoj.pojo.dto;

import lombok.Data;

/**
 * @Author: Minipax
 * @Date: 2024/3/26 14:38
 */
@Data
public class ExamRankDTO {

    private Long examId;

    private Integer limit;

    private Integer currentPage;


}
