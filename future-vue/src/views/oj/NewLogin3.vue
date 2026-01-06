<template>
  <div id="new-login">
    <!-- <img src="@/assets/newlogintop.png" alt="Dynamic Image" /> -->
    <div class="content">
      <el-row>
        <el-col :span="14">
          <div class="title" style="margin-top: 50px;">
            2025年 第五届 “苏州市青少年数字公民培育计划”人工智能普及竞技比赛
          </div>
          <div class="title">
            
          </div>
          <div class="title" style="margin-bottom: 50px;">
            中学组（Python）
          </div>
          <div style="display: flex;justify-content: center;">
            <div
              class="background-div"
              style="color:white;
          border: 2px dashed white;
          background-color: rgba(0, 0, 0, 0.3);
          width: 600px;
          padding: 20px;"
            >
              <span style="font-size: 30px;">注意事项：</span>
              <p>
                1、请务必使用最新Chrome浏览器(80及以上版本)访问，下载网速高于200KB/秒。
              </p>
              <p>
                2、请勿在短时间内恶意多次提交错误代码，以免影响系统正常运行。
              </p>
              <p>
                3、发现网站问题或漏洞，请及时反馈管理员，勿恶意利用。
              </p>
              <p>
                4、遇到编程题目中的错误，先检查代码逻辑，再提交修正版本。
              </p>
              <p>
                5、发布帖子前，请确保内容与编程学习相关，避免无关讨论。
              </p>
            </div>
          </div>
        </el-col>
        <el-col :span="10">
          <div class="table">
            <h2 style="text-align: center;margin-bottom: 20px;">用户登录</h2>
            <el-form
              :model="formLogin"
              :rules="rules"
              ref="formLogin"
              label-width="80px"
            >
              <el-form-item prop="username" label="用户名：">
                <el-input
                  v-model="formLogin.username"
                  prefix-icon="el-icon-user-solid"
                  :placeholder="$t('m.Login_Username')"
                  width="100%"
                  @keyup.enter.native="enterHandleLogin"
                ></el-input>
              </el-form-item>
              <el-form-item prop="password" label="密  码：">
                <el-input
                  v-model="formLogin.password"
                  prefix-icon="el-icon-lock"
                  :placeholder="$t('m.Login_Password')"
                  type="password"
                  @keyup.enter.native="enterHandleLogin"
                ></el-input>
              </el-form-item>
            </el-form>
            <div class="footer">
              <el-button
                type="primary"
                v-if="!needVerify"
                @click="handleLogin"
                :loading="btnLoginLoading"
                >{{ $t("m.Login_Btn") }}</el-button
              >
              <el-popover
                placement="bottom"
                width="350"
                v-model="loginSlideBlockVisible"
                trigger="click"
                v-else
              >
                <el-button
                  type="primary"
                  :loading="btnLoginLoading"
                  slot="reference"
                  >{{ $t("m.Login_Btn") }}</el-button
                >
                <slide-verify
                  :l="42"
                  :r="10"
                  :w="325"
                  :h="100"
                  :accuracy="3"
                  @success="handleLogin"
                  :slider-text="$t('m.Slide_Verify')"
                  ref="slideBlock"
                  v-if="!verify.loginSuccess"
                  :imgs="puzzleImgList"
                >
                </slide-verify>
                <el-alert
                  :title="$t('m.Slide_Verify_Success')"
                  type="success"
                  :description="verify.loginMsg"
                  v-show="verify.loginSuccess"
                  :center="true"
                  :closable="false"
                  show-icon
                >
                </el-alert>
              </el-popover>
            </div></div
        ></el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapActions } from "vuex";
