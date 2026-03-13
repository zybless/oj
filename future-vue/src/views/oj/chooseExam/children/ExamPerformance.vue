<template>
  <div style="padding: 20px; background-color: #ffffff;">
    <!-- 未提交提示 -->
    <el-result
      v-if="notSubmitted"
      icon="info"
      title="暂无成绩"
      subTitle="您尚未提交本次考试，无法查看成绩">
    </el-result>

    <!-- 加载中 -->
    <div v-else-if="loading" v-loading="loading" style="min-height: 200px;"></div>

    <!-- 成绩详情 -->
    <div v-else>
      <el-row :gutter="20">
        <!-- 左侧题目列表 -->
        <el-col :span="18">
          <div
            v-for="(item, index) in examRepoProblemVOList"
            :key="index"
            :id="'div' + index"
            class="question-card"
            :class="yourAnswerIsRight[index] ? 'question-card--right' : 'question-card--wrong'"
          >
            <!-- 题目标题栏 -->
            <div class="question-header">
              <span class="question-index">第 {{ index + 1 }} 题</span>
              <span class="question-type">{{ PROBLEM_TYPE[item.type] }}</span>
              <el-tag
                size="small"
                :type="yourAnswerIsRight[index] ? 'success' : 'danger'"
                style="margin-left: auto;">
                {{ yourAnswerIsRight[index] ? '✓ 正确' : '✗ 错误' }}
              </el-tag>
            </div>

            <!-- 题目内容 -->
            <div class="question-content">
              <EditorShow :value="item.content"></EditorShow>
            </div>

            <!-- 填空题 -->
            <template v-if="item.type === 3">
              <div
                v-for="(subItem, index2) in item.examProblemAnswerList"
                :key="index2"
                style="display: flex; margin-bottom: 8px;">
                <span style="white-space: nowrap; line-height: 32px;">{{ index2 + 1 }}.</span>
                <el-input
                  type="textarea"
                  autosize
                  v-model="yourAnswer[index][index2]"
                  style="margin-left: 8px;"
                  disabled>
                </el-input>
              </div>
            </template>

            <!-- 多选题 -->
            <template v-else-if="item.type === 1">
              <el-checkbox-group v-model="yourAnswer[index]">
                <div
                  v-for="(subItem, index2) in item.examProblemAnswerList"
                  :key="index2"
                  class="option-row">
                  <el-checkbox :label="subItem.id" :key="subItem.id" disabled>
                    {{ String.fromCharCode(index2 + 65) }}.
                  </el-checkbox>
                  <EditorShow :value="subItem.content"></EditorShow>
                </div>
              </el-checkbox-group>
            </template>

            <!-- 单选/判断题 -->
            <template v-else>
              <div
                v-for="(subItem, index2) in item.examProblemAnswerList"
                :key="index2"
                class="option-row">
                <el-radio :label="subItem.id" disabled v-model="yourAnswer[index]">
                  {{ String.fromCharCode(index2 + 65) }}.
                </el-radio>
                <EditorShow :value="subItem.content"></EditorShow>
              </div>
            </template>

            <!-- 正确答案 & 解析 -->
            <div class="question-footer">
              <div class="answer-row">
                <span class="answer-label">正确答案：</span>
                <span class="answer-value">{{ getAnswer(item) }}</span>
              </div>
              <div class="analysis-row" v-if="item.analysis">
                <span class="answer-label">解析：</span>
                <EditorShow :value="item.analysis"></EditorShow>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 右侧成绩面板 -->
        <el-col :span="6">
          <div class="score-panel">
            <!-- 总分 -->
            <div class="score-display">
              <div class="score-number">{{ examHistory.userScore != null ? examHistory.userScore : '--' }}</div>
              <div class="score-label">总分</div>
            </div>

            <!-- 答题统计 -->
            <div class="score-stats">
              <div class="stat-item">
                <span class="stat-value correct-count">{{ correctCount }}</span>
                <span class="stat-label">答对</span>
              </div>
              <div class="stat-divider"></div>
              <div class="stat-item">
                <span class="stat-value wrong-count">{{ wrongCount }}</span>
                <span class="stat-label">答错</span>
              </div>
            </div>

            <!-- 题目导航 -->
            <div class="question-nav-title">题目导航</div>
            <div class="have-history">
              <el-tag
                v-for="(item, index) in examRepoProblemVOList"
                :key="index"
                @click="toItem(index)"
                type="info"
                :class="{ 'not-right': !yourAnswerIsRight[index] }">
                {{ index + 1 }}
              </el-tag>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { PROBLEM_TYPE } from "@/common/constants";
import { getChooseExamMyResult } from "@/common/newApi/chooseExam/index";
const EditorShow = () => import("@/components/oj/common/EditorShow.vue");

