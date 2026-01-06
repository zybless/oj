<template>
  <div class="container">
    <div style="display: inline-block;width: calc(75% - 10px);">
      <el-card shadow>
        <div slot="header">
          <span class="panel-title">{{ repo.title }}</span>
        </div>
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
          :key="index"
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
              :key="index2"
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
          <span>提交答卷？</span>
          <span slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" :loading="loading" @click="confirmRepo"
              >确 定</el-button
            >
          </span>
        </el-dialog>
      </el-card>
    </div>
    <div
      style="float: right;width:25%;margin-left: 10px;position: sticky;top:0"
    >
      <div style="margin-bottom: 10px;font-weight: 700;font-size: 16px;">
        题目列表
      </div>
      <div>
        <el-tag
          v-for="(item, index) in questionList"
          :key="index"
          @click="toItem(index)"
          :class="{
            'el-tag-complete': myAnswer[index].isComplete,
            'el-tag-uncomplete': !myAnswer[index].isComplete,
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
  getProblemListInRepoExam,
  getRepoById,
} from "@/common/newApi/questionbank/ojIndex";
import { startExercise, submitExercise } from "@/common/newApi/examjudge/index";
import { PROBLEM_TYPE } from "@/common/constants";
const EditorShow = () => import("@/components/oj/common/EditorShow.vue");
export default {
  name: "training-exam-problem-list",
  components: {
    EditorShow,
  },
  data() {
    return {
      dialogVisible: false,
      query: {
        status: "",
        keyword: "",
      },
      total: 0,
      repoID: "",
      questionList: [],
      PROBLEM_TYPE: {},
      repo: {},
      myAnswer: [],
      exerciseId: "",
      loading: false,
    };
  },
  created() {
    this.repoID = this.$route.params["repoID"];
    this.PROBLEM_TYPE = Object.assign({}, PROBLEM_TYPE);
  },
  mounted() {
    getRepoById({ id: this.repoID }).then((res) => {
      this.repo = res.data.data;
    });
    this.getQuestionBank();
    let trainingId = parseInt(this.$route.params.trainingID);
    let params = {
      trainingId,
      repoId: this.repoID,
    };
    startExercise(params).then((res) => {
      this.exerciseId = res.data.data;
    });
    // window.addEventListener("beforeunload", function(event) {
    //   event.returnValue = "a"; // 这一行会在大多数浏览器中显示一个确认对话框，提示用户是否离开页面
    // });
  },
  methods: {
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
      let trainingId = this.$route.params.trainingID;
      let req = {
        historyId: this.exerciseId,
        problemIds,
        problemAnswers,
        trainingId: parseInt(trainingId),
      };
      this.loading = true;
      submitExercise(req).then((res) => {
        this.loading = false;
        this.dialogVisible = false;
        this.$router.push({
          name: "TrainingExamList",
          params: { trainingID: trainingId },
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
        "repoAnswer" + this.repoID,
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
      let trainingId = parseInt(this.$route.params.trainingID);
      getProblemListInRepoExam({
        currentPage: 1,
        repoId: this.repoID,
        limit: 1000,
        trainingId,
      }).then((res) => {
        this.questionList = res.data.data.records;
        localStorage;
        const savedData = localStorage.getItem("repoAnswer" + this.repoID);
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
              // let answer = [];
              // for (
              //   let j = 0;
              //   j < this.questionList[i].examProblemAnswerList.length;
              //   j++
              // ) {
              //   answer.push(false);
              // }
              // this.myAnswer.push({
              //   type: this.questionList[i].type,
              //   answer,
              //   isComplete: false,
              // });
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
      });
    },
  },
  computed: {
    ...mapGetters(["isAuthenticated", "userInfo"]),
  },
  watch: {
    $route(newVal, oldVal) {
      if (newVal !== oldVal) {
        this.init();
      }
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
</style>
