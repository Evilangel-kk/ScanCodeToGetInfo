<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
import { RouterLink, RouterView } from 'vue-router'
import SocketService from './components/websocket'
export default {
    data(){
        return{
            isLogin:false,
            socketServe: SocketService.Instance,
        }
    },
    components: {
        
    },
    mounted() {
        SocketService.Instance.connect();
        this.socketServe=SocketService.Instance;
	  },
    methods:{
        
    }
};
</script>

<style scoped>

</style>
