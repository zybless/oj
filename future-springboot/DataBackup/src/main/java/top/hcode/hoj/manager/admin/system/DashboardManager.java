package top.hcode.hoj.manager.admin.system;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.hcode.hoj.dao.contest.ContestEntityService;
import top.hcode.hoj.dao.judge.JudgeEntityService;
import top.hcode.hoj.dao.user.SessionEntityService;
import top.hcode.hoj.dao.user.UserAcproblemEntityService;
import top.hcode.hoj.dao.user.UserInfoEntityService;
import top.hcode.hoj.pojo.entity.judge.Judge;
import top.hcode.hoj.pojo.entity.user.Session;
import top.hcode.hoj.pojo.entity.user.UserAcproblem;
import top.hcode.hoj.pojo.entity.user.UserInfo;
import top.hcode.hoj.pojo.vo.MonthRankingVO;
import top.hcode.hoj.pojo.vo.RankingListDetailVO;
import top.hcode.hoj.pojo.vo.RankingListVO;
import top.hcode.hoj.shiro.AccountProfile;
import top.hcode.hoj.utils.Constants;
import top.hcode.hoj.utils.RedisUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static top.hcode.hoj.utils.CommonUtils.addElementToPQ;
import static top.hcode.hoj.utils.CommonUtils.convertToLocalDateTime;

/**
 * @Author: Himit_ZH
 * @Date: 2022/3/9 21:44
 * @Description:
 */
@Component
@Slf4j
public class DashboardManager {

    @Autowired
    private ContestEntityService contestEntityService;

    @Autowired
    private JudgeEntityService judgeEntityService;

    @Autowired
    private UserInfoEntityService userInfoEntityService;

    @Autowired
    private SessionEntityService sessionEntityService;

    @Autowired
    private UserAcproblemEntityService userAcproblemEntityService;

    @Autowired
    private RedisUtils redisUtils;

    public Session getRecentSession() {
        // 需要获取一下该token对应用户的数据
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();
        QueryWrapper<Session> wrapper = new QueryWrapper<Session>().eq("uid", userRolesVo.getUid()).orderByDesc("gmt_create");
        List<Session> sessionList = sessionEntityService.list(wrapper);
        if (sessionList.size() > 1) {
            return sessionList.get(1);
        } else {
            return sessionList.get(0);
        }
    }

    public Map<Object, Object> getDashboardInfo() {
        int userNum = userInfoEntityService.count();
        int recentContestNum = contestEntityService.getWithinNext14DaysContests().size();
        int todayJudgeNum = judgeEntityService.getTodayJudgeNum();
        return MapUtil.builder()
                .put("userNum", userNum)
                .put("recentContestNum", recentContestNum)
                .put("todayJudgeNum", todayJudgeNum).map();
    }

