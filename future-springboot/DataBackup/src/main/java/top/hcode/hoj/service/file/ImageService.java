package top.hcode.hoj.service.file;

import org.springframework.web.multipart.MultipartFile;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.pojo.entity.group.Group;
import top.hcode.hoj.pojo.vo.AdminCourseVO;

import java.util.Map;

public interface ImageService {

    public CommonResult<Map<Object, Object>> uploadAvatar(MultipartFile image);

    public CommonResult<Group> uploadGroupAvatar(MultipartFile image, Long gid);

    public CommonResult<Map<Object, Object>> uploadCarouselImg(MultipartFile image);

    CommonResult<String> uploadCourseImg(MultipartFile image, Long courseId);

    CommonResult<String> uploadTrainingCampImg(MultipartFile image, Long trainingCampId);

    CommonResult<String> uploadImg(MultipartFile image);

    CommonResult<Void> deleteImg(String url);

    CommonResult<String> uploadContestImg(MultipartFile image, Long contestId);
}
