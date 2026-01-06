<template>
  <div class="problem-list">
    <vxe-table
      border="inner"
      stripe
      auto-resize
      highlight-hover-row
      :data="exams"
      align="center"
      @cell-click="goTrainingProblem"
    >
      <!-- <vxe-table-column field="id" title="ID" width="150" show-overflow>
      </vxe-table-column> -->
      <vxe-table-column
        field="title"
        :title="$t('m.Title')"
        min-width="150"
        show-overflow
      ></vxe-table-column>
      <vxe-table-column field="ac" title="通过率" min-width="120">
        <template v-slot="{ row }">
          <span>
            <el-tooltip
              effect="dark"
              :content="row.userScore + '/' + row.totalScore"
              placement="top"
            >
              <el-progress
                :text-inside="true"
                :stroke-width="20"
                :percentage="getPassingRate(row.userScore, row.totalScore)"
              ></el-progress>
            </el-tooltip>
          </span>
        </template>
      </vxe-table-column>
      <vxe-table-column title="操作" width="150">
        <template v-slot="{ row }">
          <el-button
            type="text"
            @click="
              (e) => {
                openDialog(row.id, e);
              }
            "
            >查看提交记录</el-button
          >
        </template>
      </vxe-table-column>
    </vxe-table>
    <el-dialog
      title="提交记录"
      :visible.sync="dialogTableVisible"
      width="500px"
    >
      <vxe-table :data="gridData" :loading="loading2" align="center">
        <vxe-table-column field="userScore" label="分数"></vxe-table-column>
        <vxe-table-column field="submitTime" label="提交时间">
          <template v-slot="{ row }">{{
            parseTime(row.submitTime)
          }}</template></vxe-table-column
        >
        <vxe-table-column title="操作" width="150">
          <template v-slot="{ row }">
            <el-button type="text" @click="goExamHistory(row)">查看</el-button>
          </template>
        </vxe-table-column>
      </vxe-table>
    </el-dialog>
  </div>
</template>

<script>
import { mapState, mapGetters } from "vuex";
import utils from "@/common/utils";
import { JUDGE_STATUS } from "@/common/constants";
import { getTrainingExamList } from "@/common/newApi/trainingExam/ojIndex";
import api from "@/common/api";
import { getExerciseHistory } from "@/common/newApi/examhistory/index";
export default {
  name: "TrainingExamList",
  data() {
    return {
      JUDGE_STATUS: {},
      isGetStatusOk: false,
      testcolor: "rgba(0, 206, 209, 1)",
      showTags: false,
      groupID: null,
      trainingID: "",
      exams: [],
      dialogTableVisible: false,
      gridData: [],
      loading2: false,
    };
  },
  created() {
    let gid = this.$route.params.groupID;
    if (gid) {
      this.groupID = gid;
    }
    this.trainingID = this.$route.params.trainingID;
  },
  mounted() {
    this.JUDGE_STATUS = Object.assign({}, JUDGE_STATUS);
    this.getExamList();
  },
  methods: {
    parseTime(time) {
      return utils.parseTime(time);
    },
    openDialog(eid, e) {
      this.dialogTableVisible = true;
      this.loading2 = true;
      getExerciseHistory(eid).then((res) => {
        this.gridData = res.data.data.filter((item) => {
          return item.submitTime !== null;
        });
        this.loading2 = false;
      });
      e.stopPropagation();
    },
    getExamList() {
      this.$store.dispatch("getTrainingExamList2").then(() => {
        if (this.isAuthenticated) {
          this.exams = this.examList;
          for (let i = 0; i < this.exams.length; i++) {
            this.exams[i].userScore = this.exams[i].userScore
              ? this.exams[i].userScore
              : 0;
          }
        }
      });
    },
    goTrainingProblem(event) {
      this.$router.push({
        name: "TrainingExamProblemList",
        params: {
          trainingID: this.$route.params.trainingID,
          repoID: event.row.id,
        },
      });
    },
    goExamHistory(row) {
      this.$router.push({
        name: "TrainingExamHistory",
        params: {
          trainingID: this.$route.params.trainingID,
          repoID: row.repoId,
        },
        query: { historyId: row.id },
      });
    },
    getPassingRate(ac, total) {
      if (!total) {
        return 0;
      }
      return ((ac / total) * 100).toFixed(2);
    },
  },
  computed: {
    ...mapState({
      examList: (state) => state.training.trainingExamList,
    }),
    ...mapGetters(["isAuthenticated"]),
  },
};
</script>

<style scoped>
@media screen and (min-width: 1050px) {
  /deep/ .vxe-table--body-wrapper {
    overflow-x: hidden !important;
  }
}
/* /deep/.vxe-table{
  font-size: 16px !important;
}
.el-link{
  font-size: 16px;
} */
</style>
