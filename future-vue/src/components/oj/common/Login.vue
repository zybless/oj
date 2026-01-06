<template>
  <div>
    <el-form
      :model="formLogin"
      :rules="rules"
      ref="formLogin"
      label-width="100px"
    >
      <el-form-item prop="username">
        <el-input
          v-model="formLogin.username"
          prefix-icon="el-icon-user-solid"
          :placeholder="$t('m.Login_Username')"
          width="100%"
          @keyup.enter.native="enterHandleLogin"
        ></el-input>
      </el-form-item>
      <el-form-item prop="password">
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
        <el-button type="primary" :loading="btnLoginLoading" slot="reference">{{
          $t("m.Login_Btn")
        }}</el-button>
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
      <el-link
        v-if="websiteConfig.register"
        type="primary"
        @click="switchMode('Register')"
        >{{ $t("m.Login_No_Account") }}</el-link
      >
      <el-link
        type="primary"
        @click="switchMode('ResetPwd')"
        style="float: right"
        >{{ $t("m.Login_Forget_Password") }}</el-link
      >
    </div>
  </div>
</template>
<script>
import { mapGetters, mapActions } from "vuex";
import api from "@/common/api";
import mMessage from "@/common/message";
export default {
  data() {
    return {
      puzzleImgList: [
        require("@/assets/imgs/img1.jpg"),
        require("@/assets/imgs/img2.jpg"),
        require("@/assets/imgs/img3.jpg"),
        require("@/assets/imgs/img4.jpg"),
        require("@/assets/imgs/img5.jpg"),
        require("@/assets/imgs/img6.jpg"),
      ],
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
    };
  },
  methods: {
    setCookie(name, value, days) {
      let expires = "";
      if (days) {
        const date = new Date();
        date.setTime(date.getTime() + days * 24 * 60 * 60 * 1000);
        expires = "; expires=" + date.toUTCString();
      }
      document.cookie = name + "=" + (value || "") + expires + "; path=/";
    },
    getCookie(name) {
      const nameEQ = name + "=";
      const ca = document.cookie.split(";");
      for (let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) === " ") c = c.substring(1, c.length);
        if (c.indexOf(nameEQ) === 0)
          return c.substring(nameEQ.length, c.length);
      }
      return null;
    },
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
              const currentDate = new Date();
              const targetDate = new Date("2025-02-15");
              if (currentDate < targetDate) {
                const flagStr = this.getCookie("yhFlag"); // 读取 cookie 值（字符串）
                if (flagStr == null) {
                  this.setCookie("yhFlag", "true", 100); // 将布尔值转换为字符串并存储
                  setTimeout(() => {
                    this.setCookie("yhFlag", "false", 100); // 将布尔值转换为字符串并存储
                  }, 3000);
                }
              }
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
  computed: {
    ...mapGetters(["modalStatus", "loginFailNum", "websiteConfig"]),
    visible: {
      get() {
        return this.modalStatus.visible;
      },
      set(value) {
        this.changeModalStatus({ visible: value });
      },
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
.footer {
  overflow: auto;
  margin-top: 20px;
  margin-bottom: -15px;
  text-align: left;
}
/deep/.el-button {
  margin: 0 0 15px 0;
  width: 100%;
}

/deep/ .el-form-item__content {
  margin-left: 0px !important;
}
</style>
