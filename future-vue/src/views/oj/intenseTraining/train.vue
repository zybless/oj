<template>
  <div>
    <div class="container" v-loading="loading">
      <div class="title-article" style="text-align: left">
        <h1 class="title" id="sharetitle">
          <span>{{ discussion.title }}</span>
        </h1>
      </div>
      <div class="body-article">
        <Markdown :content="discussion.content"> </Markdown>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import Markdown from "@/components/oj/common/Markdown";
import { getTrainingCamp } from "@/common/newApi/trainningcamp/ojIndex";
export default {
  components: {
    Markdown,
  },
  data() {
    return {
      discussion: {},
      discussionID: 0,
      loading: false,
    };
  },
  mounted() {
    this.init();
  },
  methods: {
    init() {
      this.discussionID = this.$route.params.id || "";
      this.loading = true;
      getTrainingCamp(this.discussionID).then(
        (res) => {
          this.discussion = res.data.data;

          this.loading = false;
        },
        (err) => {
          this.loading = false;
        }
      );
    },
  },
  computed: {
    ...mapGetters(["isAuthenticated"]),
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
.title-article .title-msg a.report {
  position: absolute;
  top: 30px;
  right: 5px;
  color: #4caf50 !important;
  font-weight: bold;
  font-size: 14px;
}
.title-article .title-msg a.like {
  position: absolute;
  top: 30px;
  right: 68px;
  color: #ff6700 !important;
  font-weight: bold;
  font-size: 14px;
}
@media screen and (max-width: 768px) {
  .title-article .title-msg a.report {
    top: 50px !important;
    right: 12px !important;
  }
  .title-article .title-msg a.like {
    top: 24px !important;
    right: 12px !important;
  }
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
