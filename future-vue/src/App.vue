<template>
  <div id="app">
    <template v-if="this.currentRoute!='/newlogin' && this.currentRoute!='/newlogin2' && this.currentRoute!='/newlogin3' && this.currentRoute!='/newlogin4'">
      <el-backtop :right="10"></el-backtop>
      <div v-if="!isAdminView" class="full-height" style="display: flex;">
        <div id="nav">
          <NavBar></NavBar>
        </div>
        <div style="width: calc(100% - 150px);margin-left: 150px;">
          <div style="display: flex;background-color: #fff;height: 60px;width:100%;">
            <div
              v-if="!isAuthenticated"
              class="btn-menu"
            >
              <el-button @click="toLive" size="small" style="margin-right: 10px;">课程直播频道</el-button>
              <el-button
                type="primary"
                size="medium"
                round
                @click="handleBtnClick('Login')"
                >{{ $t("m.NavBar_Login") }}
              </el-button>
              <el-button
                v-if="websiteConfig.register"
                size="medium"
                round
                @click="handleBtnClick('Register')"
                style="margin:0 20px 0 10px"
                >{{ $t("m.NavBar_Register") }}
              </el-button>
            </div>
            <div class="btn-menu" v-else>
              <el-button @click="toLive" size="small" style="margin-right: 10px;">课程直播频道</el-button>
              <!-- <el-link type="primary" class="scratch" :underline="false" @click="jumpToScratch">
                scratch
              </el-link> -->
              <el-dropdown
                class="drop-msg"
                @command="handleRoute"
                placement="bottom"
              >
                <span class="el-dropdown-link">
                  <i class="el-icon-message-solid"></i>
                  <svg
                    v-if="
                      unreadMessage.comment > 0 ||
                        unreadMessage.reply > 0 ||
                        unreadMessage.like > 0 ||
                        unreadMessage.sys > 0 ||
                        unreadMessage.mine > 0
                    "
                    width="10"
                    height="10"
                    style="vertical-align: top;margin-left: -11px;margin-top: 3px;"
                  >
                    <circle cx="5" cy="5" r="5" style="fill: red;"></circle>
                  </svg>
                </span>

                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="/message/discuss">
                    <span>{{ $t("m.DiscussMsg") }}</span>
                    <span class="drop-msg-count" v-if="unreadMessage.comment > 0">
                      <MsgSvg :total="unreadMessage.comment"></MsgSvg>
                    </span>
                  </el-dropdown-item>
                  <el-dropdown-item command="/message/reply">
                    <span>{{ $t("m.ReplyMsg") }}</span>
                    <span class="drop-msg-count" v-if="unreadMessage.reply > 0">
                      <MsgSvg :total="unreadMessage.reply"></MsgSvg>
                    </span>
                  </el-dropdown-item>
                  <el-dropdown-item command="/message/like">
                    <span>{{ $t("m.LikeMsg") }}</span>
                    <span class="drop-msg-count" v-if="unreadMessage.like > 0">
                      <MsgSvg :total="unreadMessage.like"></MsgSvg>
                    </span>
                  </el-dropdown-item>
                  <el-dropdown-item command="/message/sys">
                    <span>{{ $t("m.SysMsg") }}</span>
                    <span class="drop-msg-count" v-if="unreadMessage.sys > 0">
                      <MsgSvg :total="unreadMessage.sys"></MsgSvg>
                    </span>
                  </el-dropdown-item>
                  <el-dropdown-item command="/message/mine">
                    <span>{{ $t("m.MineMsg") }}</span>
                    <span class="drop-msg-count" v-if="unreadMessage.mine > 0">
                      <MsgSvg :total="unreadMessage.mine"></MsgSvg>
                    </span>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
              <avatar
                :username="userInfo.username"
                :inline="true"
                :size="30"
                color="#FFF"
                :src="avatar"
                class="drop-avatar"
              ></avatar>
              <el-dropdown
                class="drop-menu"
                @command="handleRoute"
                placement="bottom"
                trigger="hover"
              >
                <span class="el-dropdown-link">
                  {{ userInfo.username }}<i class="el-icon-caret-bottom"></i>
                </span>

                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="/user-home">{{
                    $t("m.NavBar_UserHome")
                  }}</el-dropdown-item>
                  <el-dropdown-item command="/status?onlyMine=true">{{
                    $t("m.NavBar_Submissions")
                  }}</el-dropdown-item>
                  <el-dropdown-item command="/setting">{{
                    $t("m.NavBar_Setting")
                  }}</el-dropdown-item>
                  <el-dropdown-item v-if="isAdminRole" command="/admin">{{
                    $t("m.NavBar_Management")
                  }}</el-dropdown-item>
                  <el-dropdown-item divided command="/logout">{{
                    $t("m.NavBar_Logout")
                  }}</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
              <button
                class="wbv-button"
                style="width: 102px;"
                @click="dialogVisible = true"
                v-if="!userInfo.isVip"
              >
                开通会员
              </button>
              <el-tooltip effect="dark" content="您已是VIP">
                <icon
                  style="margin-right: 30px;cursor: pointer;"
                  v-if="userInfo.isVip"
                  name="VIP"
                  :w="30"
                  :h="30"
                ></icon>
              </el-tooltip>
              <el-dialog class="vip" :visible.sync="dialogVisible" width="640">
                <div
                  slot="title"
                  :style="{
                    background:
                      'url(' + require('@/assets/vip-background.png') + ')',
                  }"
                  class="vip-header"
                >
                  <div style="margin: 20px 0 0 20px;display: flex;">
                    <avatar
                      :username="userInfo.username"
                      :inline="true"
                      :size="48"
                      color="#FFF"
                      :src="avatar"
                      class="drop-avatar"
                    ></avatar>
                    <div style="margin-left:11px;text-align: left;height: auto;">
                      <div style="font-size: 14px;margin-top:4px;display: flex;">
                        {{ userInfo.username }}
                      </div>
                      <div
                        style="margin-top: 6px;font-size: 12px;
                      color:#939393"
                      >
                        你还不是会员
                      </div>
                    </div>
                  </div>
                </div>
                <div style="background-color: #f5f5f5;padding: 20px;">
                  <div
                    :class="
                    {
                      'is-active':isActive,
                      'no-active':!isActive
                    }"
                    style="height: 125px;
                    width: 125px;
                    border-radius:10px;
                    text-align: center;
                    cursor: pointer;"
                    @click="isActive=true"
                  >
                    <h2
                      style="font-size: 16px;margin: 19px 0 8px;font-weight: 400;"
                    >
                      12个月
                    </h2>
                    <h3 style="font-size: 26px;margin: 0 0 5px;">399<span style="font-size: 14px;font-weight: 700;">元</span></span></h3>
                  </div>
                  <div style="display: flex;margin: 20px 0 ;">
                    <span style="line-height: 34px;">选择支付方式：</span>
                      <div class="ipayment"  @click="showImage">
                      <img style="height: 16px;width: 16px;" :src="require('@/assets/zfblogo.png')"/>
                      支付宝支付 </div>
                  </div>
                </div>
              </el-dialog>
            </div>
          </div>
          <div id="oj-content">
            <transition name="el-zoom-in-bottom">
              <router-view></router-view>
            </transition>
          </div>
          <footer class="fix-to-bottom">
            <div class="mundb-footer">
              <a
                style="color:#1E9FFF"
                :href="'https://beian.miit.gov.cn/'"
                target="_blank"
              >苏ICP备 2024129332号</a>
            </div>
          </footer>
        </div>
      </div>
      <template v-else>
        <div id="admin-content">
          <transition name="el-zoom-in-bottom">
            <router-view></router-view>
          </transition>
        </div>
      </template>
      <div v-if="isImageShown" class="overlay" @click="hideImage">
        <img :src="imgUrl2" alt="Image" />
      </div>
    </template>
    <template v-else>
      <router-view></router-view>
    </template>
  </div>
