package top.hcode.hoj.manager.group.training;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.jsonwebtoken.lang.Collections;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.hcode.hoj.common.exception.StatusFailException;
import top.hcode.hoj.common.exception.StatusForbiddenException;
import top.hcode.hoj.common.exception.StatusNotFoundException;
import top.hcode.hoj.dao.group.GroupEntityService;
import top.hcode.hoj.dao.group.GroupTrainingEntityService;
import top.hcode.hoj.dao.training.*;
import top.hcode.hoj.manager.admin.training.AdminTrainingRecordManager;
import top.hcode.hoj.pojo.dto.TrainingDTO;
import top.hcode.hoj.pojo.entity.group.Group;
import top.hcode.hoj.pojo.entity.training.*;
import top.hcode.hoj.pojo.vo.TrainingVO;
import top.hcode.hoj.shiro.AccountProfile;
import top.hcode.hoj.utils.Constants;
import top.hcode.hoj.validator.GroupValidator;
import top.hcode.hoj.validator.TrainingValidator;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: LengYun
 * @Date: 2022/3/11 13:36
 * @Description:
 */
@Component
public class GroupTrainingManager {

    @Autowired
    private GroupTrainingEntityService groupTrainingEntityService;

    @Autowired
    private GroupEntityService groupEntityService;

    @Autowired
    private TrainingEntityService trainingEntityService;

    @Autowired
    private TrainingCategoryEntityService trainingCategoryEntityService;

    @Autowired
    private MappingTrainingCategoryEntityService mappingTrainingCategoryEntityService;

    @Autowired
    private TrainingRegisterEntityService trainingRegisterEntityService;

    @Autowired
    private AdminTrainingRecordManager adminTrainingRecordManager;

    @Autowired
    private TrainingProblemEntityService trainingProblemEntityService;

    @Autowired
    private TrainingExamEntityService trainingExamEntityService;

    @Autowired
    private GroupValidator groupValidator;

    @Autowired
    private TrainingValidator trainingValidator;

    public IPage<TrainingVO> getTrainingList(Integer limit, Integer currentPage, Long gid) throws StatusNotFoundException, StatusForbiddenException {
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();

        boolean isRoot = SecurityUtils.getSubject().hasRole("root");

        Group group = groupEntityService.getById(gid);

        if (group == null || group.getStatus() == 1 && !isRoot) {
            throw new StatusNotFoundException("获取失败，该团队不存在或已被封禁！");
        }

        if (!isRoot && !groupValidator.isGroupMember(userRolesVo.getUid(), gid)) {
            throw new StatusForbiddenException("对不起，您无权限操作！");
        }

        if (currentPage == null || currentPage < 1) currentPage = 1;
        if (limit == null || limit < 1) limit = 10;

        return groupTrainingEntityService.getTrainingList(limit, currentPage, gid);
    }

    public IPage<Training> getAdminTrainingList(Integer limit, Integer currentPage, Long gid) throws StatusNotFoundException, StatusForbiddenException {
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();

        boolean isRoot = SecurityUtils.getSubject().hasRole("root");

        Group group = groupEntityService.getById(gid);

        if (group == null || group.getStatus() == 1 && !isRoot) {
            throw new StatusNotFoundException("获取失败，该团队不存在或已被封禁！");
        }

        if (!isRoot && !groupValidator.isGroupAdmin(userRolesVo.getUid(), gid)) {
            throw new StatusForbiddenException("对不起，您无权限操作！");
        }

        if (currentPage == null || currentPage < 1) currentPage = 1;
        if (limit == null || limit < 1) limit = 10;

        return groupTrainingEntityService.getAdminTrainingList(limit, currentPage, gid);
    }

