<template>

    <el-container style="min-height: 100vh; ">

        <el-aside width="'sideWidth'+px" style="background-color: rgb(238, 241, 246);">
            <Aside :isCollapse="isCollapse" :logoTextShow="logoTextShow" />
        </el-aside>

        <el-container>

            <el-header style="border-bottom: 1px solid #ccc;">
                <Header :collapseBtnClass="collapseBtnClass" :user="user" @asideCollapse="collapse"/>
            </el-header>

            <el-main>
                <!--表示当前页面的子路由会在当前界面展示-->
                <router-view @refreshUser="getUser" />
            </el-main>
        </el-container>
    </el-container>
</template>
<script>
    import Aside from "../components/Aside";
    import Header from "../components/Header";

    export default {
        name: 'ManageView',
        components: {Header, Aside},
        data() {

            return {

                collapseBtnClass: 'el-icon-s-fold',
                isCollapse: false,
                sideWidth: 200,
                logoTextShow: true,
                user:{}
            }
        },
        created() {
            this.getUser();
        },
        methods: {
            getUser() {
                 let username = JSON.parse(localStorage.getItem("user")).username;
                 //从后台获取user封装数据
                 this.request.get("http://localhost:9090/user/username/" + username).then(res =>{
                   //从新赋值后台的最新数据
                     this.user = res.data
                 })
            },
            collapse() {//点击收缩按钮触发
                this.isCollapse = !this.isCollapse
                if (this.isCollapse) {    //收缩
                    this.sideWidth = 64
                    this.collapseBtnClass = 'el-icon-s-unfold'
                    this.logoTextShow = !this.logoTextShow
                } else {    //展开
                    this.sideWidth = 200
                    this.collapseBtnClass = 'el-icon-s-fold'
                    this.logoTextShow = !this.logoTextShow
                }
            },
        }
    }
</script>

