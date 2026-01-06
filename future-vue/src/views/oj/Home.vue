<template>
  <el-row :gutter="20">
    <el-col :md="15" :sm="24">
      <el-card>
        <div slot="header" class="content-center">
          <span class="panel-title home-title welcome-title"
            >{{ $t("m.Welcome_to") }}{{ websiteConfig.shortName }}</span
          >
        </div>
        <el-carousel
          :interval="interval"
          class="img-carousel"
          arrow="always"
          indicator-position="outside"
          :height="elementHeight.toString() + 'px'"
        >
          <el-carousel-item
            v-for="(item, index) in carouselImgList"
            :key="index"
            >
            <img
              :src="item.url"
              style="object-fit: contain;width: 100%;"
              ref="images"
            />
            <!-- <el-image
              :src="item.url"
              fit="fill"
              ref="images"
            >
              <div slot="error" class="image-slot">
                <i class="el-icon-picture-outline"></i>
              </div>
            </el-image> -->
          </el-carousel-item>
        </el-carousel>
      </el-card>
      <CurrentContest class="card-top"></CurrentContest>
      <SubmissionStatistic class="card-top"></SubmissionStatistic>
    </el-col>
    <el-col :md="9" :sm="24" class="phone-margin">
      <Announcements></Announcements>
      <ToQuestion class="card-top"></ToQuestion>
      <!-- <HonorRoll class="card-top"></HonorRoll> -->
      <ACRankings class="card-top"></ACRankings>
      <el-card class="card-top">
        <div slot="header" class="clearfix">
          <span class="panel-title home-title">
            <i class="el-icon-s-grid"></i>
            待完成题目</span
          >
        </div>
        <vxe-table
          highlight-hover-row
          stripe
          :loading="loading.recentUpdatedProblemsLoading"
          auto-resize
          :data="groupProblem"
          @cell-click="goProblem"
          max-height="350"
        >
          <vxe-table-column
            field="problemId"
            :title="$t('m.Problem_ID')"
            min-width="100"
            show-overflow
            align="center"
          >
          </vxe-table-column>
          <vxe-table-column
            field="title"
            :title="$t('m.Title')"
            show-overflow
            min-width="130"
            align="center"
          >
          </vxe-table-column>
        </vxe-table>
      </el-card>
    </el-col>
  </el-row>
</template>

<script>
import api from "@/common/api";
import { mapState, mapGetters } from "vuex";
import Avatar from "vue-avatar";
import { getUserGroupProblemStatus } from "@/common/newApi/ojproblem/index";
const Announcements = () => import("@/components/oj/common/Announcements.vue");
const ToQuestion = () => import("@/components/oj/common/ToQuestion.vue");
import HonorRoll from "@/components/oj/common/HonorRoll.vue";
import ACRankings from "@/components/oj/common/ACRankings.vue";
import CurrentContest from "@/components/oj/home/CurrentContest.vue";
import ResizeObserver from "resize-observer-polyfill";
// const SubmissionStatistic = () =>
//   import("@/components/oj/home/SubmissionStatistic.vue");
import SubmissionStatistic from "@/components/oj/home/SubmissionStatistic.vue";