    public TrainingDTO getTraining(Long tid) throws StatusForbiddenException, StatusNotFoundException, StatusFailException {
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();

        boolean isRoot = SecurityUtils.getSubject().hasRole("root");

        Training training = trainingEntityService.getById(tid);

        if (training == null) {
            throw new StatusNotFoundException("该训练不存在！");
        }

        Long gid = training.getGid();

        if (gid == null){
            throw new StatusForbiddenException("获取失败，不可访问非团队内的训练！");
        }

        Group group = groupEntityService.getById(gid);

        if (group == null || group.getStatus() == 1 && !isRoot) {
            throw new StatusNotFoundException("获取训练失败，该团队不存在或已被封禁！");
        }

        if (!userRolesVo.getUsername().equals(training.getAuthor())
                && !isRoot
                && !groupValidator.isGroupRoot(userRolesVo.getUid(), gid)) {
            throw new StatusForbiddenException("对不起，您无权限操作！");
        }

        TrainingDTO trainingDto = new TrainingDTO();
        trainingDto.setTraining(training);

        QueryWrapper<MappingTrainingCategory> mappingTrainingCategoryQueryWrapper = new QueryWrapper<>();
        mappingTrainingCategoryQueryWrapper.eq("tid", tid);

        MappingTrainingCategory mappingTrainingCategory = mappingTrainingCategoryEntityService.getOne(mappingTrainingCategoryQueryWrapper);
        TrainingCategory trainingCategory = null;

        if (mappingTrainingCategory != null) {
            trainingCategory = trainingCategoryEntityService.getById(mappingTrainingCategory.getCid());
        }
        trainingDto.setTrainingCategory(trainingCategory);
        return trainingDto;
    }

