package top.hcode.hoj.dao.payment.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.hcode.hoj.dao.payment.VipInfoEntityService;
import top.hcode.hoj.mapper.VipInfoMapper;
import top.hcode.hoj.pojo.entity.payment.VipInfo;

/**
 * @Author: Minipax
 * @Date: 2023/10/9 20:04
 */
@Service
public class VipInfoEntityServiceImpl extends ServiceImpl<VipInfoMapper, VipInfo> implements VipInfoEntityService {
}
