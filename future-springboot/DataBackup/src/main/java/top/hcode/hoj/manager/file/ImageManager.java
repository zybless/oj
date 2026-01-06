package top.hcode.hoj.manager.file;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.hcode.hoj.common.exception.StatusFailException;
import top.hcode.hoj.common.exception.StatusForbiddenException;
import top.hcode.hoj.common.exception.StatusSystemErrorException;
import top.hcode.hoj.dao.common.FileEntityService;
import top.hcode.hoj.dao.contest.ContestEntityService;
import top.hcode.hoj.dao.course.CourseEntityService;
import top.hcode.hoj.dao.group.GroupEntityService;
import top.hcode.hoj.dao.trainingcampteam.TrainingCampEntityService;
import top.hcode.hoj.dao.user.UserInfoEntityService;
import top.hcode.hoj.dao.user.UserRoleEntityService;
import top.hcode.hoj.pojo.entity.contest.Contest;
import top.hcode.hoj.pojo.entity.course.Course;
import top.hcode.hoj.pojo.entity.group.Group;
import top.hcode.hoj.pojo.entity.training.TrainingCamp;
import top.hcode.hoj.pojo.entity.user.Role;
import top.hcode.hoj.pojo.entity.user.UserInfo;
import top.hcode.hoj.pojo.vo.UserRolesVO;
import top.hcode.hoj.shiro.AccountProfile;
import top.hcode.hoj.utils.Constants;
import top.hcode.hoj.validator.GroupValidator;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @Author: Himit_ZH
 * @Date: 2022/3/10 14:31
 * @Description:
 */
@Component
@Slf4j(topic = "hoj")
public class ImageManager {

    @Autowired
    private FileEntityService fileEntityService;

    @Autowired
    private UserInfoEntityService userInfoEntityService;

    @Autowired
    private GroupEntityService groupEntityService;

    @Autowired
    private GroupValidator groupValidator;

    @Autowired
    private UserRoleEntityService userRoleEntityService;

    @Autowired
    private CourseEntityService courseEntityService;

    @Autowired
    private TrainingCampEntityService trainingCampEntityService;

    @Autowired
    private ContestEntityService contestEntityService;

    @Transactional(rollbackFor = Exception.class)
    public Map<Object, Object> uploadAvatar(MultipartFile image) throws StatusFailException, StatusSystemErrorException {
        if (image == null) {
            throw new StatusFailException("上传的头像图片文件不能为空！");
        }
        if (image.getSize() > 1024 * 1024 * 2) {
            throw new StatusFailException("上传的头像图片文件大小不能大于2M！");
        }
        //获取文件后缀
        String suffix = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf(".") + 1);
        if (!"jpg,jpeg,gif,png,webp".toUpperCase().contains(suffix.toUpperCase())) {
            throw new StatusFailException("请选择jpg,jpeg,gif,png,webp格式的头像图片！");
        }
        //若不存在该目录，则创建目录
        FileUtil.mkdir(Constants.File.USER_AVATAR_FOLDER.getPath());
        //通过UUID生成唯一文件名
        String filename = IdUtil.simpleUUID() + "." + suffix;
        try {
            //将文件保存指定目录
            image.transferTo(FileUtil.file(new File(Constants.File.USER_AVATAR_FOLDER.getPath()).getAbsolutePath() + File.separator + filename));
        } catch (Exception e) {
            log.error("头像文件上传异常-------------->", e);
            throw new StatusSystemErrorException("服务器异常：头像上传失败！");
        }

        // 获取当前登录用户
        AccountProfile accountProfile = (AccountProfile) SecurityUtils.getSubject().getPrincipal();

        // 将当前用户所属的file表中avatar类型的实体的delete设置为1；
        fileEntityService.updateFileToDeleteByUidAndType(accountProfile.getUid(), "avatar");

