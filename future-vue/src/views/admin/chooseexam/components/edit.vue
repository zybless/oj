<template>
  <el-dialog :title="title" :visible.sync="dialogFormVisible" top="5vh">
    <el-form :model="form" ref="ruleForm" label-width="80px">
      <el-form-item prop="title" :label="$t('m.Title')" required>
        <el-input v-model="form.title"></el-input>
      </el-form-item>
      <el-form-item prop="password" label="密码" required>
        <el-input
          v-model="form.password"
        ></el-input>
      </el-form-item>
      <el-form-item prop="examRepoId" label="试卷" required>
        <el-select v-model="form.examRepoId" placeholder="请选择">
          <el-option
            v-for="item in repoList"
            :key="item.id"
            :label="item.title"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="startTime" label="开始时间" required>
        <el-date-picker
          v-model="form.startTime"
          type="datetime"
          placeholder="选择日期时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item prop="endTime" label="结束时间" required>
        <el-date-picker
          v-model="form.endTime"
          type="datetime"
          placeholder="选择日期时间">
      </el-date-picker>
      </el-form-item>
      <el-form-item prop="remark" label="备注">
        <el-input v-model="form.remark"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogFormVisible = false">取 消</el-button>
      <el-button type="primary" @click="confirm">确 定</el-button>
    </div>
  </el-dialog>
</template>
<script>
// import {
//   createQuestionBank,
//   getQuestionBankById,
//   updateQuestionBank,
// } from "@/common/newApi/questionbank/index";
import {
  createChooseExam,
  getChooseExamById,
  updateChooseExam,
} from "@/common/newApi/chooseExam/index";
import {
  getQuestionBankList,
} from "@/common/newApi/questionbank/index";
export default {
  name: "editCommonProblem",
  data() {
    return {
      dialogFormVisible: false,
      form: {
        title: "",
        remark: "",
      },
      title: "",
      repoList: []
    };
  },
  mounted() {
    let params = {
      limit: 10000,
      currentPage: 1,
    };
    getQuestionBankList(params).then(
        (res) => {
          this.repoList = res.data.data.records;
        }
      );
  },
  methods: {
    show(title, id) {
      this.title = title;
      if (id) {
        getChooseExamById(id).then((res) => {
          this.form = Object.assign({}, res.data.data);
          this.form.id = id
        });
      } else {
        this.form = {
          title: "",
          problemCount: 0,
          remark: "",
          timeLimit: 1,
        };
      }
      this.dialogFormVisible = true;
    },
    confirm() {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          if (this.form.id) {
            updateChooseExam(this.form).then((res) => {
              this.dialogFormVisible = false;
              this.$parent.getRepoList();
            });
          } else {
            createChooseExam(this.form).then((res) => {
              this.dialogFormVisible = false;
              this.$parent.getRepoList();
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
  width: 500px;
}
.option-editor {
  height: 200px;
}
</style>
