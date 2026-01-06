<template>
  <div>
    <el-card shadow>
      <!-- <div slot="header">
        <span class="panel-title">课程清单</span>
      </div> -->
      <div class="filter-row">
        <span class="name">
          课程类型
        </span>
        <ul class="items">
          <li
            v-for="(item, index) in COURSE_TYPE_LIST"
            :class="{
              selected: chosenItem === item.code,
            }"
            :key="index"
          >
            <a @click="chooseItem(item.code)">{{ item.name }}</a>
          </li>
        </ul>
      </div>
    </el-card>
    <div
      style="display: grid;grid-gap: 10px;
      grid-template-columns: repeat(4, 1fr);margin-top: 10px;"
    >
      <el-card shadow v-for="(item, index) in courses" :key="index">
        <img class="header" :src="item.image" />
        <div class="content">
          <h2 class="title">
            {{ item.title }}
          </h2>
          <div class="brief">
            {{ item.description }}
          </div>
          <div class="time">
            <span
              >开课时间:{{ parseTime(item.startTime) }}~
              {{ parseTime(item.endTime) }}
            </span>
          </div>
          <div class="last">
            <span class="price"> ￥{{ item.price }} </span>
            <el-button type="danger" size="small" @click="toDetail(item)"
              >了解详情</el-button
            >
          </div>
        </div>
      </el-card>
    </div>
    <!-- <el-row
      v-for="i in parseInt((courses.length + 3) / 4)"
      :gutter="20"
      :key="i"
    >
      <el-col :span="6" v-for="j in 4" :key="j">
        <el-card shadow v-if="i * 4 + j < courses.length">
          <img class="header" :src="courses[i * 4 + j].image" />
          <div class="content">
            <h2 class="title">
              {{ courses[i * 4 + j].title }}
            </h2>
            <div class="brief">
              {{ courses[i * 4 + j].description }}
            </div>
            <div class="time">
              <span
                >开课时间:{{ courses[i * 4 + j].startTime }}~
                {{ courses[i * 4 + j].endTime }}
              </span>
            </div>
            <div class="last">
              <span class="price"> ￥{{ courses[i * 4 + j].price }} </span>
              <el-button
                type="danger"
                size="small"
                @click="toDetail(courses[i * 4 + j])"
                >了解详情</el-button
              >
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row> -->
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { getCourseListUser } from "@/common/newApi/course/index";
const Pagination = () => import("@/components/oj/common/Pagination");
import utils from "@/common/utils";
import { COURSE_TYPE_LIST } from "@/common/constants";

export default {
  name: "course-list",
  components: {
    Pagination,
  },
  data() {
    return {
      query: {
        status: "",
        keyword: "",
      },
      courses: [],
      courseTypes: [
        {
          name: "全部",
          code: "",
        },
        {
          name: "编程入门",
          code: "1",
        },
        {
          name: "编程入门",
          code: "2",
        },
        {
          name: "编程入门",
          code: "3",
        },
        {
          name: "编程入门",
          code: "4",
        },
      ],
      chosenItem: -1,
    };
  },
  created() {
    // for (let i = 0; i < 10; i++) {
    //   this.courses.push({
    //     id: i,
    //     title: "洛谷算法竞赛秋令营 - 基础组",
    //     picUrl: "https://ipic.luogu.com.cn/2020autumn/small-pj.png",
    //     brief:
    //       "面向已经掌握初级算法学员。通过讲题和模拟增加经验​，提升CSP-J组应试能力。初赛课学员立减 100！",
    //     timeStart: "2023-09-30",
    //     timeEnd: "2023-10-16",
    //     peopleCount: 212,
    //     price: 599,
    //   });
    // }
    this.COURSE_TYPE_LIST = [{ code: -1, name: "全部" }, ...COURSE_TYPE_LIST];
  },
  mounted() {
    this.getCourseListUser();
  },
  methods: {
    chooseItem(index) {
      this.chosenItem = index;
      this.getCourseListUser();
    },
    toDetail(row) {
      this.$router.push({
        name: "CourseDetail",
        params: { courseID: row.id },
      });
    },
    getCourseListUser() {
      let type = this.chosenItem === -1 ? null : this.chosenItem;
      getCourseListUser({
        currentPage: 1,
        limit: 10000,
        type,
      }).then((res) => {
        this.courses = res.data.data.records.map((item) => {
          return item.course;
        });
      });
    },
    parseTime(time) {
      return utils.parseTime(time, "{y}-{m}-{d}");
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
.name {
  width: 7em;
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
.el-row {
  margin-top: 20px;
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
  font-size: 28px;
  margin-right: 10px;
}
</style>