</template>

<script>
import NavBar from "@/components/oj/common/NavBar";
import { mapActions, mapState, mapGetters } from "vuex";
import storage from "@/common/storage";
import utils from "@/common/utils";
import Avatar from "vue-avatar";
import api from '@/common/api';
import {getLiveStream} from '@/common/newApi/user/index'
import MsgSvg from "@/components/oj/msg/msgSvg";
export default {
  name: "app-content",
  components: {
    NavBar,
    Avatar,
    MsgSvg,
  },
  data() {
    return {
      isAdminView: false,
      imgUrl2: require("@/assets/zhifubao.jpg"),
      showFooter: true,
      dialogVisible: false,
      vipPrice: [{ time: 12, price: 399 }],
      isActive:false,
      isImageShown: false,
      liveurl:'https://meeting.tencent.com/dm/w0D9UEJqYgoh',
      currentRoute: "",
    };
  },
  methods: {
    showImage() {
      this.isImageShown = true;
    },
    hideImage() {
      this.isImageShown = false;
    },
    toLive(){
      window.open(this.liveurl);
    },
    jumpToScratch() {
      window.open("https://noip.wlbbai.com/scratch/");
    },
    pay(){
      if(!this.isActive){
        this.$message({
          message: '请选择套餐',
          type: 'warning'
        }); 
      }else{
        window.open('/api/alipay/buy-vip?id=1');
        this.$confirm('完成支付？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          api.getUserAuthInfo().then(res=>{
            if(res.data.data.roles.filter(item=>{
              return item ==="vip"
            }).length>0){
              this.userInfo.isVip = true
              this.$store.dispatch('setUserInfo', this.userInfo);
              location.reload()
            }else{
              this.$message({
                type: 'error',
                message: '支付失败'
              });  
            }
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '取消支付'
          });          
        });
      }
    },
    ...mapActions(["changeDomTitle", "getWebsiteConfig", "changeModalStatus"]),
    handleBtnClick(mode) {
      this.changeModalStatus({
        mode,
        visible: true,
      });
    },
    handleRoute(route) {
      //电脑端导航栏路由跳转事件
      if (route && route.split("/")[1] != "admin") {
        this.$router.push(route);
      } else {
        window.open("/admin/");
      }
    },
    goRoute(path) {
      this.$router.push({
        path: path,
      });
    },
    changeWebLanguage(language) {
      this.$store.commit("changeWebLanguage", { language: language });
    },
    autoChangeLanguge() {
      /**
       * 语言自动转换优先级：路径参数 > 本地存储 > 浏览器自动识别
       */
      let lang = this.$route.query.l;
      if (lang) {
        lang = lang.toLowerCase();
        if (lang == "zh-cn") {
          this.$store.commit("changeWebLanguage", { language: "zh-CN" });
        } else {
          this.$store.commit("changeWebLanguage", { language: "en-US" });
        }
        return;
      }

      lang = storage.get("Web_Language");
      if (lang) {
        return;
      }

      lang = navigator.userLanguage || window.navigator.language;
      lang = lang.toLowerCase();
      if (lang == "zh-cn") {
        this.$store.commit("changeWebLanguage", { language: "zh-CN" });
      } else {
        this.$store.commit("changeWebLanguage", { language: "en-US" });
      }
    },
    autoRefreshUserInfo() {
      this.$store.dispatch("setUserInfo", storage.get("userInfo"));
      let strogeToken = localStorage.getItem("token");
      if (document.hidden == false && this.token != strogeToken) {
        if (strogeToken) {
          this.$store.commit("changeUserToken", strogeToken);
          // if(this.$route.path.startsWith('/admin')){
          //   this.$router.replace({
          //     path: "/home",
          //   });
          // }
        } else {
          if (this.token) {
            this.$store.dispatch("clearUserInfoAndToken");
            let path = this.$route.path;
            if (path.startsWith("/admin")) {
              if (path != "/admin/login") {
                this.$router.replace({
                  path: "/admin/login",
                });
              }
            } else {
              if (path != "/home") {
                this.$router.replace({
                  path: "/home",
                });
              }
            }
          }
        }
      }
    },
  },
  watch: {
    $route(newVal, oldVal) {
      this.changeDomTitle();
      if (newVal !== oldVal && newVal.path.split("/")[1] == "admin") {
        this.isAdminView = true;
      } else {
        this.isAdminView = false;
      }
      if (
        newVal.name == "ProblemDetails" ||
        utils.isFocusModePage(newVal.name)
      ) {
        this.showFooter = false;
      } else {
        this.showFooter = true;
      }
    },
    websiteConfig() {
      this.changeDomTitle();
    },
  },
  computed: {
    ...mapState(["websiteConfig"]),
    ...mapGetters([
      "webLanguage",
      "isAdminRole",
      "token",
      "isAuthenticated",
      "userInfo",
      "unreadMessage",
      "modalStatus",
    ]),
    avatar() {
      return this.$store.getters.userInfo.avatar;
    },
    modalVisible: {
      get() {
        return this.modalStatus.visible;
      },
      set(value) {
        this.changeModalStatus({ visible: value });
      },
    },
    title: {
      get() {
        let ojName = this.websiteConfig.shortName
          ? this.websiteConfig.shortName
          : "OJ";
        if (this.modalStatus.mode == "ResetPwd") {
          return this.$i18n.t("m.Dialog_Reset_Password") + " - " + ojName;
        } else {
          return (
            this.$i18n.t("m.Dialog_" + this.modalStatus.mode) + " - " + ojName
          );
        }
      },
    },
  },
  created: function() {
    this.currentRoute = this.$route.path
    this.$nextTick(function() {
      try {
        document.body.removeChild(document.getElementById("app-loader"));
      } catch (e) {}
    });
    if (this.$route.path.split("/")[1] != "admin") {
      this.isAdminView = false;
    } else {
      this.isAdminView = true;
    }

    if (this.isAuthenticated) {
      this.$store.dispatch("refreshUserAuthInfo");
    }

    this.showFooter = !(
      this.$route.name == "ProblemDetails" ||
      utils.isFocusModePage(this.$route.name)
    );
    window.addEventListener("visibilitychange", this.autoRefreshUserInfo);
  },
  mounted() {
    this.autoChangeLanguge();
    this.getWebsiteConfig();
    // getLiveStream().then((res)=>{
    //   this.liveurl = res.data.msg;
    // })
  },
};
</script>

