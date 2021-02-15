import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../layouts/Home.vue'
import Main from '../layouts/Main.vue'
import Analytics2 from '../layouts/Analytics2.vue'
// import MypageChart from '../views/MypageChart.vue'

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
    name: 'Main',
    component: Main
  },
  {
    path: '/calendar',
    component: Home,
    beforeEnter:requireAuth(),
  },
  {
    path: '/analytics',
    component: Analytics2,
    beforeEnter:requireAuth(),
  },
  // {
  //   path: '/chart',
  //   component: MypageChart,
  //   beforeEnter:requireAuth(),
  // },
]

const router = new VueRouter({
  mode: 'hash',
  base: process.env.BASE_URL,
  routes
})

export default router