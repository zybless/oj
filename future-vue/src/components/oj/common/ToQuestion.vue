<template>
  <el-card shadow :padding="10">
    <div slot="header">
      <span class="home-title panel-title"
        ><i class="el-icon-magic-stick"></i> {{ $t("m.ToQuestion") }}</span
      >
    </div>
    <div>
      <el-input v-model="input" placeholder="请输入题目ID"></el-input>
      <div style="margin-top: 10px;">
        <el-button type="danger" @click="toProblemDetail(input)"
          >跳转</el-button
        >
        <el-button type="primary" @click="randomTo">随机跳转</el-button>
      </div>
    </div>
  </el-card>
</template>

<script>
import Pagination from "@/components/oj/common/Pagination";
import { getRandomProblem } from "@/common/newApi/ojproblem/index";
export default {
  name: "ToQuestion",
  components: {
    Pagination,
  },
  data() {
    return {
      input: "",
    };
  },
  methods: {
    randomTo() {
      getRandomProblem().then((res) => {
        let id = res.data.data.problemId;
        this.toProblemDetail(id);
      });
    },
    toProblemDetail(problemID) {
      this.$router.push({
        name: "ProblemDetails",
        params: { problemID },
      });
    },
  },
};
</script>

<style scoped>
.no-announcement {
  text-align: center;
  font-size: 16px;
}
</style>