<style>
* {
  -webkit-box-sizing: border-box;
  -moz-box-sizing: border-box;
  box-sizing: border-box;
}
body {
  background-color: #eff3f5 !important;
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB",
    "Microsoft YaHei", "微软雅黑", Arial, sans-serif !important;
  color: #495060 !important;
  font-size: 12px !important;
}
code,
kbd,
pre,
samp {
  font-family: Consolas, Menlo, Courier, monospace;
}
::-webkit-scrollbar {
  width: 10px;
  height: 12px;
  -webkit-box-shadow: inset 0 0 6px rgb(0 0 0 / 20%);
}

::-webkit-scrollbar-thumb {
  display: block;
  min-height: 12px;
  min-width: 10px;
  border-radius: 8px;
  background-color: #bbb;
}

::-webkit-scrollbar-thumb:hover {
  display: block;
  min-height: 12px;
  min-width: 10px;
  border-radius: 6px;
  background-color: rgb(159, 159, 159);
}

#admin-content {
  background-color: #1e9fff;
  position: absolute;
  top: 0;
  bottom: 0;
  width: 100%;
}

.mobile-menu-active {
  background-color: rgba(0, 0, 0, 0.1);
}
.mobile-menu-active .mu-item-title {
  color: #2d8cf0 !important;
}
.mobile-menu-active .mu-icon {
  color: #2d8cf0 !important;
}
#particles-js {
  position: fixed;
  z-index: 0;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
}
a {
  text-decoration: none;
  background-color: transparent;
  color: #495060;
  outline: 0;
  cursor: pointer;
  transition: color 0.2s ease;
}
a:hover {
  color: #2196f3 !important;
}
.markdown-body a {
  color: #2196f3;
  cursor: pointer;
  text-decoration: none;
  transition: all 0.28s ease;
  -moz-transition: all 0.28s ease;
  -webkit-transition: all 0.28s ease;
  -o-transition: all 0.28s ease;
}
.markdown-body a:hover {
  color: #ff5722 !important;
  text-decoration: underline;
}
.panel-title {
  font-size: 21px;
  font-weight: 500;
  padding-top: 10px;
  padding-bottom: 20px;
  line-height: 30px;
}

