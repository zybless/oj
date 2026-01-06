<template>
  <el-dialog :title="title" :visible.sync="dialogFormVisible" top="5vh">
    <el-form :model="form" ref="ruleForm" label-width="120px">
      <!-- <el-form-item prop="title" :label="$t('m.Title')" required>
        <el-input v-model="form.title"></el-input>
      </el-form-item> -->
      <el-form-item prop="difficulty" :label="$t('m.Level')" required>
        <el-radio-group v-model="form.difficulty">
          <el-radio :label="0">简单</el-radio>
          <el-radio :label="1">中等</el-radio>
          <el-radio :label="2">困难</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item prop="type" :label="$t('m.Type')" required>
        <el-radio-group v-model="form.type" @change="changeType(form.type)">
          <el-radio :label="0">单选</el-radio>
          <el-radio :label="1">多选</el-radio>
          <el-radio :label="2">判断</el-radio>
          <el-radio :label="3">填空</el-radio>
        </el-radio-group>
      </el-form-item>
      <!-- <el-form-item prop="repoIds" label="所属题库"
        ><el-select v-model="repoIds" filterable multiple placeholder="请选择">
          <el-option
            v-for="item in allRepoIds"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option> </el-select
      ></el-form-item> -->
      <el-form-item prop="content" :label="$t('m.Description')" required>
        <Editor :value.sync="form.content"></Editor>
      </el-form-item>
      <el-form-item prop="analysis" :label="$t('m.Analysis')" required>
        <Editor :value.sync="form.analysis"></Editor>
      </el-form-item>
    </el-form>
    <div v-show="form.type < 4">
      <el-button
        type="primary"
        size="small"
        @click="addItem"
        icon="el-icon-plus"
        plain
        style="margin: 0 20px 20px;"
        :disabled="form.type === 2"
        >{{ $t("m.Add_Options") }}</el-button
      >
      <el-table
        v-if="form.type < 3"
        :data="optionsData"
        border
        style="width: 100%"
      >
        <el-table-column prop="isCorrect" label="是否答案" width="80">
          <template slot-scope="scope">
            <el-checkbox
              v-model="scope.row.isCorrect"
              :true-label="1"
              :false-label="0"
              style="margin-right: 10px;"
              @change="changeCheckBox(scope.$index, scope.row)"
            ></el-checkbox
            >答案
          </template>
        </el-table-column>
        <el-table-column prop="content" label="选项内容">
          <template slot-scope="scope">
            <Editor
              class="option-editor"
              :value.sync="scope.row.content"
            ></Editor>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="80">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.$index, scope.row)"
              :disabled="form.type === 2 || optionsData.length === 1"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <el-table v-else :data="optionsData" border style="width: 100%">
        <el-table-column prop="content" label="选项内容">
          <template slot-scope="scope">
            <el-input type="textarea" autosize v-model="scope.row.content">
            </el-input>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="80">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.$index, scope.row)"
              :disabled="optionsData.length === 1"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogFormVisible = false">取 消</el-button>
      <el-button type="primary" @click="confirm">确 定</el-button>
    </div>
  </el-dialog>
</template>
<script>
const Editor = () => import("@/components/admin/Editor.vue");
import {
  createCommonProblem,
  getCommonProblemById,
  updateCommonProblem,
} from "@/common/newApi/commonproblem/index";

export default {
  name: "editCommonProblem",
  components: { Editor },
  data() {
    return {
      dialogFormVisible: false,
      form: {
        // title: "",
        difficulty: 0,
        type: 0,
        content: "",
        analysis: "",
      },
      optionsData: [],
      repoIds: [],
      allRepoIds: [],
      title: "",
      query: {
        pageSize: 10,
        keyword: "",
        currentPage: 1,
      },
    };
  },
  methods: {
    show(title, id) {
      this.title = title;
      if (id) {
        getCommonProblemById(id).then((res) => {
          this.form = Object.assign({}, res.data.data);
          this.repoIds = res.data.data.examRepoList;
          this.optionsData = res.data.data.examProblemAnswerList;
        });
      } else {
        this.form = {
          // title: "",
          difficulty: 0,
          type: 0,
          content: "",
          analysis: "",
        };
        this.optionsData = [];
        this.addItem();
        this.repoIds = [];
      }
      this.dialogFormVisible = true;
    },
    addItem() {
      if (this.form.id) {
        this.optionsData.push({ problemId: this.form.id, isCorrect: 0 });
      } else {
        this.optionsData.push({ isCorrect: 0 });
      }
    },
    handleDelete(index, row) {
      this.optionsData.splice(index, 1);
    },
    changeCheckBox(index, row) {
      if (this.form.type === 0 || this.form.type === 2) {
        for (let i = 0; i < this.optionsData.length; i++) {
          if (index !== i) {
            this.optionsData[i].isCorrect = 0;
          }
        }
      }
    },
    changeType(type) {
      if (type === 2) {
        if (this.form.id) {
          this.optionsData = [
            {
              problemId: this.form.id,
              isCorrect: 1,
              content: "正确",
            },
            {
              problemId: this.form.id,
              isCorrect: 0,
              content: "错误",
            },
          ];
        } else {
          this.optionsData = [
            {
              isCorrect: 1,
              content: "正确",
            },
            {
              isCorrect: 0,
              content: "错误",
            },
          ];
        }
      } else {
        this.optionsData = [];
        this.addItem();
      }
    },
    confirm() {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          let req = {
            examProblem: this.form,
            examProblemAnswers: this.optionsData,
            repoIds: [],
          };
          if (this.form.id) {
            updateCommonProblem(req).then((res) => {
              this.dialogFormVisible = false;
              this.$parent.getProblemList();
            });
          } else {
            createCommonProblem(req).then((res) => {
              this.dialogFormVisible = false;
              this.$parent.getProblemList();
            });
          }
        }
      });
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
</style>
