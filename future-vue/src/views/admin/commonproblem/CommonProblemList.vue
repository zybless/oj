<template>
  <div>
    <el-card>
      <div slot="header">
        <span class="panel-title home-title">{{
          query.contestId
            ? $t("m.Contest_Problem_List")
            : $t("m.Common_Problem_List")
        }}</span>
        <div class="filter-row">
          <span>
            <el-button
              type="primary"
              size="small"
              @click="goCreateProblem"
              icon="el-icon-plus"
              >{{ $t("m.Create") }}
            </el-button>
          </span>
          <span>
            <vxe-input
              v-model="query.keyword"
              :placeholder="$t('m.Enter_keyword')"
              type="search"
              size="medium"
              @search-click="filterByKeyword"
              @keyup.enter.native="filterByKeyword"
            ></vxe-input>
          </span>
          <!-- <span v-if="!query.contestId">
            <el-select
              v-model="query.auth"
              @change="ProblemListChangeFilter"
              size="small"
              style="width: 180px;"
            >
              <el-option :label="$t('m.All_Problem')" :value="0"></el-option>
              <el-option :label="$t('m.Public_Problem')" :value="1"></el-option>
              <el-option
                :label="$t('m.Private_Problem')"
                :value="2"
              ></el-option>
              <el-option
                :label="$t('m.Contest_Problem')"
                :value="3"
              ></el-option>
            </el-select>
          </span> -->
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
        <vxe-table-column
          min-width="64"
          field="id"
          title="ID"
        ></vxe-table-column>
        <vxe-table-column
          min-width="150"
          field="content"
          title="内容"
          show-overflow="tooltip"
        ></vxe-table-column>
        <vxe-table-column
          field="type"
          min-width="150"
          title="题型"
          show-overflow
        >
          <template v-slot="{ row }">
            {{ PROBLEM_TYPE[row.type] }}
          </template>
        </vxe-table-column>
        <vxe-table-column
          field="difficulty"
          min-width="130"
          :title="$t('m.Level')"
          show-overflow
        >
          <template v-slot="{ row }">
            {{ getLevelName(row.difficulty) }}
          </template>
        </vxe-table-column>
        <vxe-table-column min-width="120" :title="$t('m.Created_Time')">
          <template v-slot="{ row }">
            {{ row.gmtCreate | localtime }}
          </template>
        </vxe-table-column>
        <vxe-table-column
          min-width="96"
          field="modifiedUser"
          :title="$t('m.Modified_User')"
          show-overflow
        >
        </vxe-table-column>
        <vxe-table-column
          min-width="96"
          field="author"
          title="作者"
          show-overflow
        >
        </vxe-table-column>
        <vxe-table-column title="Option" min-width="200">
          <template v-slot="{ row }">
            <el-tooltip
              effect="dark"
              :content="$t('m.Edit')"
              placement="top"
              v-if="
                isAdmin ||
                  row.author == userInfo.username
              "
            >
              <el-button
                icon="el-icon-edit-outline"
                size="mini"
                @click.native="goEdit(row.id)"
                type="primary"
              >
              </el-button>
            </el-tooltip>
            <el-tooltip
              effect="dark"
              :content="$t('m.Remove')"
              placement="top"
              v-if="query.contestId"
            >
              <el-button
                icon="el-icon-close"
                size="mini"
                @click.native="removeProblem(row.id)"
                type="warning"
              >
              </el-button>
            </el-tooltip>

            <el-tooltip
              effect="dark"
              :content="$t('m.Delete')"
              placement="top"
              v-if="isAdmin ||
                  row.author == userInfo.username"
            >
              <el-button
                icon="el-icon-delete-solid"
                size="mini"
                @click.native="deleteProblem(row.id)"
                type="danger"
              >
              </el-button>
            </el-tooltip>
          </template>
        </vxe-table-column>
      </vxe-table>

      <div class="panel-options" v-if="showPagination">
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
    </el-card>
    <Edit ref="edit"></Edit>
  </div>
</template>

