<template>
  <div>
    <div v-for="(tableInfo, index) in monthRankingDetailVOList" :key="index">
      <div
        style="color:rgba(64,158,255,1.00);font-size: 14px;font-weight: bold;border-radius: 10px 10px 0 0;padding:10px 15px;;background-color: rgba(64,158,255,0.2);"
      >
        {{ tableInfo.name }}
      </div>
      <vxe-table
        height="186"
        align="center"
        :data="tableInfo.userInfoList"
        border="outer"
      >
        <vxe-table-column label="排名" width="100">
          <template v-slot="{ rowIndex }">
            <span v-if="rowIndex < 3">
              <icon :name="icons[rowIndex]" :w="30" :h="30"></icon>
            </span>
            <span v-else>{{ rowIndex + 1 }}</span>
          </template>
        </vxe-table-column>
        <vxe-table-column min-width="150" field="realname" title="用户">
          <template v-slot="{ row }">
            <span v-if="row.realname">{{ row.realname }}</span>
            <span v-else>{{ row.username }}</span>
          </template>
        </vxe-table-column>
      </vxe-table>
    </div>
  </div>
</template>

<script>
import Pagination from "@/components/oj/common/Pagination";
import { getMonthRanking } from "@/common/newApi/honorroll/index";
export default {
  name: "HonorRoll",
  components: {
    Pagination,
  },
  data() {
    return {
      rollname: "",
      loading: false,
      monthRankingDetailVOList: [],
      icons: ["gold_medal", "silver_medal", "bronze_medal"],
    };
  },
  mounted() {
    this.init();
  },
  methods: {
    init() {
      this.loading2 = true;
      getMonthRanking()
        .then((res) => {
          this.rollname = res.data.data.name;
          this.monthRankingDetailVOList =
            res.data.data.monthRankingDetailVOList;
          this.loading2 = false;
        })
        .catch((res) => {
          this.loading2 = false;
        });
    },
  },
};
</script>

<style scoped>
.no-announcement {
  text-align: center;
  font-size: 16px;
}
.vxe-table {
  margin-bottom: 10px;
}
</style>
