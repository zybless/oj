<template>
  <div>
    <el-card>
      <div slot="header">
        <span class="panel-title home-title">集训列表</span>
        <div class="filter-row">
          <span>
            <el-button
              type="primary"
              size="small"
              @click="goCreateRepo()"
              icon="el-icon-plus"
              >{{ $t("m.Create") }}
            </el-button>
          </span>
        </div>
      </div>
      <vxe-table
        stripe
        auto-resize
        :data="trainingCampList"
        :loading="loading"
        align="center"
      >
        <vxe-table-column min-width="64" field="id" title="ID">
        </vxe-table-column>
        <vxe-table-column
          field="title"
          min-width="150"
          :title="$t('m.Title')"
          show-overflow
        >
        </vxe-table-column>
        <vxe-table-column
          field="description"
          min-width="130"
          title="描述"
          show-overflow
        >
        </vxe-table-column>
        <!-- <vxe-table-column
          field="duration"
          min-width="130"
          title="训练周期"
          show-overflow
        >
        </vxe-table-column>
        <vxe-table-column
          field="intensity"
          min-width="130"
          title="训练强度"
          show-overflow
        >
        </vxe-table-column> -->
        <vxe-table-column min-width="120" :title="$t('m.Created_Time')">
          <template v-slot="{ row }">
            {{ row.gmtCreate | localtime }}
          </template>
        </vxe-table-column>
        <vxe-table-column
          min-width="96"
          field="modifiedUser"
          :title="$t('m.Modified_User')"
          show-overflow
        >
        </vxe-table-column>
        <vxe-table-column title="Option" min-width="200">
          <template v-slot="{ row }">
            <el-tooltip
              effect="dark"
              :content="$t('m.Edit')"
              placement="top"
              v-if="
                isAdmin ||
                  row.author == userInfo.username
              "
            >
              <el-button
                icon="el-icon-edit-outline"
                size="mini"
                @click.native="goCreateRepo(row)"
                type="primary"
              >
              </el-button>
            </el-tooltip>
            <el-tooltip
              effect="dark"
              :content="$t('m.Delete')"
              placement="top"
              v-if="isAdmin"
            >
              <el-button
                icon="el-icon-delete-solid"
                size="mini"
                @click.native="deleteById(row.id)"
                type="danger"
              >
              </el-button>
            </el-tooltip>
          </template>
        </vxe-table-column>
      </vxe-table>
    </el-card>
    <Edit ref="edit"></Edit>
  </div>
</template>

<script>
import {
  getTrainCampTeam,
  deleteTrainCamp,
} from "@/common/newApi/trainningcamp/index";
import { deleteCourse } from "@/common/newApi/course/index";
import Edit from "./components/edit.vue";
import myMessage from "@/common/message";
import { mapGetters } from "vuex";
export default {
  name: "TrainCampList",
  components: { Edit },
  data() {
    return {
      trainingCampList: [],
      loading: false,
    };
  },
  mounted() {
    this.getList();
  },
  computed: {
    ...mapGetters(["userInfo", "isAdmin"]),
  },
  methods: {
    goEdit(problemId) {
      this.$refs["edit"].show("编辑", problemId);
    },
    goCreateRepo(row) {
      if (row) {
        this.$refs["edit"].show("编辑", row);
      } else {
        this.$refs["edit"].show("添加");
      }
    },
    getList() {
      this.loading = true;
      getTrainCampTeam().then(
        (res) => {
          this.loading = false;
          this.trainingCampList = res.data.data.trainingCampList;
        },
        (err) => {
          this.loading = false;
        }
      );
    },
    deleteById(id) {
      this.$confirm("确定删除该集训吗？", "Tips", {
        type: "warning",
      }).then(
        () => {
          deleteTrainCamp(id)
            .then((res) => {
              myMessage.success(this.$i18n.t("m.Delete_successfully"));
              this.getList();
            })
            .catch(() => {});
        },
        () => {}
      );
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
