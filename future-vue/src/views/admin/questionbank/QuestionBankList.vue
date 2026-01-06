<template>
  <div>
    <el-card>
      <div slot="header">
        <span class="panel-title home-title">{{
          $t("m.Question_Bank_List")
        }}</span>
        <div class="filter-row">
          <span>
            <el-button
              type="primary"
              size="small"
              @click="goCreateRepo"
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
        </div>
      </div>
      <vxe-table
        stripe
        auto-resize
        :data="repoList"
        ref="adminQuestionBank"
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
          field="remark"
          min-width="130"
          title="备注"
          show-overflow
        >
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
              content="分配题目"
              placement="top"
              v-if="isAdminRole"
            >
              <el-button
                icon="el-icon-s-order"
                size="mini"
                @click.native="allocProblem(row.id)"
                type="primary"
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
    <AllowProblem ref="allowProblem"></AllowProblem>
  </div>
</template>

<script>
import api from "@/common/api";
import {
  getQuestionBankList,
  deleteQuestionBank,
} from "@/common/newApi/questionbank/index";
import Edit from "./components/edit.vue";
import AllowProblem from "./components/allowProblem.vue";
import myMessage from "@/common/message";
import { REMOTE_OJ } from "@/common/constants";
import { mapGetters } from "vuex";
export default {
  name: "QuestionBankList",
  components: { Edit, AllowProblem },
  data() {
    return {
      total: 0,
      query: {
        auth: 0,
        pageSize: 10,
        keyword: "",
        currentPage: 1,
      },
      repoList: [],
      loading: false,
      // for make public use
      currentProblemID: "",
      currentRow: {},
      REMOTE_OJ: {},

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
      "isAdminRole",
    ]),
  },
  methods: {
    init() {
      this.getRepoList();
      this.REMOTE_OJ = Object.assign({}, REMOTE_OJ);
    },

    goEdit(problemId) {
      this.$refs["edit"].show("编辑", problemId);
    },
    goCreateRepo() {
      this.$refs["edit"].show("添加");
    },
    // 切换页码回调
    currentChange(page) {
      this.query.currentPage = page;
      this.getRepoList();
    },
    onPageSizeChange(pageSize) {
      this.query.currentPage = 1;
      this.query.pageSize = pageSize;
      this.getRepoList();
    },
    getRepoList() {
      let params = {
        limit: this.query.pageSize,
        currentPage: this.query.currentPage,
        keyword: this.query.keyword,
      };
      this.loading = true;
      this.showPagination = false;
      getQuestionBankList(params).then(
        (res) => {
          this.loading = false;
          this.total = res.data.data.total;
          this.repoList = res.data.data.records;
          this.showPagination = true;
        },
        (err) => {
          this.loading = false;
          this.showPagination = true;
        }
      );
    },
    deleteProblem(id) {
      this.$confirm(this.$i18n.t("m.Delete_Problem_Tips"), "Tips", {
        type: "warning",
      }).then(
        () => {
          deleteQuestionBank(id)
            .then((res) => {
              myMessage.success(this.$i18n.t("m.Delete_successfully"));
              this.getRepoList();
            })
            .catch(() => {});
        },
        () => {}
      );
    },
    filterByKeyword() {
      this.query.currentPage = 1;
      this.getRepoList();
    },
    allocProblem(id) {
      this.$refs["allowProblem"].show(id);
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