    public void updateRankingList() {
        long startTime = System.currentTimeMillis();
        RankingListVO rankingListVO = new RankingListVO();
        Map<String, List<Integer>> acNumMap = new HashMap<>();

        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.select("uuid", "realname", "username");
        List<UserInfo> userInfoList = userInfoEntityService.list(userInfoQueryWrapper);

        PriorityQueue<RankingListDetailVO> heapOneDay = new PriorityQueue<>(100);
        PriorityQueue<RankingListDetailVO> heapOneWeek = new PriorityQueue<>(100);
        PriorityQueue<RankingListDetailVO> heapOneMonth = new PriorityQueue<>(100);
        PriorityQueue<RankingListDetailVO> heapOneYear = new PriorityQueue<>(100);
        PriorityQueue<RankingListDetailVO> heapAllTheTime = new PriorityQueue<>(100);

        List<String> adminUidList = userInfoEntityService.getAdminUidList();
        Set<String> adminUidSet = new HashSet<>(adminUidList);

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<Void>> futures = new ArrayList<>();

        for (UserInfo userInfo : userInfoList) {
            String name = "";
            if(userInfo.getRealname() != null && !userInfo.getRealname().isEmpty()) {
                name = userInfo.getRealname();
            } else {
                name = userInfo.getUsername();
            }
            if (adminUidSet.contains(userInfo.getUuid())) {
                continue;
            }
            String finalName = name;
            futures.add(executorService.submit(() -> {
                QueryWrapper<UserAcproblem> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("uid", userInfo.getUuid())
                        .select("pid", "gmt_create")
                        .orderByAsc("gmt_create");

                List<UserAcproblem> userAcproblemList = userAcproblemEntityService.list(queryWrapper);

                List<UserAcproblem> userAcproblemListFiltered = new ArrayList<>();
                HashSet<Long> pidSet = new HashSet<>();
                for (UserAcproblem userAcproblem : userAcproblemList) {
                    if (!pidSet.contains(userAcproblem.getPid())) {
                        pidSet.add(userAcproblem.getPid());
                        userAcproblemListFiltered.add(userAcproblem);
                    }
                }

                LocalDateTime now = LocalDateTime.now();

                // 统计一天内的数据数量
                long countWithinOneDay = userAcproblemListFiltered.stream()
                        .filter(userAcproblem -> convertToLocalDateTime(userAcproblem.getGmtCreate()).isAfter(now.minusDays(1)))
                        .count();

                // 统计一周内的数据数量
                long countWithinOneWeek = userAcproblemListFiltered.stream()
                        .filter(userAcproblem -> convertToLocalDateTime(userAcproblem.getGmtCreate()).isAfter(now.minusWeeks(1)))
                        .count();

                // 统计一个月内的数据数量
                long countWithinOneMonth = userAcproblemListFiltered.stream()
                        .filter(userAcproblem -> convertToLocalDateTime(userAcproblem.getGmtCreate()).isAfter(now.minusMonths(1)))
                        .count();

                // 统计一年内的数据数量
                long countWithinOneYear = userAcproblemListFiltered.stream()
                        .filter(userAcproblem -> convertToLocalDateTime(userAcproblem.getGmtCreate()).isAfter(now.minusYears(1)))
                        .count();

                long countWithAllTheTime = userAcproblemListFiltered.size();

                synchronized (heapOneDay) {
                    addElementToPQ(heapOneDay, new RankingListDetailVO(userInfo.getUuid(), finalName, (int) countWithinOneDay), 100);
                }
                synchronized (heapOneWeek) {
                    addElementToPQ(heapOneWeek, new RankingListDetailVO(userInfo.getUuid(), finalName, (int) countWithinOneWeek), 100);
                }
                synchronized (heapOneMonth) {
                    addElementToPQ(heapOneMonth, new RankingListDetailVO(userInfo.getUuid(), finalName, (int) countWithinOneMonth), 100);
                }
                synchronized (heapOneYear) {
                    addElementToPQ(heapOneYear, new RankingListDetailVO(userInfo.getUuid(), finalName, (int) countWithinOneYear), 100);
                }
                synchronized (heapAllTheTime) {
                    addElementToPQ(heapAllTheTime, new RankingListDetailVO(userInfo.getUuid(), finalName, (int) countWithAllTheTime), 100);
                }

                acNumMap.put(userInfo.getUuid(), new ArrayList<Integer>(){{
                    add((int) countWithinOneDay);
                    add((int) countWithinOneWeek);
                    add((int) countWithinOneMonth);
                    add((int) countWithinOneYear);
                    add((int) countWithAllTheTime);
                }});
                return null;
            }));
        }

        for (Future<Void> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        rankingListVO.getRankingListDetailVOList().add(heapOneDay.
                stream().
                sorted(Collections.reverseOrder()).
                collect(Collectors.toCollection(ArrayList::new)));

        rankingListVO.getRankingListDetailVOList().add(heapOneWeek.
                stream().
                sorted(Collections.reverseOrder()).
                collect(Collectors.toCollection(ArrayList::new)));

        rankingListVO.getRankingListDetailVOList().add(heapOneMonth.
                stream().
                sorted(Collections.reverseOrder()).
                collect(Collectors.toCollection(ArrayList::new)));

        rankingListVO.getRankingListDetailVOList().add(heapOneYear.
                stream().
                sorted(Collections.reverseOrder()).
                collect(Collectors.toCollection(ArrayList::new)));

        rankingListVO.getRankingListDetailVOList().add(heapAllTheTime.
                stream().
                sorted(Collections.reverseOrder()).
                collect(Collectors.toCollection(ArrayList::new)));

        redisUtils.set(Constants.Training.RANKING_LIST_KEY.getValue(), rankingListVO, 60 * 60 * 24);
        redisUtils.set(Constants.Training.USER_ACNUM_KEY.getValue(), JSONUtil.toJsonStr(acNumMap), 60 * 60 * 24);
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        log.info("排行榜更新耗时：" + executionTime);
    }

    public void updateMonthRanking(MonthRankingVO monthRankingVO) {
        redisUtils.set(Constants.Training.MONTH_RANKING_KEY.getValue(), monthRankingVO, 0);
    }

    public void updateAc() {
        QueryWrapper<UserAcproblem> userAcproblemQueryWrapper = new QueryWrapper<>();
        userAcproblemQueryWrapper.eq("uid", "90013dd6996643308dbbff3e0d40c78f")
                .select("pid", "gmt_create")
                .orderByAsc("gmt_create");
        List<UserAcproblem> list = userAcproblemEntityService.list(userAcproblemQueryWrapper);
        System.out.println(1);

//        QueryWrapper<UserAcproblem> userAcproblemQueryWrapper = new QueryWrapper<>();
//        userAcproblemQueryWrapper.gt("id", 7151);
//        List<UserAcproblem> userAcproblemList = userAcproblemEntityService.list(userAcproblemQueryWrapper);
//
//        HashSet<Long> submitIdSet = userAcproblemList.stream()
//                .map(UserAcproblem::getSubmitId)
//                .collect(Collectors.toCollection(HashSet::new));
//
//        QueryWrapper<Judge> judgeQueryWrapper = new QueryWrapper<>();
//        judgeQueryWrapper.in("submit_id", submitIdSet);
//        judgeQueryWrapper.select("submit_id", "status", "uid", "pid", "gmt_create");
//        List<Judge> judgeList = judgeEntityService.list(judgeQueryWrapper);
//
//        HashMap<Long, Date> hm = new HashMap<>();
//        for (Judge judge : judgeList) {
//            hm.put(judge.getSubmitId(), judge.getGmtCreate());
//        }
//
//        for (UserAcproblem userAcproblem : userAcproblemList) {
//            userAcproblem.setGmtCreate(hm.get(userAcproblem.getSubmitId()));
//        }
//
//        userAcproblemEntityService.updateBatchById(userAcproblemList);

//        QueryWrapper<UserAcproblem> userAcproblemQueryWrapper = new QueryWrapper<>();
//        userAcproblemQueryWrapper.select("submit_id");
//        List<UserAcproblem> userAcproblemList = userAcproblemEntityService.list(userAcproblemQueryWrapper);
//
//        HashSet<Long> submitIdSet = userAcproblemList.stream()
//                .map(UserAcproblem::getSubmitId)
//                .collect(Collectors.toCollection(HashSet::new));
//
//        QueryWrapper<Judge> judgeQueryWrapper = new QueryWrapper<>();
//        judgeQueryWrapper.select("submit_id", "status", "uid", "pid");
//        List<Judge> judgeList = judgeEntityService.list(judgeQueryWrapper);
//
//        List<UserAcproblem> userAcproblemListAdd = new ArrayList<>();
//        int count = 0;
//        for (Judge judge : judgeList) {
//            if (!submitIdSet.contains(judge.getSubmitId()) && judge.getStatus().intValue() == Constants.Judge.STATUS_ACCEPTED.getStatus()) {
//                UserAcproblem userAcproblem = new UserAcproblem();
//                userAcproblem.setUid(judge.getUid());
//                userAcproblem.setPid(judge.getPid());
//                userAcproblem.setSubmitId(judge.getSubmitId());
//                userAcproblemListAdd.add(userAcproblem);
//            }
//            if (judge.getStatus().intValue() == Constants.Judge.STATUS_ACCEPTED.getStatus()) {
//                count++;
//            }
//        }
//
//        userAcproblemEntityService.saveBatch(userAcproblemListAdd);

    }
}