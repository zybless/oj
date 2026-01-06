<template>
  <div>
    <el-card>
      <div slot="header">
        <span class="panel-title home-title">题目导入导出记录</span>
      </div>
      <div class="filter-row">
        <vxe-input
          v-model="keywords"
          placeholder="请输入用户名"
          type="search"
          size="medium"
          @search-click="filterByKeyword"
          @keyup.enter.native="filterByKeyword"
        ></vxe-input>
      </div>
      <div class="list">
        <vxe-table
          :loading="loading"
          ref="table"
          :data="announcementList"
          auto-resize
          stripe
        >
          <vxe-table-column min-width="50" field="id" title="ID">
          </vxe-table-column>
          <vxe-table-column
            min-width="150"
            field="type"
            show-overflow
            title="操作类型"
          >
            <template v-slot="{ row }">
              <span v-if="row.type == 0">导入</span>
              <span v-else>导出</span>
            </template>
          </vxe-table-column>
          <!-- <vxe-table-column
            min-width="150"
            field="content"
            show-overflow
            title="内容"
          >
          </vxe-table-column>
          <vxe-table-column
            min-width="150"
            field="num"
            show-overflow
            title="数量"
          >
          </vxe-table-column> -->
          <vxe-table-column
            min-width="150"
            field="modifiedUser"
            show-overflow
            title="执行人"
          >
          </vxe-table-column>
          <vxe-table-column
            min-width="150"
            field="gmtCreate"
            :title="$t('m.Created_Time')"
          >
            <template v-slot="{ row }">
              {{ row.gmtCreate | localtime }}
            </template>
          </vxe-table-column>
          <vxe-table-column
            min-width="150"
            field="gmtModified"
            :title="$t('m.Modified_Time')"
          >
            <template v-slot="{ row }">
              {{ row.gmtModified | localtime }}
            </template>
          </vxe-table-column>
        </vxe-table>

        <div class="panel-options">
          <el-pagination
            class="page"
            layout="prev, pager, next"
            @current-change="currentChange"
            :page-size="pageSize"
            :total="total"
          >
          </el-pagination>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getIoRecord } from "@/common/newApi/ojproblem/index";
import { mapGetters } from "vuex";
export default {
  name: "record-info",
  data() {
    return {
      announcementList: [],
      pageSize: 15,
      total: 0,
      loading: false,
      currentPage: 0,
      keywords: "",
    };
  },
  mounted() {
    this.getAnnouncementList();
  },
  methods: {
    // 切换页码回调
    currentChange(page) {
      this.currentPage = page;
      this.getAnnouncementList();
    },

    getAnnouncementList() {
      this.loading = true;
      let req = {
        limit: this.pageSize,
        currentPage: this.currentPage,
        keywords: this.keywords,
      };
      getIoRecord(req).then(
        (res) => {
          this.loading = false;
          this.total = res.data.data.total;
          this.announcementList = res.data.data.records;
        },
        (res) => {
          this.loading = false;
        }
      );
    },
    filterByKeyword() {
      this.currentPage = 1;
      this.getAnnouncementList();
    },
  },
  computed: {
    ...mapGetters(["userInfo"]),
  },
};
</script>

<style scoped>
.filter-row {
  margin-bottom: 20px;
}
</style>
