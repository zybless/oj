<template>
  <div style="padding: 20px;background-color: #ffffff;">
    <el-button style="margin-bottom: 10px;" type="primary" size="small" @click="exportRecord">导出记录</el-button>
    <div v-if="showTable">
      <vxe-table
        stripe
        auto-resize
        :data="list"
        ref="adminQuestionBank"
        :loading="loading"
        align="center"
      >
        <vxe-table-column min-width="64" field="rank" title="排名">
        </vxe-table-column>
        <vxe-table-column
          field="username"
          min-width="150"
          title="用户"
          show-overflow
        >
          <template v-slot="{ row }">
            <div class="contest-rank-user-box" style="display: flex;justify-content: center;">
              <span>
                <avatar
                  :username="row.username"
                  :inline="true"
                  :size="37"
                  color="#FFF"
                  :src="row.avatar"
                  :title="row.rankShowName"
                ></avatar>
                {{ row.username }}
              </span>
            </div>
          </template>
        </vxe-table-column>
        <vxe-table-column
          field="realname"
          min-width="130"
          title="真实姓名"
          show-overflow
        >
        </vxe-table-column>
        <vxe-table-column
          field="userScore"
          min-width="150"
          title="总分"
          show-overflow
        ></vxe-table-column>
        <vxe-table-column
          field="school"
          min-width="150"
          title="学校"
          show-overflow
        ></vxe-table-column>
        <vxe-table-column
          field="historyId"
          min-width="150"
          title="答题详情"
          show-overflow
        >
          <template v-slot="{ row }">
            <el-button
              size="mini"
              @click.native="go(row.historyId)"
              type="primary"
              >查看
            </el-button>
          </template>
        </vxe-table-column>
      </vxe-table>

      <div class="panel-options">
        <el-pagination
          class="page"
          layout="prev, pager, next, sizes"
          @current-change="currentChange"
          :page-size.sync="query.pageSize"
          :total.sync="total"
          :current-page.sync="query.currentPage"
          @size-change="onPageSizeChange"
          :page-sizes="[10, 30, 50, 100]"
        >
        </el-pagination>
      </div>
    </div>
    <div v-else v-loading="loading">
      <el-button @click="showTable = true"
        ><i class="el-icon-arrow-left"></i>返回</el-button
      >

      <el-row :gutter="20">
        <el-col :span="18">
          <div
            v-for="(item, index) in examRepoProblemVOList"
            :key="index"
            :id="'div' + index"
          >
            <div style="margin-bottom: 10px;font-weight: 700;font-size: 16px;">
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
        </el-col>
        <el-col :span="6">
          <p
            v-if="examHistory.userScore"
            style="font-size: 48px;margin: 0 0 16px;
      text-align:center;"
          >
            {{ examHistory.userScore }}分
          </p>
          <div style="margin-bottom: 10px;font-weight: 700;font-size: 16px;">
            题目列表
          </div>
          <div class="have-history">
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
          </div></el-col
        >
      </el-row>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import utils from '@/common/utils';
import { getExamRank, getHistory } from "@/common/newApi/chooseExam/ojIndex";
import Avatar from "vue-avatar";
import { PROBLEM_TYPE } from "@/common/constants";
import { getExam, exportExamHistory} from "@/common/newApi/chooseExam/index";
import { getProblemListInRepo } from "@/common/newApi/commonproblem/index";
const EditorShow = () => import("@/components/oj/common/EditorShow.vue");
export default {
  name: "exam-rank",
  components: {
    Avatar,
  },
  components: {
    EditorShow,
  },
  data() {
    return {
      currentPage: 1,
      total: 0,
      limit: 10,
      loading: true,
      query: {
        pageSize: 10,
        currentPage: 1,
      },
      showTable: true,
      PROBLEM_TYPE: {},
      yourAnswerIsRight: [],
    };
  },
  computed: {},
  created() {
    this.examID = this.$route.params.chooseExamID;
    this.getData();
    this.PROBLEM_TYPE = Object.assign({}, PROBLEM_TYPE);
    getExam(this.examID).then((res) => {
      this.chooseExamDetail = Object.assign({}, res.data.data);
      getProblemListInRepo({
        currentPage: 1,
        repoId: this.chooseExamDetail.examRepoId,
        limit: 1000,
      }).then((res) => {
        this.questionList = res.data.data.records;
        this.question = this.questionList[0];
        this.itemIndex = 0;
      });
    });
  },
  methods: {
    exportRecord(){
      let url = '/api/admin/exam/export-history?eid=' +this.examID.toString();
      utils.downloadFile(url);
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
    go(historyId) {
      this.loading = true;
      getHistory({ historyId }).then((res) => {
        console.log("history", res);
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
        this.showTable = false;
      });
    },
    getData() {
      this.loading = true;
      let params = {
        limit: this.query.pageSize,
        currentPage: this.query.currentPage,
        examId: this.examID,
      };
      getExamRank(params).then((res) => {
        this.list = res.data.data.records;
        console.log("this.list", this.list);
        this.total = res.data.data.total;
        this.loading = false;
      });
    },
    // 切换页码回调
    currentChange(page) {
      this.query.currentPage = page;
      this.getData();
    },
    onPageSizeChange(pageSize) {
      this.query.currentPage = 1;
      this.query.pageSize = pageSize;
      this.getData();
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
