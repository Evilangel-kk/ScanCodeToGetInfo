<template>
    <div id="pieChart" style="width: 600px;height:400px;"></div>
</template>
  
<script>
import SocketService from '../components/websocket'
import * as echarts from 'echarts';
  export default {
    data(){
        return{
            socketServe: SocketService.Instance,
            pieData:[],
        }
    },
    mounted() {
        this.socketServe=SocketService.Instance;
        this.socketServe.send("GetCourierOrdersRadio");
        // 添加socket通知监听
	    window.addEventListener('onmessageWS', this.getSocketData);
        // this.drawPie();
    },
    methods:{
        getSocketData(res){
            console.log(res.detail.data);
            if(res.detail.data.includes("CourierOrdersRadio")){
                var msg=res.detail.data.split("&");
                var i=1;
                var all=0;
                while(msg[i]){
                    var m=msg[i].split(":");
                    var item={
                        "name":m[0]=="null"?"未分配":m[0],
                        "value":m[1]
                    }
                    all+=parseInt(m[1]);
                    this.pieData.push(item);
                    i++;
                }
                this.pieData.push({
                    value: all,
                    itemStyle: {
                        // stop the chart from rendering this piece
                        color: 'none',
                        decal: {
                            symbol: 'none'
                        }
                    },
                    label: {
                        show: false
                    }
                })

                this.drawPie();
            }
        },
        drawPie(){
            const option = {
                tooltip: {
                    trigger: 'item'
                },
                legend: {
                    top: '5%',
                    left: 'center',
                    // doesn't perfectly work with our tricks, disable it
                    selectedMode: false
                },
                series: [{
                    name: '快递比例',
                    type: 'pie',
                    radius: ['40%', '70%'],
                    center: ['50%', '70%'],
                    // adjust the start angle
                    startAngle: 180,
                    label: {
                        show: true,
                        formatter(param) {
                        // correct the percentage
                        return param.name + ' (' + param.percent * 2 + '%)';
                        }
                    },
                    data: this.pieData
                    }]
                };
            const pieChart = echarts.init(document.getElementById('pieChart'));
            pieChart.setOption(option);
        },
    },
  };
</script>
  