<template>
    <el-card style="width: 500px; padding: 20px; ">
        <el-form :model="form" label-width="80px" >
            <el-upload
                    class="avatar-uploader"
                    action="http://localhost:9090/file/upload/"
                    :show-file-list="false"
                    :on-success="handleAvatarSuccess">
                <img v-if="form.avatarUrl" :src="form.avatarUrl" class="avatar">
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
            <el-form-item label="用户名" >
                <el-input v-model="form.username" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="密码" >
                <el-input v-model="form.password" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="昵称" >
                <el-input v-model="form.nickname" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="邮箱" >
                <el-input v-model="form.email" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="电话" >
                <el-input v-model="form.phone" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="地址" >
                <el-input v-model="form.address" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item>

                <el-button type="primary"  @click="save()">确 定</el-button>
            </el-form-item>
        </el-form>

    </el-card>

</template>

<script>
    export default {
        name: "Person",
        data(){
            return{
                form:{},
                user:localStorage.getItem("user")?JSON.parse(localStorage.getItem("user")):{},
            }
        },

        created() {
            this.request.get("http://localhost:9090/user/username/"+ this.user.username).then(res => {
                if (res){
                    this.form = res.data
                }
            })
        },
        methods:{
            async getUser() {
                return (await this.request.get("http://localhost:9090/user/username/" + this.user.username)).data
            },
            save(){
                this.request.post("http://localhost:9090/user",this.form).then(res => {
                    if (res) {
                        this.$message.success("保存成功")

                        this.$emit("refreshUser")

                        // 更新浏览器存储的用户信息
/*
                        this.getUser().then(res => {
                            res.token = JSON.parse(localStorage.getItem("user")).token
                            localStorage.setItem("user", JSON.stringify(res))
                        })
*/
                    }else {
                        this.$message.success("保存失败")
                    }
                })
            },
            handleAvatarSuccess(res){
                this.form.avatarUrl = res
            },

        }
    }
</script>

<style>
    .avatar-uploader{
        text-align: center;
        padding-bottom: 10px;
    }
    .avatar-uploader .el-upload {
        text-align: center;
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;

    }
    .avatar-uploader .el-upload:hover {
        border-color: #409EFF;
    }
    .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 138px;
        height: 138px;
        line-height: 138px;
        text-align: center;
    }
    .avatar {
        width: 138px;
        height: 138px;
        display: block;
    }
</style>