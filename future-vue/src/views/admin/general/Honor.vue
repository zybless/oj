<template>
  <el-card>
    <el-input placeholder="请输入榜单名称" v-model="rollname"></el-input>
    <vxe-toolbar>
      <template v-slot:buttons>
        <vxe-button icon="fa fa-plus" @click="insertEvent(-1)">新增</vxe-button>
        <vxe-button icon="fa fa-save" @click="saveEvent">保存</vxe-button>
        <vxe-button @click="$refs.xTable.removeCheckboxRow()"
          >删除选中</vxe-button
        >
      </template>
    </vxe-toolbar>
    <vxe-table
      border
      show-overflow
      keep-source
      ref="xTable"
      max-height="400"
      :data="tableData"
      :loading="loading2"
      :edit-rules="validRules"
      :edit-config="{
        trigger: 'click',
        mode: 'cell',
        icon: 'fa fa-pencil',
        showStatus: true,
      }"
    >
      <vxe-table-column type="checkbox" width="60"></vxe-table-column>
      <vxe-table-column
        field="name"
        title="组名"
        :edit-render="{ name: 'input' }"
      ></vxe-table-column>
      <vxe-table-column field="userInfoList" title="用户">
        <template v-slot="{ row }">
          <el-select
            v-model="row.userInfoList"
            multiple
            filterable
            remote
            reserve-keyword
            :remote-method="remoteMethod"
            :loading="loading"
          >
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </template>
      </vxe-table-column>
    </vxe-table>
  </el-card>
</template>
<script>
import {
  getMonthRanking,
  updateMonthRanking,
} from "@/common/newApi/honorroll/index";
import api from "@/common/api";

export default {
  name: "SystemHonor",
  components: {},
  data() {
    return {
      tableData: [],
      dialogTableVisible: false,
      loading: false,
      options: [],
      validRules: {
        name: [{ required: true, message: "组名不能为空" }],
      },
      rollname: "",
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
          this.tableData = res.data.data.monthRankingDetailVOList;
          this.options = [];
          for (let item of this.tableData) {
            item.userInfoList = item.userInfoList.map((userinfo) => {
              let obj = {
                uuid: userinfo.uuid,
                realname: userinfo.realname,
                username: userinfo.username,
              };
              let temp = JSON.stringify(obj);
              let flag = true;
              for (let option of this.options) {
                if (temp == option.value) {
                  flag = false;
                  break;
                }
              }
              if (flag) {
                this.options.push({
                  value: temp,
                  label: userinfo.username,
                });
              }
              return temp;
            });
          }
          this.loading2 = false;
        })
        .catch((res) => {
          this.loading2 = false;
        });
    },
    async insertEvent(row) {
      let record = {
        name: "",
        userInfoList: [],
      };
      let { row: newRow } = await this.$refs.xTable.insertAt(record, row);
      await this.$refs.xTable.setActiveCell(newRow, "sex");
    },
    remoteMethod(query) {
      this.loading = true;
      api.admin_getUserList(1, 20, query, false).then(
        (res) => {
          this.loading = false;
          if (res.data.data.records) {
            this.options = res.data.data.records.map((item) => {
              let value = {
                uuid: item.uid,
                realname: item.realname,
                username: item.username,
              };
              return {
                value: JSON.stringify(value),
                label: item.username,
              };
            });
          }
        },
        (res) => {
          this.loading = false;
        }
      );
    },
    async saveEvent() {
      if (!this.rollname || this.rollname == "") {
        this.$message({
          message: "榜单名不能为空",
          type: "error",
        });
        return;
      }
      const errMap = await this.$refs.xTable
        .validate()
        .catch((errMap) => errMap);
      if (errMap) {
        this.$XModal.message({ status: "error", message: "组名不能为空！" });
      } else {
        let data = this.$refs.xTable.getTableData().tableData;
        let tabledata = [...data];
        for (let item of tabledata) {
          let list = [];
          for (let userInfo of item.userInfoList) {
            let temp = JSON.parse(userInfo);
            list.push(temp);
          }
          item.userInfoList = list;
        }
        await updateMonthRanking({
          name: this.rollname,
          MonthRankingDetailVOList: tabledata,
        });
        this.init();
      }
    },
  },
};
</script>
<style scoped>
.el-select {
  width: 100%;
}
</style>
