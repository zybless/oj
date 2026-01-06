<template>
  <el-dialog title="分配题目" :visible.sync="dialogFormVisible" top="5vh">
    <el-steps :active="active" finish-status="success">
      <el-step title="选择新增题目"></el-step>
      <el-step title="分数设置"></el-step>
      <el-step title="调整顺序"></el-step>
    </el-steps>
    <template v-if="active === 0">
      <div class="filter-row" style="margin-bottom: 10px;">
        <span style="margin-right: 20px;">
          <vxe-input
            v-model="query.keyword"
            :placeholder="$t('m.Enter_keyword')"
            type="search"
            size="medium"
            @search-click="filterByKeyword"
            @keyup.enter.native="filterByKeyword"
          ></vxe-input>
        </span>
        <!-- <el-button type="primary" @click="confirmItem">
          <template v-if="chosenCount > 0">确认{{ chosenCount }}项</template>
          <template v-else>请选择题目</template>
        </el-button> -->
      </div>
      <vxe-table
        stripe
        auto-resize
        :data="problemList"
        ref="questionBankList"
        :loading="loading"
        align="center"
        row-id="id"
        :checkbox-config="{ reserve: true }"
        @checkbox-change="checkboxChange"
        @checkbox-all="checkboxChange"
      >
        <vxe-table-column type="checkbox" width="60"></vxe-table-column>
        <vxe-table-column
          min-width="64"
          field="id"
          title="ID"
        ></vxe-table-column>
        <!-- <vxe-table-column
          min-width="150"
          field="content"
          title="内容"
          show-overflow="tooltip"
        ></vxe-table-column> -->
        <vxe-table-column
          min-width="150"
          field="content"
          title="内容"
          show-overflow="tooltip"
        >
          <template v-slot="{ row }">
            <el-tooltip effect="dark" :content="row.content" placement="top">
              <div style="overflow: hidden;text-overflow: ellipsis;">
                <span>{{ row.content }}</span>
              </div>
            </el-tooltip>
          </template>
        </vxe-table-column>
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
            <span v-if="row.gmtCreate">{{ row.gmtCreate | localtime }}</span>
          </template>
        </vxe-table-column>
        <vxe-table-column
          min-width="96"
          field="modifiedUser"
          :title="$t('m.Modified_User')"
          show-overflow
        >
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
    </template>
    <template v-else-if="active == 1">
      <el-row v-for="(item, index) in allPostItem" :key="index" :gutter="20">
        <el-col :span="18"
          ><div class="grid-content bg-purple">
            <div style="margin-bottom: 10px;font-weight: 700;">
              第{{ index + 1 }}题 {{ PROBLEM_TYPE[item.type] }}
            </div>
            <EditorShow :value="item.content"></EditorShow>
            <div
              v-for="(subItem, index) in item.examProblemAnswerList"
              :key="index"
              style="display: flex;"
            >
              <div
                style="display: flex;  
                justify-content: center;  
                align-items: center;
                margin-bottom: 16px;"
              >
                <el-checkbox
                  v-if="item.type !== 3"
                  v-model="subItem.isCorrect"
                  :disabled="true"
                  style="margin-right: 5px;"
                ></el-checkbox>
                <template v-if="item.type !== 3"
                  >{{ String.fromCharCode(index + 65) }}.</template
                >
              </div>
              <EditorShow :value="subItem.content"></EditorShow>
            </div>
          </div>
          <div class="qu-answer"></div>
        </el-col>
        <el-col :span="6">
          <!-- <div style="display: flex;"> -->
          <el-button size="mini" type="danger" @click="deleteItem(index)"
            >删除</el-button
          >
          <div class="opt-box">
            本题<el-input-number
              v-model="item.grade"
              :controls="false"
              :min="1"
              :max="100"
              size="small"
              style="margin: 0 5px;"
            ></el-input-number
            >分
          </div>
          <!-- </div> -->
        </el-col>
      </el-row>
    </template>
    <template v-else>
      <div
        v-for="(item, index) in allPostItem"
        draggable="true"
        @dragstart="onDragStart(index)"
        @dragover.prevent="onDragOver(index)"
        @drop="onDrop(index)"
        style="cursor: move;"
        :key="item.problemId"
      >
        <div class="grid-content bg-purple">
          <div style="margin-bottom: 10px;font-weight: 700;">
            第{{ index + 1 }}题 {{ PROBLEM_TYPE[item.type] }}
          </div>
          <EditorShow :value="item.content"></EditorShow>
        </div>
        <div class="qu-answer"></div>
      </div>
    </template>
    <div slot="footer" class="dialog-footer">
      <el-button v-if="active == 0" @click="next">下一步</el-button>
      <template v-else-if="active == 1">
        <el-button @click="pre">上一步</el-button>
        <el-button @click="next">下一步</el-button>
      </template>
      <template v-else>
        <el-button @click="pre">上一步</el-button>
        <el-button type="primary" @click="confirm">确 定</el-button>
      </template>
    </div>
  </el-dialog>
