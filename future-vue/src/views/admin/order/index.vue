<template>
  <div>
    <el-card>
      <div slot="header">
        <span class="panel-title home-title">订单列表</span>
      </div>
      <vxe-table
        stripe
        auto-resize
        :data="orderList"
        ref="adminCourse"
        :loading="loading"
        align="center"
      >
        <vxe-table-column min-width="64" field="id" title="ID">
        </vxe-table-column>
        <vxe-table-column
          field="userId"
          min-width="150"
          title="用户id"
          show-overflow
        >
        </vxe-table-column>
        <vxe-table-column
          field="state"
          min-width="130"
          title="支付状态"
          show-overflow
        >
          <template slot-scope="scope">
            <span v-if="scope.row.state === 0">未支付</span>
            <span v-else>已支付</span>
          </template>
        </vxe-table-column>
        <vxe-table-column min-width="120" :title="$t('m.Created_Time')">
          <template v-slot="{ row }">
            {{ row.gmtCreate | localtime }}
          </template>
        </vxe-table-column>
      </vxe-table>

      <div class="panel-options" v-if="showPagination">
        <el-pagination
          class="page"
          layout="prev, pager, next, sizes"
          @current-change="currentChange"
          :page-size.sync="query.pageSize"
          :total.sync="total"
          :current-page.sync="query.currentPage"
          @size-change="onPageSizeChange"
          :page-sizes="[10, 30, 50, 100]"
        >
        </el-pagination>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getOrdersList } from "@/common/newApi/order/index";
import { mapGetters } from "vuex";
export default {
  name: "OrderList",
  components: {},
  data() {
    return {
      total: 0,
      query: {
        pageSize: 10,
        currentPage: 1,
      },
      orderList: [],
      loading: false,
      showPagination: false,
      predefineColors: [
        "#ff4500",
        "#ff8c00",
        "#ffd700",
        "#90ee90",
        "#00ced1",
        "#1e90ff",
        "#c71585",
      ],
    };
  },
  mounted() {
    this.init();
  },
  computed: {
    ...mapGetters(["userInfo"]),
  },
  methods: {
    init() {
      this.getOrderList();
    },
    // 切换页码回调
    currentChange(page) {
      this.query.currentPage = page;
      this.getOrderList();
    },
    onPageSizeChange(pageSize) {
      this.query.currentPage = 1;
      this.query.pageSize = pageSize;
      this.getOrderList();
    },
    getOrderList() {
      let params = {
        limit: this.query.pageSize,
        currentPage: this.query.currentPage,
      };
      this.loading = true;
      this.showPagination = false;
      getOrdersList(params).then(
        (res) => {
          this.loading = false;
          this.total = res.data.data.total;
          this.orderList = res.data.data.records;
          this.showPagination = true;
        },
        (err) => {
          this.loading = false;
          this.showPagination = true;
        }
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
