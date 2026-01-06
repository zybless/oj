<template>
  <el-dialog :title="title" :visible.sync="dialogFormVisible" top="5vh">
    <el-form :model="form" ref="ruleForm" label-width="120px">
      <el-form-item prop="title" label="标题" required>
        <el-input v-model="form.title"></el-input>
      </el-form-item>
      <el-form-item prop="type" label="类型" required>
        <el-select v-model="form.type" placeholder="请选择">
          <el-option
            v-for="item in COURSE_TYPE_LIST"
            :key="item.code"
            :label="item.name"
            :value="item.code"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="image" label="课程封面" required>
        <el-upload
          action="/api/file/upload-img"
          list-type="picture-card"
          :file-list="fileList"
          accept="image/gif,image/jpeg,image/jpg,image/png,image/svg,image/jfif,image/webp"
          :on-preview="handlePictureCardPreview"
          :on-remove="handleRemove"
          style="display: inline;"
          :limit="1"
          :on-success="handleSuccess"
          name="image"
        >
          <i class="el-icon-plus"></i>
        </el-upload>
        <el-dialog :visible.sync="dialogVisible" append-to-body>
          <img width="100%" :src="dialogImageUrl" alt="" />
        </el-dialog>
      </el-form-item>
      <el-form-item prop="price" label="价格（元）" required>
        <el-input-number
          v-model="form.price"
          :controls="false"
          :min="0"
          :precision="2"
        ></el-input-number>
      </el-form-item>
      <el-form-item prop="date" label="起止时间" required>
        <el-date-picker
          v-model="form.date"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item prop="replayTime" label="回放有效期" required>
        <el-date-picker
          v-model="form.replayTime"
          type="date"
          placeholder="选择日期"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item prop="limitNumber" label="人数上限" required>
        <el-input-number
          v-model="form.limitNumber"
          :controls="false"
          :min="0"
          :precision="0"
        ></el-input-number>
      </el-form-item>
      <el-form-item prop="teacherIds" label="授课教师" required>
        <el-select v-model="form.teacherIds" multiple placeholder="请选择">
          <el-option
            v-for="item in allTeachers"
            :key="item.uid"
            :label="item.username"
            :value="item.uid"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="description" label="描述" required>
        <el-input type="textarea" autosize v-model="form.description">
        </el-input>
      </el-form-item>
      <el-form-item prop="content" label="详情" required>
        <Editor :value.sync="form.content"></Editor>
      </el-form-item>
      <el-form-item prop="faq" label="常见问题" required>
        <Editor :value.sync="form.faq"></Editor>
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
  createCourse,
  updateCourse,
} from "@/common/newApi/course/index";
import api from "@/common/api";
import utils from "@/common/utils";
import myMessage from "@/common/message";
import { COURSE_TYPE_LIST } from "@/common/constants";
export default {
  name: "editCourse",
  components: { Editor },
  data() {
    return {
      dialogFormVisible: false,
      form: {},
      title: "",
      dialogImageUrl: "",
      dialogVisible: false,
      fileList: [],
      allTeachers: [],
      COURSE_TYPE_LIST: [],
    };
  },
  created() {
    this.COURSE_TYPE_LIST = [...COURSE_TYPE_LIST];
  },
  mounted() {
    api.admin_getUserList(1, 10000, "", true).then((res) => {
      this.allTeachers = res.data.data.records;
    });
  },
  watch: {
    fileList(newval) {
      if (newval.length > 0) {
        this.form.image = newval[0].url;
      } else {
        this.form.image = null;
      }
    },
  },
  methods: {
    uploadFile(params) {
      const file = params.file;
      let form = new FormData();
      form.append("image", file);
      if (this.form.id) {
        form.append("courseId", this.form.id);
      }
      this.$http({
        method: "post",
        url: "/api/file/upload-course-img",
        data: form,
        headers: { "content-type": "multipart/form-data" },
      }).then((res) => {
        this.fileList = [
          {
            url: res.data.msg,
          },
        ];
      });
    },
    cancel() {
      this.dialogFormVisible = false;
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    handleSuccess(response, file) {
      if(response.status != 200){
        this.$message.error('图片大小不能超过1MB!');
        return;
      }
      this.fileList = [
        {
          name: file.name,
          url: response.msg,
        },
      ];
    },
    handleRemove(file) {
      let url = file.url;
      if (url) {
        this.fileList = [];
        // deleteImg(url).then((res) => {
        //   myMessage.success(this.$i18n.t("m.Delete_successfully"));
        // });
      }
    },
    show(title, row) {
      this.title = title;
      if (row) {
        this.form = Object.assign({}, row);
        this.form.date = [row.startTime, row.endTime];
        this.fileList = [{ url: this.form.image }];
      } else {
        this.form = {};
        this.fileList = [];
      }
      this.dialogFormVisible = true;
    },
    confirm() {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          this.form.startTime = this.form.date[0];
          this.form.endTime = this.form.date[1];
          let req = {
            teacherIds: this.form.teacherIds,
            course: this.form,
          };
          if (this.form.id) {
            updateCourse(req).then((res) => {
              this.dialogFormVisible = false;
              this.$parent.getCourseList();
            });
          } else {
            createCourse(req).then((res) => {
              this.dialogFormVisible = false;
              this.$parent.getCourseList();
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