.home-title {
  color: #409eff;
  font-family: "Raleway";
}
.contest-config {
  text-align: right;
}
.contest-config-switches p span {
  margin-left: 8px;
  margin-right: 4px;
}

.contest-rank-filter {
  margin: 10px 0;
}
.contest-rank-config {
  text-align: right;
  margin-top: 15px;
}
.contest-scoreBoard-config {
  margin-top: 30px !important;
}
.contest-rank-config span {
  margin-left: 5px;
}
.contest-config span {
  margin-left: 5px;
}
@media screen and (max-width: 992px) {
  .contest-rank-config {
    text-align: center;
    margin-bottom: 10px;
    margin-top: -1px;
  }
  .contest-config {
    margin-top: 5px;
    text-align: center;
  }
  .contest-scoreBoard-config {
    margin-top: 10px !important;
  }
}
.contest-rank-concerned {
  font-size: 1rem;
  margin-left: 0.5rem !important;
  margin-right: 0.5rem !important;
  vertical-align: top;
}
.contest-rank-concerned i {
  margin-top: 11px;
  cursor: pointer;
}
.contest-rank-user-box {
  display: flex;
}
.contest-rank-user-info {
  flex: 1;
  text-align: right;
  min-width: 0;
}

.contest-username {
  display: block;
  overflow: hidden;
  color: black;
  font-size: 13.5px;
  font-weight: 550;
  white-space: nowrap;
  text-overflow: ellipsis;
}
.contest-school {
  font-size: 12px;
  font-weight: normal;
  color: dimgrey;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  display: block;
}
.contest-rank-flag {
  margin-right: 20px !important;
  background-color: rgb(255, 193, 10);
  border-radius: 4px;
  color: rgb(73, 36, 0);
  padding: 1px 3px !important;
}