        //更新user_info里面的avatar
        UpdateWrapper<UserInfo> userInfoUpdateWrapper = new UpdateWrapper<>();
        userInfoUpdateWrapper.set("avatar", Constants.File.IMG_API.getPath() + filename)
                .eq("uuid", accountProfile.getUid());
        userInfoEntityService.update(userInfoUpdateWrapper);

        // 插入file表记录
        top.hcode.hoj.pojo.entity.common.File imgFile = new top.hcode.hoj.pojo.entity.common.File();
        imgFile.setName(filename).setFolderPath(Constants.File.USER_AVATAR_FOLDER.getPath())
                .setFilePath(Constants.File.USER_AVATAR_FOLDER.getPath() + File.separator + filename)
                .setSuffix(suffix)
                .setType("avatar")
                .setUid(accountProfile.getUid());
        fileEntityService.saveOrUpdate(imgFile);
        // 更新session
        accountProfile.setAvatar(Constants.File.IMG_API.getPath() + filename);

        UserRolesVO userRolesVo = userRoleEntityService.getUserRoles(accountProfile.getUid(), null);

        return MapUtil.builder()
                .put("uid", userRolesVo.getUid())
                .put("username", userRolesVo.getUsername())
                .put("nickname", userRolesVo.getNickname())
                .put("avatar", Constants.File.IMG_API.getPath() + filename)
                .put("email", userRolesVo.getEmail())
                .put("number", userRolesVo.getNumber())
                .put("school", userRolesVo.getSchool())
                .put("course", userRolesVo.getCourse())
                .put("signature", userRolesVo.getSignature())
                .put("realname", userRolesVo.getRealname())
                .put("github", userRolesVo.getGithub())
                .put("blog", userRolesVo.getBlog())
                .put("cfUsername", userRolesVo.getCfUsername())
                .put("roleList", userRolesVo.getRoles().stream().map(Role::getRole))
                .map();
    }

    @Transactional(rollbackFor = Exception.class)
    public Group uploadGroupAvatar(MultipartFile image, Long gid) throws StatusFailException, StatusSystemErrorException, StatusForbiddenException {
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();

        boolean isRoot = SecurityUtils.getSubject().hasRole("root");
        if (!isRoot && !groupValidator.isGroupRoot(userRolesVo.getUid(), gid)) {
            throw new StatusForbiddenException("对不起，您无权限操作！");
        }

        if (image == null) {
            throw new StatusFailException("上传的头像图片文件不能为空！");
        }
        if (image.getSize() > 1024 * 1024 * 2) {
            throw new StatusFailException("上传的头像图片文件大小不能大于2M！");
        }

        String suffix = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf(".") + 1);
        if (!"jpg,jpeg,gif,png,webp".toUpperCase().contains(suffix.toUpperCase())) {
            throw new StatusFailException("请选择jpg,jpeg,gif,png,webp格式的头像图片！");
        }

        FileUtil.mkdir(Constants.File.GROUP_AVATAR_FOLDER.getPath());

        String filename = IdUtil.simpleUUID() + "." + suffix;
        try {
            image.transferTo(FileUtil.file(new File(Constants.File.GROUP_AVATAR_FOLDER.getPath()).getAbsolutePath() + File.separator + filename));
        } catch (Exception e) {
            log.error("头像文件上传异常-------------->", e);
            throw new StatusSystemErrorException("服务器异常：头像上传失败！");
        }

        fileEntityService.updateFileToDeleteByGidAndType(gid, "avatar");

        UpdateWrapper<Group> GroupUpdateWrapper = new UpdateWrapper<>();
        GroupUpdateWrapper.set("avatar", Constants.File.IMG_API.getPath() + filename)
                .eq("id", gid);
        groupEntityService.update(GroupUpdateWrapper);

        top.hcode.hoj.pojo.entity.common.File imgFile = new top.hcode.hoj.pojo.entity.common.File();
        imgFile.setName(filename).setFolderPath(Constants.File.GROUP_AVATAR_FOLDER.getPath())
                .setFilePath(Constants.File.GROUP_AVATAR_FOLDER.getPath() + File.separator + filename)
                .setSuffix(suffix)
                .setType("avatar")
                .setGid(gid);
        fileEntityService.saveOrUpdate(imgFile);

        Group group = groupEntityService.getById(gid);

        return group;
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<Object, Object> uploadCarouselImg(MultipartFile image) throws StatusFailException, StatusSystemErrorException {

        if (image == null) {
            throw new StatusFailException("上传的图片文件不能为空！");
        }

        //获取文件后缀
        String suffix = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf(".") + 1);
        if (!"jpg,jpeg,gif,png,webp,jfif,svg".toUpperCase().contains(suffix.toUpperCase())) {
            throw new StatusFailException("请选择jpg,jpeg,gif,png,webp,jfif,svg格式的头像图片！");
        }
        //若不存在该目录，则创建目录
        FileUtil.mkdir(Constants.File.HOME_CAROUSEL_FOLDER.getPath());
        //通过UUID生成唯一文件名
        String filename = IdUtil.simpleUUID() + "." + suffix;
        try {
            //将文件保存指定目录
            image.transferTo(FileUtil.file(new File(Constants.File.HOME_CAROUSEL_FOLDER.getPath()).getAbsolutePath() + File.separator + filename));
        } catch (Exception e) {
            log.error("图片文件上传异常-------------->{}", e.getMessage());
            throw new StatusSystemErrorException("服务器异常：图片上传失败！");
        }

        // 获取当前登录用户
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();


        // 插入file表记录
        top.hcode.hoj.pojo.entity.common.File imgFile = new top.hcode.hoj.pojo.entity.common.File();
        imgFile.setName(filename).setFolderPath(Constants.File.HOME_CAROUSEL_FOLDER.getPath())
                .setFilePath(Constants.File.HOME_CAROUSEL_FOLDER.getPath() + File.separator + filename)
                .setSuffix(suffix)
                .setType("carousel")
                .setUid(userRolesVo.getUid());
        fileEntityService.saveOrUpdate(imgFile);

        return MapUtil.builder()
                .put("id", imgFile.getId())
                .put("url", Constants.File.IMG_API.getPath() + filename)
                .map();
    }

    @Transactional(rollbackFor = Exception.class)
    public String uploadCourseImg(MultipartFile image, Long courseId) throws StatusForbiddenException, StatusFailException, StatusSystemErrorException {
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();

        boolean isRoot = SecurityUtils.getSubject().hasRole("root");
        if (!isRoot) {
            throw new StatusForbiddenException("对不起，您无权限操作！");
        }

        if (image == null) {
            throw new StatusFailException("上传的课程图片文件不能为空！");
        }
        if (image.getSize() > 1024 * 1024 * 1) {
            throw new StatusFailException("上传的图片文件大小不能大于1MB！");
        }

        String suffix = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf(".") + 1);
        if (!"jpg,jpeg,gif,png,webp".toUpperCase().contains(suffix.toUpperCase())) {
            throw new StatusFailException("请选择jpg,jpeg,gif,png,webp格式的图片！");
        }

        FileUtil.mkdir(Constants.File.COURSE_IMG_FOLDER.getPath());

        String filename = IdUtil.simpleUUID() + "." + suffix;
        try {
            image.transferTo(FileUtil.file(new File(Constants.File.COURSE_IMG_FOLDER.getPath()).getAbsolutePath() + File.separator + filename));
        } catch (Exception e) {
            log.error("课程图片上传异常-------------->", e);
            throw new StatusSystemErrorException("服务器异常：课程图片上传失败！");
        }

        fileEntityService.updateFileToDeleteByCidAndType(courseId, "image");

        UpdateWrapper<Course> courseUpdateWrapper = new UpdateWrapper<>();
        courseUpdateWrapper.set("image", Constants.File.COURSE_IMG_FOLDER.getPath() + filename)
                .eq("id", courseId);
        courseEntityService.update(courseUpdateWrapper);

        top.hcode.hoj.pojo.entity.common.File imgFile = new top.hcode.hoj.pojo.entity.common.File();
        imgFile.setName(filename).setFolderPath(Constants.File.COURSE_IMG_FOLDER.getPath())
                .setFilePath(Constants.File.COURSE_IMG_FOLDER.getPath() + File.separator + filename)
                .setSuffix(suffix)
                .setType("avatar")
                .setCid(courseId)
                .setUid(userRolesVo.getUid());
        fileEntityService.saveOrUpdate(imgFile);

        return Constants.File.COURSE_IMG_FOLDER.getPath() + filename;
    }

    @Transactional(rollbackFor = Exception.class)
    public String uploadContestImg(MultipartFile image, Long contestId) throws StatusFailException, StatusSystemErrorException {
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();

        if (image == null) {
            throw new StatusFailException("上传的课程图片文件不能为空！");
        }
        if (image.getSize() > 1024 * 1024) {
            throw new StatusFailException("上传的图片文件大小不能大于1MB！");
        }

        String suffix = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf(".") + 1);
        if (!"jpg,jpeg,gif,png,webp".toUpperCase().contains(suffix.toUpperCase())) {
            throw new StatusFailException("请选择jpg,jpeg,gif,png,webp格式的图片！");
        }

        FileUtil.mkdir(Constants.File.CONTEST_IMG_FOLDER.getPath());

        String filename = IdUtil.simpleUUID() + "." + suffix;
        try {
            image.transferTo(FileUtil.file(new File(Constants.File.CONTEST_IMG_FOLDER.getPath()).getAbsolutePath() + File.separator + filename));
        } catch (Exception e) {
            log.error("课程图片上传异常-------------->", e);
            throw new StatusSystemErrorException("服务器异常：课程图片上传失败！");
        }

        UpdateWrapper<Contest> contestUpdateWrapper = new UpdateWrapper<>();
        contestUpdateWrapper.set("image", Constants.File.CONTEST_IMG_FOLDER.getPath() + filename)
                .eq("id", contestId);
        contestEntityService.update(contestUpdateWrapper);

        top.hcode.hoj.pojo.entity.common.File imgFile = new top.hcode.hoj.pojo.entity.common.File();
        imgFile.setName(filename).setFolderPath(Constants.File.CONTEST_IMG_FOLDER.getPath())
                .setFilePath(Constants.File.CONTEST_IMG_FOLDER.getPath() + File.separator + filename)
                .setSuffix(suffix)
                .setType("contest_image")
                .setCid(contestId)
                .setUid(userRolesVo.getUid());
        fileEntityService.saveOrUpdate(imgFile);

        return Constants.File.CONTEST_IMG_FOLDER.getPath() + filename;
    }

    public String uploadTrainingCampImg(MultipartFile image, Long trainingCampId) throws StatusForbiddenException, StatusFailException, StatusSystemErrorException {
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();

        boolean isRoot = SecurityUtils.getSubject().hasRole("root");
        if (!isRoot) {
            throw new StatusForbiddenException("对不起，您无权限操作！");
        }

        if (image == null) {
            throw new StatusFailException("上传的集训图片不能为空！");
        }
        if (image.getSize() > 1024 * 1024) {
            throw new StatusFailException("上传的头像图片文件大小不能大于1MB！");
        }

        String suffix = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf(".") + 1);
        if (!"jpg,jpeg,gif,png,webp".toUpperCase().contains(suffix.toUpperCase())) {
            throw new StatusFailException("请选择jpg,jpeg,gif,png,webp格式的图片！");
        }

        FileUtil.mkdir(Constants.File.TRAINING_CAMP_IMG_FOLDER.getPath());


        String filename = IdUtil.simpleUUID() + "." + suffix;
        try {
            image.transferTo(FileUtil.file(new File(Constants.File.TRAINING_CAMP_IMG_FOLDER.getPath()).getAbsolutePath() + File.separator + filename));
        } catch (Exception e) {
            log.error("集训图片上传异常-------------->", e);
            throw new StatusSystemErrorException("服务器异常：集训图片上传失败！");
        }