import api from "@/common/api";
import mMessage from "@/common/message";
export default {
  components: {},
  name: "new-login3",
  computed: {
    ...mapGetters([
      "isAuthenticated",
      "modalStatus",
      "loginFailNum",
      "websiteConfig",
    ]),
    visible: {
      get() {
        return this.modalStatus.visible;
      },
      set(value) {
        this.changeModalStatus({ visible: value });
      },
    },
  },
  created() {
    if (this.isAuthenticated) {
      this.$router.push({ path: "/contest/1981", query: { needRefresh: true } });
    }
  },
  data() {
    return {
      btnLoginLoading: false,
      verify: {
        loginSuccess: false,
        loginMsg: "",
      },
      needVerify: false,
      formLogin: {
        username: "",
        password: "",
      },
      loginSlideBlockVisible: false,
      rules: {
        username: [
          {
            required: true,
            message: this.$i18n.t("m.Username_Check_Required"),
            trigger: "blur",
          },
          {
            max: 20,
            message: this.$i18n.t("m.Username_Check_Max"),
            trigger: "blur",
          },
        ],
        password: [
          {
            required: true,
            message: this.$i18n.t("m.Password_Check_Required"),
            trigger: "blur",
          },
          {
            min: 6,
            max: 20,
            message: this.$i18n.t("m.Password_Check_Between"),
            trigger: "blur",
          },
        ],
      },
      puzzleImgList: [
        require('@/assets/imgs/img1.jpg'),
        require('@/assets/imgs/img2.jpg'),
        require('@/assets/imgs/img3.jpg'),
        require('@/assets/imgs/img4.jpg'),
        require('@/assets/imgs/img5.jpg'),
        require('@/assets/imgs/img6.jpg'),]
    };
  },
  methods: {
    ...mapActions(["changeModalStatus"]),
    switchMode(mode) {
      this.changeModalStatus({
        mode,
        visible: true,
      });
    },
    enterHandleLogin() {
      if (this.needVerify) {
        this.visible.loginSlideBlock = true;
      } else {
        this.handleLogin();
      }
    },
    handleLogin(times) {
      if (this.needVerify) {
        this.verify.loginSuccess = true;
        let time = (times / 1000).toFixed(1);
        this.verify.loginMsg = "Total time " + time + "s";
        setTimeout(() => {
          this.loginSlideBlockVisible = false;
          this.verify.loginSuccess = false;
        }, 1000);
      }
      this.$refs["formLogin"].validate((valid) => {
        if (valid) {
          this.btnLoginLoading = true;
          let formData = Object.assign({}, this.formLogin);
          api.login(formData).then(
            (res) => {
              this.btnLoginLoading = false;
              this.changeModalStatus({ visible: false });
              const jwt = res.headers["authorization"];
              this.$store.commit("changeUserToken", jwt);
              this.$store.dispatch("setUserInfo", res.data.data);
              this.$store.dispatch("incrLoginFailNum", true);
              mMessage.success(this.$i18n.t("m.Welcome_Back"));
              this.$router.push({
                path: "/contest/1981",
                query: { needRefresh: true },
              });
            },
            (_) => {
              this.$store.dispatch("incrLoginFailNum", false);
              this.btnLoginLoading = false;
            }
          );
        }
      });
    },
  },
  watch: {
    loginFailNum(newVal, oldVal) {
      if (newVal >= 5) {
        this.needVerify = true;
      } else {
        this.needVerify = false;
      }
    },
  },
};
</script>
<style scoped>
#new-login {
  width: 100%;
}
.content {
  width: 100%;
  /* height: calc(100vh - 110px); */
  height: calc(100vh - 0px);
  background-image: url("../../assets/dashboard-bg.jpg");
  background-size: cover; /* 按照原始比例拉伸背景图片 */
  background-repeat: no-repeat; /* 不重复背景图片 */
}
.table {
  background-color: white;
  border-radius: 20px;
  padding: 30px 60px 50px 30px;
  margin-top: 100px;
  width: 450px;
  position: relative;
  left: -60px;
}
.footer {
  overflow: auto;
  margin-top: 20px;
  margin-bottom: -15px;
  text-align: left;
}
.el-col-10 {
  display: flex;
  align-items: center;
  justify-content: center;
}
/deep/.el-button {
  margin: 0 0 15px 80px;
  width: calc(100% - 85px);
}
.title {
  text-align: center;
  font-size: 40px;
  color: white;
}
</style>