.bg-female {
  background-color: rgb(255, 153, 203);
}
.bg-star {
  background-color: #ffffcc;
}
.bg-concerned {
  background-color: lightyellow;
}

.contest-rank-balloon {
  vertical-align: top;
  margin-left: -10px !important;
  margin-right: -7px !important;
}

.oi-100 {
  background-color: #19be6b;
  color: #fff;
  font-weight: 700;
}

.oi-0 {
  color: #a94442;
  background-color: #f2dede;
}

.oi-between {
  background-color: #2d8cf0;
  color: #fff;
}
.after-ac {
  background-color: rgba(92, 184, 92, 0.4);
}
.first-ac {
  background-color: #1daa1d;
}
.ac {
  background-color: #60e760;
}
.wa {
  background-color: #e87272;
}
.try {
  background-color: #ff9800;
}

.status-green {
  background-color: #19be6b !important;
  color: #fff !important;
}
.status-red {
  background-color: #ed3f14 !important;
  color: #fff !important;
}
.status-yellow {
  background-color: #f90 !important;
  color: #fff !important;
}
.status-blue {
  background-color: #2d8cf0 !important;
  color: #fff !important;
}
.status-gray {
  background-color: #909399 !important;
  color: #fff !important;
}
.status-purple {
  background-color: #676fc1 !important;
  color: #fff !important;
}
.own-submit-row {
  background: rgb(230, 255, 223) !important;
}
.submission-hover:hover {
  cursor: pointer;
}
.vxe-table {
  color: #000 !important;
  font-size: 14px !important;
  font-weight: 500 !important;
}
.row--hover {
  cursor: pointer;
  background-color: #ebf7ff !important;
}
.vxe-table .vxe-body--column:not(.col--ellipsis),
.vxe-table .vxe-footer--column:not(.col--ellipsis),
.vxe-table .vxe-header--column:not(.col--ellipsis) {
  padding: 9px 0 !important;
}
#nprogress .bar {
  background: #66b1ff !important;
}
@media screen and (min-width: 1050px) {
  #oj-content {
    margin-top: 20px;
    padding: 0 3%;
    padding-bottom: 1.5rem;
    min-height: calc(100vh - 128px);
  }
}
.markdown-body img {
  max-width: 100%;
}
.contest-description img {
  max-width: 100%;
}
@media screen and (max-width: 1050px) {
  #oj-content {
    margin-top: 20px;
    padding: 0 5px;
    padding-bottom: 1.5rem;
  }
  .el-row {
    margin-left: 0px !important;
    margin-right: 0px !important;
  }
  .el-col {
    padding-left: 0px !important;
    padding-right: 0px !important;
  }
  .el-message-box {
    width: 70% !important;
  }
}
#problem-content .sample pre {
  -ms-flex: 1 1 auto;
  flex: 1 1 auto;
  -ms-flex-item-align: stretch;
  align-self: stretch;
  border-style: solid;
  background: #fafafa;
  border-left: 2px solid #3498db;
}

