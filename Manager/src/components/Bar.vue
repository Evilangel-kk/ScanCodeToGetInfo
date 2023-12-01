<template>
    <div id="barChart" style="width: 600px;height:300px;"></div>
</template>
  
<script>
import SocketService from '../components/websocket'
import * as echarts from 'echarts';
  export default {
    data(){
      return{
        socketServe: SocketService.Instance,
        barData:[],
        barX:[],
      }
    },
    mounted() {
      this.socketServe=SocketService.Instance;
      this.socketServe.send("GetAddresseeOrdersRadio");
      // 添加socket通知监听
	    window.addEventListener('onmessageWS', this.getSocketData);
    },
    methods: {
      getSocketData(res){
        console.log(res.detail.data);
        if(res.detail.data.includes("AddresseeOrdersRadio")){
            var msg=res.detail.data.split("&");
            var i=1;
            var all=0;
            while(msg[i]){
                var m=msg[i].split(":");
                var item={
                    "name":m[0],
                    "value":m[1]
                }
                this.barX.push(m[0]);
                all+=parseInt(m[1]);
                this.barData.push(item);
                i++;
            }
            console.log(this.barX);
            this.drawBar();
        }
      },
      drawBar() {
        const option = {
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow'
            }
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: [
            {
              type: 'category',
              data: this.barX,
              axisTick: {
                alignWithLabel: true
              }
            }
          ],
          yAxis: [
            {
              type: 'value'
            }
          ],
          series: [
            {
              name: '订单数量',
              type: 'bar',
              barWidth: '60%',
              data: this.barData,
            }
          ]
        };
        const barChart = echarts.init(document.getElementById('barChart'));
        barChart.setOption(option);
      }
    }
  }
</script>
  
<style>

</style>