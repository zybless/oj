<template>
  <el-dialog :title="title" :visible.sync="dialogFormVisible" top="5vh">
    <el-form :model="form" ref="ruleForm" label-width="120px">
      <el-form-item prop="title" label="标题" required>
        <el-input v-model="form.title"></el-input>
      </el-form-item>
      <el-form-item prop="duration" label="训练周期" required>
        <el-input v-model="form.duration"></el-input>
      </el-form-item>
      <el-form-item prop="intensity" label="训练强度" required>
        <el-input v-model="form.intensity"></el-input>
      </el-form-item>
      <el-form-item prop="description" label="描述" required>
        <el-input type="textarea" autosize v-model="form.description">
        </el-input>
      </el-form-item>
      <el-form-item prop="content" label="详情" required>
        <Editor :value.sync="form.content"></Editor>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="cancel">取 消</el-button>
      <el-button type="primary" @click="confirm">确 定</el-button>
    </div>
  </el-dialog>
</template>
<script>
const Editor = () => import("@/components/admin/Editor.vue");
import {
  addTrainCamp,
  updateTrainCamp,
} from "@/common/newApi/trainningcamp/index";
import myMessage from "@/common/message";
export default {
  name: "editCourse",
  components: { Editor },
  data() {
    return {
      dialogFormVisible: false,
      form: {},
      title: "",
    };
  },
  created() {},
  methods: {
    cancel() {
      this.dialogFormVisible = false;
    },

    show(title, row) {
      this.title = title;
      if (row) {
        this.form = Object.assign({}, row);
      } else {
        this.form = {};
      }
      this.dialogFormVisible = true;
    },
    confirm() {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          if (this.form.id) {
            updateTrainCamp(this.form).then((res) => {
              this.dialogFormVisible = false;
              this.$parent.getList();
            });
          } else {
            addTrainCamp(this.form).then((res) => {
              this.dialogFormVisible = false;
              this.$parent.getList();
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