<script>
import api from "@/common/api";
import {
  getCommonProblemList,
  deleteCommonProblem,
} from "@/common/newApi/commonproblem/index";
import Edit from "./components/edit.vue";
import utils from "@/common/utils";
import myMessage from "@/common/message";
import { REMOTE_OJ, PROBLEM_TYPE } from "@/common/constants";
import { mapGetters } from "vuex";
export default {
  name: "CommonProblemList",
  components: { Edit },
  data() {
    return {
      total: 0,
      query: {
        pageSize: 10,
        keyword: "",
        currentPage: 1,
        contestId: null,
      },
      problemList: [],
      contestProblemMap: {},
      loading: false,
      routeName: "",
      // for make public use
      currentProblemID: "",
      currentRow: {},
      REMOTE_OJ: {},
      PROBLEM_TYPE: {},
      showPagination: false,

      predefineColors: [
        "#ff4500",
        "#ff8c00",
        "#ffd700",
        "#90ee90",
        "#00ced1",
        "#1e90ff",
        "#c71585",
      ],
    };
  },
  mounted() {
    this.init();
  },
  computed: {
    ...mapGetters([
      "userInfo",
      "isAdmin",
    ]),
    isContest() {
      return !(this.routeName == "admin-problem-list" && !this.query.contestId);
    },
  },
  methods: {
    getLevelName(difficulty) {
      return utils.getLevelName(difficulty);
    },
    init() {
      this.routeName = this.$route.name;
      let query = this.$route.query;
      this.query.currentPage = query.currentPage || 1;
      this.query.pageSize = parseInt(query.pageSize) || 10;
      this.query.keyword = query.keyword;
      this.query.contestId = this.$route.params.contestId;
      this.contestProblemMap = {};
      this.getProblemList();
      this.REMOTE_OJ = Object.assign({}, REMOTE_OJ);
      this.PROBLEM_TYPE = Object.assign({}, PROBLEM_TYPE);
    },

    goEdit(problemId) {
      this.$refs["edit"].show("编辑", problemId);
    },
    goCreateProblem() {
      this.$refs["edit"].show("添加");
    },

    pushRouter() {
      if (this.query.contestId) {
        this.$router.push({
          name: "admin-contest-problem-list",
          query: this.query,
          params: {
            contestId: this.query.contestId,
          },
        });
      } else {
        this.$router.push({
          name: "common-problem-list",
          query: this.query,
        });
      }
    },

    // 切换页码回调
    currentChange(page) {
      this.query.currentPage = page;
      this.pushRouter();
    },
    onPageSizeChange(pageSize) {
      this.query.pageSize = pageSize;
      this.pushRouter();
    },
    getProblemList() {
      let params = {
        limit: this.query.pageSize,
        currentPage: this.query.currentPage,
        keyword: this.query.keyword,
      };
      this.loading = true;
      this.showPagination = false;
      getCommonProblemList(params).then(
        (res) => {
          this.loading = false;
          this.total = res.data.data.total;
          this.problemList = res.data.data.records;
          this.showPagination = true;
        },
        (err) => {
          this.loading = false;
          this.showPagination = true;
        }
      );
    },

    changeProblemAuth(row) {
      api.admin_changeProblemAuth(row).then((res) => {
        myMessage.success(this.$i18n.t("m.Update_Successfully"));
      });
    },

    deleteProblem(id) {
      this.$confirm(this.$i18n.t("m.Delete_Problem_Tips"), "Tips", {
        type: "warning",
      }).then(
        () => {
          deleteCommonProblem(id)
            .then((res) => {
              myMessage.success(this.$i18n.t("m.Delete_successfully"));
              this.getProblemList();
            })
            .catch(() => {});
        },
        () => {}
      );
    },
    removeProblem(pid) {
      this.$confirm(this.$i18n.t("m.Remove_Contest_Problem_Tips"), "Tips", {
        type: "warning",
      }).then(
        () => {
          api
            .admin_deleteContestProblem(pid, this.query.contestId)
            .then((res) => {
              myMessage.success("success");
              this.getProblemList();
            })
            .catch(() => {});
        },
        () => {}
      );
    },
    updateProblem(row) {
      let data = Object.assign({}, row);
      let funcName = "";
      if (this.query.contestId) {
        data.contest_id = this.query.contestId;
        funcName = "admin_editContestProblem";
      } else {
        funcName = "admin_editProblem";
      }
      api[funcName](data)
        .then((res) => {
          myMessage.success(this.$i18n.t("m.Update_Successfully"));
          this.getProblemList();
        })
        .catch(() => {});
    },
    downloadTestCase(problemID) {
      let url = "/api/file/download-testcase?pid=" + problemID;
      utils.downloadFile(url).then(() => {
        this.$alert(this.$i18n.t("m.Download_Testcase_Success"), "Tips");
      });
    },
    ProblemListChangeFilter() {
      this.query.currentPage = 1;
      this.pushRouter();
    },
    filterByKeyword() {
      this.query.currentPage = 1;
      this.pushRouter();
    },
    changeContestProblemColor(id, color) {
      let data = {
        id: id,
        color: color,
      };
      api.admin_setContestProblemInfo(data).then((res) => {
        myMessage.success(this.$i18n.t("m.Update_Balloon_Color_Successfully"));
      });
    },
  },
  watch: {
    $route(newVal, oldVal) {
      this.init();
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
