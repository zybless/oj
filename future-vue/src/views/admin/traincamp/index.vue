<template>
  <div class="view">
    <el-card>
      <div slot="header">
        <span class="panel-title home-title">
          编辑集训队信息
        </span>
      </div>
      <el-form label-position="top">
        <el-form-item label="标题" required>
          <el-input
            v-model="trainingCampTeam.title"
            placeholder="请填写集训队名称"
          ></el-input>
        </el-form-item>
        <el-form-item label="描述" required>
          <el-input
            v-model="trainingCampTeam.description"
            placeholder="请填写集训队描述"
            type="textarea"
          ></el-input>
        </el-form-item>
        <el-form-item label="申请公告" required>
          <el-select
            v-model="applyGGId"
            filterable
            placeholder="请选择申请公告"
            :loading="btnLoading"
          >
            <el-option
              v-for="item in announcements"
              :key="item.id"
              :label="item.title"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="轮播图公告" required>
          <el-select
            v-model="gGId"
            filterable
            multiple
            placeholder="请选择轮播图公告"
            :loading="btnLoading"
          >
            <el-option
              v-for="item in announcements"
              :key="item.id"
              :label="item.title"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="经验分享公告" required>
          <el-select
            v-model="shareGGId"
            filterable
            multiple
            placeholder="请选择经验分享公告"
            :loading="btnLoading"
          >
            <el-option
              v-for="item in announcements"
              :key="item.id"
              :label="item.title"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <el-button type="primary" @click.native="save">{{
        $t("m.Save")
      }}</el-button>
    </el-card>
  </div>
</template>

<script>
import {
  getTrainCampTeam,
  updateTrainCampTeam,
} from "@/common/newApi/trainningcamp/index";
import myMessage from "@/common/message";
import { mapGetters } from "vuex";
import api from "@/common/api";
export default {
  name: "TrainningCamp",
  data() {
    return {
      trainingCampTeam: {},
      applyGGId: "",
      btnLoading: false,
      announcements: [],
      gGId: [],
      shareGGId: [],
      relatedAnnouncement: [],
      announcementCategory: [],
    };
  },
  mounted() {
    this.getTrainCamp();
    this.getDiscussion();
  },
  computed: {
    ...mapGetters(["userInfo"]),
  },
  methods: {
    getDiscussion() {
      let req = {
        currentPage: 1,
        onlyMine: false,
        cid: 1,
      };
      this.btnLoading = true;
      api.getDiscussionList(10000, req).then(
        (res) => {
          this.announcements = res.data.data.records;
          this.btnLoading = false;
        },
        () => {
          this.btnLoading = false;
        }
      );
    },
    getTrainCamp() {
      getTrainCampTeam().then((res) => {
        this.trainingCampTeam = res.data.data;
        this.relatedAnnouncement = this.trainingCampTeam.relatedAnnouncement;
        this.announcementCategory = this.trainingCampTeam.announcementCategory;
        this.gGId = [];
        this.shareGGId = [];
        for (let i = 0; i < this.relatedAnnouncement.length; i++) {
          if (this.announcementCategory[i] == "1") {
            this.applyGGId = this.relatedAnnouncement[i].id;
          } else if (this.announcementCategory[i] == "2") {
            this.gGId.push(this.relatedAnnouncement[i].id);
          } else if (this.announcementCategory[i] == "3") {
            this.shareGGId.push(this.relatedAnnouncement[i].id);
          }
        }
        // this.trainingCampIdList = res.data.data.trainingCampIdList;
      });
    },

    save() {
      this.trainingCampTeam.announcementCategory = ["1"];
      this.trainingCampTeam.relatedAnnouncement = [this.applyGGId.toString()];
      for (let i = 0; i < this.gGId.length; i++) {
        this.trainingCampTeam.announcementCategory.push("2");
        this.trainingCampTeam.relatedAnnouncement.push(this.gGId[i].toString());
      }
      for (let i = 0; i < this.shareGGId.length; i++) {
        this.trainingCampTeam.announcementCategory.push("3");
        this.trainingCampTeam.relatedAnnouncement.push(
          this.shareGGId[i].toString()
        );
      }
      updateTrainCampTeam(this.trainingCampTeam).then((res) => {
        this.getTrainCamp();
      });
    },
  },
};
</script>

<style scoped>
.filter-row span button {
  margin-top: 5px;
  margin-bottom: 5px;
}
.filter-row span div {
  margin-top: 8px;
}
@media screen and (max-width: 768px) {
  .filter-row span {
    margin-right: 5px;
  }
  .filter-row span div {
    width: 80%;
  }
}
@media screen and (min-width: 768px) {
  .filter-row span {
    margin-right: 20px;
  }
}
</style>