export default {
  name: "ExamPerformance",
  components: { EditorShow },
  data() {
    return {
      loading: true,
      notSubmitted: false,
      PROBLEM_TYPE: {},
      examHistory: {},
      examRepoProblemVOList: [],
      examHistoryDetailList: [],
      yourAnswer: [],
      yourAnswerIsRight: [],
    };
  },
  computed: {
    correctCount() {
      return this.yourAnswerIsRight.filter(v => v).length;
    },
    wrongCount() {
      return this.yourAnswerIsRight.filter(v => !v).length;
    },
  },
  created() {
    this.examID = this.$route.params.chooseExamID;
    this.PROBLEM_TYPE = Object.assign({}, PROBLEM_TYPE);
    this.loadMyResult();
  },
  methods: {
    loadMyResult() {
      this.loading = true;
      getChooseExamMyResult(this.examID).then(
        (res) => {
          const data = res.data.data;
          this.examHistory = data.examHistory || {};
          this.examRepoProblemVOList = data.examRepoProblemVOList || [];
          this.examHistoryDetailList = data.examHistoryDetailList || [];
          this.buildAnswers();
          this.loading = false;
        },
        () => {
          this.notSubmitted = true;
          this.loading = false;
        }
      );
    },
    buildAnswers() {
      this.yourAnswer = [];
      this.yourAnswerIsRight = [];
      this.examRepoProblemVOList.forEach((problem) => {
        this.examHistoryDetailList.forEach((detail) => {
          if (problem.id === detail.problemId) {
            this.yourAnswerIsRight.push(detail.isCorrect);
            if (problem.type === 0 || problem.type === 2) {
              const userAnswer = JSON.parse(detail.userAnswer);
              this.yourAnswer.push(userAnswer.length > 0 ? parseInt(userAnswer[0]) : "");
            } else if (problem.type === 1) {
              const userAnswer = JSON.parse(detail.userAnswer);
              this.yourAnswer.push(userAnswer.map((i) => parseInt(i)));
            } else {
              this.yourAnswer.push(JSON.parse(detail.userAnswer));
            }
          }
        });
      });
    },
    toItem(index) {
      const id = "#div" + index;
      document.querySelector(id).scrollIntoView({
        behavior: "smooth",
        block: "center",
        inline: "nearest",
      });
    },
    getAnswer(question) {
      let res = "";
      if (question.type < 3) {
        for (let i = 0; i < question.examProblemAnswerList.length; i++) {
          if (question.examProblemAnswerList[i].isCorrect) {
            res += String.fromCharCode(65 + i) + ",";
          }
        }
        res = res.substring(0, res.length - 1);
      } else if (question.type === 3) {
        for (let i = 0; i < question.examProblemAnswerList.length; i++) {
          res += question.examProblemAnswerList[i].content + ",";
        }
        res = res.substring(0, res.length - 1);
      }
      return res;
    },
  },
};
</script>

<style scoped>
/* 题目卡片 */
.question-card {
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  margin-bottom: 16px;
  overflow: hidden;
}
.question-card--right {
  border-left: 4px solid #5eb95e;
}
.question-card--wrong {
  border-left: 4px solid #e74c3c;
}

/* 题目标题栏 */
.question-header {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  background-color: #f5f7fa;
  border-bottom: 1px solid #e4e7ed;
  gap: 8px;
}
.question-index {
  font-weight: 700;
  font-size: 15px;
  color: #303133;
}
.question-type {
  font-size: 12px;
  color: #909399;
  background: #e4e7ed;
  padding: 2px 8px;
  border-radius: 10px;
}

/* 题目内容 */
.question-content {
  padding: 8px 16px;
}
.question-content /deep/ .v-show-content {
  padding: 0 !important;
  margin: 0 !important;
}
.question-content /deep/ .v-note-wrapper {
  min-height: auto !important;
  border: 0;
}
.question-content /deep/ p {
  margin: 0 !important;
  line-height: 1.6;
}

/* 选项行 */
.option-row {
  display: flex;
  align-items: center;
  padding: 0 16px;
  margin-bottom: 4px;
  min-height: 32px;
}
.option-row /deep/ .v-show-content {
  padding: 0 !important;
  margin: 0 !important;
}
.option-row /deep/ .v-note-wrapper {
  min-height: auto !important;
  border: 0;
}
.option-row /deep/ p {
  margin: 0 !important;
  line-height: 32px;
}

/* 答案解析区 */
.question-footer {
  margin: 8px 16px 12px;
  padding: 10px 12px;
  background: #f9f9f9;
  border-radius: 6px;
}
.answer-row {
  display: flex;
  align-items: center;
  margin-bottom: 6px;
}
.answer-label {
  font-weight: 600;
  color: #606266;
  white-space: nowrap;
}
.answer-value {
  color: #5eb95e;
  font-size: 15px;
  font-weight: 600;
}
.analysis-row {
  display: flex;
  align-items: flex-start;
}
.analysis-row /deep/ .v-show-content {
  padding: 0 !important;
  margin: 0 !important;
}
.analysis-row /deep/ .v-note-wrapper {
  min-height: auto !important;
  border: 0;
}
.analysis-row /deep/ p {
  margin: 0 !important;
  line-height: 1.6;
}

/* 右侧成绩面板 */
.score-panel {
  position: sticky;
  top: 20px;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  overflow: hidden;
}
.score-display {
  text-align: center;
  padding: 24px 0 16px;
  background: linear-gradient(135deg, #409eff, #66b1ff);
}
.score-number {
  font-size: 56px;
  font-weight: 700;
  color: #fff;
  line-height: 1;
}
.score-label {
  font-size: 14px;
  color: rgba(255,255,255,0.85);
  margin-top: 6px;
}

/* 答题统计 */
.score-stats {
  display: flex;
  border-bottom: 1px solid #e4e7ed;
}
.stat-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 14px 0;
}
.stat-divider {
  width: 1px;
  background: #e4e7ed;
}
.stat-value {
  font-size: 24px;
  font-weight: 700;
}
.stat-label {
  font-size: 12px;
  color: #909399;
  margin-top: 2px;
}
.correct-count { color: #5eb95e; }
.wrong-count { color: #e74c3c; }

/* 题目导航 */
.question-nav-title {
  padding: 12px 16px 8px;
  font-weight: 700;
  font-size: 14px;
  color: #303133;
}
.have-history {
  padding: 0 16px 16px;
}
.el-tag {
  margin: 0 5px 5px 0;
  cursor: pointer;
  color: #fff;
  background-color: rgb(170, 170, 170);
  border-color: transparent;
}
.have-history .el-tag {
  background-color: rgb(94, 185, 94);
}
.have-history .not-right {
  background-color: rgb(231, 76, 60);
}
</style>