.markdown-body pre {
  padding: 5px 10px;
  white-space: pre-wrap;
  margin-top: 15px;
  margin-bottom: 15px;
  background: #f8f8f9;
  border: 1px dashed #e9eaec;
}

.el-menu--popup {
  min-width: 120px !important;
  text-align: center;
}
.panel-options {
  margin-top: 10px;
  text-align: center;
}
.el-tag--dark {
  border-color: #fff !important;
}
.v-note-wrapper .v-note-panel {
  height: 460px !important;
}

.tex-formula {
  font-family: times new roman, sans-serif;
  vertical-align: middle;
  margin: 0;
  border: medium none;
  position: relative;
  bottom: 2px;
}

.tex-span {
  font-size: 125%;
  font-family: times new roman, sans-serif;
  white-space: nowrap;
}

.tex-font-size-tiny {
  font-size: 70%;
}

.tex-font-size-script {
  font-size: 75%;
}

.tex-font-size-footnotes {
  font-size: 85%;
}

.tex-font-size-small {
  font-size: 85%;
}

.tex-font-size-normal {
  font-size: 100%;
}

.tex-font-size-large-1 {
  font-size: 115%;
}

.tex-font-size-large-2 {
  font-size: 130%;
}

.tex-font-size-large-3 {
  font-size: 145%;
}

.tex-font-size-huge-1 {
  font-size: 175%;
}

.tex-font-size-huge-2 {
  font-size: 200%;
}

.tex-font-style-sf {
  font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
}

.tex-font-style-tt {
  font-size: 110%;
  font-family: courier new, monospace;
}

.tex-font-style-bf {
  font-weight: bold;
}

.tex-font-style-it {
  font-style: italic;
}

.tex-font-style-sl {
  font-style: italic;
}

.tex-font-style-sc {
  text-transform: uppercase;
}

.tex-font-style-striked {
  text-decoration: line-through;
}

.tex-font-style-underline {
  text-decoration: underline;
}

.tex-graphics {
  display: block;
}

.full-height {
  height: 100vh;
}
.flex-column {
  display: flex;
  flex-direction: column;
}
.fix-to-bottom {
  margin-top: auto;
}

footer {
  color: #555 !important;
  background-color: #fff;
  text-align: center;
}
footer a {
  color: #555;
}
footer a:hover {
  color: #409eff;
  text-decoration: none;
}
footer h1 {
  font-family: -apple-system, BlinkMacSystemFont, Segoe UI, PingFang SC,
    Hiragino Sans GB, Microsoft YaHei, Helvetica Neue, Helvetica, Arial,
    sans-serif, Apple Color Emoji, Segoe UI Emoji, Segoe UI Symbol;
  font-weight: 300;
  color: #3d3d3d;
  line-height: 1.1;
  font-size: 1.5rem;
}

.mundb-footer {
  padding: 1rem 2.5rem;
  width: 100%;
  font-weight: 400;
  font-size: 1rem;
  line-height: 1;
}
@media (min-width: 768px) {
  .hr-none {
    display: none !important;
  }
}
.el-empty {
  max-width: 256px;
  margin: 0 auto;
}
.el-empty__description {
  text-align: center;
  color: #3498db;
  font-size: 13px;
}
</style>
<style>
.markdown-body pre {
  display: block;
  border-radius: 3px !important;
  border: 1px solid #c3ccd0;
  padding: 0 16px 0 50px !important;
  position: relative !important;
  overflow-y: hidden !important;
  font-size: 1rem !important;
  background: #f8f8f9 !important;
  white-space: pre !important;
}
.markdown-body pre code {
  line-height: 26px !important;
}
.markdown-body pre ol.pre-numbering {
  position: absolute;
  top: 0;
  left: 0;
  line-height: 26px;
  margin: 0;
  padding: 0;
  list-style-type: none;
  counter-reset: sectioncounter;
  background: #f1f1f1;
  color: #777;
  font-size: 12px;
}
.markdown-body pre ol.pre-numbering li {
  margin-top: 0 !important;
}
.markdown-body pre ol.pre-numbering li:before {
  content: counter(sectioncounter) "";
  counter-increment: sectioncounter;
  display: inline-block;
  width: 40px;
  text-align: center;
}
.markdown-body pre i.code-copy {
  position: absolute;
  top: 0;
  right: 0;
  background-color: #2196f3;
  display: none;
  padding: 5px;
  margin: 5px 5px 0 0;
  font-size: 11px;
  border-radius: inherit;
  color: #fff;
  cursor: pointer;
  transition: all 0.3s ease-in-out;
}

