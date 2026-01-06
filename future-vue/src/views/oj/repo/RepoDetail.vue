<template>
  <div class="container">
    <div style="display: inline-block;width: calc(75% - 10px);">
      <el-card shadow>
        <div slot="header">
          <span class="panel-title">{{ repo.title }}</span>
        </div>
        <div style="font-size: 16px;height: 40px;margin-bottom: 10px;">
          <el-button type="primary" @click="freeTest" plain>自由练习</el-button>
          <div
            style="margin-left: auto;height: 100%;float: right;display: flex;"
          >
            <span style="line-height: 40px;"
              >题目数量：{{ repo.problemCount }}</span
            >
            <div
              style="height: 26px;width: 2px;background-color: black;margin: 7px;"
            ></div>
            <span style="line-height: 40px;"
              >测试时间：{{ repo.timeLimit }}分钟</span
            >
          </div>
        </div>
        <el-tabs v-model="activeName" type="border-card">
          <el-tab-pane label="测验描述" name="first">{{
            repo.remark
          }}</el-tab-pane>
          <el-tab-pane label="查看题目" name="second">
            <div style="margin-bottom: 10px;font-weight: 700;font-size: 16px;">
              第{{ itemIndex + 1 }}题 {{ PROBLEM_TYPE[question.type] }}
            </div>
            <EditorShow :value="question.content"></EditorShow>
            <template v-if="question.type !== 3">
              <div
                v-for="(subItem, index) in question.examProblemAnswerList"
                :key="index"
                style="display: flex;"
              >
                <div
                  style="display: flex;  
                justify-content: center;  
                align-items: center;
                margin-bottom: 16px;"
                >
                  {{ String.fromCharCode(index + 65) }}.
                </div>
                <EditorShow :value="subItem.content"></EditorShow>
              </div
            ></template>
            <div v-if="showAnwser">
              <span style="color: #5eb95e;font-size: 16px;">
                正确答案：{{ getAnswer(question) }}
              </span>
              <div style="font-size: 16px;">
                解析：
              </div>
              <EditorShow :value="question.analysis"></EditorShow>
            </div>
            <div style="margin: 5px 0;">
              本题共
              <span style="font-weight: bold;">{{ question.grade }}</span> 分
            </div>
            <el-button type="primary" @click="showAnwser = !showAnwser">
              <template v-if="showAnwser">隐藏答案与解析</template>
              <template v-else>显示答案与解析</template></el-button
            >
          </el-tab-pane>
          <el-tab-pane
            v-if="currentRecord.length > 0"
            label="历史答卷"
            name="third"
            v-loading="loading"
          >
            <div
              v-for="(item, index) in examRepoProblemVOList"
              :key="index"
              :id="'div' + index"
            >
              <div
                style="margin-bottom: 10px;font-weight: 700;font-size: 16px;"
              >
                第{{ index + 1 }}题 {{ PROBLEM_TYPE[item.type] }}
              </div>
              <div
                :class="{
                  'not-right': !yourAnswerIsRight[index],
                }"
              >
                <EditorShow :value="item.content"></EditorShow>
              </div>
              <template v-if="item.type === 3">
                <div
                  v-for="(subItem, index2) in item.examProblemAnswerList"
                  :key="index2"
                  style="display: flex;"
                >
                  {{ index2 + 1 }}.<el-input
                    type="textarea"
                    autosize
                    v-model="yourAnswer[index][index2]"
                    style="margin: 0 0 5px 5px;"
                    disabled
                  >
                  </el-input>
                </div>
              </template>
              <template v-else-if="item.type === 1">
                <el-checkbox-group v-model="yourAnswer[index]">
                  <div
                    v-for="(subItem, index2) in item.examProblemAnswerList"
                    :key="index2"
                    style="display: flex;"
                  >
                    <div
                      style="display: flex;  
                      justify-content: center;  
                      align-items: center;
                      margin-bottom: 16px;"
                    >
                      <el-checkbox
                        style="margin-right: 5px;"
                        :label="subItem.id"
                        :key="subItem.id"
                        disabled
                      >{{ String.fromCharCode(index2 + 65) }}.</el-checkbox>
                    </div>
                    <EditorShow :value="subItem.content"></EditorShow>
                  </div>
                </el-checkbox-group>
              </template>
              <template v-else>
                <div
                  v-for="(subItem, index2) in item.examProblemAnswerList"
                  :key="index2"
                  style="display: flex;"
                >
                  <div
                    style="display: flex;  
                    justify-content: center;  
                    align-items: center;
                    margin-bottom: 16px;"
                  >
                    <el-radio
                      :label="subItem.id"
                      style="margin-right: 5px;"
                      disabled
                      v-model="yourAnswer[index]"
                      >{{ String.fromCharCode(index2 + 65) }}.</el-radio
                    >
                  </div>
                  <EditorShow :value="subItem.content"></EditorShow>
                </div>
              </template>
              <span style="color: #5eb95e;font-size: 16px;">
                正确答案：{{ getAnswer(item) }}
              </span>
              <div style="font-size: 16px;">
                解析：
              </div>
              <EditorShow :value="item.analysis"></EditorShow>
              <div style="margin: 5px 0;">
                <!-- 本题共
                <span style="font-weight: bold;">{{ item.grade }}</span> 分 -->
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
        <!-- 测验描述：{{ repo.remark }} -->
      </el-card>
    </div>
    <div
      style="float: right;width:25%;margin-left: 10px;position: sticky;top:65px"
    >
      <p
        v-if="examHistory.userScore"
        style="font-size: 48px;margin: 0 0 16px;
      text-align:center;"
      >
        {{ examHistory.userScore }}分
      </p>
      <div
        v-if="currentRecord.length > 0"
        style="margin-bottom: 10px;font-weight: 700;font-size: 16px;"
      >
        最近提交记录
        <span style="float: right;">
          <el-link v-if="showRecord" type="primary" @click="showRecord = false"
            >收起</el-link
          >
          <el-link v-else type="primary" @click="showRecord = true"
            >展开</el-link
          >
        </span>
      </div>
      <ul
        v-if="showRecord"
        style="
        font-size:14px;
        list-style: none;padding-left: 0;
        font-weight: 700;"
      >
        <li v-for="(item, index) in currentRecord" :key="index">
          <el-link @click="pushRecord(item)"
            >{{ item.userScore }}分 - 提交于{{
              parseTime(item.submitTime)
            }}</el-link
          >
        </li>
      </ul>
      <div style="margin-bottom: 10px;font-weight: 700;font-size: 16px;">
        题目列表
      </div>
      <div
        :class="{
          'have-history': currentRecord.length > 0,
        }"
      >
        <el-tag
          v-for="(item, index) in questionList"
          :key="index"
          @click="toItem(index)"
          type="info"
          :class="{
            'not-right': !yourAnswerIsRight[index],
          }"
          >第{{ index + 1 }}题</el-tag
        >
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import {
  getProblemListInRepo,
  getRepoById,
} from "@/common/newApi/questionbank/ojIndex";
import {
  getExerciseHistory,
  getExerciseHistoryDetail,
} from "@/common/newApi/examhistory/index";
import { PROBLEM_TYPE } from "@/common/constants";
import utils from "@/common/utils";
const EditorShow = () => import("@/components/oj/common/EditorShow.vue");

