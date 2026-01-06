<template>
    <div class="contest-body">
      <el-row>
        <el-col :xs="24" :md="24" :lg="24">
          <el-card shadow>
            <div class="contest-title">
              <div slot="header">
                <span class="panel-title">{{ chooseExamDetail.title }}</span>
              </div>
            </div>
            <el-row style="margin-top: 10px;">
              <el-col :span="14" class="text-align:left">
                <el-tooltip
                  content="私有赛 - 用户需要密码才可查看与提交"
                  placement="top"
                >
                  <el-tag
                    type="danger"
                    effect="plain"
                    style="font-size:13px"
                  >
                    <i class="el-icon-collection-tag"></i>
                    私有赛
                  </el-tag>
                </el-tooltip>
                <el-tooltip
                  v-if="contest.gid != null"
                  :content="$t('m.Go_To_Group_Contest_List')"
                  style="margin-left:10px;"
                  placement="top">
                  <el-button 
                    size="small" 
                    type="primary"
                    @click="toGroupContestList(contest.gid)">
                    <i class="fa fa-users"></i>
                      {{ $t('m.Group_Contest_Tag')}}
                  </el-button>
               </el-tooltip>
              </el-col>
            </el-row>
            <div class="contest-time">
              <el-row>
                <el-col :xs="24" :md="12" class="left">
                  <p>
                    <i class="fa fa-hourglass-start" aria-hidden="true"></i>
                    {{ $t('m.StartAt') }}：{{ chooseExamDetail.startTime | localtime }}
                  </p>
                </el-col>
                <el-col :xs="24" :md="12" class="right">
                  <p>
                    <i class="fa fa-hourglass-end" aria-hidden="true"></i>
                    {{ $t('m.EndAt') }}：{{ chooseExamDetail.endTime | localtime }}
                  </p>
                </el-col>
              </el-row>
            </div>
            <div class="contest-config" v-if="isShowContestSetting">
              <el-popover
                trigger="hover"
                placement="left-start"
              >
                <el-button
                  round
                  size="small"
                  slot="reference"
                >
                  {{$t('m.Contest_Setting')}}
                </el-button>
                <div class="contest-config-switches">
                  <p>
                    <span>{{ $t('m.Contains_Submission_After_Contest') }}</span>
                    <el-switch v-model="isContainsAfterContestJudge"></el-switch>
                  </p>
                  </div>
                </el-popover>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <div class="sub-menu">
        <!-- 判断是否需要密码验证 -->
  
        <el-tabs @tab-click="tabClick" v-model="route_name">
          <el-tab-pane name="ChooseExamDetails" lazy>
            <span slot="label"
              ><i class="el-icon-s-home"></i>&nbsp;{{ $t('m.Overview') }}</span
            >
            <el-card class="box-card">
              <Markdown 
                :content="chooseExamDetail.remark">
              </Markdown>
            </el-card>
          </el-tab-pane>
  
          <el-tab-pane
            name="ExamProblemList"
            lazy
          >
            <span slot="label"
              ><i class="fa fa-list" aria-hidden="true"></i>&nbsp;{{
                $t('m.Problem')
              }}</span
            >
            <transition name="el-zoom-in-bottom">
              <router-view
                v-if="route_name === 'ExamProblemList'"
              ></router-view>
            </transition>
          </el-tab-pane>
  
          <el-tab-pane v-if="isAdminRole" name="ExamRank" lazy >
            <span slot="label"
              ><i class="fa fa-bar-chart" aria-hidden="true"></i>&nbsp;{{
                $t('m.NavBar_Rank')
              }}</span
            >
            <transition name="el-zoom-in-bottom">
              <router-view v-if="route_name === 'ExamRank'"></router-view>
            </transition>
          </el-tab-pane>
  
        </el-tabs>
      </div>
    </div>
  </template>
  <script>
  import time from '@/common/time';
  import moment from 'moment';
  import api from '@/common/api';
  import { mapState, mapGetters, mapActions } from 'vuex';
  import { addCodeBtn } from '@/common/codeblock';
  import {
    CONTEST_STATUS_REVERSE,
    CONTEST_STATUS,
    CONTEST_TYPE_REVERSE,
    RULE_TYPE,
    buildContestAnnounceKey,
  } from '@/common/constants';
  import myMessage from '@/common/message';
  import storage from '@/common/storage';
  import Markdown from "@/components/oj/common/Markdown";
  import {
  createChooseExam,
  getChooseExamById,
  getExam,
} from "@/common/newApi/chooseExam/index";
  export default {
    name: 'ChooseExamDetails',
    components: {
      Markdown
    },
    data() {
      return {
        route_name: 'ChooseExamDetails',
        timer: null,
        CONTEST_STATUS: {},
        CONTEST_STATUS_REVERSE: {},
        CONTEST_TYPE_REVERSE: {},
        RULE_TYPE: {},
        btnLoading: false,
        contestPassword: '',
        chooseExamDetail:{},
      };
    },
    created() {
      this.route_name = this.$route.name;
      this.contestID = this.$route.params.chooseExamID;
      getExam(this.contestID).then((res)=>{
        this.chooseExamDetail = Object.assign({}, res.data.data);
      })
    },
    methods: {
      ...mapActions(['changeDomTitle']),
      checkPassword() {
        if (this.contestPassword === '') {
          myMessage.warning(this.$i18n.t('m.Enter_the_contest_password'));
          return;
        }
        this.btnLoading = true;
        // if (this.contestPassword === this.chooseExamDetail.password ) {
        //   this.$store.commit('contestIntoAccess', { intoAccess: true });

        // }
        api.registerContest(this.contestID + '', this.contestPassword).then(
          (res) => {
            myMessage.success(this.$i18n.t('m.Register_contest_successfully'));
            this.$store.commit('contestIntoAccess', { intoAccess: true });
            this.btnLoading = false;
          },
          (res) => {
            this.btnLoading = false;
          }
        );
      },
      tabClick(tab) {
        let name = tab.name;
        if (name !== this.$route.name) {
          this.$router.push({ name: name });
        }
      },
      toGroupContestList(gid){
        this.$router.push({
          name: 'GroupContestList',
          params: {
            groupID: gid,
          },
        })
      },
    },
    computed: {
      ...mapState({
        contest: (state) => state.contest.contest,
        now: (state) => state.contest.now,
      }),
      ...mapGetters([
        'contestRuleType',
        'contestStatus',
        'countdown',
        'isShowContestSetting',
        'BeginToNowDuration',
        'isContestAdmin',
        'ContestRealTimePermission',
        'userInfo',
        'websiteConfig',
        'isAdminRole'
      ]),

      showAdminHelper() {
        return this.isContestAdmin && this.contestRuleType === RULE_TYPE.ACM;
      },
      showScrollBoard(){
        return this.isContestAdmin && this.contestRuleType === RULE_TYPE.ACM;
      },
      contestEnded() {
        return this.contestStatus === CONTEST_STATUS.ENDED;
      },
      isContainsAfterContestJudge:{
        get () {
          return this.$store.state.contest.isContainsAfterContestJudge;
        },
        set (value) {
          this.$store.commit('changeContainsAfterContestJudge', {value: value})
        }
      },
    },
    watch: {
      $route(newVal) {
        this.route_name = newVal.name;
        this.contestID = newVal.params.chooseExamID;
        this.changeDomTitle({ title: this.contest.title });
      },
    },
    beforeDestroy() {
      clearInterval(this.timer);
      clearInterval(this.announceTimer);
      this.$store.commit('clearContest');
    },
  };
  </script>
  <style scoped>
  .panel-title {
    font-size: 1.5rem !important;
    font-weight: 500;
  }
  @media screen and (min-width: 768px) {
    .contest-time .left {
      text-align: left;
    }
    .contest-time .right {
      text-align: right;
    }
    .password-form-card {
      width: 400px;
      margin: 0 auto;
    }
  }
  @media screen and (max-width: 768px) {
    .contest-time .left,
    .contest-time .right {
      text-align: center;
    }
  }
  /deep/.el-slider__button {
    width: 20px !important;
    height: 20px !important;
    background-color: #409eff !important;
  }
  /deep/.el-slider__button-wrapper {
    z-index: 500;
  }
  /deep/.el-slider__bar {
    height: 10px !important;
    background-color: #09be24 !important;
  }
  /deep/ .el-card__header {
    border-bottom: 0px;
    padding-bottom: 0px;
  }
  /deep/.el-tabs__nav-wrap {
    background: #fff;
    border-radius: 3px;
  }
  /deep/.el-tabs--top .el-tabs__item.is-top:nth-child(2) {
    padding-left: 20px;
  }
  .contest-title {
    text-align: center;
  }
  .contest-time {
    width: 100%;
    font-size: 16px;
  }
  .el-tag--dark {
    border-color: #fff;
  }
  .el-tag {
    color: rgb(25, 190, 107);
    background: #fff;
    border: 1px solid #e9eaec;
    font-size: 18px;
  }
  .sub-menu {
    margin-top: 15px;
  }
  .password-form-tips {
    text-align: center;
    font-size: 14px;
  }
  </style>
  