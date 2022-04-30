<template>
    <div>
        <div style="margin: 10px 0">
            <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
            <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
            <el-button type="warning" @click="reset">重置</el-button>
        </div>
        <div style="margin: 10px 0">
            <el-upload action="http://localhost:9090/file/upload" :show-file-list="false" :on-success="handleFileUploadSuccess" style="display: inline-block">
                <el-button type="primary" class="ml-5">上传文件 <i class="el-icon-top"></i></el-button>
            </el-upload>
            <el-popconfirm
                    class="ml-5"
                    confirm-button-text='确定'
                    cancel-button-text='我再想想'
                    icon="el-icon-info"
                    icon-color="red"
                    title="您确定批量删除这些数据吗？"
                    @confirm="delBatch"
            >
                <el-button type="danger" slot="reference">批量删除 <i class="el-icon-remove-outline"></i></el-button>
            </el-popconfirm>

        </div>
        <el-table
                :data="tableData"
                style="width: 100%"
                :row-class-name="tableRowClassName" border
                @selection-change="handleSelectionChange">

            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column prop="id" label="ID" width="80" align="center"></el-table-column>
            <el-table-column prop="name" label="文件名称" align="center"></el-table-column>
            <el-table-column prop="type" label="文件类型" align="center"></el-table-column>
            <el-table-column prop="size" label="文件大小(kb)" align="center"></el-table-column>
          <el-table-column label="预览" align="center">
            <template slot-scope="scope">
              <el-button type="primary" @click="preview(scope.row.url)">预览</el-button>
            </template>
          </el-table-column>
            <el-table-column label="下载" align="center">
                <template slot-scope="scope">
                    <el-button type="primary" @click="download(scope.row.url)">下载</el-button>
                </template>
            </el-table-column>
            <el-table-column label="启用" align="center">
                <template slot-scope="scope">
                    <el-switch v-model="scope.row.enable" active-color="#13ce66" inactive-color="#ccc" @change="changeEnable(scope.row)"></el-switch>
                </template>
            </el-table-column>
            <el-table-column label="操作"  width="200" align="center">
                <template slot-scope="scope">
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
                    :page-sizes="[2, 5, 10, 20]"
                    :page-size="pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total">
            </el-pagination>
        </div>
    </div>
</template>

<script>
    export default {
        name: "File",
        data(){
            return{
                tableData: [],
                name: '',
                multipleSelection: [],
                pageNum: 1,
                pageSize: 10,
                total: 0

            }
        },
        created(){
            this.load()
        },
        methods:{
            tableRowClassName({row, rowIndex}) {
                if (rowIndex % 4 == 1) {
                    return 'warning-row';
                } else if (rowIndex % 4 == 3) {
                    return 'success-row';
                }
                return '';
            },
            load(){
                this.request.get("http://localhost:9090/file/page",{
                    params: {
                        pageNum:this.pageNum,
                        pageSize:this.pageSize,
                        name:this.name,
                    }
                }) .then((res) =>{

                    //console.log(res);
                    this.tableData = res.data.records
                    this.total = res.data.total
                })
                /*fetch("http://localhost:9090/user/page?pageNum="+this.pageNum+"&pageSize="+this.pageSize+"&username="+this.username+"&email="+this.email+"&address="+this.address).then(res => res.json()).then(res =>{
                    //console.log(res)
                    this.tableData = res.records
                    this.total = res.total
                })*/
            },
            changeEnable(row) {
                this.request.post("http://localhost:9090/file/update", row).then(res => {
                    if (res.code === '200') {
                        this.$message.success("操作成功")
                    }
                })
            },
            reset(){
                this.name =''

                this.load()
            },



            del(id){
                this.request.delete("http://localhost:9090/file/"+id).then(res => {
                    if (res){
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
                this.request.post("http://localhost:9090/file/del/batch",ids).then(res => {
                    if (res) {
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
            handleFileUploadSuccess(res){
                console.log(res)
                this.load()
            },
            download(url) {
                window.open(url)
            },
          preview(url) {
            window.open('https://file.keking.cn/onlinePreview?url=' + encodeURIComponent(window.btoa((url))))
          },

        },
    }
</script>

<style scoped>

</style>