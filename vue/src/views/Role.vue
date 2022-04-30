<template>
  <div>
    <!--新增弹框-->
    <el-dialog title="用户信息" :visible.sync="dialogFormVisible" size="small" width="30%">
      <el-form :model="form" label-width="80px" >
        <el-form-item label="名称" >
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="唯一标识" >
          <el-input v-model="form.value" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="描述" >
          <el-input v-model="form.description" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary"  @click="save()">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="菜单分配" :visible.sync="menuDialogVis" size="small" width="20%">
      <el-tree
          :props="props"
          :data="menuData"
          ref="tree"
          show-checkbox
          node-key="id"
          :default-expanded-keys="expends"
          :default-checked-keys="checks">
        <span class="custom-tree-node" slot-scope="{ node, data }">
        <span><i :class="data.icon"/>{{ data.name }}</span>
        </span>
      </el-tree>
      <div slot="footer" class="dialog-footer">
        <el-button @click="menuDialogVis = false">取 消</el-button>
        <el-button type="primary" @click="saveRoleMenu()">确 定</el-button>
      </div>
    </el-dialog>

    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
      <el-input style="width: 200px" placeholder="请输入描述信息" suffix-icon="el-icon-massage" class="ml-5" v-model="des"></el-input>
      <el-button class="ml-5" type="primary" @click="load()">搜索</el-button>
      <el-button class="ml-5" type="danger" @click="reset()">重置</el-button>
    </div>
    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd()">新增 <i class="el-icon-circle-plus-outline "></i></el-button>
      <el-popconfirm

          class="ml-5"
          confirm-button-text='确定'
          cancel-button-text='我再想想'
          icon="el-icon-info"
          icon-color="red"
          title="您确定删除吗？"
          @confirm="delBatch()"
      >
        <el-button type="danger" slot="reference">批量删除 <i class="el-icon-remove-outline "></i></el-button>
      </el-popconfirm>
      <el-upload
          action="http://localhost:9090/role/import" :show-file-list="false" :accept="'xlsx'" :on-success="handleExcelImportSuccess" style="display: inline-block">
        <el-button type="primary" class="ml-5" >导入 <i class="el-icon-bottom "></i></el-button>
      </el-upload>
      <el-button type="primary" class="ml-5" @click="exp">导出 <i class="el-icon-top "></i></el-button>
    </div>
    <el-table
        :data="tableData"
        style="width: 100%"
        :row-class-name="tableRowClassName" border
        @selection-change="handleSelectionChange">

      <el-table-column
          type="selection"
          >
      </el-table-column>
      <el-table-column
          prop="id"
          label="ID"
          width="80px"
          >
      </el-table-column>
      <el-table-column
          prop="name"
          label="名称"
          >
      </el-table-column>
      <el-table-column
          prop="value"
          label="唯一标识"
      >
      </el-table-column>
      <el-table-column
          prop="description"
          label="描述信息"
          >
      </el-table-column>
      <el-table-column label="操作" align="center" width="300" >
        <template slot-scope="scope">
          <el-button type="info" @click="selectMenu(scope.row)">分配菜单<i class="el-icon-menu"></i></el-button>
          <el-button type="success" @click="handleEdit(scope.row)">编辑<i class="el-icon-edit"></i></el-button>

          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference" >删除<i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[5, 10, 15, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
export default {
  name: "Role",
  data(){
    return {
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: '',
      des: '',
      form: {},
      multipleSelection: [],
      dialogFormVisible: false,
      menuDialogVis: false,
      tableData: [],
      menuData: [],
      expends: [],
      checks: [],
      props: {
        label: 'name',
      },
      roleId: 0,
      roleValue: '',
      ids: []
    }
  },
  created() {
   this.load()
  },
  methods: {
    tableRowClassName({row, rowIndex}) {
      if (rowIndex % 4 == 1) {
        return 'warning-row';
      } else if (rowIndex % 4 == 3) {
        return 'success-row';
      }
      return '';
    },
    load(){
      this.request.get("http://localhost:9090/role/page",{
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          des: this.des,
        }
      }) .then((res) =>{
        //console.log(res);
        this.tableData = res.data.records
        this.total = res.data.total
      })

      this.request.get("/menu/ids").then(r => {
        this.ids = r.data
      })
    },
    reset(){
      this.name =''
      this.des =''
      this.load()
    },
    save(){
      this.request.post("http://localhost:9090/role",this.form).then(res => {
        if (res.code === '200'){
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        }else {
          this.$message.error("保存失败")
        }
      })
    },
    saveRoleMenu(){
      this.request.post("http://localhost:9090/role//roleMenu/" + this.roleId, this.$refs.tree.getCheckedKeys()).then(
          res => {
              if (res.code === '200'){
                this.$message.success("绑定成功")
                this.menuDialogVis = false

                // 操作管理员角色后需要重新登录
                if (this.roleValue === 'ROLE_ADMIN') {
                  this.$store.commit("logout")
                }
              }else {
                this.$message.error(res.msg)
              }
          })
    },
    handleAdd(){
      this.dialogFormVisible = true
      this.form = {}
    },
    handleEdit(row){
      this.form = row
      this.dialogFormVisible = true
    },
    async selectMenu(role) {
      this.roleId = role.id
      this.roleValue = role.value

      // 请求菜单数据
      this.request.get("http://localhost:9090/menu").then(res => {
        this.menuData = res.data

        // 把后台返回的菜单数据处理成 id数组
        this.expends = this.menuData.map(v => v.id)
      })

      this.request.get("http://localhost:9090/role/roleMenu/" + this.roleId).then(res => {
        this.checks = res.data
        this.ids.forEach(id => {
          if (!this.checks.includes(id)) {
            // 可能会报错：Uncaught (in promise) TypeError: Cannot read properties of undefined (reading 'setChecked')
            this.$nextTick(() => {
              this.$refs.tree.setChecked(id, false)
            })
          }
        })
        this.menuDialogVis = true
      })
    },
    del(id){
      this.request.delete("http://localhost:9090/role/"+id).then(res => {
        if (res.code === '200'){
          this.$message.success("删除成功")
          this.load()
        }else {
          this.$message.error("删除失败")
        }
      })
    },
    handleSelectionChange(val){
      this.multipleSelection = val
    },
    delBatch(){
      let ids = this.multipleSelection.map(v => v.id) //[{} ,{} ,{}] => [1,2,3]
      this.request.post("http://localhost:9090/role/del/batch",ids).then(res => {
        if (res.code === '200') {

          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    handleSizeChange(pageSize){
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum){
      //console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
    exp(){
      window.open("http://localhost:9090/role/export")
    },
    handleExcelImportSuccess(){
      this.$message.success("文件导入成功")
      this.load()
    },
  }
}
</script>

<style scoped>

</style>