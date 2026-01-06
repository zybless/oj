<template>
  <el-card shadow :padding="10">
    <div slot="header">
      <span class="home-title panel-title"
        ><i class="el-icon-medal"></i>&nbsp;排行榜</span
      >
    </div>

    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane
        v-for="(item, index) in titles"
        :key="item"
        :label="item"
        :name="index.toString()"
      ></el-tab-pane>
    </el-tabs>
    <HonorRoll v-show="activeName == '0'"></HonorRoll>
    <div v-show="parseInt(activeName) > 0">
      <el-row :gutter="10">
        <el-col :span="12">
          <h3>前三名</h3>
          <div v-for="index in 3" :key="index">
            <el-divider></el-divider>
            <div style="display: flex;">
              <!-- <div
                class="ac-rankings-color-module"
                :style="'background-color:' + colors[index - 1]"
              >
                {{ index }}
              </div> -->
              <icon :name="icons[index - 1]" :w="35" :h="35"></icon>
              <div v-if="list.length > 3" style="margin-left: 30px;">
                <div style="color: #409eff;font-weight: bold;">
                  {{ list[index - 1].username }}
                </div>
                <div style="color:grey;">{{ list[index - 1].acNum }}</div>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="12"
          ><h3>我的排名</h3>
          <el-divider></el-divider>
          <div style="display: flex;">
            <div
              class="ac-rankings-color-module"
              style="background-color:gainsboro"
            >
              <span v-if="myRank == -1">99+</span>
              <span v-else>{{ myRank }}</span>
            </div>
            <div style="margin-left: 30px;">
              <div style="color: #409eff;font-weight: bold;">
                {{ myname }}
              </div>
              <div style="color:grey;">{{ myAc }}</div>
            </div>
          </div>
        </el-col>
      </el-row>
      <vxe-table :data="list" height="400px" style="margin-top: 10px;">
        <vxe-table-column type="index" label="排名" width="60">
        </vxe-table-column>
        <vxe-table-column field="username" title="用户"></vxe-table-column>
        <vxe-table-column
          field="acNum"
          title="解答数"
          width="80"
        ></vxe-table-column>
      </vxe-table>
    </div>
  </el-card>
</template>

<script>
import Pagination from "@/components/oj/common/Pagination";
import { getRankingList } from "@/common/newApi/honorroll/index";
import HonorRoll from "@/components/oj/common/HonorRoll.vue";
export default {
  name: "ACRankings",
  components: {
    Pagination,
    HonorRoll,
  },
  data() {
    return {
      loading: false,
      list: [],
      activeName: "0",
      titles: ["月赛榜", "近24小时", "近七天", "全部时间"],
      myRanks: [],
      myRank: -1,
      myAcs: [],
      myAc: 0,
      rankingListDetail: [],
      colors: ["yellowgreen", "orange", "brown"],
      myname: "匿名",
      icons: ["gold_medal", "silver_medal", "bronze_medal"],
    };
  },
  created() {
    this.init();
  },
  methods: {
    init() {
      this.loading2 = true;
      getRankingList().then((res) => {
        let myRanks = res.data.data.myRank;
        this.myRanks = [myRanks[2], myRanks[0], myRanks[1], myRanks[4]];
        let rankingListDetail = res.data.data.rankingListDetailVOList;
        for (let x of rankingListDetail) {
          for (let y of x) {
            if (!y.username) {
              y.username = "匿名";
            }
          }
        }
        this.rankingListDetail = [
          rankingListDetail[2],
          rankingListDetail[0],
          rankingListDetail[1],
          rankingListDetail[4],
        ];
        let activeName = parseInt(this.activeName);
        this.list = this.rankingListDetail[activeName];
        this.myRank = this.myRanks[activeName];
        if (res.data.data.username) {
          this.myname = res.data.data.username;
        }
        if (res.data.data.acNum) {
          let myAcs = res.data.data.acNum;
          this.myAcs = [myAcs[2], myAcs[0], myAcs[1], myAcs[4]];
          this.myAc = this.myAcs[activeName];
        }
      });
    },
  },
  watch: {
    activeName: {
      handler(newVal) {
        let temp = parseInt(newVal);
        this.list = this.rankingListDetail[temp];
        this.myRank = this.myRanks[temp];
        if (this.myAcs.length > 0) {
          this.myAc = this.myAcs[temp];
        }
      },
    },
  },
};
</script>

<style scoped>
.no-announcement {
  text-align: center;
  font-size: 16px;
}
.el-table {
  margin-bottom: 10px;
}
.el-divider {
  margin: 12px 0;
}
.ac-rankings-color-module {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  color: white;
  font-size: 20px;
  line-height: 40px;
  text-align: center;
}
</style>
