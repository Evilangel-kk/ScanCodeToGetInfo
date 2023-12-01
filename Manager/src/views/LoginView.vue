<template>
    <div id="loginPage" v-loading="show">
        <div id="loginLogo"></div>
        <div id="loginMsg">
            <h2 id="welcomeLog">欢迎登录</h2>
            <el-form :model="admin" style="display:flex;justify-content: center;align-items: center;flex-direction: column;">
                <el-form-item
                    :rules="[{
                        required:true,
                        message:'此项为必填项',
                        trigger:'blur'
                    }]">
                    <el-input 
                        v-model="admin.name" 
                        prefix-icon="user" 
                        placeholder="请输入用户名"
                    ></el-input>
                </el-form-item>
                <el-form-item
                    :rules="[{
                        required:true,
                        message:'此项为必填项',
                        trigger:'blur'
                    }]">
                    <el-input
                        v-model="admin.password"
                        prefix-icon="lock"
                        type="password"
                        placeholder="请输入密码"
                    ></el-input>
                </el-form-item>
                <el-form-item style="display:flex;">
                    <el-button type="primary" @click="loginSubmit()" style="background-color:red;border-color:white">登录</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
import SocketService from '../components/websocket'
    export default{
        name:"LoginView",
        data(){
            return{
                admin:{},
                socketServe: SocketService.Instance,
                show:false,
            };
        },
        mounted(){
            // 添加socket通知监听
	        window.addEventListener('onmessageWS', this.getSocketData)
        },
        methods: {
            loginSubmit() {
                this.show=true;
                this.socketServe=SocketService.Instance;
                this.socketServe.send("ManagerLogin:"+this.admin.name+":"+this.admin.password);
            },
            getSocketData(res){
                console.log(res.detail.data);
                if(res.detail.data=="FUND_MANAGER"){
                    this.show=false;
                    this.$router.push("/");
                }else if(res.detail.data=="NOTFUND_MANAGER"){
                    this.show=false;
                    this.$message.error('账号或密码错误');
                }
            },
        },
    }
</script>

<style scoped>
#loginPage {
    width: 100%;
    height: 100%;
    background-image: url('../assets/background.png');
    background-size: cover;
    background-repeat: none;
    display: flex;
    align-items: center;
    /* justify-content: center; */
}
#loginLogo{
    width: 200px;
    height: 200px;
    margin-right: 200px;
    background-image: url('../assets/logo.png');
    background-size: cover;
    background-repeat: none;
    border-radius: 50%;
    margin-left: 400px;
    box-shadow: -10px -10px 20px rgba(255, 255, 255, 0.1),
        10px 10px 20px rgba(241, 171, 171, 0.5), 0px 40px 50px rgba(241, 171, 171, 0.4);
}
#loginMsg {
    width: 20%;
    height: 40%;
    border-radius: 20px;
    background: radial-gradient(transparent, rgba(239, 188, 188, 0.2));
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;
    backdrop-filter: blur(25px);
    border-bottom: none;
    border-right: none;
    box-shadow: -10px -10px 20px rgba(255, 255, 255, 0.1),
        10px 10px 20px rgba(0, 0, 0, 0.1), 0px 40px 50px rgba(0, 0, 0, 0.2);
}
#welcomeLog {
    margin-bottom: 20px;
    color: white;
    font-size: 30px;
    font-weight: 600;
}
h2{
    color: white;
    background-color: red;
    padding: 10px 20px;
}
</style>