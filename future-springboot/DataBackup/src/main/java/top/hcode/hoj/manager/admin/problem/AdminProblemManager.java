package top.hcode.hoj.manager.admin.problem;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ZipUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.Yaml;
import top.hcode.hoj.common.exception.StatusFailException;
import top.hcode.hoj.common.exception.StatusForbiddenException;
import top.hcode.hoj.common.exception.StatusSystemErrorException;
import top.hcode.hoj.common.result.CommonResult;
import top.hcode.hoj.crawler.problem.ProblemStrategy;
import top.hcode.hoj.dao.judge.JudgeEntityService;
import top.hcode.hoj.dao.problem.ProblemCaseEntityService;
import top.hcode.hoj.dao.problem.ProblemEntityService;
import top.hcode.hoj.dao.problem.ProblemIoRecordEntityService;
import top.hcode.hoj.judge.Dispatcher;
import top.hcode.hoj.manager.oj.CommonManager;
import top.hcode.hoj.pojo.dto.CompileDTO;
import top.hcode.hoj.pojo.dto.ProblemDTO;
import top.hcode.hoj.pojo.entity.judge.Judge;
import top.hcode.hoj.pojo.entity.problem.Language;
import top.hcode.hoj.pojo.entity.problem.Problem;
import top.hcode.hoj.pojo.entity.problem.ProblemCase;
import top.hcode.hoj.pojo.entity.problem.ProblemIoRecord;
import top.hcode.hoj.shiro.AccountProfile;
import top.hcode.hoj.utils.Constants;
import top.hcode.hoj.validator.ProblemValidator;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: Himit_ZH
 * @Date: 2022/3/9 16:32
 * @Description:
 */

@Component
@RefreshScope
@Slf4j(topic = "hoj")
public class AdminProblemManager {
    @Autowired
    private ProblemEntityService problemEntityService;

    @Autowired
    private ProblemCaseEntityService problemCaseEntityService;

    @Autowired
    private Dispatcher dispatcher;

    @Value("${hoj.judge.token}")
    private String judgeToken;

    @Resource
    private JudgeEntityService judgeEntityService;

    @Resource
    private ProblemValidator problemValidator;

    @Autowired
    private RemoteProblemManager remoteProblemManager;

    @Autowired
    private ProblemIoRecordEntityService problemIoRecordEntityService;

    @Autowired
    private CommonManager commonManager;

    public IPage<Problem> getProblemList(Integer limit, Integer currentPage, String keyword, Integer auth, String oj) {
        if (currentPage == null || currentPage < 1) currentPage = 1;
        if (limit == null || limit < 1) limit = 10;
        IPage<Problem> iPage = new Page<>(currentPage, limit);
        IPage<Problem> problemList;

        QueryWrapper<Problem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_group", false)
                .orderByDesc("id");

        // 根据oj筛选过滤
        if (oj != null && !"All".equals(oj)) {
            if (!Constants.RemoteOJ.isRemoteOJ(oj)) {
                queryWrapper.eq("is_remote", false);
            } else {
                queryWrapper.eq("is_remote", true).likeRight("problem_id", oj);
            }
        }

        if (auth != null && auth != 0) {
            queryWrapper.eq("auth", auth);
        }

        if (!StringUtils.isEmpty(keyword)) {
            final String key = keyword.trim();
            queryWrapper.and(wrapper -> wrapper.like("title", key).or()
                    .like("author", key).or()
                    .like("problem_id", key));
            problemList = problemEntityService.page(iPage, queryWrapper);
        } else {
            problemList = problemEntityService.page(iPage, queryWrapper);
        }
        return problemList;
    }

    public Problem getProblem(Long pid) throws StatusForbiddenException, StatusFailException {
        Problem problem = problemEntityService.getById(pid);

        if (problem != null) { // 查询成功
            // 获取当前登录的用户
            AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();

            boolean isRoot = SecurityUtils.getSubject().hasRole("root");
            boolean isTeacher = SecurityUtils.getSubject().hasRole("Teacher");
            // 只有超级管理员和题目管理员、题目创建者才能操作
            if (!isRoot && !isTeacher && !userRolesVo.getUsername().equals(problem.getAuthor())) {
                throw new StatusForbiddenException("对不起，你无权限查看题目！");
            }

            return problem;
        } else {
            throw new StatusFailException("查询失败！");
        }
    }

