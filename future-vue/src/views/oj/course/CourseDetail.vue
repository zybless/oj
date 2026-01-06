<template>
  <div class="container">
    <el-card shadow :body-style="{ display: 'flex' }">
      <div class="info-left">
        <img :src="course.image" />
      </div>
      <div class="info-right">
        <div class="info-title">
          <h2>{{ course.title }}</h2>
        </div>
        <div class="info-time">
          <span class="info-title-left">课程班级</span>
          <div>
            <span>八月班晚上</span>
          </div>
        </div>
        <div class="info-time">
          <span class="info-title-left">课程时间</span>
          <div>
            <span
              >开课时间:{{ parseTime(course.startTime) }}~
              {{ parseTime(course.endTime) }}</span
            ><br />
            <span>课程回放:{{ parseTime(course.replayTime) }}前无限回放</span>
          </div>
        </div>
        <div class="info-advantage">
          <span>随时可报名</span>
          <span>开课前随时退</span>
          <span>课后有回放</span>
        </div>
        <div class="last">
          <span class="price"> ￥{{ course.price }} </span>
          <el-button type="danger" size="small" @click="showImage"
            >点击报名</el-button
          >
        </div>
      </div>
    </el-card>
    <el-row :gutter="20">
      <el-col :span="16">
        <el-card shadow>
          <el-tabs v-model="activeName">
            <el-tab-pane label="详情" name="first">
              <EditorShow :value="course.content"></EditorShow>
            </el-tab-pane>
            <el-tab-pane label="常见问题" name="second">
              <EditorShow :value="course.faq"></EditorShow>
            </el-tab-pane>
          </el-tabs>
        </el-card></el-col
      >
      <el-col :span="8">
        <el-card shadow>
          <h3 class="lfe-h3">
            授课讲师
          </h3>
          <div v-for="(item, index) in teachers" :key="index" class="teachers">
            <avatar
              :username="userInfo.username"
              :inline="true"
              :size="30"
              color="#FFF"
              :src="item.avatar"
              class="drop-avatar"
            ></avatar>
            <div>
              <div class="name">
                {{ item.username }}
              </div>
              <div class="profession">{{ item.profession }}</div>
            </div>
          </div>
        </el-card>
        <!-- <el-card shadow>
          <h3 class="lfe-h3">
            近期课程
          </h3>
          <a
            v-for="(item, index) in courses"
            :key="index"
            class="related-course"
            @click="toDetail(item)"
          >
            <img :src="item.picUrl" class="img" />
            <div class="related-right">
              <div class="name">{{ item.title }}</div>
              <div class="price">￥{{ item.price }}</div>
            </div>
          </a>
        </el-card> -->
      </el-col>
    </el-row>
    <div v-if="isImageShown" class="overlay" @click="hideImage">
      <img :src="imgUrl" alt="Image" />
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { getCourseDetailUser } from "@/common/newApi/course/index";
import utils from "@/common/utils";
import Avatar from "vue-avatar";
const EditorShow = () => import("@/components/oj/common/EditorShow.vue");

export default {
  name: "course-detail",
  components: { Avatar, EditorShow },
  data() {
    return {
      imgUrl: require("@/assets/erweima.jpg"),
      isImageShown: false,
      query: {
        status: "",
        keyword: "",
      },
      course: {},
      teachers: [
        {
          avatar: "https://cdn.luogu.com.cn/upload/usericon/116664.png",
          username: "Daniel_lele",
          profession: "Ynoi和往届NOI命题人 清华大学，擅长数据结构",
        },
        {
          avatar: "https://cdn.luogu.com.cn/upload/usericon/116664.png",
          username: "Daniel_lele",
        },
        {
          avatar: "https://cdn.luogu.com.cn/upload/usericon/116664.png",
          username: "Daniel_lele",
        },
        {
          avatar: "https://cdn.luogu.com.cn/upload/usericon/116664.png",
          username: "Daniel_lele",
        },
        {
          avatar: "https://cdn.luogu.com.cn/upload/usericon/116664.png",
          username: "Daniel_lele",
        },
      ],
      courses: [],
      activeName: "first",
    };
  },
  created() {
    for (let i = 0; i < 5; i++) {
      this.courses.push({
        id: i,
        title: "洛谷算法竞赛秋令营 - 基础组",
        picUrl: "https://ipic.luogu.com.cn/2020autumn/small-pj.png",
        brief:
          "面向已经掌握初级算法学员。通过讲题和模拟增加经验​，提升CSP-J组应试能力。初赛课学员立减 100！",
        timeStart: "2023-09-30",
        timeEnd: "2023-10-16",
        peopleCount: 212,
        price: 599,
      });
    }
  },
  mounted() {
    getCourseDetailUser({ id: this.$route.params.courseID }).then((res) => {
      this.course = res.data.data.course;
      this.teachers = res.data.data.teacherList;
    });
  },
  methods: {
    parseTime(time) {
      return utils.parseTime(time, "{y}-{m}-{d}");
    },
    showImage() {
      this.isImageShown = true;
    },
    hideImage() {
      this.isImageShown = false;
    },
    toDetail(row) {
      this.$router.push({
        name: "CourseDetail",
        params: { courseID: row.id },
      });
    },
  },
  computed: {
    ...mapGetters(["isAuthenticated", "userInfo"]),
  },
};
</script>
<style scoped>
.last .price {
  font-size: 28px;
}
#no-contest {
  text-align: center;
  font-size: 16px;
  padding: 20px;
}
@media screen and (max-width: 768px) {
  ol {
    padding-inline-start: 5px;
  }
  /deep/ .el-card__header {
    margin-bottom: 5px;
  }
}

