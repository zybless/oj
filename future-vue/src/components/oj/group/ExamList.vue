<template>
  <el-dialog
    title="试卷列表"
    :visible.sync="dialogVisible"
    top="10vh"
    width="800px"
  >
    <div>
      <div slot="header">
        <span class="panel-title home-title">
          作业试卷列表
        </span>
        <div class="filter-row">
          <span>
            <el-button
              type="primary"
              size="small"
              icon="el-icon-plus"
              @click="addProblemDialogVisible = true"
              >添加试卷
            </el-button>
          </span>
          <span>
            <vxe-input
              v-model="keyword"
              :placeholder="$t('m.Enter_keyword')"
              type="search"
              size="medium"
              @search-click="filterByKeyword"
              @keyup.enter.native="filterByKeyword"
            ></vxe-input>
          </span>
        </div>
      </div>
      <vxe-table
        stripe
        auto-resize
        :data="problemList"
        ref="adminProblemList"
        :loading="loading"
        align="center"
      >
        <vxe-table-column min-width="64" field="id" title="ID">
        </vxe-table-column>
        <vxe-table-column
          field="title"
          min-width="150"
          :title="$t('m.Title')"
          show-overflow
        >
        </vxe-table-column>

        <vxe-table-column
          field="problemCount"
          min-width="100"
          title="题目数目"
          show-overflow
        >
        </vxe-table-column>
        <vxe-table-column title="Option" min-width="200">
          <template v-slot="{ row }">
            <el-tooltip effect="dark" :content="$t('m.Remove')" placement="top">
              <el-button
                icon="el-icon-close"
                size="mini"
                @click.native="removeProblem(row.id)"
                type="warning"
              >
              </el-button>
            </el-tooltip>
          </template>
        </vxe-table-column>
      </vxe-table>
      <div class="panel-options">
        <el-pagination
          class="page"
          layout="prev, pager, next, sizes"
          @current-change="currentChange"
          :page-size="pageSize"
          :total="total"
          @size-change="onPageSizeChange"
          :page-sizes="[10, 30, 50, 100]"
        >
        </el-pagination>
      </div>
      <el-dialog
        :title="$t('m.Add_Training_Problem')"
        width="800px"
        top="10vh"
        :visible.sync="addProblemDialogVisible"
        :close-on-click-modal="false"
        append-to-body
      >
        <AddExam :trainingID="trainingId" @on-change="getExams"></AddExam>
      </el-dialog>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import AddExam from "@/views/admin/training/components/AddExam.vue";
import myMessage from "@/common/message";
import { mapGetters } from "vuex";
import { getExamList, deleteExam } from "@/common/newApi/trainingExam/index";

export default {
  name: "Group-exam-List",
  components: {
    AddExam,
  },
  props: {
    trainingId: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      problemListAuth: 0,
      oj: "All",
      pageSize: 10,
      total: 0,
      problemList: [],
      keyword: "",
      loading: false,
      currentPage: 1,
      // for make public use
      currentProblemID: "",
      currentRow: {},
      addProblemDialogVisible: false,
      displayId: "",
      dialogVisible: false,
      trainingId: ""
    };
  },
  computed: {
    ...mapGetters(["userInfo", "isAdmin"]),
  },
  methods: {
    open(trainingId){
      this.trainingId = trainingId;
      this.getExams(this.currentPage);
      this.dialogVisible = true;
    },
    // 切换页码回调
    currentChange(page) {
      this.currentPage = page;
      this.getExams(page);
    },
    onPageSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.getExams(this.currentPage);
    },
    getExams(page) {
      this.loading = true;
      let params = {
        keyword: this.keyword,
        currentPage: page,
        limit: this.pageSize,
        tid: this.trainingId,
      };
      getExamList(params)
        .then((res) => {
          this.loading = false;
          this.total = res.data.data.total;
          this.problemList = res.data.data.records;
        })
        .catch(() => {
          this.loading = false;
        });
    },
    removeProblem(pid) {
      this.$confirm(this.$i18n.t("m.Remove_Training_Problem_Tips"), "Tips", {
        type: "warning",
      }).then(
        () => {
          deleteExam({ eid: pid, tid: this.trainingId })
            .then((res) => {
              myMessage.success("success");
              this.getExams(this.currentPage);
            })
            .catch(() => {});
        },
        () => {}
      );
    },
    filterByKeyword() {
      this.currentChange(1);
    },
  },
};
</script>

<style scoped>
.filter-row span button {
  margin-top: 5px;
  margin-bottom: 5px;
}
.filter-row span div {
  margin-top: 8px;
}
@media screen and (max-width: 768px) {
  .filter-row span {
    margin-right: 5px;
  }
  .filter-row span div {
    width: 80%;
  }
}
@media screen and (min-width: 768px) {
  .filter-row span {
    margin-right: 20px;
  }
}
</style>