.markdown-body pre:hover i.code-copy {
  display: block;
}
.markdown-body pre i.code-copy:hover i.code-copy {
  display: block;
}

.markdown-body blockquote {
  color: #666;
  border-left: 4px solid #8bc34a;
  padding: 10px;
  margin-left: 0;
  font-size: 14px;
  background: #f8f8f8;
}
.markdown-body h1,
.markdown-body h2,
.markdown-body h3,
.markdown-body h4,
.markdown-body h5,
.markdown-body h6 {
  position: relative;
  margin-top: 1em;
  margin-bottom: 16px;
  font-weight: bold;
  line-height: 1.4;
}
.markdown-body h1 {
  padding-bottom: 0.3em;
  font-size: 1.86em;
  line-height: 1.2;
  border-bottom: 1px solid #eee;
}
.markdown-body h2 {
  font-size: 1.45em;
  line-height: 1.425;
  border-bottom: 1px solid #eee;
  background: #cce5ff;
  padding: 8px 10px;
  color: #545857;
  border-radius: 3px;
}
.markdown-body h3 {
  font-size: 1.3em;
  line-height: 1.43;
}
.markdown-body h3:before {
  content: "";
  border-left: 4px solid #03a9f4;
  padding-left: 6px;
}
.markdown-body h4 {
  font-size: 1.12em;
}
.markdown-body h4:before {
  content: "";
  border-left: 4px solid #bbb;
  padding-left: 6px;
}
.markdown-body img {
  border: 0;
  background: #ffffff;
  padding: 15px;
  margin: 5px 0;
  box-shadow: inset 0 0 12px rgb(219 219 219);
}
.markdown-body p {
  font-size: 15px;
  word-wrap: break-word;
  word-break: break-word;
  line-height: 1.8;
}
.hljs {
  padding: 0 !important;
}
.btn-menu {
  margin-left: auto;height: 100%;display: flex;align-items: center;
}
.drop-menu {
  margin-right: 5px;
  font-weight: 500;
  font-size: 18px;
  height: 100%;
}
.el-dropdown-link {
  cursor: pointer;
  color: #409eff !important;
  line-height: 60px;
}
.drop-avatar {
  margin-right: 15px;
  height: 100%;
}
.drop-msg {
  font-size: 25px;
  margin-right: 10px;
  height: 100%;
}
.drop-msg-count {
  margin-left: 2px;
}
#nav {
  width: 150px;
  height: 100vh;
  text-align: center;
  z-index: 5;
  background-color: #fff;
  position: fixed;
}

.wbv-button {
  height: 32px;
  text-align: center;
  padding-left: 10px;
  padding-right: 10px;
  display: inline-block;
  border-radius: 20px;
  border: 0;
  outline: none;
  cursor: pointer;
  background-image: linear-gradient(90deg, #ffd9a3, #f3be77);
  color: #764e19;
  margin-right: 30px;
}
.vip .el-dialog__header {
  padding: 0;
}
.vip-header {
  height: 92px;
  color: #fff;
  display: flex;
}
.vip .el-dialog__body {
  padding: 0;
}
.vip .no-active{
  background-color: #fff;
  border: 1px solid #ded9d8;
  color: #333;
}
.vip .is-active{
  background-color: #fffaf2;
  border: 1px solid #d9bf96;
  color: #764e19;
}
.ipayment{
  width: 118px;
    height: 34px;
    line-height: 35px;
    overflow: hidden;
    text-align: center;
    background: #fff;
    border: 1px solid #ded9d8;
    border-radius: 4px;
    font-size: 14px;
    color: #333;
    margin-right: 16px;
    cursor: pointer;
}
.ipayment img{
    width: 16px;
    height: 16px;
    margin-right: 6px;
    vertical-align: middle;
    margin-top: -3px;
}
 .byweibopay{
  background-size:100% 100%
}
.scratch-icon{
  height: 75%;margin-right: 10px;cursor: pointer;
}
.scratch{
  margin-right: 10px;cursor: pointer;
  font-size: 16px;
}
.overlay {
  z-index: 3000;
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
