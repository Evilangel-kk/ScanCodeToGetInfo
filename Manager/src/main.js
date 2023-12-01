import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementPlus from 'element-plus';
import * as ElIconModules from '@element-plus/icons-vue'
import 'element-plus/dist/index.css';
const app = createApp(App)
app.use(store)
app.use(ElementPlus)
    //  统一注册el-icon图标
for (let iconName in ElIconModules) {
    app.component(iconName, ElIconModules[iconName])
}
app.use(router)

app.mount('#app')