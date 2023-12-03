<template>
    <div id="asideDiv">
        <el-menu
            :default-active="$route.path"
            router
            class="el-menu-vertical-demo"
            @open="handleOpen"
            @close="handleClose"
            background-color="#000"
            text-color="#fff"
            active-text-color="#ff0000"
        >
            <div id="logo" width="200px">
                <img src="../assets/logo.png" alt id="logoPic" @click="ToHome()"/>
                <h4>码上识件管理系统</h4>
            </div>

            <el-menu-item index="/home" >
                <HomeFilled style="width: 1em; height: 1em;margin-right:5px;"/>
                <span slot="title">系统首页</span>
            </el-menu-item>

            <el-sub-menu index="2">
                <template #title>
                    <Avatar style="width: 1em; height: 1em;margin-right:5px;"/>
                    <span style="color:white;">账号管理</span>
                </template>

                <el-menu-item-group>
                    <el-menu-item index="/courier">快递员</el-menu-item>
                    <el-menu-item index="/addressee">收件人</el-menu-item>
                </el-menu-item-group>
            </el-sub-menu>

            <el-sub-menu index="3">
                <template #title>
                    <Notebook style="width: 1em; height: 1em;margin-right:5px;"/>
                    <span style="color:white;">订单管理</span>
                </template>

                <el-menu-item-group>
                    <el-menu-item index="/assigned">已分配</el-menu-item>
                    <el-menu-item index="/notAssigned">未分配</el-menu-item>
                </el-menu-item-group>
            </el-sub-menu>
        </el-menu>
    </div>
</template>

<script>
export default {
    name: "Container",
    methods: {
        handleOpen(key, keyPath) {
            console.log(key, keyPath);
        },
        handleClose(key, keyPath) {
            console.log(key, keyPath);
        },
        ToHome(){
            this.$router.push("/");
        },
    },
};
</script>

<style>
#logo {
    width: 100%;
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
    padding-top: 10px;
    padding-bottom: 10px;
    padding-left: 0;
    padding-right: 0;
}
#logoPic {
    width: 30px;
    height: 30px;
    border-radius: 50%;
}
h4 {
    color: aliceblue;
    font-weight: 600;
    padding-left: 5px;
}
.el-menu {
    border-right: none !important;
}
</style>