<template>
  <div style="width: 100%;" v-loading="loading">
    <template v-if="showContent">
      <div class="problem-list">
        <div style="font-size: 16px;height: 40px;margin-bottom: 10px;">
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
        <div
          v-for="(item, index) in questionList"
          :key="item"
          :id="'div' + index"
        >
          <div style="margin-bottom: 10px;font-size: 16px;">
            <span style="font-weight: 700"
              >第{{ index + 1 }}题 {{ PROBLEM_TYPE[item.type] }}</span
            >&nbsp;
            <span>({{ item.grade }}分)</span>
          </div>
          <EditorShow :value="item.content"></EditorShow>
          <template v-if="item.type === 3">
            <div
              v-for="(subItem, index2) in item.examProblemAnswerList"
              :key="subItem"
              style="display: flex;"
            >
              {{ index2 + 1 }}.<el-input
                type="textarea"
                autosize
                v-model="myAnswer[index].answer[index2]"
                style="margin: 0 0 5px 5px;"
                @change="changeAnswer(index)"
              >
              </el-input>
            </div>
          </template>
          <template v-else-if="item.type === 1">
            <el-checkbox-group
              v-model="myAnswer[index].answer"
              @change="changeAnswer(index)"
            >
              <div
                v-for="(subItem, index2) in item.examProblemAnswerList"
                :key="subItem"
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
                    >{{ String.fromCharCode(index2 + 65) }}.</el-checkbox
                  >
                </div>
                <EditorShow :value="subItem.content"></EditorShow>
              </div>
            </el-checkbox-group>
          </template>
          <template v-else>
            <div
              v-for="(subItem, index2) in item.examProblemAnswerList"
              :key="subItem"
              style="display: flex;"
            >
              <div
                style="display: flex;  
                justify-content: center;  
                align-items: center;
                margin-bottom: 16px;"
              >
                <el-radio
                  v-model="myAnswer[index].answer"
                  :label="subItem.id"
                  style="margin-right: 5px;"
                  @change="changeAnswer(index)"
                  >{{ String.fromCharCode(index2 + 65) }}.</el-radio
                >
              </div>
              <EditorShow :value="subItem.content"></EditorShow>
            </div>
          </template>
        </div>
        <div style="text-align: center;">
          <el-button type="primary" round @click="dialogVisible = true"
            >提交答卷</el-button
          >
        </div>
        <el-dialog :visible.sync="dialogVisible" width="30%">
          <span>提交答卷？提交后不可修改</span>
          <span slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" :loading="loading" @click="confirmRepo"
              >确 定</el-button
            >
          </span>
        </el-dialog>
      </div>
      <div class="right-div">
        <div style="margin-bottom: 10px;font-weight: 700;font-size: 16px;">
          题目列表
        </div>
        <div>
          <el-tag
            v-for="(item, index) in questionList"
            :key="item"
            @click="toItem(index)"
            :class="{
              'el-tag-complete': myAnswer[index].isComplete,
              'el-tag-uncomplete': !myAnswer[index].isComplete,
            }"
            >第{{ index + 1 }}题</el-tag
          >
        </div>
      </div>
    </template>
    <template v-else>
      <el-card
        class="password-form-card"
        style="text-align:center;margin-bottom:15px"
      >
        <div slot="header">
          <span class="panel-title" style="color: #e6a23c;"
            ><i class="el-icon-warning">
              {{ $t("m.Password_Required") }}</i
            ></span
          >
        </div>
        <p class="password-form-tips">
          {{ $t("m.To_Enter_Need_Password") }}
        </p>
        <el-input
          v-model="contestPassword"
          type="password"
          :placeholder="$t('m.Enter_the_contest_password')"
          @keydown.enter.native="checkPassword"
          style="width:70%"
        />
        <el-button
          type="primary"
          @click="checkPassword"
          style="float:right;"
          >{{ $t("m.Enter") }}</el-button
        >
      </el-card>
    </template>
  </div>
</template>

<script>
import { mapState, mapGetters } from "vuex";
import { JUDGE_STATUS } from "@/common/constants";
import api from "@/common/api";
import {
  getProblemListInRepoExam,
  getRepoById,
} from "@/common/newApi/questionbank/ojIndex";
import { getChooseExamById, getExam } from "@/common/newApi/chooseExam/index";
import { PROBLEM_TYPE } from "@/common/constants";
import { startExam, submitExam } from "@/common/newApi/chooseExam/ojIndex";
import myMessage from "@/common/message";
const EditorShow = () => import("@/components/oj/common/EditorShow.vue");

