<template>
  <div>
    <div class="container" v-loading="loading">
      <div class="title-article" style="text-align: left">
        <h1 class="title" id="sharetitle">
          <span>{{ announcement.title }}</span>
        </h1>
        <div class="title-msg">
          <span>
            <a
              class="c999"
              @click="
                getInfoByUsername(announcement.uid, announcement.username)
              "
              :title="announcement.username"
            >
              <avatar
                :username="announcement.username"
                :inline="true"
                :size="26"
                color="#FFF"
                class="user-avatar"
                :src="announcement.avatar"
              ></avatar>
              <span class="user-name">{{ announcement.username }}</span>
            </a>
          </span>
          <span>
            <i class="fa fa-clock-o"> {{ $t("m.Release_Time") }}：</i>
            <span>
              <el-tooltip
                :content="announcement.gmtCreate | localtime"
                placement="top"
              >
                <span>{{ announcement.gmtCreate | fromNow }}</span>
              </el-tooltip>
            </span>
          </span>
        </div>
      </div>
      <div class="body-article">
        <Markdown :content="announcement.content"> </Markdown>
      </div>
    </div>
  </div>
</template>

<script>
import api from "@/common/api";
import { addCodeBtn } from "@/common/codeblock";
import Avatar from "vue-avatar";
import { mapGetters, mapActions } from "vuex";
const Editor = () => import("@/components/admin/Editor.vue");
import Markdown from "@/components/oj/common/Markdown";
import { getAnnouncementList } from "@/common/newApi/announcement/index";
export default {
  components: {
    Avatar,
    Markdown,
    Editor,
  },
  data() {
    return {
      query: {
        currentPage: 1,
        did: null,
      },
      loading: false,
      announcementID: 0,
      announcement: {
        username: "DMY",
        avatar: "",
      },
    };
  },
  mounted() {
    this.init();
  },
  methods: {
    ...mapActions(["changeDomTitle"]),
    init() {
      this.announcementID = this.$route.params.announcementID || "";
      this.loading = true;
      
      getAnnouncementList(this.announcementID).then(
        (res) => {
          this.announcement = res.data.data;
          this.changeDomTitle({ title: this.announcement.title });
          this.$nextTick((_) => {
            addCodeBtn();
          });
          this.loading = false;
        },
        (err) => {
          this.loading = false;
        }
      );
    },
    getInfoByUsername(uid, username) {
      this.$router.push({
        path: "/user-home",
        query: { uid, username },
      });
    },
  },
  computed: {
    ...mapGetters(["isAuthenticated", "isAdminRole", "userInfo"]),
  },
  watch: {
    isAuthenticated(newVal, oldVal) {
      if (newVal != oldVal) {
        this.init();
      }
    },
  },
};
</script>

<style scoped>
/deep/ .el-dialog__body {
  padding: 0px 20px;
}
.container {
  box-sizing: border-box;
  background-color: #fff;
  box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);
  border: 1px solid #ebeef5;
  margin-bottom: 20px;
}
.title-article {
  background: #fff;
  overflow: hidden;
  padding: 10px 20px;
  position: relative;
  text-align: center;
}
.title-article h1.title {
  font-size: 25px;
  font-weight: 600;
  color: #34495e;
  padding: 0 0 10px;
  width: 80%;
  line-height: 32px;
  word-break: break-all;
}
.title-article .title-msg {
  margin-bottom: 0px;
  font-size: 12px;
  color: #999;
}
.title-article .title-msg span {
  margin-right: 2px;
}
.title-article .title-msg span a.c999 {
  color: #999 !important;
}
.title-article .title-msg span a.c999:hover {
  color: #007bff !important;
  text-decoration: none;
}
.user-avatar {
  vertical-align: middle;
}
.user-name {
  margin: 0 0.25rem !important;
}

.body-article {
  background: #fff;
  overflow: hidden;
  width: 100%;
  padding: 20px 20px;
  text-align: left;
  font-size: 14px;
  line-height: 1.6;
}
</style>
