<template>
  <el-card>
    <div class="filter-row">
      <el-row>
        <el-col :md="3" :xs="24">
          <span class="title">{{ $t("m.Group_Training") }}</span>
        </el-col>
        <el-col
          :md="18"
          :xs="24"
          v-if="
            (isAdmin || isGroupAdmin) && !problemPage && !editProblemPage
          "
        >
          <el-button
            v-if="!editPage && !createPage"
            type="primary"
            size="small"
            @click="importTraining"
            >导入公共题单</el-button
          >
          <el-button
            v-if="!editPage"
            :type="createPage ? 'warning' : 'primary'"
            size="small"
            @click="handleCreatePage"
            :icon="createPage ? 'el-icon-back' : 'el-icon-plus'"
            >{{
              createPage ? $t("m.Back_To_Admin_Training_List") : $t("m.Create")
            }}</el-button
          >
          <el-button
            v-if="editPage && adminPage"
            type="warning"
            size="small"
            @click="handleEditPage"
            icon="el-icon-back"
            >{{ $t("m.Back_To_Admin_Training_List") }}</el-button
          >
          <el-button
            :type="adminPage ? 'danger' : 'success'"
            v-if="!editPage && !createPage"
            size="small"
            @click="handleAdminPage"
            :icon="adminPage ? 'el-icon-back' : 'el-icon-s-opportunity'"
            >{{ adminPage ? "返回作业列表" : "作业列表" }}</el-button
          >
        </el-col>
        <el-col
          :md="18"
          :xs="24"
          v-else-if="
            (isAdmin || isGroupAdmin) && problemPage && !editProblemPage
          "
        >
          <el-button
            type="primary"
            size="small"
            @click="publicPage = true"
            icon="el-icon-plus"
            >{{ $t("m.Add_From_Public_Problem") }}</el-button
          >
          <!-- <el-button
            type="success"
            size="small"
            @click="handleGroupPage"
            icon="el-icon-plus"
            >{{ $t('m.Add_From_Group_Problem') }}</el-button
          > -->
          <el-button
            type="warning"
            size="small"
            @click="handleProblemPage(null)"
            icon="el-icon-back"
            >{{ $t("m.Back_To_Admin_Training_List") }}</el-button
          >
        </el-col>
        <el-col
          :md="18"
          :xs="24"
          v-else-if="(isAdmin || isGroupAdmin) && editProblemPage"
        >
          <el-button
            type="primary"
            size="small"
            @click="handleEditProblemPage"
            icon="el-icon-back"
            >{{ $t("m.Back_Admin_Training_Problem_List") }}</el-button
          >`
        </el-col>
      </el-row>
    </div>
    <template v-if="!adminPage && !createPage && !problemPage">
      <vxe-table
        border="inner"
        stripe
        auto-resize
        highlight-hover-row
        :data="trainingList"
        :loading="loading"
        align="center"
        @cell-click="goGroupTraining"
      >
        <vxe-table-column
          field="rank"
          :title="$t('m.Number')"
          min-width="60"
          show-overflow
        >
        </vxe-table-column>
        <vxe-table-column
          field="title"
          :title="$t('m.Title')"
          min-width="200"
          align="center"
        >
        </vxe-table-column>

        <vxe-table-column
          field="auth"
          :title="$t('m.Auth')"
          min-width="100"
          align="center"
        >
          <template v-slot="{ row }">
            <el-tag :type="TRAINING_TYPE[row.auth]['color']" effect="dark">
              {{ $t("m.Zuoye_" + row.auth) }}
            </el-tag>
          </template>
        </vxe-table-column>
        <vxe-table-column
          field="categoryName"
          :title="$t('m.Category')"
          min-width="130"
          align="center"
        >
          <template v-slot="{ row }">
            <el-tag
              size="large"
              :style="
                'background-color: #fff; color: ' +
                  row.categoryColor +
                  '; border-color: ' +
                  row.categoryColor +
                  ';'
              "
              >{{ row.categoryName }}</el-tag
            >
          </template>
        </vxe-table-column>

        <vxe-table-column
          field="acCount"
          :title="$t('m.Progress')"
          min-width="120"
          align="center"
        >
          <template v-slot="{ row }">
            <span>
              <el-tooltip
                effect="dark"
                :content="row.acCount + '/' + row.problemCount"
                placement="top"
              >
                <el-progress
                  :text-inside="true"
                  :stroke-width="20"
                  :percentage="getPassingRate(row.acCount, row.problemCount)"
                ></el-progress>
              </el-tooltip>
            </span>
          </template>
        </vxe-table-column>

        <vxe-table-column
          field="problemCount"
          :title="$t('m.Problem_Number')"
          min-width="70"
          align="center"
        >
        </vxe-table-column>
      </vxe-table>
      <Pagination
        :total="total"
        :page-size="limit"
        @on-change="currentChange"
        :current.sync="currentPage"
        @on-page-size-change="onPageSizeChange"
        :layout="'prev, pager, next, sizes'"
      ></Pagination>
    </template>
    <TrainingList
      ref="trainingList"
      v-if="adminPage && !createPage && !problemPage"
      @handleEditPage="handleEditPage"
      @currentChange="currentChange"
      @handleProblemPage="handleProblemPage"
    ></TrainingList>
    <TrainingProblemList
      v-if="problemPage"
      :trainingId="trainingId"
      @currentChangeProblem="currentChangeProblem"
      @handleEditProblemPage="handleEditProblemPage"
      ref="trainingProblemList"
    >
    </TrainingProblemList>
    <Training
      v-if="createPage && !editPage && !problemPage"
      mode="add"
      title="创建作业"
      apiMethod="addGroupTraining"
      @handleCreatePage="handleCreatePage"
      @currentChange="currentChange"
    ></Training>
    <el-dialog
      :title="$t('m.Add_Training_Problem')"
      width="90%"
      :visible.sync="publicPage"
      :close-on-click-modal="false"
    >
      <AddPublicProblem
        v-if="publicPage"
        :trainingId="trainingId"
        apiMethod="getGroupTrainingProblemList"
        @currentChangeProblem="currentChangeProblem"
        ref="addPublicProblem"
      ></AddPublicProblem>
    </el-dialog>
    <el-dialog
      :title="$t('m.Add_Training_Problem')"
      width="350px"
      :visible.sync="groupPage"
      :close-on-click-modal="false"
    >
      <AddGroupProblem
        :trainingId="trainingId"
        @currentChangeProblem="currentChangeProblem"
        @handleGroupPage="handleGroupPage"
      ></AddGroupProblem>
    </el-dialog>
    <el-dialog
      title="选择公共题单"
      width="400px"
      :visible.sync="importPage"
      :close-on-click-modal="false"
    >
      <el-select
        v-model="publicTraining"
        multiple
        filterable
        remote
        reserve-keyword
        placeholder="请输入关键词"
        :remote-method="remoteMethod"
        :loading="loading2"
      >
        <el-option
          v-for="item in options"
          :key="item.id"
          :label="item.title"
          :value="item.id"
        >
        </el-option>
      </el-select>
      <div slot="footer" class="dialog-footer">
        <el-button @click="importPage = false">取 消</el-button>
        <el-button type="primary" @click="confirmImport">确 定</el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
import { mapGetters } from "vuex";
import { TRAINING_TYPE } from "@/common/constants";
import Pagination from "@/components/oj/common/Pagination";
import TrainingList from "@/components/oj/group/TrainingList";
import Training from "@/components/oj/group/Training";
import TrainingProblemList from "@/components/oj/group/TrainingProblemList";
import AddPublicProblem from "@/components/oj/group/AddPublicProblem.vue";
import AddGroupProblem from "@/components/oj/group/AddGroupProblem.vue";
import api from "@/common/api";
import { addTrainingFromPublic } from "@/common/newApi/grouptraining/index";
export default {
  name: "GroupTrainingList",
  components: {
    Pagination,
    TrainingList,
    Training,
    TrainingProblemList,
    AddPublicProblem,
    AddGroupProblem,
  },
  data() {
    return {
      total: 0,
      currentPage: 1,
      limit: 10,
      trainingList: [],
      TRAINING_TYPE: {},
      loading: false,
      adminPage: false,
      createPage: false,
      editPage: false,
      problemPage: false,
      publicPage: false,
      groupPage: false,
      editProblemPage: false,
      trainingId: null,
      importPage: false,
      publicTraining: [],
      loading2: false,
      options: [],
    };
  },
  mounted() {
    this.TRAINING_TYPE = Object.assign({}, TRAINING_TYPE);
    this.init();
  },
  methods: {
    importTraining() {
      this.importPage = true;
    },
    remoteMethod(query) {
      this.loading2 = true;
      api.getTrainingList(1, 100, { keyword: query , auth: "Public"}).then(
        (res) => {
          this.options = res.data.data.records;
          this.loading2 = false;
        },
        (err) => {
          this.loading2 = false;
        }
      );
    },
    confirmImport() {
      let params = {
        groupId: this.$route.params.groupID,
        tidList: this.publicTraining,
      };
      console.log("params", params);
      addTrainingFromPublic(params).then((res) => {
        this.importPage = false;
        this.getGroupTrainingList();
      });
    },
    init() {
      this.getGroupTrainingList();
    },
    onPageSizeChange(pageSize) {
      this.limit = pageSize;
      this.init();
    },
    currentChange(page) {
      this.currentPage = page;
      this.init();
    },
    currentChangeProblem() {
      this.$refs.trainingProblemList.currentChange(1);
    },
    getGroupTrainingList() {
      this.loading = true;
      api
        .getGroupTrainingList(
          this.currentPage,
          this.limit,
          this.$route.params.groupID
        )
        .then(
          (res) => {
            this.trainingList = res.data.data.records;
            this.total = res.data.data.total;
            this.loading = false;
          },
          (err) => {
            this.loading = false;
          }
        );
    },
    goGroupTraining(event) {
      this.$router.push({
        name: "GroupTrainingDetails",
        params: {
          trainingID: event.row.id,
          groupID: this.$route.params.groupID,
        },
      });
    },
    handleCreatePage() {
      this.createPage = !this.createPage;
    },
    handleEditPage() {
      this.editPage = !this.editPage;
      this.$refs.trainingList.editPage = this.editPage;
    },
    handleAdminPage() {
      this.adminPage = !this.adminPage;
      this.createPage = false;
      this.editPage = false;
    },
    handleProblemPage(trainingId) {
      this.trainingId = trainingId;
      this.problemPage = !this.problemPage;
    },
    handleGroupPage() {
      this.groupPage = !this.groupPage;
    },
    handleEditProblemPage() {
      this.editProblemPage = !this.editProblemPage;
      this.$refs.trainingProblemList.editPage = this.editProblemPage;
    },
    getPassingRate(ac, total) {
      if (!total) {
        return 0;
      }
      return ((ac / total) * 100).toFixed(2);
    },
  },
  computed: {
    ...mapGetters(["isAuthenticated", "isAdmin", "isGroupAdmin"]),
  },
};
</script>

<style scoped>
.title {
  font-size: 20px;
  vertical-align: middle;
  float: left;
}
.filter-row {
  margin-bottom: 5px;
  text-align: center;
}
@media screen and (max-width: 768px) {
  .filter-row span {
    margin-left: 5px;
    margin-right: 5px;
  }
}
@media screen and (min-width: 768px) {
  .filter-row span {
    margin-left: 10px;
    margin-right: 10px;
  }
}
</style>