export default {
  name: "ExamProblemList",
  components: {
    EditorShow,
  },
  data() {
    return {
      isGetStatusOk: false,
      testcolor: "rgba(0, 206, 209, 1)",
      chooseExamDetail: {},
      contestID: 0,
      repo: [],
      questionList: [],
      PROBLEM_TYPE: {},
      myAnswer: [],
      dialogVisible: false,
      exerciseId: "",
      showContent: false,
      contestPassword: "",
      loading: true,
      loading3: true,
      loading4: true,
    };
  },
  created() {
    this.PROBLEM_TYPE = Object.assign({}, PROBLEM_TYPE);
    this.contestID = this.$route.params.chooseExamID;
    this.loading = true;
    getExam(this.contestID).then((res) => {
      this.chooseExamDetail = Object.assign({}, res.data.data);
      this.loading = false;
      this.repo = this.chooseExamDetail.examRepo;
    });
  },
  mounted() {},
  methods: {
    checkPassword() {
      if (this.contestPassword === "") {
        myMessage.warning(this.$i18n.t("m.Enter_the_contest_password"));
        return;
      }
      this.loading = true;
      this.loading3 = true;
      this.loading4 = true;
      startExam(this.chooseExamDetail.id, this.contestPassword)
        .then((res) => {
          this.exerciseId = res.data.data;
          this.showContent = true;
          this.getQuestionBank().then((res) => {
            this.loading4 = false;
            if (!this.loading3 && !this.loading4) {
              this.loading = false;
            }
          });
        })
        .catch((err) => {
          this.loading = false;
        });
      // myMessage.success(this.$i18n.t('m.Register_contest_successfully'));
    },
    confirmRepo() {
      let problemIds = this.questionList.map((item) => {
        return item.id;
      });
      let problemAnswers = this.myAnswer.map((item) => {
        if (item.answer === "") {
          return [];
        } else if (typeof item.answer === "number") {
          return [item.answer.toString()];
        } else {
          return item.answer.map((i) => {
            return i.toString();
          });
        }
      });
      let req = {
        historyId: this.exerciseId,
        problemIds,
        problemAnswers,
      };
      this.loading = true;
      submitExam(req)
        .then((res) => {
          this.loading = false;
          this.dialogVisible = false;
          myMessage.success("提交成功");

          this.$router.push({
            name: "ChooseExamDetails",
            params: {
              chooseExamID: this.contestID,
            },
          });
        })
        .catch((err) => {
          this.loading = false;
          this.dialogVisible = false;
          this.$router.push({
            name: "ChooseExamDetails",
            params: {
              chooseExamID: this.contestID,
            },
          });
        });
    },
    changeAnswer(index) {
      if (this.myAnswer[index].type === 0 || this.myAnswer[index].type === 2) {
        if (this.myAnswer[index].answer) {
          this.myAnswer[index].isComplete = true;
        }
      } else if (this.myAnswer[index].type === 1) {
        if (this.myAnswer[index].answer.length > 0) {
          this.myAnswer[index].isComplete = true;
        } else {
          this.myAnswer[index].isComplete = false;
        }
      } else {
        for (let i = 0; i < this.myAnswer[index].answer.length; i++) {
          if (!this.myAnswer[index].answer[i]) {
            this.myAnswer[index].isComplete = false;
            return;
          }
        }
        this.myAnswer[index].isComplete = true;
      }
      localStorage.setItem(
        "repoAnswer2" + this.contestID + this.repoID,
        JSON.stringify(this.myAnswer)
      );
    },
    toItem(index) {
      let id = "#div" + index;
      document.querySelector(id).scrollIntoView({
        behavior: "smooth",
        block: "center",
        inline: "nearest",
      });
    },
    getQuestionBank() {
      getProblemListInRepoExam({
        currentPage: 1,
        repoId: this.chooseExamDetail.examRepoId,
        examId: this.chooseExamDetail.id,
        limit: 1000,
      }).then((res) => {
        this.questionList = res.data.data.records;
        localStorage;
        const savedData = localStorage.getItem(
          "repoAnswer2" + this.contestID + this.repoID
        );
        if (savedData) {
          this.myAnswer = JSON.parse(savedData);
        } else {
          this.myAnswer = [];
          for (let i = 0; i < this.questionList.length; i++) {
            if (
              this.questionList[i].type === 0 ||
              this.questionList[i].type === 2
            ) {
              this.myAnswer.push({
                type: this.questionList[i].type,
                answer: "",
                isComplete: false,
              });
            } else if (this.questionList[i].type === 1) {
              let answer = [];
              this.myAnswer.push({
                type: this.questionList[i].type,
                answer,
                isComplete: false,
              });
            } else {
              let answer = [];
              for (
                let j = 0;
                j < this.questionList[i].examProblemAnswerList.length;
                j++
              ) {
                answer.push("");
              }
              this.myAnswer.push({
                type: this.questionList[i].type,
                answer,
                isComplete: false,
              });
            }
          }
        }
        console.log("this.questionList", this.questionList);
        this.loading3 = false;
        if (!this.loading3 && !this.loading4) {
          this.loading = false;
        }
      });
    },
  },
  computed: {
    ...mapState({
      problems: (state) => state.contest.contestProblems,
    }),
    ...mapGetters([
      "isAuthenticated",
      "contestRuleType",
      "ContestRealTimePermission",
      "isContainsAfterContestJudge",
    ]),
  },
  watch: {
    isContainsAfterContestJudge() {
      this.getContestProblems();
    },
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
}
.el-tag-complete {
  background-color: #409eff;
}
.el-tag-uncomplete {
  background-color: rgb(170, 170, 170);
}
.problem-list {
  background-color: #ffffff;
  padding: 20px;
  width: calc(75% - 10px);
  display: inline-block;
}
.right-div {
  float: right;
  width: 25%;
  margin-left: 10px;
  position: sticky;
  top: 0;
}
.password-form-card {
  width: 400px;
  margin: 0 auto;
}
</style>
