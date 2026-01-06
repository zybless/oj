<template>
    <div>
        <!-- <ContestListAttention></ContestListAttention> -->
        <el-row type="flex" justify="space-around">
          <el-col :span="24">
            <el-card shadow>
              <div slot="header">
                <span class="panel-title"
                  >苏州工业园区信奥拔尖人才培养计划—2024年CSP- J/S NOIP</span
                >
              </div>
              <div v-loading="loading">
                <p id="no-contest" v-show="contests.length == 0">
                  <el-empty :description="$t('m.No_contest')"></el-empty>
                </p>
                <ol id="contest-list">
                  <li
                    v-for="contest in contests"
                    :key="contest.title"
                  >
                    <el-row type="flex" justify="space-between" align="middle">
                      <el-col
                        :xs="10"
                        :sm="16"
                        :md="19"
                        :lg="20"
                        class="contest-main"
                      >
                        <p class="title">
                          <a class="entry" @click.stop="toContest(contest)">
                            {{ contest.title }}
                          </a>
                        </p>
                        <ul class="detail">
                          <li>
                            <i
                              class="fa fa-calendar"
                              aria-hidden="true"
                              style="color: #3091f2"
                            ></i>
                            {{ contest.startTime | localtime }}
                          </li>
                          <li>
                            <i
                              class="fa fa-clock-o"
                              aria-hidden="true"
                              style="color: #3091f2"
                            ></i>
                            {{ getDuration(contest.startTime, contest.endTime) }}
                          </li>
                          <li>
                            <el-tooltip
                              content="私有赛 - 用户需要密码才可查看与提交"
                              placement="top"
                              effect="light"
                            >
                              <el-tag
                                type="danger"
                                effect="plain"
                              >私有赛
                              </el-tag>
                            </el-tooltip>
                          </li>
                        </ul>
                      </el-col>
                     
                    </el-row>
                  </li>
                </ol>
              </div>
            </el-card>
            <Pagination
              :total="total"
              :pageSize="limit"
              @on-change="onCurrentPageChange"
              :current.sync="currentPage"
            ></Pagination>
          </el-col>
        </el-row>
    </div>
  </template>
  
  <script>
  import api from '@/common/api';
  import { mapGetters } from 'vuex';
  import utils from '@/common/utils';
  import time from '@/common/time';
  import {
    CONTEST_STATUS_REVERSE,
    CONTEST_TYPE_REVERSE,
    CONTEST_STATUS,
  } from '@/common/constants';
  import { getChooseExamList ,getExamList} from "@/common/newApi/chooseExam/index";

  import myMessage from '@/common/message';
  const Pagination = () => import('@/components/oj/common/Pagination');
  // const ContestListAttention = () => import('@/components/oj/contest/ContestListAttention');
  const limit = 10;
  
  export default {
    name: 'contest-list',
    components: {
      Pagination,
      // ContestListAttention
    },
    data() {
      return {
        currentPage: 1,
        query: {
          status: '',
          keyword: '',
          type: '',
        },
        limit: limit,
        total: 0,
        rows: '',
        contests: [],
        chooseExams: [],
        CONTEST_STATUS_REVERSE: {},
        CONTEST_STATUS: {},
        CONTEST_TYPE_REVERSE: {},
        acmSrc: require('@/assets/acm.jpg'),
        oiSrc: require('@/assets/oi.jpg'),
        loading: true,
      };
    },
    created() {
      let route = this.$route.query;
      this.currentPage = parseInt(route.currentPage) || 1;
      console.log('tyty',this.$route.query.needRefresh); // 输出传递过来的用户 ID
      if (this.$route.query.needRefresh) {
        this.$router.replace({ query:{} });
        location.reload()
      }
    },
    mounted() {
      this.CONTEST_STATUS_REVERSE = Object.assign({}, CONTEST_STATUS_REVERSE);
      this.CONTEST_TYPE_REVERSE = Object.assign({}, CONTEST_TYPE_REVERSE);
      this.CONTEST_STATUS = Object.assign({}, CONTEST_STATUS);
      this.init();
    },
    methods: {
      init() {
        let route = this.$route.query;
        this.query.status = route.status || '';
        if(route.type === 0 || route.type === '0'){
          this.query.type = 0;
        }else if(route.type === 1 || route.type === '1'){
          this.query.type = 1;
        }else{
          this.query.type = '';
        }
        this.query.keyword = route.keyword || '';
        this.currentPage = parseInt(route.currentPage) || 1;
        this.getList();
      },
      getList() {
        this.loading = true;
        let params = {
        limit: this.limit,
        currentPage: this.currentPage,
        title: this.query.keyword,
      };
      getExamList(params).then(
          (res) => {
            this.contests = res.data.data.records;
            this.total = res.data.data.total;
            this.loading = false;
          },
          (err) => {
            this.loading = false;
          }
        )
      },
  
      filterByChange() {
        let query = Object.assign({}, this.query);
        query.currentPage = this.currentPage;
        this.$router.push({
          name: 'ChooseExamList',
          query: utils.filterEmptyValue(query),
        });
      },
  
  
      onCurrentPageChange(page) {
        this.currentPage = page;
        this.filterByChange();
      },
      onStatusChange(status) {
        this.query.status = status;
        this.currentPage = 1;
        this.filterByChange();
      },
      onKeywordChange() {
        this.currentPage = 1;
        this.filterByChange();
      },
      toContest(contest) {
        if (!this.isAuthenticated) {
          myMessage.warning(this.$i18n.t('m.Please_login_first'));
          this.$store.dispatch('changeModalStatus', { visible: true });
        } else {
          this.$router.push({
            name: 'ChooseExamDetails',
            params: { chooseExamID: contest.id },
          });
        }
      },
      toContestOutsideScoreBoard(cid, type) {
        if (type == 0) {
          this.$router.push({
            name: 'ACMScoreBoard',
            params: { contestID: cid },
          });
        } else if (type == 1) {
          this.$router.push({
            name: 'OIScoreBoard',
            params: { contestID: cid },
          });
        }
      },
      getDuration(startTime, endTime) {
        return time.formatSpecificDuration(startTime, endTime);
      },
      getborderColor(contest) {
        return (
          'border-left: 4px solid ' +
          CONTEST_STATUS_REVERSE[contest.status]['color']
        );
      },
    },
    computed: {
      ...mapGetters(['isAuthenticated', 'userInfo']),
    },
    watch: {
      $route(newVal, oldVal) {
        if (newVal !== oldVal) {
          this.init();
        }
      },
    },
  };
  </script>
  <style scoped>
  #no-contest {
    text-align: center;
    font-size: 16px;
    padding: 20px;
  }
  .filter-row {
    float: right;
  }
  @media screen and (max-width: 768px) {
    .filter-row span {
      margin-right: 2px;
    }
    ol {
      padding-inline-start: 5px;
    }
    /deep/ .el-card__header {
      margin-bottom: 5px;
    }
  }
  @media screen and (min-width: 768px) {
    .filter-row span {
      margin-right: 20px;
    }
  }
  /deep/ .el-card__header {
    border-bottom: 0px;
  }
  
  #contest-list > li {
    padding: 5px;
    margin-left: -20px;
    margin-top: 10px;
    width: 100%;
    border-bottom: 1px solid rgba(187, 187, 187, 0.5);
    list-style: none;
  }
  #contest-list .trophy {
    height: 70px;
    margin-left: 10px;
    margin-right: -20px;
  }
  @media screen and (max-width: 1500px) and (min-width: 1200px){
    #contest-list .trophy {
      width: 100% !important;
    }
    #contest-list .contest-main{
      margin-left: 20px;
    }
  }
  #contest-list .contest-main {
    text-align: left;
  }
  #contest-list .contest-main .title {
    font-size: 1.25rem;
    padding-left: 8px;
    margin-bottom: 0;
  }
  #contest-list .contest-main .title a.entry {
    color: #495060;
  }
  #contest-list .contest-main .title a:hover {
    color: #2d8cf0;
    border-bottom: 1px solid #2d8cf0;
  }
  #contest-list .contest-main .detail {
    font-size: 0.875rem;
    padding-left: 0;
    padding-bottom: 10px;
  }
  #contest-list .contest-main li {
    display: inline-block;
    padding: 10px 0 0 10px;
  }
  </style>
  