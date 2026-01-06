package top.hcode.hoj.dao.livestream.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.hcode.hoj.dao.livestream.LiveStreamEntityService;
import top.hcode.hoj.mapper.LiveStreamMapper;
import top.hcode.hoj.pojo.entity.livestream.LiveStream;

/**
 * @Author: Minipax
 * @Date: 2023/12/1 14:59
 */
@Service
public class LiveStreamEntityServiceImpl extends ServiceImpl<LiveStreamMapper, LiveStream> implements LiveStreamEntityService {
}
