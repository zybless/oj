<template>
  <el-dialog :title="title" :visible.sync="dialogFormVisible" top="5vh">
    <el-form :model="form" ref="ruleForm" label-width="80px">
      <el-form-item prop="title" :label="$t('m.Title')" required>
        <el-input v-model="form.title"></el-input>
      </el-form-item>
      <el-form-item prop="timeLimit" label="时间限制" required>
        <el-input-number
          :min="1"
          :step="1"
          :step-strictly="true"
          v-model="form.timeLimit"
        ></el-input-number>
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
import {
  createQuestionBank,
  getQuestionBankById,
  updateQuestionBank,
} from "@/common/newApi/questionbank/index";

export default {
  name: "editCommonProblem",
  data() {
    return {
      dialogFormVisible: false,
      form: {
        title: "",
        problemCount: 0,
        remark: "",
        timeLimit: 1,
      },
      title: "",
    };
  },
  methods: {
    show(title, id) {
      this.title = title;
      if (id) {
        getQuestionBankById(id).then((res) => {
          this.form = Object.assign({}, res.data.data);
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
            updateQuestionBank(this.form).then((res) => {
              this.dialogFormVisible = false;
              this.$parent.getRepoList();
            });
          } else {
            createQuestionBank(this.form).then((res) => {
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
