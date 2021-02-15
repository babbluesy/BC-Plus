import Vue from 'vue'
import VueRouter from 'vue-router'
import Agenda from '../views/Agenda.vue'
import Home2 from '../views/Home2.vue'
import MypageChart from '../views/MypageChart.vue'

const requireAuth = () => (to,from,next) => {
  if(sessionStorage.getItem("access-token")==null) {
    alert("로그인이 필요한 접근입니다.");
    next('/');
  } else {
    next();
  }
}

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home2',
    component: Home2
  },
  {
    path: '/calendar',
    component: Agenda,
    beforeEnter:requireAuth(),
  },
  {
    path: '/chart',
    component: MypageChart,
    beforeEnter:requireAuth(),
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router