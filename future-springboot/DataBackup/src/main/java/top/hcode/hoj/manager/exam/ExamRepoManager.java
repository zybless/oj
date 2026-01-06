package top.hcode.hoj.manager.exam;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import top.hcode.hoj.dao.exam.ExamRepoEntityService;
import top.hcode.hoj.pojo.entity.exam.ExamRepo;
import top.hcode.hoj.pojo.vo.ExamProblemVO;
import top.hcode.hoj.pojo.vo.ExamRepoVO;

/**
 * @Author: Minipax
 * @Date: 2023/9/16 21:43
 * @Description:
 */

@Component
@RefreshScope
@Slf4j(topic = "hoj")
public class ExamRepoManager {

    @Autowired
    private ExamRepoEntityService examRepoEntityService;

    public IPage<ExamRepo> getRepoList(Integer limit, Integer currentPage, String keyword, Integer auth) {
        if (currentPage == null || currentPage < 1) currentPage = 1;
        if (limit == null || limit < 1) limit = 10;

        // 关键词查询不为空
        if (!StringUtils.isEmpty(keyword)) {
            keyword = keyword.trim();
        }

        return examRepoEntityService.getRepoList(limit, currentPage, keyword, auth);
    }

    public ExamRepoVO getRepo(Long id) {
        return(new ExamRepoVO(examRepoEntityService.getById(id)));

    }
}