</template>
<script>
const EditorShow = () => import("@/components/oj/common/EditorShow.vue");
import {
  getCommonProblemList,
  getProblemListInRepo,
  getProblemListNotInRepo,
} from "@/common/newApi/commonproblem/index";
import utils from "@/common/utils";
import { PROBLEM_TYPE } from "@/common/constants";
import { updateRepoProblems } from "@/common/newApi/questionbank/index";
export default {
  name: "allocProblem",
  components: { EditorShow },
  data() {
    return {
      dialogFormVisible: false,
      loading: false,
      showPagination: true,
      total: 0,
      problemList: [],
      PROBLEM_TYPE: {},
      query: {
        auth: 0,
        pageSize: 10,
        keyword: "",
        currentPage: 1,
      },
      chosenItem: [],
      allPostItem: [],
      chosenCount: 0,
      repoId: "",
      active: 0,
      repo: {},
      disabled: true,
      draggedIndex: null,
      problemListInRepo: [],
    };
  },
  watch: {
    chosenItem(newVal) {
      this.chosenCount = newVal.length;
    },
  },
  mounted() {
    this.init();
  },
  methods: {
    onDragStart(index) {
      console.log("onDragStart");
      this.draggedIndex = index;
      this.startAutoScroll();
    },
    onDragOver(index) {
      if (this.draggedIndex !== null && this.draggedIndex !== index) {
        // 交换位置
        const draggedItem = this.allPostItem.splice(this.draggedIndex, 1)[0];
        this.allPostItem.splice(index, 0, draggedItem);
        this.draggedIndex = index;
      }
    },
    onDrop() {
      this.draggedIndex = null;
      this.stopAutoScroll();
      console.log("拖动结束，当前列表顺序：", this.allPostItem);
    },
    startAutoScroll() {
      this.scrollInterval = setInterval(() => {
        const scrollThreshold = 100; // 距离视口边缘的阈值
        const scrollSpeed = 10; // 滚动速度

        // 检查是否接近视口底部
        const {
          scrollTop,
          clientHeight,
          scrollHeight,
        } = document.documentElement;
        if (scrollHeight - (scrollTop + clientHeight) < scrollThreshold) {
          window.scrollBy(0, scrollSpeed); // 向下滚动
        }

        // 检查是否接近视口顶部
        if (scrollTop < scrollThreshold) {
          window.scrollBy(0, -scrollSpeed); // 向上滚动
        }
      }, 100);
    },
    stopAutoScroll() {
      clearInterval(this.scrollInterval);
    },
    show() {
      this.disabled =
        this.$refs.resName.scrollWidth <=
        Math.round(this.$refs.resName.getBoundingClientRect().width);
    },
    confirm() {
      let examRepoProblemList = this.allPostItem.map((item) => {
        return {
          grade: item.grade,
          problemId: item.id,
        };
      });
      for (let i = 0; i < examRepoProblemList.length; i++) {
        if (!examRepoProblemList[i].grade) {
          this.$message.error("分数不能为空");
          return;
        }
      }
      updateRepoProblems(examRepoProblemList, this.repoId).then((res) => {
        this.dialogFormVisible = false;
        this.$message({
          message: "分配成功",
          type: "success",
        });
      });
    },
    next() {
      if( this.active == 0) {
        this.allPostItem = [...this.chosenItem, ...this.problemListInRepo];
      }
      this.active = this.active + 1;
    },
    pre() {
      this.active = this.active - 1;
    },
    init() {
      this.PROBLEM_TYPE = Object.assign({}, PROBLEM_TYPE);
    },
    // 切换页码回调
    currentChange(page) {
      this.query.currentPage = page;
      this.getProblemListNotInRepo();
    },
    checkboxChange() {
      let list1 = this.$refs["questionBankList"].getCheckboxReserveRecords();
      let list2 = this.$refs["questionBankList"].getCheckboxRecords();
      this.chosenItem = [...list1, ...list2];
    },
    onPageSizeChange(pageSize) {
      this.query.pageSize = pageSize;
      this.getProblemListNotInRepo();
    },
    show(id) {
      this.repoId = id;
      this.getProblemListNotInRepo();
      this.chosenItem = [];
      // getQuestionBankById(id).then((res) => {
      //   this.repo = Object.assign({}, res.data.data);
      //   let problemIds = this.repo.examProblemList.map(item=>{
      //     return item.id
      //   })

      // });
      this.dialogFormVisible = true;
      this.active = 0;
      let params = {
        limit: 100000,
        currentPage: 1,
        repoId: this.repoId,
      };
      getProblemListInRepo(params).then((res) => {
        this.problemListInRepo = res.data.data.records;
      });
    },
    filterByKeyword() {
      this.query.currentPage = 1;
      this.getProblemListNotInRepo();
    },
    getProblemListNotInRepo() {
      let params = {
        limit: this.query.pageSize,
        currentPage: this.query.currentPage,
        keyword: this.query.keyword,
        repoId: this.repoId,
      };
      this.loading = true;
      this.showPagination = false;
      getProblemListNotInRepo(params).then(
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
    getLevelName(difficulty) {
      return utils.getLevelName(difficulty);
    },
    deleteItem(index) {
      this.allPostItem.splice(index, 1);
    },
  },
};
</script>
<style scoped>
/deep/.el-dialog {
  width: 1300px;
}
.option-editor {
  height: 200px;
}

/deep/.el-row {
  border-bottom: #eee 3px dotted;
  margin-bottom: 10px;
  padding-bottom: 10px;
}
.el-button.is-round {
  padding: 7px;
}
.opt-box {
  margin: 10px 0;
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
</style>
