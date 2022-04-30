<template>
  <div>
    <!--新增弹框-->
    <el-dialog title="用户信息" :visible.sync="dialogFormVisible" size="small" width="30%">
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="角色">
          <el-select clearable v-model="form.role" placeholder="请选择角色" style="width: 100%">
            <el-option v-for="item in roles" :key="item.name" :label="item.name" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickname" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save()">确 定</el-button>
      </div>
    </el-dialog>
    <!--查看老师授课课程-->
    <el-dialog title="课程信息" :visible.sync="vis" size="small" width="30%">
      <el-table :data="courses" border stripe>
        <el-table-column prop="name" label="课程名称"></el-table-column>
        <el-table-column prop="score" label="课程学分"></el-table-column>
      </el-table>
    </el-dialog>
    <!--查看学生选课课程-->
    <el-dialog title="选课信息" :visible.sync="stuVis" size="small" width="30%">
      <el-table :data="stuCourses" border stripe>
        <el-table-column prop="name" label="课程名称"></el-table-column>
        <el-table-column prop="score" label="课程学分"></el-table-column>
      </el-table>
    </el-dialog>

    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入用户名" suffix-icon="el-icon-search" v-model="username"></el-input>
      <el-input style="width: 200px" placeholder="请输入邮箱" suffix-icon="el-icon-massage" class="ml-5"
                v-model="email"></el-input>
      <el-input style="width: 200px" placeholder="请输入地址" suffix-icon="el-icon-position" class="ml-5"
                v-model="address"></el-input>
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
          action="http://localhost:9090/user/import" :show-file-list="false" :accept="'xlsx'"
          :on-success="handleExcelImportSuccess" style="display: inline-block">
        <el-button type="primary" class="ml-5">导入 <i class="el-icon-bottom "></i></el-button>
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
          width="55">
      </el-table-column>
      <el-table-column
          prop="id"
          label="ID"
          width="80"
          align="center">
      </el-table-column>
      <el-table-column
          prop="username"
          label="用户名"
          align="center"
      >
      </el-table-column>
      <el-table-column
          prop="role"
          label="角色"
          align="center">
        <template slot-scope="scope">
          <el-tag type="primary" v-if="scope.row.role === 'ROLE_ADMIN'">管理员</el-tag>
          <el-tag type="warning" v-if="scope.row.role === 'ROLE_TEACHER'">老师</el-tag>
          <el-tag type="success" v-if="scope.row.role === 'ROLE_STUDENT'">学生</el-tag>
        </template>
      </el-table-column>
      <el-table-column
          prop="nickname"
          label="昵称"
          align="center"
      >
      </el-table-column>
      <el-table-column
          prop="email"
          label="邮箱"
          align="center"
      >
      </el-table-column>
      <el-table-column
          prop="phone"
          label="电话"
          align="center">
      </el-table-column>
      <el-table-column
          prop="address"
          label="地址"
          align="center">
      </el-table-column>


      <el-table-column label="操作" width="300" align="center">
        <template slot-scope="scope">
          <el-button type="primary" @click="lookStuCourse(scope.row.stuCourses)" v-if="scope.row.role === 'ROLE_STUDENT'">
            查看已选课程 <i class="el-icon-document"></i></el-button>
          <el-button type="warning" @click="lookCourse(scope.row.courses)" v-if="scope.row.role === 'ROLE_TEACHER'">
            查看教授课程 <i class="el-icon-document"></i></el-button>
          <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>
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
  name: "User",
  data() {
    return {
      total: 0,
      pageNum: 1,
      pageSize: 10,
      username: '',
      email: '',
      address: '',
      form: {},
      roles: [],
      multipleSelection: [],
      dialogFormVisible: false,
      tableData: [],
      courses: [],
      vis: false,
      stuVis: false,
      stuCourses: [],
    }
  },
  created() {
    this.load()
    /*this.request.get("http://localhost:9090/user/page", {
      params: {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        username: this.username,
        email: this.email,
        address: this.address,
      }
    }).then((res) => {

      //console.log(res);
      this.tableData = res.records
      this.total = res.total
    })*/
    /*//请求分页查询参数
    fetch("http://localhost:9090/user/page?pageNum="+this.pageNum+"&pageSize="+this.pageSize+"&username="+this.username+"&email="+this.email+"&address="+this.address).then(res => res.json()).then(res =>{
        //console.log(res)
        this.tableData = res.records
        this.total = res.total
    })*/
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
    load() {
      this.request.get("http://localhost:9090/user/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          username: this.username,
          email: this.email,
          address: this.address,
        }
      }).then((res) => {

        this.tableData = res.data.records
        this.total = res.data.total
      })

      this.request.get("http://localhost:9090/role").then(res => {
        this.roles = res.data
      })
      /*fetch("http://localhost:9090/user/page?pageNum="+this.pageNum+"&pageSize="+this.pageSize+"&username="+this.username+"&email="+this.email+"&address="+this.address).then(res => res.json()).then(res =>{
          //console.log(res)
          this.tableData = res.records
          this.total = res.total
      })*/
    },
    reset() {
      this.username = ''
      this.email = ''
      this.address = ''
      this.load()
    },
    save() {
      this.request.post("http://localhost:9090/user", this.form).then(res => {
        if (res) {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("保存失败")
        }
      })
    },
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
    },
    lookStuCourse(stuCourses){
      this.stuCourses = stuCourses
      this.stuVis = true
    },
    lookCourse(courses) {
      this.courses = courses
      this.vis = true
    },
    handleEdit(row) {
      this.form = row
      this.dialogFormVisible = true
    },
    del(id) {
      this.request.delete("http://localhost:9090/user/" + id).then(res => {
        if (res) {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id) //[{} ,{} ,{}] => [1,2,3]
      this.request.post("http://localhost:9090/user/del/batch", ids).then(res => {
        if (res) {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      //console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
    exp() {
      window.open("http://localhost:9090/user/export")
    },
    handleExcelImportSuccess() {
      this.$message.success("文件导入成功")
      this.load()
    },
  }
}
</script>

<style>
.el-table .warning-row {
  background: oldlace;
}

.el-table .success-row {
  background: #f0f9eb;
}
</style>
