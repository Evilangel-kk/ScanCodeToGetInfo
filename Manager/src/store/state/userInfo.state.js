export default {
    state: {
        userInfo: (localStorage.getItem("loginInfo") && JSON.parse(localStorage.getItem("loginInfo"))) || {},
    },
    mutations: {
        setUserInfo(state, uInfo) {
            state.userInfo = uInfo;
        }
    },
}