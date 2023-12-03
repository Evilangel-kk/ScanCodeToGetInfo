// 1.创建store/index.js  并导入 createStore 方法
import { createStore } from 'vuex'
import uInfo from './state/userInfo.state.js'

export default createStore({
    modules: {
        uInfo,
    }
})