package top.hcode.hoj.pojo.dto.trainingCategory;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zybless
 * @date 2026/3/11 17:53
 */
@Data
public class CategoryRankDTO {
    @ApiModelProperty(value = "被拖拽的分类ID", required = true)
    private Long dragId;

    @ApiModelProperty(value = "拖拽后的前一个分类ID，插到最前面时为 null")
    private Long prevId;

    @ApiModelProperty(value = "拖拽后的后一个分类ID，插到最后面时为 null")
    private Long nextId;
}
