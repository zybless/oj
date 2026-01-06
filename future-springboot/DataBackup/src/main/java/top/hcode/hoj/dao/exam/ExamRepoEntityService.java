package top.hcode.hoj.dao.exam;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.models.auth.In;
import top.hcode.hoj.pojo.entity.exam.ExamRepo;
import top.hcode.hoj.pojo.vo.ExamRepoVO;

import java.util.List;

/**
 * @Author: Minipax
 * @Date: 2023/9/10 0:01
 */
public interface ExamRepoEntityService extends IService<ExamRepo> {

    public boolean addProblemToReposBatch(List<Long> repoIds);

    public boolean removeProblemFromReposBatch(List<Long> repoIds);

    boolean addProblemsToRepoBatch(Long repoId, Integer size);

    boolean removeProblemsFromRepoBatch(Long repoId, int size);

    IPage<ExamRepo> getRepoList(Integer limit, Integer currentPage, String keyword, Integer auth);

}
