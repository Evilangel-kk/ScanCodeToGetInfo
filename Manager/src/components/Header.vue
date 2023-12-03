<template>
    <div id="header">
        <div id="login">
            <img src="../assets/manager.png" alt id="pic" style="background-color: white;border-radius:50%"/>
            <el-button
                type="danger"
                style="height:30px;padding-top:2px;padding-bottom:0;margin-top:0;margin-bottom:0"
                @click="ToLogin"
            >退出</el-button>
        </div>
        <div id="rightHead">
            <span id="date" class="headMsg">{{ date.y+"/"+date.m+"/"+date.d }}</span>
            <span id="week" class="headMsg">星期{{ weekday }}</span>
            <span id="time" class="headMsg">{{ time.h+":"+time.m+":"+time.s }}</span>
        </div>
    </div>
</template>

<script>

export default {
    data() {
        return {
            date: {
                y: "",
                m: "",
                d: "",
            },
            weekday: "",
            time: {
                h: "",
                m: "",
                s: "",
            },
            timer: null,
        };
    },
    mounted() {
        this.getDateAndTime();
        this.timer = setInterval(() => {
            this.getDateAndTime();
        }, 1000);
    },
    methods: {
        ToLogin() {
            this.$store.commit("setUserInfo",{});
            localStorage.setItem("loginInfo",JSON.stringify({}));
            this.$router.replace({ name: "login" });
        },
        getDateAndTime() {
            var d = new Date();
            this.date.y = d.getFullYear();
            this.date.m = d.getMonth() + 1;
            this.date.d = d.getDate();
            var week = d.getDay();
            if (week == 0) {
                this.weekday = "日";
            } else if (week == 1) {
                this.weekday = "一";
            } else if (week == 2) {
                this.weekday = "二";
            } else if (week == 3) {
                this.weekday = "三";
            } else if (week == 4) {
                this.weekday = "四";
            } else if (week == 5) {
                this.weekday = "五";
            } else if (week == 6) {
                this.weekday = "六";
            }
            this.time.h = d.getHours();
            if (this.time.h < 10) {
                this.time.h = "0" + this.time.h;
            }
            this.time.m = d.getMinutes();
            if (this.time.m < 10) {
                this.time.m = "0" + this.time.m;
            }
            this.time.s = d.getSeconds();
            if (this.time.s < 10) {
                this.time.s = "0" + this.time.s;
            }
        },
    },
};
</script>

<style>
#header {
    display: block;
    position: relative;
    background-color: #000000;
    padding: 10px;
    align-items: center;
    justify-content: center;
    font-size: large;
    font-weight: 600;
    color: white;
}
#leftHead {
    float: left;
    margin-top: 3px;
    margin-left: 10px;
    margin-right: 10px;
}
#rightHead {
    float: right;
    margin-top: 3px;
    margin-left: 10px;
    margin-right: 20px;
}
.headMsg {
    margin-left: 5px;
    margin-right: 5px;
}
#pic {
    float: left;
    width: 30px;
    height: 30px;
    padding: 0;
    border-radius: 50%;
    margin-left: 10px;
    margin-right: 20px;
}
#login {
    float: right;
    position: relative;
    margin-left: 10px;
    margin-right: 20px;
}
</style>