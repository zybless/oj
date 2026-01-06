package top.hcode.hoj.judge;

import cn.hutool.json.JSONObject;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.hcode.hoj.manager.admin.system.ConfigManager;
import top.hcode.hoj.mapper.RemoteJudgeAccountMapper;
import top.hcode.hoj.dao.judge.JudgeServerEntityService;
import top.hcode.hoj.pojo.entity.judge.JudgeServer;
import top.hcode.hoj.pojo.entity.judge.RemoteJudgeAccount;
import top.hcode.hoj.utils.Constants;
import top.hcode.hoj.utils.RedisUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: Himit_ZH
 * @Date: 2021/5/24 17:30
 * @Description: 筛选可用判题机
 */
@Component
@Slf4j(topic = "hoj")
public class ChooseUtils {

    @Autowired
    private NacosDiscoveryProperties discoveryProperties;

    @Value("${service-url.name}")
    private String JudgeServiceName;

    @Autowired
    private JudgeServerEntityService judgeServerEntityService;

    @Autowired
    private RemoteJudgeAccountMapper remoteJudgeAccountMapper;

    @Autowired
    private ConfigManager configManager;

    public static final boolean openCodeforcesFixServer = false;
    @Autowired
    private RedisUtils redisUtils;

    /**
     * @param
     * @MethodName chooseServer
     * @Description 选择可以用调用判题的判题服务器
     * @Return
     * @Since 2021/4/15
     */
//    @Transactional(rollbackFor = Exception.class)
//    public JudgeServer chooseServer(Boolean isRemote) {
//        // 获取该微服务的所有健康实例
//        List<Instance> instances = getInstances(JudgeServiceName);
//        if (instances.size() <= 0) {
//            return null;
//        }
//        List<String> keyList = new ArrayList<>();
//        // 获取当前健康实例取出ip和port拼接
//        for (Instance instance : instances) {
//            keyList.add(instance.getIp() + ":" + instance.getPort());
//        }
//
//        List<JSONObject> judgeServiceInfo = configManager.getJudgeServiceInfo();
//
//        // 过滤出小于或等于规定最大并发判题任务数的服务实例且健康的判题机
//        QueryWrapper<JudgeServer> judgeServerQueryWrapper = new QueryWrapper<>();
//        judgeServerQueryWrapper
//                .in("url", keyList)
//                .eq("is_remote", isRemote)
//                .orderByAsc("task_number")
//                .last("for update"); // 开启悲观锁
//
//        /**
//         * 如果一个条件无法通过索引快速过滤，存储引擎层面就会将所有记录加锁后返回，
//         * 再由MySQL Server层进行过滤，但在实际使用过程当中，MySQL做了一些改进，
//         * 在MySQL Server过滤条件，发现不满足后，会调用unlock_row方法，
//         * 把不满足条件的记录释放锁 (违背了二段锁协议的约束)。
//         */
//        List<JudgeServer> judgeServerList = judgeServerEntityService.list(judgeServerQueryWrapper);
//
//        // 获取可用判题机
//        for (JudgeServer judgeServer : judgeServerList) {
//            if (judgeServer.getTaskNumber() < judgeServer.getMaxTaskNumber()) {
//                judgeServer.setTaskNumber(judgeServer.getTaskNumber() + 1);
//                boolean isOk = judgeServerEntityService.updateById(judgeServer);
//                if (isOk) {
//                    return judgeServer;
//                }
//            }
//        }
//
//        return null;
//    }

    /**
     * 选择判题服务器。
     * @param isRemote
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JudgeServer chooseServer(Boolean isRemote) {

        // 获取健康实例（ip:port）
        List<Instance> instances = getInstances(JudgeServiceName);
        if (instances.isEmpty()) return null;

        List<String> keyList = instances.stream()
                .map(i -> i.getIp() + ":" + i.getPort())
                .collect(Collectors.toList());

        // 心跳信息
        List<JSONObject> judgeServiceInfo = (List<JSONObject>)redisUtils.get(Constants.RedisKey.JUDGE_SERVER_INFO.getKey());
        if (judgeServiceInfo == null || judgeServiceInfo.isEmpty()) {
            judgeServiceInfo = configManager.getJudgeServiceInfo();
        }
        // 查 DB 并锁表
        List<JudgeServer> judgeServerList = judgeServerEntityService.list(
                new QueryWrapper<JudgeServer>()
                        .in("url", keyList)
                        .eq("is_remote", isRemote)
                        .last("for update")
        );

        // 映射 url -> 心跳信息
        Map<String, JSONObject> infoMap = judgeServiceInfo.stream()
                .collect(Collectors.toMap(
                        js -> js.getJSONObject("service").getStr("host"),
                        js -> js
                ));

        JudgeServer best = null;
        double bestScore = Double.MAX_VALUE;

        for (JudgeServer js : judgeServerList) {

            if (js.getTaskNumber() >= js.getMaxTaskNumber()) {
                continue; // 忙
            }

            JSONObject info = infoMap.get(js.getIp());
            if (info == null) {
                continue; // 没心跳，不选
            }

            // 使用 Hutool 读取字符串百分比: "46.91%" -> 46.91
            double cpu = parsePercent(info.getStr("percentCpuLoad"));
            double mem = parsePercent(info.getStr("percentMemoryLoad"));
            double task = (double) js.getTaskNumber() / js.getMaxTaskNumber();

            // 负载评分公式（你可以调整权重）
            double score = cpu * 0.4 + mem * 0.3 + task * 0.3;

            if (score < bestScore) {
                bestScore = score;
                best = js;
            }
        }

        if (best == null) return null;

        // 更新 taskNumber
        best.setTaskNumber(best.getTaskNumber() + 1);
        judgeServerEntityService.updateById(best);
        log.info("选择判题机：{}", best.getIp());

        return best;
    }


    /** "46.91%" -> 46.91 */
    private double parsePercent(String str) {
        if (str == null) return 100;
        return Double.parseDouble(str.replace("%", ""));
    }

