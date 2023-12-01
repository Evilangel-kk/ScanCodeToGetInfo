// 1.创建store/index.js  并导入 createStore 方法
import { createStore } from 'vuex'

// 2.创建 store 仓库并导出
//   简写：export default createStore({ ...配置项 }) 

export default createStore({
    // 全局状态初始值
    state: {

    },
    // 计算state，获取对应的值
    getters: {

    },
    // 更新state的唯一方法，commit mutations
    mutations: {

    },
    // 可以异步操作，可以返回promise，更改数据传递到mutations去更改
    actions: {

    },
    // 数据较多，分模块
    modules: {

    }
})