export default {
  name: "home",
  components: {
    Announcements,
    ToQuestion,
    SubmissionStatistic,
    Avatar,
    CurrentContest,
    HonorRoll,
    ACRankings,
  },
  data() {
    return {
      elementHeight: 490,
      interval: 5000,
      recentUpdatedProblems: [],
      loading: {
        recentUpdatedProblemsLoading: false,
      },
      carouselImgList: [
        {url:""}
      ],
      srcHight: "440px",
      groupProblem: [],
    };
  },
  mounted() {
    let screenWidth = window.screen.width;
    if (screenWidth < 768) {
      this.srcHight = "200px";
    } else {
      this.srcHight = "440px";
    }
    this.getHomeCarousel();
    this.getRecentUpdatedProblemList();
    getUserGroupProblemStatus().then((res) => {
      let problemDetail = res.data.data.problemDetail;
      this.groupProblem = [];
      for (let key in problemDetail) {
        this.groupProblem.push(problemDetail[key]);
      }
    });
    //监听走马灯图片高度
    this.observer = new ResizeObserver((entries) => {
      for (let entry of entries) {
        this.elementHeight = entry.contentRect.height;
      }
    });
    // Observe the element
    this.observer.observe(this.$refs.images[0]);
  },
  beforeDestroy() {
    // Clean up the observer when the component is destroyed
    if (this.observer) {
      this.observer.unobserve(this.$refs.image0);
      this.observer.disconnect();
    }
  },
  methods: {
    getHomeCarousel() {
      api.getHomeCarousel().then((res) => {
        if (res.data.data != null && res.data.data.length > 0) {
          this.carouselImgList = res.data.data;
        }
      });
    },
    getRecentUpdatedProblemList() {
      this.loading.recentUpdatedProblemsLoading = true;
      api.getRecentUpdatedProblemList().then(
        (res) => {
          this.recentUpdatedProblems = res.data.data;
          this.loading.recentUpdatedProblemsLoading = false;
        },
        (err) => {
          this.loading.recentUpdatedProblemsLoading = false;
        }
      );
    },

    goProblem(event) {
      this.$router.push({
        name: "ProblemDetails",
        params: {
          problemID: event.row.problemId,
        },
      });
    },
  },
  computed: {
    ...mapState(["websiteConfig"]),
    ...mapGetters(["isAuthenticated"]), //是否登录
  },
};
</script>
<style scoped>
/deep/.el-card__header {
  padding: 0.6rem 1.25rem !important;
}
.card-top {
  margin-top: 20px;
}

.el-carousel__item h3 {
  color: #475669;
  font-size: 14px;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
}

ul,
li {
  padding: 0;
  margin: 0;
  list-style: none;
}

.content-center {
  text-align: center;
}
.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both;
}
.welcome-title {
  font-weight: 600;
  font-size: 25px;
  font-family: "Raleway";
}
/* .img-carousel {
  height: 490px;
} */

@media screen and (max-width: 768px) {
  .img-carousel {
    height: 220px;
    overflow: hidden;
  }
  .phone-margin {
    margin-top: 20px;
  }
}
.title .el-link {
  font-size: 21px;
  font-weight: 500;
  color: #444;
}
.clearfix h2 {
  color: #409eff;
}
.el-link.el-link--default:hover {
  color: #409eff;
  transition: all 0.28s ease;
}
.contest .content-info {
  padding: 0 70px 40px 70px;
}
.contest .contest-description {
  margin-top: 25px;
}
span.rank-tag.no1 {
  line-height: 24px;
  background: #bf2c24;
}

span.rank-tag.no2 {
  line-height: 24px;
  background: #e67225;
}

span.rank-tag.no3 {
  line-height: 24px;
  background: #e6bf25;
}

span.rank-tag {
  font: 16px/22px FZZCYSK;
  min-width: 14px;
  height: 22px;
  padding: 0 4px;
  text-align: center;
  color: #fff;
  background: #000;
  background: rgba(0, 0, 0, 0.6);
}
.user-avatar {
  margin-right: 5px !important;
  vertical-align: middle;
}
.cite {
  display: block;
  width: 14px;
  height: 0;
  margin: 0 auto;
  margin-top: -3px;
  border-right: 11px solid transparent;
  border-bottom: 0 none;
  border-left: 11px solid transparent;
}
.cite.no0 {
  border-top: 5px solid #bf2c24;
}
.cite.no1 {
  border-top: 5px solid #e67225;
}
.cite.no2 {
  border-top: 5px solid #e6bf25;
}

@media screen and (min-width: 1050px) {
  /deep/ .vxe-table--body-wrapper {
    overflow-x: hidden !important;
  }
}
</style>
