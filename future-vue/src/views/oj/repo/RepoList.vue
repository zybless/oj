<template>
  <div class="container">
    <el-card shadow>
      <div slot="header">
        <span class="panel-title" style="font-weight: 700;">题库</span>
        <div class="filter-row">
          <span>
            <vxe-input
              v-model="query.keyword"
              :placeholder="$t('m.Enter_keyword')"
              type="search"
              size="medium"
              @keyup.enter.native="onKeywordChange"
              @search-click="onKeywordChange"
            ></vxe-input>
          </span>
        </div>
      </div>
      <div v-loading="loading">
        <p id="no-contest" v-show="repos.length == 0">
          <el-empty description="暂无试卷"></el-empty>
        </p>
        <vxe-table :data="repos" :show-header="false" border="inner">
          <vxe-table-column type="seq" width="60">
            <template v-slot="{ rowIndex }">
              <span style="font-size: 16px;">{{ rowIndex + 1 }}</span>
            </template>
          </vxe-table-column>
          <vxe-table-column field="title" title="标题">
            <template v-slot="{ row }">
              <el-link
                style="font-size: 16px;"
                type="primary"
                @click="toDetail(row)"
                >{{ row.title }}</el-link
              >
            </template>
          </vxe-table-column>
        </vxe-table>
      </div>
    </el-card>
    <Pagination
      :total="total"
      :pageSize="limit"
      @on-change="onCurrentPageChange"
      :current.sync="currentPage"
    ></Pagination>
  </div>
</template>

<script>
import api from "@/common/api";
import { getQuestionBankList } from "@/common/newApi/questionbank/ojIndex";

import { mapGetters } from "vuex";
import utils from "@/common/utils";
import time from "@/common/time";
import { CONTEST_TYPE_REVERSE, CONTEST_STATUS } from "@/common/constants";
import myMessage from "@/common/message";
const Pagination = () => import("@/components/oj/common/Pagination");
// const ContestListAttention = () => import('@/components/oj/contest/ContestListAttention');
const limit = 10;

export default {
  name: "repo-list",
  components: {
    Pagination,
    // ContestListAttention
  },
  data() {
    return {
      currentPage: 1,
      query: {
        status: "",
        keyword: "",
      },
      limit: limit,
      total: 0,
      rows: "",
      repos: [],
      CONTEST_STATUS: {},
      CONTEST_TYPE_REVERSE: {},
      acmSrc: require("@/assets/acm.jpg"),
      oiSrc: require("@/assets/oi.jpg"),
      loading: true,
    };
  },
  created() {
    let route = this.$route.query;
    this.currentPage = parseInt(route.currentPage) || 1;
  },
  mounted() {
    this.CONTEST_TYPE_REVERSE = Object.assign({}, CONTEST_TYPE_REVERSE);
    this.CONTEST_STATUS = Object.assign({}, CONTEST_STATUS);
    this.getQuestionBankList();
  },
  methods: {
    toDetail(row) {
      this.$router.push({
        name: "RepoDetail",
        params: { repoID: row.id },
      });
    },
    getQuestionBankList() {
      this.loading = true;
      let req = {
        keyword: this.query.keyword,
        limit: this.limit,
        currentPage: this.currentPage,
      };
      getQuestionBankList(req).then(
        (res) => {
          this.repos = res.data.data.records;
          this.total = res.data.data.total;
          this.loading = false;
        },
        (err) => {
          this.loading = false;
        }
      );
    },

    filterByChange() {
      let query = Object.assign({}, this.query);
      query.currentPage = this.currentPage;
      this.getQuestionBankList();
    },

    onCurrentPageChange(page) {
      this.currentPage = page;
      this.filterByChange();
    },
    onKeywordChange() {
      this.currentPage = 1;
      this.filterByChange();
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
</style>
