<template>
  <el-card>
    <div shadow slot="header" :padding="10">
      <span class="home-title panel-title"
        ><i class="el-icon-trophy"></i>近期比赛</span
      >
    </div>
    <div class="echarts" v-loading="loading">
      <div
        v-for="(item, index) in contests"
        :key="item.id"
        class="card"
        :style="'border: 1px solid ' + details[index].bgColor"
      >
        <div
          class="card-header"
          :style="'background-color: ' + details[index].bgColor"
        >
          <h3 style="margin: 0;">
            <a
              :style="'color: ' + details[index].color + '!important'"
              @click.stop="toContest(item)"
            >
              {{ item.title }}
            </a>
          </h3>
        </div>
        <div style="height: 75px;padding: 12px;">
          <div style="float: left;width: 80px;height: 100%;">
            <div>
              <el-tag
                :type="CONTEST_TYPE_REVERSE[item.auth]['color']"
                size="small"
                effect="dark"
              >
                {{ $t("m." + CONTEST_TYPE_REVERSE[item.auth]["name"]) }}
              </el-tag>
            </div>
            <el-tag v-if="item.type == 0" effect="dark" size="small"
              >ACM
            </el-tag>
            <el-tag v-else type="warning" effect="dark" size="small">
              OI
            </el-tag>
          </div>
          <div style="float: right;width: 70px;height: 100%;">
            <div>
              <span class="small-font">
                {{ item.startTime | localtimeSimple }}
              </span>
            </div>
            <span class="small-font">
              {{ item.endTime | localtimeSimple }}
            </span>
          </div>
          <div style="overflow: hidden;height: 100%;">
            <span
              class="contest-status"
              :style="'color:' + CONTEST_STATUS_REVERSE[item.status]['color']"
            >
              {{ $t("m." + CONTEST_STATUS_REVERSE[item.status]["name"]) }}
            </span>
            <span class="small-font" v-if="item.status == 1">
              比赛已结束
            </span>
            <span class="small-font" v-else-if="item.status == 0">
              比赛进行中
            </span>
            <span class="small-font" v-else>
              {{ item.dur }}
            </span>
            <div>
              <el-link
                type="primary"
                :underline="false"
                @click.stop="toUser(item.author)"
              >
                {{ item.author }}</el-link
              >
            </div>
          </div>
        </div>
      </div>
    </div>
  </el-card>
</template>
<script>
import api from "@/common/api";
import { mapGetters } from "vuex";
import {
  CONTEST_STATUS_REVERSE,
  CONTEST_TYPE_REVERSE,
  CONTEST_STATUS,
} from "@/common/constants";
import myMessage from "@/common/message";
import time from "@/common/time";

export default {
  name: "CurrentContest",
  props: {
    title: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      loading: false,
      CONTEST_STATUS_REVERSE: {},
      contests: [],
      details: [],
      CONTEST_TYPE_REVERSE: {},
    };
  },
  mounted() {
    this.getContestList();
    this.CONTEST_STATUS_REVERSE = Object.assign({}, CONTEST_STATUS_REVERSE);
    this.CONTEST_TYPE_REVERSE = Object.assign({}, CONTEST_TYPE_REVERSE);
  },
  methods: {
    getContestList() {
      this.loading = true;
      api.getContestList(1, 6, "").then(
        (res) => {
          this.contests = res.data.data.records;
          this.loading = false;
          this.details = this.contests.map((contest) => {
            if (contest.status == -1) {
              let now = new Date();
              let duration = time.durationMs(now, contest.startTime);
              duration = Math.floor(duration / 60);
              let dur = "";
              if (duration > 0) {
                let min = duration % 60;
                dur = min + "分后";
                duration = Math.floor(duration / 60);
                if (duration > 0) {
                  let hour = duration % 60;
                  dur = hour + "时" + dur;
                  duration = Math.floor(duration / 24);
                  if (duration > 0) {
                    dur = duration + "天" + dur;
                  }
                }
              }
              contest.dur = dur;
            }
            return {
              bgColor: CONTEST_STATUS_REVERSE[contest.status].bgColor,
              contestType: CONTEST_TYPE_REVERSE[contest.auth],
              color: CONTEST_STATUS_REVERSE[contest.status].color,
            };
          });
        },
        () => {
          this.loading = false;
        }
      );
    },
    toContest(contest) {
      if (!this.isAuthenticated) {
        myMessage.warning(this.$i18n.t("m.Please_login_first"));
        this.$store.dispatch("changeModalStatus", { visible: true });
      } else {
        this.$router.push({
          name: "ContestDetails",
          params: { contestID: contest.id },
        });
      }
    },
    toUser(username) {
      this.$router.push({
        path: "/user-home",
        query: { username },
      });
    },
  },
  computed: {
    ...mapGetters(["isAuthenticated", "userInfo"]),
  },
};
</script>
<style scoped>
.echarts {
  width: 100%;
  display: grid;
  grid-template-rows: auto auto auto; /* 三行 */
  grid-template-columns: auto auto; /* 两列 */
  gap: 20px; /* 间隔 */
}
/deep/.el-card__body {
  padding: 20px 10px !important;
}
.am-badge {
  display: inline-block;
  min-width: 10px;
  padding: 0.25em 0.625em;
  font-size: 1.2rem;
  font-weight: 700;
  color: #fff;
  line-height: 1;
  vertical-align: baseline;
  white-space: nowrap;
}
.card {
  border-radius: 5px;
  margin: 3px;
  width: 100%;
  min-width: 300px;
  -webkit-box-shadow: 0 1px 1px rgba(0, 0, 0, 0.05);
  box-shadow: 0 1px 1px rgba(0, 0, 0, 0.05);
}
.card-header {
  padding: 6px 12.5px;
}
.contest-status {
  font-weight: bolder;
  margin-right: 10px;
  font-size: 12px;
  line-height: 24px;
}
.small-font {
  font-size: 12px;
  color: #7f7f7f;
  line-height: 24px;
}
</style>
