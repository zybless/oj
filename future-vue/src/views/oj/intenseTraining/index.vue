<template>
  <div class="container" style="padding-bottom: 20px">
    <div class="header">
      <el-row style="display: flex;align-items: center;">
        <el-col :span="14">
          <h1>{{ trainingCampTeam.title }}</h1>
          <div style="margin: 10px 20px 10px 0;font-size: 14px;">
            <p>
              {{ trainingCampTeam.description }}
            </p>
          </div>
          <el-button @click="goApply(applyGG.id)">点击申请加入</el-button>
        </el-col>
        <el-col :span="10" style="display: flex;justify-content: center;">
          <img :src="imgUrl1" style="width: 70%;" />
        </el-col>
      </el-row>
    </div>
    <div class="section">
      <el-row style="display: flex;align-items: center;">
        <el-col :span="10" style="display: flex;justify-content: center;">
          <img :src="imgUrl2" style="width: 70%;" />
        </el-col>
        <el-col :span="14">
          <div style="width: 100%;display: flex;">
            <i
              class="el-icon-medal-1"
              style="line-height: 27px;font-size: 27px;color:#409eff"
            ></i>
            <h2 style="margin: 0 0 0 10px;">集训队信息</h2>
          </div>
          <el-carousel :autoplay="false" height="320px">
            <el-carousel-item v-for="item in lbGG" :key="item">
              <div>
                <p style="font-size: 20px;font-weight: bold;">
                  {{ item.title }}
                </p>
                <div
                  style="font-size: 12px;overflow-y: auto;max-height: 190px;margin-bottom: 10px;overflow: hidden;text-overflow: ellipsis"
                >
                  {{ item.description }}
                </div>
                <a
                  @click="goApply(item.id)"
                  style="color: #ff69b4;font-size: 12px;"
                  >查看详情</a
                >
              </div>
            </el-carousel-item>
          </el-carousel>
        </el-col>
      </el-row>
    </div>
    <div class="section">
      <div
        style="width: 100%;margin-bottom: 20px;text-align: center;height: 27px;"
      >
        <i class="el-icon-s-flag" style="font-size: 27px;color:#409eff"></i
        >&nbsp;&nbsp;<span
          style="line-height: 27px;font-size: 18px;font-weight: 700;"
          >加入集训</span
        >
      </div>
      <div class="training-grid">
        <div
          v-for="item in trainingCampVOList"
          :key="item"
          class="training-card"
        >
          <div style="text-align: center;">
            <h3>{{ item.title }}</h3>
          </div>
          <p>{{ item.description }}</p>
          <p>
            训练周期：{{ item.duration }}<br />训练强度：{{ item.intensity }}
          </p>
          <button @click="goApply2(item.id)">查看大纲</button>
        </div>
      </div>
    </div>
    <div class="section">
      <el-row style="display: flex;align-items: center;">
        <el-col :span="10" style="display: flex;justify-content: center;">
          <img :src="imgUrl3" style="width: 70%;" />
        </el-col>
        <el-col :span="14">
          <div style="width: 100%;display: flex;margin-bottom: 15px;">
            <i
              class="el-icon-s-opportunity"
              style="line-height: 27px;font-size: 27px;color:#409eff"
            ></i>
            <h2 style="margin: 0 0 0 10px;">经验分享</h2>
          </div>
          <div style="width: 100%;max-height: 200px;overflow-y: auto;">
            <p
              v-for="item in shareGG"
              :key="item.title"
              style="font-size: 20px;font-weight: bold;margin: 10px 0;"
            >
              <a @click="goApply(item.id)">{{ item.title }}</a>
            </p>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>
<script>
import {
  getTrainCampTeam,
  getTrainingCamp,
} from "@/common/newApi/trainningcamp/ojIndex";
export default {
  name: "intenseTraining",
  data() {
    return {
      trainingCampTeam: {},
      imgUrl1: require("@/assets/intenseTraining/intenseTraining1.png"),
      imgUrl2: require("@/assets/intenseTraining/intenseTraining2.png"),
      imgUrl3: require("@/assets/intenseTraining/intenseTraining3.png"),
      applyGG: {},
      lbGG: [],
      shareGG: [],
      trainingCampVOList: [],
    };
  },
  created() {
    getTrainCampTeam().then((res) => {
      this.trainingCampTeam = res.data.data;
      let announcementCategory = this.trainingCampTeam.announcementCategory;
      let relatedAnnouncement = this.trainingCampTeam.relatedAnnouncement;
      this.applyGG = {};
      this.lbGG = [];
      this.shareGG = [];
      for (let i = 0; i < announcementCategory.length; i++) {
        if (announcementCategory[i] == "1") {
          this.applyGG = relatedAnnouncement[i];
        } else if (announcementCategory[i] == "2") {
          this.lbGG.push(relatedAnnouncement[i]);
        } else if (announcementCategory[i] == "3") {
          this.shareGG.push(relatedAnnouncement[i]);
        }
      }
      this.trainingCampVOList = this.trainingCampTeam.trainingCampVOList;
      console.log("this.trainingCampTeam", this.trainingCampTeam);
    });
  },
  methods: {
    goApply(discussionID) {
      this.$router.push({
        name: "DiscussionDetails",
        params: { discussionID },
      });
    },
    goApply2(id) {
      this.$router.push({
        name: "TrainCampInfo",
        params: { id },
      });
    },
  },
};
</script>
<style scoped>
.header {
  width: 100%;
}
.header h1 {
  font-size: 2.5em;
  margin: 0;
}
.header button {
  background-color: #ff69b4;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 1em;
}
.section {
  margin: 40px 0;
  width: 100%;
}
.section h2 {
  font-size: 1.5em;
  margin-bottom: 20px;
}
.card {
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin: 20px 0;
}
.card img {
  width: 100%;
  border-radius: 10px;
}
.card-content {
  display: flex;
  flex-direction: row;
  align-items: center;
}
.card-content img {
  width: 50px;
  height: 50px;
  margin-right: 20px;
}
.card-content div {
  flex: 1;
}
.training-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 10px;
}
.training-card {
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  width: 100%;
  margin: 1%;
  padding: 20px;
  box-sizing: border-box;
}
.training-card h3 {
  font-size: 1.2em;
  margin-bottom: 10px;
}
.training-card p {
  margin: 10px 0;
}
.training-card button {
  background-color: #ff69b4;
  color: white;
  padding: 10px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 1em;
}
/deep/ .is-active button {
  background-color: #ff69b4;
}
</style>
