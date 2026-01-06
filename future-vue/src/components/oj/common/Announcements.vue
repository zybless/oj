<template>
  <el-card shadow :padding="10">
    <div slot="header">
      <span class="home-title panel-title"
        ><i class="el-icon-data-board"></i> {{ $t("m.Announcement") }}</span
      >
      <span style="float: right">
        <el-button
          type="primary"
          @click="init"
          size="small"
          icon="el-icon-refresh"
          :loading="btnLoading"
          >{{ $t("m.Refresh") }}</el-button
        >
      </span>
    </div>
    <div
      class="no-announcement"
      v-if="!announcements.length"
      key="no-announcement"
    >
      <el-empty :description="$t('m.No_Announcements')"></el-empty>
    </div>
    <ul class="announcements-container" key="list">
      <li
        v-for="announcement in announcements"
        :key="announcement.title"
        class="entry"
      >
        <a @click="goAnnouncement(announcement)"> {{ announcement.title }}</a>
      </li>
    </ul>
    <Pagination
      key="page"
      :total="total"
      :pageSize="limit"
      @on-change="getAnnouncementList"
      :current.sync="currentPage"
    ></Pagination>
  </el-card>
</template>

<script>
import api from "@/common/api";
import Pagination from "@/components/oj/common/Pagination";
export default {
  name: "Announcement",
  components: {
    Pagination,
  },
  props: {
    limit: {
      type: Number,
      default: 5,
    },
  },
  data() {
    return {
      total: 0,
      btnLoading: false,
      announcements: [],
      currentPage: 1,
      isForContest: false,
    };
  },
  mounted() {
    this.contestID = this.$route.params.contestID;
    // 如果路由中有竞赛id，说明不是首页的公告栏
    if (this.contestID) {
      this.isForContest = true;
    }
    this.init();
  },
  methods: {
    init() {
      this.getAnnouncementList();
    },
    getAnnouncementList() {
      this.btnLoading = true;
      if (this.isForContest) {
        api
          .getContestAnnouncementList(
            this.currentPage,
            this.limit,
            this.$route.params.contestID
          )
          .then(
            (res) => {
              this.announcements = res.data.data.records;
              this.btnLoading = false;
              this.total = res.data.data.total;
            },
            () => {
              this.btnLoading = false;
            }
          );
      } else {
        let req = {
          currentPage: this.currentPage,
          onlyMine: false,
          cid: 1,
        };
        api.getDiscussionList(this.limit, req).then(
          (res) => {
            this.announcements = res.data.data.records;
            this.btnLoading = false;
            this.total = res.data.data.total;
          },
          () => {
            this.btnLoading = false;
          }
        );
      }
    },
    goAnnouncement(announcement) {
      if (this.isForContest) {
        this.$router.push({
          name: "AnnouncementDetails",
          params: { announcementID: announcement.id },
        });
      } else {
        this.$router.push({
          name: "DiscussionDetails",
          params: { discussionID: announcement.id },
        });
      }
    },
  },
};
</script>

<style scoped>
.announcements-container {
  margin-top: -10px;
  margin-bottom: 10px;
}
.announcements-container li {
  padding-top: 12px;
  list-style: none;
  padding-bottom: 12px;
  margin-left: 20px;
  font-size: 16px;
}

.no-announcement {
  text-align: center;
  font-size: 16px;
}

.announcement-animate-enter-active {
  animation: fadeIn 1s;
}
ul {
  list-style-type: none;
  padding-inline-start: 0px;
}
.entry {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