//        fileEntityService.updateFileToDeleteByCidAndType(trainingCampId, "image");

        UpdateWrapper<TrainingCamp> trainingCampUpdateWrapper = new UpdateWrapper<>();
        trainingCampUpdateWrapper.set("image", Constants.File.TRAINING_CAMP_IMG_FOLDER.getPath() + filename)
                .eq("id", trainingCampId);
        trainingCampEntityService.update(trainingCampUpdateWrapper);

        top.hcode.hoj.pojo.entity.common.File imgFile = new top.hcode.hoj.pojo.entity.common.File();
        imgFile.setName(filename).setFolderPath(Constants.File.TRAINING_CAMP_IMG_FOLDER.getPath())
                .setFilePath(Constants.File.TRAINING_CAMP_IMG_FOLDER.getPath() + File.separator + filename)
                .setSuffix(suffix)
                .setType("avatar")
                .setCid(trainingCampId);
        fileEntityService.saveOrUpdate(imgFile);

        return Constants.File.TRAINING_CAMP_IMG_FOLDER.getPath() + filename;
    }

    public String uploadImg(MultipartFile image) throws StatusForbiddenException, StatusFailException, StatusSystemErrorException {
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();

        if (image == null) {
            throw new StatusFailException("上传的图片文件不能为空！");
        }

        if (image.getSize() > 1024 * 1024) {
            throw new StatusSystemErrorException("上传的图片文件大小不能大于1MB！");
        }

        String suffix = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf(".") + 1);
        if (!"jpg,jpeg,gif,png,webp".toUpperCase().contains(suffix.toUpperCase())) {
            throw new StatusFailException("请选择jpg,jpeg,gif,png,webp格式的图片！");
        }

        FileUtil.mkdir(Constants.File.DEFAULT_IMG_FOLDER.getPath());

        String filename = IdUtil.simpleUUID() + "." + suffix;
        try {
            image.transferTo(FileUtil.file(new File(Constants.File.DEFAULT_IMG_FOLDER.getPath()).getAbsolutePath() + File.separator + filename));
        } catch (Exception e) {
            log.error("课程图片上传异常-------------->", e);
            throw new StatusSystemErrorException("服务器异常：图片上传失败！");
        }



        top.hcode.hoj.pojo.entity.common.File imgFile = new top.hcode.hoj.pojo.entity.common.File();
        imgFile.setName(filename).setFolderPath(Constants.File.DEFAULT_IMG_FOLDER.getPath())
                .setFilePath(Constants.File.DEFAULT_IMG_FOLDER.getPath() + File.separator + filename)
                .setSuffix(suffix)
                .setType("default");
        fileEntityService.saveOrUpdate(imgFile);

        return Constants.File.IMG_API.getPath() + filename;
    }

    public void deleteImg(String url) throws StatusForbiddenException, StatusFailException {
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();

        boolean isRoot = SecurityUtils.getSubject().hasRole("root");
        if (!isRoot) {
            throw new StatusForbiddenException("对不起，您无权限操作！");
        }

        QueryWrapper<top.hcode.hoj.pojo.entity.common.File> fileQueryWrapper = new QueryWrapper<>();
        fileQueryWrapper.eq("file_path", url);
        List<top.hcode.hoj.pojo.entity.common.File> fileList = fileEntityService.list(fileQueryWrapper);
        if(fileList == null || fileList.size() == 0) {
            throw new StatusFailException("不存在这个图片");
        }

        top.hcode.hoj.pojo.entity.common.File file = fileList.get(0);
        file.setDelete(Boolean.TRUE);
        fileEntityService.saveOrUpdate(file);

    }

}