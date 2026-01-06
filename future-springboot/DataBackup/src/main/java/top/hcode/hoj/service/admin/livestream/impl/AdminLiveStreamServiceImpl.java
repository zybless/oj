package top.hcode.hoj.service.admin.livestream.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.dao.livestream.LiveStreamEntityService;
import top.hcode.hoj.pojo.entity.livestream.LiveStream;
import top.hcode.hoj.service.admin.livestream.AdminLiveStreamService;

/**
 * @Author: Minipax
 * @Date: 2023/12/1 14:55
 */

@Service
public class AdminLiveStreamServiceImpl implements AdminLiveStreamService {

    @Autowired
    private LiveStreamEntityService liveStreamEntityService;

    @Override
    public CommonResult<String> getLiveStream() {
        LiveStream liveStream = liveStreamEntityService.getById(1);
        return CommonResult.successResponse(liveStream.getUrl());
    }

    @Override
    public CommonResult<Void> updateLiveStream(String url) {
        LiveStream liveStream = new LiveStream();
        liveStream.setId(1L);
        liveStream.setUrl(url);
        liveStreamEntityService.saveOrUpdate(liveStream);
        return CommonResult.successResponse();
    }
}
