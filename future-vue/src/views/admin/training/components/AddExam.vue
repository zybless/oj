<template>
  <div style="text-align:center">
    <div style="margin-bottom:10px" v-if="contest.type != undefined">
      <span class="tips">添加题单试卷</span>
    </div>
    <vxe-input
      v-model="keyword"
      :placeholder="$t('m.Enter_keyword')"
      type="search"
      size="medium"
      @search-click="filterByKeyword"
      @keyup.enter.native="filterByKeyword"
      style="margin-bottom:10px"
    ></vxe-input>
    <vxe-table
      :data="problems"
      :loading="loading"
      auto-resize
      stripe
      align="center"
    >
      <vxe-table-column title="ID" min-width="100" field="id">
      </vxe-table-column>
      <vxe-table-column min-width="150" :title="$t('m.Title')" field="title">
      </vxe-table-column>
      <vxe-table-column :title="$t('m.Option')" align="center" min-width="100">
        <template v-slot="{ row }">
          <el-tooltip effect="dark" :content="$t('m.Add')" placement="top">
            <el-button
              icon="el-icon-plus"
              size="mini"
              @click.native="handleAddProblem(row.id)"
              type="primary"
            >
            </el-button>
          </el-tooltip>
        </template>
      </vxe-table-column>
    </vxe-table>

    <el-pagination
      class="page"
      layout="prev, pager, next"
      @current-change="getExams"
      :page-size="limit"
      :current-page.sync="page"
      :total="total"
    >
    </el-pagination>
  </div>
</template>
<script>
import { addExamFromPublic } from "@/common/newApi/trainingExam/index";
import { getQuestionBankList } from "@/common/newApi/questionbank/index";
import myMessage from "@/common/message";
export default {
  name: "add-exam-to-training",
  props: ["trainingID"],
  data() {
    return {
      page: 1,
      limit: 10,
      total: 0,
      loading: false,
      problems: [],
      contest: {},
      keyword: "",
    };
  },
  mounted() {
    this.getExams(1);
  },
  methods: {
    getExams(page) {
      this.loading = true;
      let params = {
        keyword: this.keyword,
        currentPage: page,
        limit: this.limit,
      };
      getQuestionBankList(params)
        .then((res) => {
          this.loading = false;
          this.total = res.data.data.total;
          this.problems = res.data.data.records;
        })
        .catch(() => {
          this.loading = false;
        });
    },
    handleAddProblem(id) {
      let data = {
        eid: id,
        tid: parseInt(this.trainingID),
      };
      addExamFromPublic(data).then(
        (res) => {
          this.$emit("on-change");
          myMessage.success(this.$i18n.t("m.Add_Successfully"));
          this.getExams(this.page);
        },
        () => {}
      );
    },
    filterByKeyword() {
      this.page = 1;
      this.getExams(this.page);
    },
  },
};
</script>
<style scoped>
.page {
  margin-top: 20px;
  text-align: right;
}
.tips {
  color: red;
  font-weight: bolder;
  font-size: 1rem;
}
</style>