    @Transactional(rollbackFor = Exception.class)
    @Deprecated
    public JudgeServer chooseFixedServer(Boolean isRemote, String fixedTag, Integer index, Integer total) {
        // 获取该微服务的所有健康实例
        List<Instance> instances = getInstances(JudgeServiceName);
        if (instances.size() <= 0) {
            return null;
        }
        List<String> keyList = new ArrayList<>();
        // 获取当前健康实例取出ip和port拼接
        for (Instance instance : instances) {
            keyList.add(instance.getIp() + ":" + instance.getPort());
        }

        // 过滤出小于或等于规定最大并发判题任务数的服务实例且健康的判题机
        QueryWrapper<JudgeServer> judgeServerQueryWrapper = new QueryWrapper<>();
        judgeServerQueryWrapper
                .in("url", keyList)
                .eq("is_remote", isRemote)
                .last("for update"); // 开启悲观锁

        /**
         * 如果一个条件无法通过索引快速过滤，存储引擎层面就会将所有记录加锁后返回，
         * 再由MySQL Server层进行过滤，但在实际使用过程当中，MySQL做了一些改进，
         * 在MySQL Server过滤条件，发现不满足后，会调用unlock_row方法，
         * 把不满足条件的记录释放锁 (违背了二段锁协议的约束)。
         */
        List<JudgeServer> judgeServerList = judgeServerEntityService.list(judgeServerQueryWrapper);
        // CF的VJ判題需要一机一题(根据序号保持一定的固定)
        int len = judgeServerList.size();
        for (int i = 0; i < len; i++) {
            if (i % total == index) {
                JudgeServer judgeServer = judgeServerList.get(i);
                UpdateWrapper<JudgeServer> judgeServerUpdateWrapper = new UpdateWrapper<>();
                judgeServerUpdateWrapper.set(fixedTag, false)
                        .eq("id", judgeServer.getId())
                        .eq(fixedTag, true);
                boolean isOk = judgeServerEntityService.update(judgeServerUpdateWrapper);
                if (isOk) {
                    return judgeServer;
                }
            }
        }
        return null;
    }


    /**
     * @param serviceId
     * @MethodName getInstances
     * @Description 根据服务id获取对应的健康实例列表
     * @Return
     * @Since 2021/4/15
     */
    private List<Instance> getInstances(String serviceId) {
        // 获取服务发现的相关API
        NamingService namingService = discoveryProperties.namingServiceInstance();
        try {
            // 获取该微服务的所有健康实例
            return namingService.selectInstances(serviceId, true);
        } catch (NacosException e) {
            log.error("获取微服务健康实例发生异常--------->{}", e);
            return Collections.emptyList();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public RemoteJudgeAccount chooseRemoteAccount(String remoteOJAccountType, String username, Boolean isNeedAccountRejudge) {

        // 过滤出当前远程oj可用的账号列表 悲观锁
        List<RemoteJudgeAccount> remoteJudgeAccountList = remoteJudgeAccountMapper.getAvailableAccount(remoteOJAccountType);

        for (RemoteJudgeAccount remoteJudgeAccount : remoteJudgeAccountList) {
            // POJ已有submitId的重判需要使用原来的账号获取结果
            if (isNeedAccountRejudge) {
                if (remoteJudgeAccount.getUsername().equals(username)) {
                    int count = remoteJudgeAccountMapper.updateAccountStatusById(remoteJudgeAccount.getId());
                    if (count > 0) {
                        return remoteJudgeAccount;
                    }
                }
            } else {
                int count = remoteJudgeAccountMapper.updateAccountStatusById(remoteJudgeAccount.getId());
                if (count > 0) {
                    return remoteJudgeAccount;
                }
            }
        }

        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public HashMap<String, Object> chooseFixedAccount(String remoteOJAccountType) {
        List<Instance> instances = getInstances(JudgeServiceName);
        // 过滤出当前远程可用的账号列表 悲观锁
        QueryWrapper<RemoteJudgeAccount> remoteJudgeAccountQueryWrapper = new QueryWrapper<>();
        remoteJudgeAccountQueryWrapper.eq("oj", remoteOJAccountType)
                .last("for update");

        List<RemoteJudgeAccount> remoteJudgeAccountList = remoteJudgeAccountMapper.selectList(remoteJudgeAccountQueryWrapper);
        int len = remoteJudgeAccountList.size();
        for (int i = 0; i < len && i < instances.size(); i++) {
            RemoteJudgeAccount remoteJudgeAccount = remoteJudgeAccountList.get(i);
            int count = remoteJudgeAccountMapper.updateAccountStatusById(remoteJudgeAccount.getId());
            if (count > 0) {
                HashMap<String, Object> result = new HashMap<>();
                result.put("index", i);
                result.put("size", len);
                result.put("account", remoteJudgeAccount);
                return result;
            }
        }
        return null;
    }


}