export default {
  name: "repo-detail",
  components: { EditorShow },
  data() {
    return {
      loading: false,
      query: {
        status: "",
        keyword: "",
      },
      total: 0,
      repoID: "",
      questionList: [],
      repo: {},
      question: {},
      PROBLEM_TYPE: {},
      itemIndex: 0,
      showAnwser: false,
      activeName: "first",
      currentRecord: [],
      showRecord: false,
      historyId: "",
      examRepoProblemVOList: [],
      examHistoryDetailList: [],
      yourAnswer: [],
      yourAnswerIsRight: [],
      examHistory: {},
    };
  },
  watch: {
    $route: {
      immediate: true,
      handler(newV, oldV) {
        this.historyId = newV.query["historyId"];
        this.loading = true;
        getExerciseHistoryDetail({
          historyId: this.historyId,
          repoId: this.$route.params["repoID"],
        }).then((res) => {
          if (res.data.data) {
            this.examRepoProblemVOList = res.data.data.examRepoProblemVOList
              ? res.data.data.examRepoProblemVOList
              : [];
            this.examHistoryDetailList = res.data.data.examHistoryDetailList
              ? res.data.data.examHistoryDetailList
              : [];
            this.examHistory = res.data.data.examHistory
              ? res.data.data.examHistory
              : [];
          }
          this.yourAnswer = [];
          this.yourAnswerIsRight = [];
          this.examRepoProblemVOList.forEach((val) => {
            this.examHistoryDetailList.forEach((val2) => {
              if (val.id === val2.problemId) {
                this.yourAnswerIsRight.push(val2.isCorrect);
                if (val.type === 0 || val.type === 2) {
                  let userAnswer = JSON.parse(val2.userAnswer);
                  if (userAnswer.length > 0) {
                    this.yourAnswer.push(parseInt(userAnswer[0]));
                  } else {
                    this.yourAnswer.push("");
                  }
                } else if (val.type === 1) {
                  let userAnswer = JSON.parse(val2.userAnswer);
                  let checkList = userAnswer.map((i) => {
                    return parseInt(i);
                  });
                  this.yourAnswer.push(checkList);
                } else {
                  let userAnswer = JSON.parse(val2.userAnswer);
                  this.yourAnswer.push(userAnswer);
                }
              }
            });
          });
          this.loading = false;
        });
      },
    },
  },
  created() {
    this.repoID = this.$route.params["repoID"];
    this.PROBLEM_TYPE = Object.assign({}, PROBLEM_TYPE);
    getProblemListInRepo({
      currentPage: 1,
      repoId: this.repoID,
      limit: 1000,
    }).then((res) => {
      this.questionList = res.data.data.records;
      this.question = this.questionList[0];
      this.itemIndex = 0;
    });
  },
  mounted() {
    getRepoById({ id: this.repoID }).then((res) => {
      this.repo = res.data.data;
    });
    //获取历史记录
    getExerciseHistory(this.repoID).then((res) => {
      this.currentRecord = res.data.data.filter((item) => {
        return item.submitTime !== null;
      });
    });
  },
  methods: {
    pushRecord(item) {
      this.$router.push({
        name: "RepoDetail",
        params: { repoID: this.repoID },
        query: {
          historyId: item.id,
        },
      });
    },
    parseTime(time) {
      return utils.parseTime(time);
    },
    toItem(index) {
      if (this.activeName === "second") {
        this.question = this.questionList[index];
        this.itemIndex = index;
        this.showAnwser = false;
      } else if (this.activeName === "third") {
        let id = "#div" + index;
        document.querySelector(id).scrollIntoView({
          behavior: "smooth",
          block: "center",
          inline: "nearest",
        });
      }
    },
    freeTest() {
      this.$router.push({
        name: "RepoProblemList",
        params: { repoID: this.repoID },
      });
    },
    getAnswer(question) {
      let res = "";
      if (question.type < 3) {
        for (let i = 0; i < question.examProblemAnswerList.length; i++) {
          if (question.examProblemAnswerList[i].isCorrect) {
            res = res + String.fromCharCode(65 + i) + ",";
          }
        }
        res = res.substring(0, res.length - 1);
      } else if (question.type === 3) {
        for (let i = 0; i < question.examProblemAnswerList.length; i++) {
          res = res + question.examProblemAnswerList[i].content + ",";
        }
        res = res.substring(0, res.length - 1);
      }
      return res;
    },
  },
  computed: {
    ...mapGetters(["isAuthenticated", "userInfo"]),
  },
};
</script>
<style scoped>
#no-contest {
  text-align: center;
  font-size: 16px;
  padding: 20px;
}
.filter-row {
  float: right;
}
@media screen and (max-width: 768px) {
  .filter-row span {
    margin-right: 2px;
  }
  ol {
    padding-inline-start: 5px;
  }
  /deep/ .el-card__header {
    margin-bottom: 5px;
  }
}
@media screen and (min-width: 768px) {
  .filter-row span {
    margin-right: 20px;
  }
}
/deep/ .el-card__header {
  border-bottom: 0px;
}

.v-note-wrapper {
  min-height: auto;
}
/deep/.v-show-content {
  padding: 0 !important;
}
.v-note-wrapper {
  border: 0;
}
.el-tag {
  margin: 0 5px 5px 0;
  cursor: pointer;
  color: #fff;
  background-color: rgb(170, 170, 170);
}
.have-history .el-tag {
  background-color: rgb(94, 185, 94);
}
.have-history .not-right {
  background-color: rgb(231, 76, 60);
}
.not-right .v-note-wrapper {
  color: rgb(231, 76, 60);
}
</style>
