<template>
    <div id="HomePage">
        <span class="welcome">欢迎来到码上识件管理系统</span>
        <div class="page-content">
            <div class="manager">
                <div class="avatar">
                    <img src="../assets/manager.png" alt="头像">
                </div>
                <h1>超级管理员</h1>
            </div>
            <div class="webmessage">

                <div class="message">
                    <span class="title">订单数量</span>
                    <span class="num">{{ numOfOrder }}</span>
                </div>

                <div class="message">
                    <span class="title">快递员数量</span>
                    <span class="num">{{ numOfCourier }}</span>
                </div>

                <div class="message">
                    <span class="title">收件人数量</span>
                    <span class="num">{{ numOfAddressee }}</span>
                </div>
            </div>
        </div>
        <div class="charts">
            <PieChart/>
            <BarChart/>
        </div>
    </div>
</template>

<script>
import SocketService from '../components/websocket'
import PieChart from '../components/Pie.vue'
import BarChart from '../components/Bar.vue'
    export default{
        name:"HomeView",
        data(){
            return{
                socketServe: SocketService.Instance,
                numOfCourier:null,
                numOfAddressee:null,
                numOfOrder:null,
            }
        },
        components:{
            PieChart,
            BarChart,
        },
        mounted(){
            this.socketServe=SocketService.Instance;
            this.socketServe.send("GetNumbers");
            // 添加socket通知监听
	        window.addEventListener('onmessageWS', this.getSocketData);
        },
        methods:{
            getSocketData(res){
                console.log(res.detail.data);
                if(res.detail.data.includes("numOfOrder")){
                    this.numOfOrder=res.detail.data.split(":")[1];
                }else if(res.detail.data.includes("numOfAddressee")){
                    this.numOfAddressee=res.detail.data.split(":")[1];
                }else if(res.detail.data.includes("numOfCourier")){
                    this.numOfCourier=res.detail.data.split(":")[1];
                }
            },
        },
    }
</script>

<style scoped>
#HomePage{
    padding: 20px;
}
.page-content{
    display: flex;
}
.welcome{
    font-family: '锐字真言体免费商用';
    font-size: 40px;
    background: linear-gradient(-210deg, rgb(0, 0, 0) 0%, red 90%);
    color: transparent;
    -webkit-background-clip: text;
    padding-left: 50px;
}
.manager{
    padding: 50px;
    align-items: center;
    flex-direction: column;
}
.avatar{
    height: 150px;
    width: 150px;
    background-color: white;
    border-radius: 50%;
    box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
    display: flex;
    align-items: center;
    justify-content: center;
}
.avatar img{
    width: 95%;
    height: 95%;
}
.manager h1{
    font-size: 30px;
    font-weight: 700;
}
.webmessage{
    display: flex;
}

.message{
    padding: 20px;
    margin: 50px;
    width: 200px;
    height: 200px;
    background-color: rgb(250, 45, 9);
    border-radius: 20px;
    box-shadow: -10px -10px 20px rgba(255, 255, 255, 0.1),
        10px 10px 20px rgba(0, 0, 0, 0.1), 0px 40px 50px rgba(0, 0, 0, 0.2);
}
.message span{
    display: block;
}

.message .title{
    font-size: 20px;
    font-weight: 600;
    color: black;
}

.message .num{
    font-size: 100px;
    font-weight: 700;
    text-align: center;
    color: rgb(255, 255, 255);
}
.charts{
    display: flex;
}
</style>