    @Transactional(rollbackFor = Exception.class)
    public void addTraining(TrainingDTO trainingDto) throws StatusForbiddenException, StatusNotFoundException, StatusFailException {

        trainingValidator.validateTraining(trainingDto.getTraining());

        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();

        boolean isRoot = SecurityUtils.getSubject().hasRole("root");

        Long gid = trainingDto.getTraining().getGid();
        if (gid == null){
            throw new StatusForbiddenException("添加失败，训练所属的团队ID不可为空！");
        }

        Group group = groupEntityService.getById(gid);

        if (group == null || group.getStatus() == 1 && !isRoot) {
            throw new StatusNotFoundException("添加训练失败，该团队不存在或已被封禁！");
        }

        if (!isRoot && !groupValidator.isGroupAdmin(userRolesVo.getUid(), gid)) {
            throw new StatusForbiddenException("对不起，您无权限操作！");
        }

        trainingDto.getTraining().setIsGroup(true);

        Training training = trainingDto.getTraining();
        trainingEntityService.save(training);
        TrainingCategory trainingCategory = trainingDto.getTrainingCategory();

        if (trainingCategory.getGid() != null && !Objects.equals(trainingCategory.getGid(), gid)) {
            throw new StatusForbiddenException("对不起，您无权限操作！");
        }

        if (trainingCategory.getId() == null) {
            try {
                trainingCategory.setGid(gid);
                trainingCategoryEntityService.save(trainingCategory);
            } catch (Exception ignored) {
                QueryWrapper<TrainingCategory> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("name", trainingCategory.getName());
                trainingCategory = trainingCategoryEntityService.getOne(queryWrapper, false);
            }
        }

        boolean isOk = mappingTrainingCategoryEntityService.save(new MappingTrainingCategory()
                .setTid(training.getId())
                .setCid(trainingCategory.getId()));
        if (!isOk) {
            throw new StatusFailException("添加失败！");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateTraining(TrainingDTO trainingDto) throws StatusForbiddenException, StatusNotFoundException, StatusFailException {

        trainingValidator.validateTraining(trainingDto.getTraining());

        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();

        boolean isRoot = SecurityUtils.getSubject().hasRole("root");

        Long tid = trainingDto.getTraining().getId();

        if (tid == null){
            throw new StatusForbiddenException("更新失败，训练ID不能为空！");
        }

        Training training = trainingEntityService.getById(tid);

        if (training == null) {
            throw new StatusNotFoundException("该训练不存在！");
        }

        Long gid = training.getGid();

        if (gid == null){
            throw new StatusForbiddenException("更新失败，不可操作非团队内的训练！");
        }

        Group group = groupEntityService.getById(gid);

        if (group == null || group.getStatus() == 1 && !isRoot) {
            throw new StatusNotFoundException("更新训练失败，该团队不存在或已被封禁！");
        }

        if (!userRolesVo.getUsername().equals(training.getAuthor()) && !isRoot
                && !groupValidator.isGroupRoot(userRolesVo.getUid(), gid)) {
            throw new StatusForbiddenException("对不起，您无权限操作！");
        }

        trainingDto.getTraining().setIsGroup(training.getIsGroup());

        trainingEntityService.updateById(trainingDto.getTraining());

        if (trainingDto.getTraining().getAuth().equals(Constants.Training.AUTH_PRIVATE.getValue())) {
            if (!Objects.equals(training.getPrivatePwd(), trainingDto.getTraining().getPrivatePwd())) {
                UpdateWrapper<TrainingRegister> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("tid", tid);
                trainingRegisterEntityService.remove(updateWrapper);
            }
        }

        TrainingCategory trainingCategory = trainingDto.getTrainingCategory();

        if (trainingCategory.getGid() != null && trainingCategory.getGid().longValue() != gid) {
            throw new StatusForbiddenException("对不起，您无权限操作！");
        }

        if (trainingCategory.getId() == null) {
            try {
                trainingCategory.setGid(gid);
                trainingCategoryEntityService.save(trainingCategory);
            } catch (Exception ignored) {
                QueryWrapper<TrainingCategory> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("name", trainingCategory.getName());
                trainingCategory = trainingCategoryEntityService.getOne(queryWrapper, false);
            }
        }

        MappingTrainingCategory mappingTrainingCategory = mappingTrainingCategoryEntityService
                .getOne(new QueryWrapper<MappingTrainingCategory>().eq("tid", training.getId()),
                        false);

        if (mappingTrainingCategory == null) {
            mappingTrainingCategoryEntityService.save(new MappingTrainingCategory()
                    .setTid(training.getId()).setCid(trainingCategory.getId()));
            adminTrainingRecordManager.checkSyncRecord(trainingDto.getTraining());
        } else {
            if (!mappingTrainingCategory.getCid().equals(trainingCategory.getId())) {
                UpdateWrapper<MappingTrainingCategory> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("tid", training.getId()).set("cid", trainingCategory.getId());
                boolean isOk = mappingTrainingCategoryEntityService.update(null, updateWrapper);
                if (isOk) {
                    adminTrainingRecordManager.checkSyncRecord(trainingDto.getTraining());
                } else {
                    throw new StatusFailException("修改失败");
                }
            }
        }
    }

    public void deleteTraining(Long tid) throws StatusForbiddenException, StatusNotFoundException, StatusFailException {
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();

        boolean isRoot = SecurityUtils.getSubject().hasRole("root");

        Training training = trainingEntityService.getById(tid);

        if (training == null) {
            throw new StatusNotFoundException("该训练不存在！");
        }

        Long gid = training.getGid();

        if (gid == null){
            throw new StatusForbiddenException("删除失败，不可操作非团队内的训练！");
        }

        Group group = groupEntityService.getById(gid);

        if (group == null || group.getStatus() == 1 && !isRoot) {
            throw new StatusNotFoundException("删除训练失败，该团队不存在或已被封禁！");
        }

        if (!userRolesVo.getUsername().equals(training.getAuthor()) && !isRoot
                && !groupValidator.isGroupRoot(userRolesVo.getUid(), gid)) {
            throw new StatusForbiddenException("对不起，您无权限操作！");
        }

        boolean isOk = trainingEntityService.removeById(tid);
        if (!isOk) {
            throw new StatusFailException("删除失败！");
        }
    }

    public void changeTrainingStatus(Long tid, Boolean status) throws StatusForbiddenException, StatusNotFoundException, StatusFailException {
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();

        boolean isRoot = SecurityUtils.getSubject().hasRole("root");

        Training training = trainingEntityService.getById(tid);

        if (training == null) {
            throw new StatusNotFoundException("该训练不存在！");
        }

        Long gid = training.getGid();

        if (gid == null){
            throw new StatusForbiddenException("修改失败，不可操作非团队内的训练！");
        }

        Group group = groupEntityService.getById(gid);

        if (group == null || group.getStatus() == 1 && !isRoot) {
            throw new StatusNotFoundException("修改训练失败，该团队不存在或已被封禁！");
        }

        if (!userRolesVo.getUsername().equals(training.getAuthor()) && !isRoot
                && !groupValidator.isGroupRoot(userRolesVo.getUid(), gid)) {
            throw new StatusForbiddenException("对不起，您无权限操作！");
        }

        UpdateWrapper<Training> trainingUpdateWrapper = new UpdateWrapper<>();
        trainingUpdateWrapper.eq("id", tid).set("status", status);

        boolean isOk = trainingEntityService.update(trainingUpdateWrapper);
        if (!isOk) {
            throw new StatusFailException("修改失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void addTrainingFromPublic(List<Long> tidList, Long groupId) throws StatusFailException {
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();

        Map<Long, Long> trainingIdMap = new HashMap<>();
        // 获得所有training
        Collection<Training> trainings = trainingEntityService.listByIds(tidList);

        // 过滤掉auth字段为"Private"的training
        trainings = trainings.stream()
                .filter(training -> !"Private".equals(training.getAuth()))
                .collect(Collectors.toList());

        if (trainings.size() != tidList.size()) {
            Set<Long> foundIds = trainings.stream().map(Training::getId).collect(Collectors.toSet());
            List<Long> notFoundIds = tidList.stream().filter(id -> !foundIds.contains(id)).collect(Collectors.toList());

            if (!notFoundIds.isEmpty()) {
                throw new StatusFailException("部分题单id不正确，请确认是否存在！" + notFoundIds);
            }
        }
        List<Long> trainingIdList = trainings.stream().map(Training::getId).collect(Collectors.toList());

        // 获得所有training 题目
        QueryWrapper<TrainingProblem> trainingProblemQueryWrapper = new QueryWrapper<>();
        trainingProblemQueryWrapper.in("tid", trainingIdList);
        List<TrainingProblem> trainingProblemList = trainingProblemEntityService.list(trainingProblemQueryWrapper);

        // 获得所有training类别
        QueryWrapper<MappingTrainingCategory> mappingTrainingCategoryQueryWrapper = new QueryWrapper<>();
        mappingTrainingCategoryQueryWrapper.in("tid", trainingIdList);
        List<MappingTrainingCategory> mappingTrainingCategoryList = mappingTrainingCategoryEntityService.list(mappingTrainingCategoryQueryWrapper);

        // 获得所有training Exam
        QueryWrapper<TrainingExam> trainingExamQueryWrapper = new QueryWrapper<>();
        trainingExamQueryWrapper.in("tid", trainingIdList);
        List<TrainingExam> trainingExamList = trainingExamEntityService.list(trainingExamQueryWrapper);

        // 插入新 training
        for (Training training : trainings) {
            Long preId = training.getId();
            training.setIsGroup(true);
            training.setGid(groupId);
            training.setId(null);
            training.setGmtCreate(null);
            training.setGmtModified(null);
            training.setPrivatePwd(null);
            training.setAuth("Public");
            training.setAuthor(userRolesVo.getUsername());

            boolean insertNewTrainings = trainingEntityService.save(training);
            if (!insertNewTrainings) {
                throw new StatusFailException("题单复制失败！");
            }
            trainingIdMap.put(preId, training.getId());
        }

        for (TrainingProblem trainingProblem : trainingProblemList) {
            trainingProblem.setTid(trainingIdMap.get(trainingProblem.getTid()));
            trainingProblem.setId(null);
            trainingProblem.setGmtCreate(null);
            trainingProblem.setGmtModified(null);
        }
        boolean insertNewTrainingProblem = trainingProblemEntityService.saveBatch(trainingProblemList);

        if (!insertNewTrainingProblem) {
            throw new StatusFailException("题单题目复制失败！");
        }

        for (MappingTrainingCategory mappingTrainingCategory : mappingTrainingCategoryList) {
            mappingTrainingCategory.setTid(trainingIdMap.get(mappingTrainingCategory.getTid()));
            mappingTrainingCategory.setId(null);
            mappingTrainingCategory.setGmtCreate(null);
            mappingTrainingCategory.setGmtModified(null);
        }

        boolean insertNewTrainingCategory = mappingTrainingCategoryEntityService.saveBatch(mappingTrainingCategoryList);
        if (!insertNewTrainingCategory) {
            throw new StatusFailException("题单类型复制失败！");
        }

        // 复制试卷
        for (TrainingExam trainingExam : trainingExamList) {
            trainingExam.setTid(trainingIdMap.get(trainingExam.getTid()));
            trainingExam.setId(null);
            trainingExam.setGmtCreate(null);
            trainingExam.setGmtModified(null);
            trainingExam.setGid(groupId);
        }

        boolean insertNewTrainingExam = trainingExamEntityService.saveBatch(trainingExamList);
        if (!insertNewTrainingExam) {
            throw new StatusFailException("题单试卷复制失败！请检查是否有重复试卷");
        }

    }
}