    public void deleteProblem(Long pid) throws StatusFailException {
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();
        Problem problem = problemEntityService.getById(pid);
        if(problem == null) {
            throw new StatusFailException("删除题目ID不存在");
        }
        boolean isRoot = SecurityUtils.getSubject().hasRole("root");
        if(!isRoot && !userRolesVo.getUsername().equals(problem.getAuthor())) {
            throw new StatusFailException("无权删除此题目");
        }

        boolean isOk = problemEntityService.removeById(pid);
        /*
        problem的id为其他表的外键的表中的对应数据都会被一起删除！
         */
        if (isOk) { // 删除成功
            FileUtil.del(Constants.File.TESTCASE_BASE_FOLDER.getPath() + File.separator + "problem_" + pid);
            log.info("[{}],[{}],pid:[{}],operatorUid:[{}],operatorUsername:[{}]",
                    "Admin_Problem", "Delete", pid, userRolesVo.getUid(), userRolesVo.getUsername());
        } else {
            throw new StatusFailException("删除失败！");
        }
    }

    public void addProblem(ProblemDTO problemDto) throws StatusFailException {

        problemValidator.validateProblem(problemDto.getProblem());

        QueryWrapper<Problem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("problem_id", problemDto.getProblem().getProblemId().toUpperCase());
        Problem problem = problemEntityService.getOne(queryWrapper);
        if (problem != null) {
            throw new StatusFailException("该题目的Problem ID已存在，请更换！");
        }

        boolean isOk = problemEntityService.adminAddProblem(problemDto);
        if (!isOk) {
            throw new StatusFailException("添加失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateProblem(ProblemDTO problemDto) throws StatusForbiddenException, StatusFailException {

        problemValidator.validateProblemUpdate(problemDto.getProblem());

        // 获取当前登录的用户
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();

        boolean isRoot = SecurityUtils.getSubject().hasRole("root");
        boolean isProblemAdmin = SecurityUtils.getSubject().hasRole("problem_admin");
        // 只有超级管理员和题目管理员、题目创建者才能操作
        if (!isRoot && !isProblemAdmin && !userRolesVo.getUsername().equals(problemDto.getProblem().getAuthor())) {
            throw new StatusForbiddenException("对不起，你无权限修改题目！");
        }

        String problemId = problemDto.getProblem().getProblemId().toUpperCase();
        QueryWrapper<Problem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("problem_id", problemId);
        Problem problem = problemEntityService.getOne(queryWrapper);

        // 如果problem_id不是原来的且已存在该problem_id，则修改失败！
        if (problem != null && problem.getId().longValue() != problemDto.getProblem().getId()) {
            throw new StatusFailException("当前的Problem ID 已被使用，请重新更换新的！");
        }

        // 记录修改题目的用户
        problemDto.getProblem().setModifiedUser(userRolesVo.getUsername());

        boolean result = problemEntityService.adminUpdateProblem(problemDto);
        if (result) { // 更新成功
            if (problem == null) { // 说明改了problemId，同步一下judge表
                UpdateWrapper<Judge> judgeUpdateWrapper = new UpdateWrapper<>();
                judgeUpdateWrapper.eq("pid", problemDto.getProblem().getId())
                        .set("display_pid", problemId);
                judgeEntityService.update(judgeUpdateWrapper);
            }

        } else {
            throw new StatusFailException("修改失败");
        }
    }

    public List<ProblemCase> getProblemCases(Long pid, Boolean isUpload) {
        QueryWrapper<ProblemCase> problemCaseQueryWrapper = new QueryWrapper<>();
        problemCaseQueryWrapper.eq("pid", pid).eq("status", 0);
        if (isUpload) {
            problemCaseQueryWrapper.last("order by length(input) asc,input asc");
        }
        return problemCaseEntityService.list(problemCaseQueryWrapper);
    }

    public CommonResult compileSpj(CompileDTO compileDTO) {
        if (StringUtils.isEmpty(compileDTO.getCode()) ||
                StringUtils.isEmpty(compileDTO.getLanguage())) {
            return CommonResult.errorResponse("参数不能为空！");
        }

        compileDTO.setToken(judgeToken);
        return dispatcher.dispatch(Constants.TaskType.COMPILE_SPJ, compileDTO);
    }

    public CommonResult compileInteractive(CompileDTO compileDTO) {
        if (StringUtils.isEmpty(compileDTO.getCode()) ||
                StringUtils.isEmpty(compileDTO.getLanguage())) {
            return CommonResult.errorResponse("参数不能为空！");
        }

        compileDTO.setToken(judgeToken);
        return dispatcher.dispatch(Constants.TaskType.COMPILE_INTERACTIVE, compileDTO);
    }

    @Transactional(rollbackFor = Exception.class)
    public void importRemoteOJProblem(String name, String problemId) throws StatusFailException {
        QueryWrapper<Problem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("problem_id", name.toUpperCase() + "-" + problemId);
        Problem problem = problemEntityService.getOne(queryWrapper);
        if (problem != null) {
            throw new StatusFailException("该题目已添加，请勿重复添加！");
        }

        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();
        try {
            ProblemStrategy.RemoteProblemInfo otherOJProblemInfo = remoteProblemManager.getOtherOJProblemInfo(name.toUpperCase(), problemId, userRolesVo.getUsername());
            if (otherOJProblemInfo != null) {
                Problem importProblem = remoteProblemManager.adminAddOtherOJProblem(otherOJProblemInfo, name);
                if (importProblem == null) {
                    throw new StatusFailException("导入新题目失败！请重新尝试！");
                }
            } else {
                throw new StatusFailException("导入新题目失败！原因：可能是与该OJ链接超时或题号格式错误！");
            }
        } catch (Exception e) {
            throw new StatusFailException(e.getMessage());
        }
    }

    public void changeProblemAuth(Problem problem) throws StatusFailException, StatusForbiddenException {
        // 普通管理员只能将题目变成隐藏题目和比赛题目
        boolean root = SecurityUtils.getSubject().hasRole("root");
        boolean teacher = SecurityUtils.getSubject().hasRole("teacher");

//        if (!problemAdmin && !root && problem.getAuth() == 1) {
//            throw new StatusForbiddenException("修改失败！你无权限公开题目！");
//        }
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();
        if (!teacher && !root && !userRolesVo.getUsername().equals(problem.getAuthor())) {
            throw new StatusForbiddenException("修改失败！你无权限修改题目状态！");
        }


        UpdateWrapper<Problem> problemUpdateWrapper = new UpdateWrapper<>();
        problemUpdateWrapper.eq("id", problem.getId())
                .set("auth", problem.getAuth())
                .set("modified_user", userRolesVo.getUsername());

        boolean isOk = problemEntityService.update(problemUpdateWrapper);
        if (!isOk) {
            throw new StatusFailException("修改失败");
        }
        log.info("[{}],[{}],value:[{}],pid:[{}],operatorUid:[{}],operatorUsername:[{}]",
                "Admin_Problem", "Change_Auth", problem.getAuth(), problem.getId(), userRolesVo.getUid(), userRolesVo.getUsername());
    }


    public IPage<ProblemIoRecord> getProblemIoRecord(Integer limit, Integer currentPage, String keywords) {
        if (currentPage == null || currentPage < 1) currentPage = 1;
        if (limit == null || limit < 1) limit = 10;
        IPage<ProblemIoRecord> iPage = new Page<>(currentPage, limit);

        QueryWrapper<ProblemIoRecord> problemIoRecordQueryWrapper = new QueryWrapper<>();
        if (keywords != null && !keywords.equals("")) {
            problemIoRecordQueryWrapper.like("modified_user", keywords);
        }
        problemIoRecordQueryWrapper.orderByDesc("gmt_create");
        problemIoRecordEntityService.page(iPage, problemIoRecordQueryWrapper);
        return iPage;
    }

    public void addProblemBatchWithZip(MultipartFile[] files, String prefix, Integer number) throws StatusFailException, StatusSystemErrorException {
        //validate problemId
        if (files == null || files.length == 0) {
            throw new StatusFailException("文件为空");
        }

        List<String> problemIdList = new ArrayList<>();
        Integer tempNumber = number;
        for(int i = 0; i < files.length; ++i) {
            problemIdList.add(prefix + tempNumber);
            tempNumber++;
        }
        QueryWrapper<Problem> problemQueryWrapper = new QueryWrapper<>();
        problemQueryWrapper.in("problem_id", problemIdList);
        List<Problem> list = problemEntityService.list(problemQueryWrapper);
        if (list != null && !list.isEmpty()) {
            throw new StatusFailException("题目ID已被使用！" + list);
        }

        for (MultipartFile file : files) {
            addProblemSingleWithZip(file, prefix + number);
            number++;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void addProblemSingleWithZip(MultipartFile file, String problemId) throws StatusFailException, StatusSystemErrorException {
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();
        //获取文件后缀
        String suffix = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        if (!"zip".toUpperCase().contains(suffix.toUpperCase())) {
            throw new StatusFailException("请上传zip格式的题目数据压缩包！");
        }

        String fileDirId = IdUtil.simpleUUID();
        String fileDir = Constants.File.PROBLEM_TMP_FOLDER.getPath() + File.separator + fileDirId; //TODO
        String filePath = fileDir + File.separator + file.getOriginalFilename();
        // 文件夹不存在就新建
        FileUtil.mkdir(fileDir);
        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            log.error("题目数据文件上传异常-------------->{}", e.getMessage());
            throw new StatusSystemErrorException("服务器异常：题目数据上传失败！");
        }

        // 将压缩包压缩到指定文件夹
        ZipUtil.unzip(filePath, fileDir);
        // 删除zip文件
        FileUtil.del(filePath);
        // 检查文件是否存在
        File testCaseFileList = new File(fileDir);
        File[] files = testCaseFileList.listFiles();
        if (files == null || files.length == 0) {
            FileUtil.del(fileDir);
            throw new StatusFailException("题目数据压缩包里文件不能为空！");
        }

        String testcasePath = "";
        String absolutePath = "";
        if (files.length == 3) {
            absolutePath = fileDir;
        } else {
            if (files[0].getName().equals("__MACOSX")) {
                absolutePath = files[1].getAbsolutePath();
            } else {
                absolutePath = files[0].getAbsolutePath();
            }
        }

        testcasePath = absolutePath + File.separator + "testdata";
        File testcaseDir = new File(testcasePath);
        if (!testcaseDir.exists()) {
            throw new StatusFailException("不存在用例文件！");
        }

        HashMap<String, String> inputData = new HashMap<>();
        HashMap<String, String> outputData = new HashMap<>();

        File[] testcaseFiles = testcaseDir.listFiles();
        // 遍历读取与检查是否in和out文件一一对应，否则报错
        for (File tmp : testcaseFiles) {
            String tmpPreName = null;
            if (tmp.getName().endsWith(".in")) {
                tmpPreName = tmp.getName().substring(0, tmp.getName().lastIndexOf(".in"));
                inputData.put(tmpPreName, tmp.getName());
            } else if (tmp.getName().endsWith(".out")) {
                tmpPreName = tmp.getName().substring(0, tmp.getName().lastIndexOf(".out"));
                outputData.put(tmpPreName, tmp.getName());
            } else if (tmp.getName().endsWith(".ans")) {
                tmpPreName = tmp.getName().substring(0, tmp.getName().lastIndexOf(".ans"));
                outputData.put(tmpPreName, tmp.getName());
            } else if (tmp.getName().endsWith(".txt")) {
                tmpPreName = tmp.getName().substring(0, tmp.getName().lastIndexOf(".txt"));
                if (tmpPreName.contains("input")) {
                    inputData.put(tmpPreName.replaceAll("input", "$*$"), tmp.getName());
                } else if (tmpPreName.contains("output")) {
                    outputData.put(tmpPreName.replaceAll("output", "$*$"), tmp.getName());
                }
            }
        }

        // 进行数据对应检查,同时生成返回数据
        List<HashMap<String, Object>> problemCaseList = new LinkedList<>();
        for (String key : inputData.keySet()) {
            HashMap<String, Object> testcaseMap = new HashMap<>();
            String inputFileName = inputData.get(key);
            testcaseMap.put("input", inputFileName);

            // 若有名字对应的out文件不存在的，直接生成对应的out文件
            String oriOutputFileName = outputData.getOrDefault(key, null);
            if (oriOutputFileName == null) {
                oriOutputFileName = key + ".out";
                if (inputFileName.endsWith(".txt")) {
                    oriOutputFileName = inputFileName.replaceAll("input", "output");
                }
                FileWriter fileWriter = new FileWriter(testcasePath + File.separator + oriOutputFileName);
                fileWriter.write("");
            }

            testcaseMap.put("output", oriOutputFileName);
            problemCaseList.add(testcaseMap);
        }

        List<HashMap<String, Object>> fileList = problemCaseList.stream()
                .sorted((o1, o2) -> {
                    String input1 = (String) o1.get("input");
                    String input2 = (String) o2.get("input");
                    String a = input1.split("\\.")[0];
                    String b = input2.split("\\.")[0];
                    if (a.length() > b.length()) {
                        return 1;
                    } else if (a.length() < b.length()) {
                        return -1;
                    }
                    return a.compareTo(b);
                })
                .collect(Collectors.toList());

        List<ProblemCase> examples = new ArrayList<>();
        int[] scores = new int[fileList.size()];
        int baseScore = 100 / fileList.size();
        int remainder = 100 % fileList.size();
        // 分配基础分数
        Arrays.fill(scores, baseScore);
        // 分配剩余分数
        for (int i = 0; i < remainder; i++) {
            scores[i]++;
        }
        int index = 0;
        for (HashMap<String, Object> rawProblemCase : fileList) {
            ProblemCase example = new ProblemCase();
            example.setInput(rawProblemCase.get("input").toString());
            example.setOutput(rawProblemCase.get("output").toString());
            example.setScore(scores[index]);
            index++;
            examples.add(example);
        }

        ProblemDTO problemDTO = new ProblemDTO();
        problemDTO.setIsUploadTestCase(true);
        problemDTO.setUploadTestcaseDir(testcaseDir.getAbsolutePath());
        problemDTO.setJudgeMode("default");
        problemDTO.setChangeModeCode(true);
        problemDTO.setChangeJudgeCaseMode(true);
        problemDTO.setSamples(examples);

        List<Language> languages = commonManager.getLanguages(null, false);
        problemDTO.setLanguages(languages);


        String yamlConfigPath = absolutePath + File.separator + "problem.yaml";

        Problem problem = new Problem();
        problem.setAuthor(userRolesVo.getUsername());
        problem.setProblemId(problemId);
        Yaml yaml = new Yaml();
        try (FileInputStream fileInputStream = new FileInputStream(yamlConfigPath)) {
            // 将 YAML 文件解析为 Map
            Map<String, Object> yamlData = yaml.load(fileInputStream);

            // 获取 title 字段
            String title = (String) yamlData.get("title");
            problem.setTitle(title);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        problem.setTitle(file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf(".")));
        problem.setType(1);
        problem.setJudgeMode("default");
        problem.setJudgeCaseMode("default");
        problem.setTimeLimit(1000);
        problem.setMemoryLimit(256);
        problem.setStackLimit(128);
        problem.setDifficulty(0);
        problem.setAuth(1);
        problem.setCodeShare(true);
        problem.setIsRemoveEndBlank(true);
        problem.setIsUploadCase(true);
        problem.setIsFileIO(false);
        problem.setSource("");

        String configPath1 = absolutePath + File.separator + "problem_zh.md";
        String configPath2 = absolutePath + File.separator + "problem.md";

        File file1 = new File(configPath1);
        File file2 = new File(configPath2);

        if (file1.exists()) {
            problem.setDescription(problemInfoReader("## 问题描述", "## 输入格式", configPath1, true, false) + problemInfoReader("## 题目描述", "## 输入", configPath1, true, false) + problemInfoReader("## 说明", "## 输入格式", configPath1, true, false));
            problem.setInput(problemInfoReader("## 输入格式", "## 输出格式", configPath1, true, false) + problemInfoReader("## 输入描述", "## 输出描述", configPath1, true, false));
            problem.setOutput(problemInfoReader("## 输出格式", "## 样例", configPath1, true, false) + problemInfoReader("## 输出描述", "## 样例", configPath1, true, false));
            problem.setExamples(getProblemExamplesFromMD(configPath1));
            problem.setHint(problemInfoReader("## 数据范围", "null", configPath1, true, true) + problemInfoReader("## 提示", null, configPath1, true, false) + problemInfoReader("## 提示/说明", null, configPath1, true, false));
        } else if(file2.exists()) {
            problem.setDescription(problemInfoReader("<h2>说明</h2>", "<h2>输入格式</h2>", configPath2, true, false));
            problem.setInput(problemInfoReader("<h2>输入格式</h2>", "<h2>输出格式</h2>", configPath2, true, false));
            problem.setOutput(problemInfoReader("<h2>输出格式</h2>", "<h2>样例</h2>", configPath2, true, false));
            problem.setExamples("<input>参考上文</input><output>参考上文</output>");
            problem.setHint(problemInfoReader("<h2>提示</h2>", "", configPath2, true, false));
        } else {
            throw new StatusFailException("没有题目配置文件");
        }

        problemDTO.setProblem(problem);
        addProblem(problemDTO);

        System.out.println(1);

    }

    private String getProblemExamplesFromMD(String path) throws StatusFailException {
        StringBuilder rtn = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            boolean isWithinSection = false;  // 用于标记是否在目标区域内
            boolean isInput = false;
            boolean isOutput = false;

            // 遍历文件的每一行
            while ((line = br.readLine()) != null) {
                // 如果找到起始标题
                if (line.trim().equals("## 样例")) {
                    isWithinSection = true;
                    continue;  // 跳过标题行本身
                }

                // 如果找到结束标题
                if (line.trim().startsWith("##")) {
                    isWithinSection = false;
                }

                // 只提取在两个标题之间的内容
                if (isWithinSection) {

                    if (line.trim().startsWith("```input")) {
                        isInput = true;
                        rtn.append("<input>");
                        continue;
                    }

                    if (line.trim().startsWith("```output")) {
                        isOutput = true;
                        rtn.append("<output>");
                        continue;
                    }

                    if (isInput && line.trim().startsWith("```")) {
                        isInput = false;
                        rtn.deleteCharAt(rtn.length() - 1);
                        rtn.append("</input>");
                        continue;
                    }

                    if (isOutput && line.trim().startsWith("```")) {
                        isOutput = false;
                        rtn.deleteCharAt(rtn.length() - 1);
                        rtn.append("</output>");
                        continue;
                    }

                    if (isInput || isOutput) {
                        rtn.append(line);
                        rtn.append("\n");
                    }
                }
            }
        } catch (IOException e) {
            throw new StatusFailException("题目样例读取错误");
        }
        return rtn.toString();
    }

    private String problemInfoReader(String startTitle, String endTitle, String path, boolean isOnce, boolean isAddTitle) throws StatusFailException {
        StringBuilder rtn = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            boolean isWithinSection = false;  // 用于标记是否在目标区域内

            // 遍历文件的每一行
            while ((line = br.readLine()) != null) {
                // 如果找到起始标题
                if (line.trim().equals(startTitle)) {
                    isWithinSection = true;
                    if (!isAddTitle) {
                        continue;  // 跳过标题行本身
                    }
                }

                // 如果找到结束标题
                if (endTitle != null && line.trim().startsWith(endTitle)) {
                    isWithinSection = false;
                    if (isOnce) {
                        break;  // 停止读取，因为已经到了指定区域的末尾
                    }
                }

                // 只提取在两个标题之间的内容
                if (isWithinSection) {
                    rtn.append(line);
                    rtn.append("\n");
//                    System.out.println(line);  // 输出或保存内容
                }
            }
        } catch (IOException e) {
            throw new StatusFailException("题目描述读取错误");
        }
        return rtn.toString();
    }
}