/deep/ .el-card__header {
  border-bottom: 0px;
}
.filter-row {
  font-size: 16px;
  display: flex;
}
.teachers .name {
  font-size: 16px;
  font-weight: 700;
}
ul {
  list-style: none;
  outline: 0;
  padding: 0;
  margin: 0;
}
.items {
  overflow: hidden;
}
.items li:first-child {
  margin-left: 0;
}
.items li {
  display: inline-block;
  border-radius: 3px;
  padding: 0.063em 0.5em;
  margin-left: 1em;
}
.selected {
  background-color: rgb(76, 175, 80);
  color: #fff;
}
.items > li.selected > a {
  color: #fff;
}
.items > li > a:hover {
  color: inherit !important;
}
.el-card {
  margin-bottom: 20px;
}
.header {
  width: 100%;
  display: flex;
  justify-content: center;
}
.header img {
  width: auto;
  height: auto;
  max-width: 100%;
  max-height: 100%;
  display: block;
}
.content {
  display: flex;
  flex-direction: column;
  padding: 0.5em 0.8em;
  overflow: hidden;
}
.title {
  font-weight: 400;
  font-size: 20px;
  margin: 0 0 8px 0;
}
.brief {
  margin: 16px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  font-size: 16px;
}
.time {
  margin-bottom: 8px;
  color: rgba(0, 0, 0, 0.5);
  font-size: 14px;
}
.last {
  display: flex;
  align-items: center;
}
.price {
  vertical-align: middle;
  font-weight: 500;
  color: #f40;
  margin-right: 10px;
}
.info-left {
  height: 300px;
  margin-right: 20px;
}
.info-left img {
  height: 300px;
}
.info-right .info-title h2 {
  font-size: 24px;
  display: block;
  margin-block-start: 0.83em;
  margin-block-end: 0.83em;
  margin-inline-start: 0px;
  margin-inline-end: 0px;
  font-weight: bold;
}
.info-time {
  padding: 2px 0;
  display: flex;
  align-items: center;
  font-size: 16px;
}
.info-title-left {
  margin-right: 16px;
  align-self: flex-start;
}
.info-advantage {
  display: flex;
  margin: 24px 0;
}
.info-advantage span {
  margin-right: 25px;
  font-size: 16px;
}
.last {
  display: flex;
  align-items: center;
}
.v-note-wrapper {
  border: 0;
  min-height: auto;
  z-index: 0;
}
/deep/.v-show-content {
  padding: 0 !important;
}
.teachers {
  display: flex;
  margin-top: 16px;
}
.drop-avatar {
  width: 42px;
  height: 42px;
  border-radius: 50%;
  margin-right: 16px;
}
.lfe-h3 {
  font-size: 18px;
  font-weight: 400;
  margin: 0 0 8px 0;
}
.profession {
  font-size: 14px;
}
.related-course {
  display: flex;
  margin-top: 16px;
  text-decoration: none;
}
.related-course img {
  width: 160px;
  height: 120px;
  margin-right: 16px;
  border-style: none;
}
.related-course .name {
  font-size: 16px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: #3498db;
}
.related-course .price {
  font-weight: 700;
}
.related-right {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  font-size: 16px;
}
.overlay {
  z-index: 1000;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.overlay img {
  max-height: 500px;
}
</style>
