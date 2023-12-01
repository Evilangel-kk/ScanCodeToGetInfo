<template>
    <div id="layout">
        <el-container id="container">
            <el-aside id="aside" style="width:200px">
                <Aside></Aside>
            </el-aside>
            <el-main style="padding:0">
                <div id="top">
                    <Header></Header>
                </div>
                <router-view />
            </el-main>
        </el-container>
    </div>
</template>

<script>
import Aside from "@/components/Aside.vue";
import Header from "@/components/Header.vue";
export default{
    name:"LayoutView",
    data(){
        return{
            isLogin:false,
        };
    },
    components: {
        Aside,
        Header,
    },
}
</script>

<style scoped>
#container {
    width: 100%;
    height: 100%;
}

#aside {
    min-height: 100vh;
    background-color: #000000;
}
#top {
    display: flex;
    flex-direction: column;
}
</style>