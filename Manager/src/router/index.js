import { createRouter, createWebHistory } from 'vue-router'
import AddresseeView from '../views/AddresseeView.vue'
import HomeView from '../views/HomeView.vue'
import LoginView from '../views/LoginView.vue'
import LayoutView from '../views/LayoutView.vue'
import CourierView from '../views/CourierView.vue'
import NotAssignedView from '../views/NotAssignedView.vue'
import AssignedView from '../views/AssignedView.vue'
import store from '../store/index.js'
const router = createRouter({
    history: createWebHistory(
        import.meta.env.BASE_URL),
    routes: [{
        path: "/login",
        name: "login",
        component: LoginView
    }, {
        path: '/',
        name: 'layout',
        component: LayoutView,
        redirect: "/home",
        children: [{
            path: 'home',
            name: 'home',
            component: HomeView
        }, {
            path: 'addressee',
            name: 'addressee',
            component: AddresseeView
        }, {
            path: 'courier',
            name: 'courier',
            component: CourierView
        }, {
            path: 'notAssigned',
            name: 'notAssigned',
            component: NotAssignedView
        }, {
            path: 'assigned',
            name: 'assigned',
            component: AssignedView
        }]
    }]
})

router.beforeEach((to, from, next) => {
    console.log(store.state.uInfo);
    if (store.state.uInfo.userInfo.name) {
        next();
    } else {
        if (to.path === "/login") {
            next();
            return;
        } else {
            next("/login");
        }
    }
})

export default router