import Vue from "vue";
import App from "./App.vue";
import store from "./store/store";
import router from './router'
import GAuth from 'vue-google-oauth2'

import modal from "vue-js-modal";
import vuetify from './plugins/vuetify';
import VueMoment from 'vue-moment'
import BootstrapVue from 'bootstrap-vue'
import Gravatar from 'vue-gravatar';
import DatetimePicker from 'vuetify-datetime-picker'
import VueChatScroll from 'vue-chat-scroll'
import VueTimepicker from 'vue2-timepicker'
import VueApexCharts from 'vue-apexcharts'
import VueNumber from 'vue-number-animation'


// (Optional) import 'vuetify-datetime-picker/src/stylus/main.styl'

import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import 'vue2-timepicker/dist/VueTimepicker.css'

Vue.use(GAuth, {clientId: '367773692104-ldevrvvo0gghju43m8cc1m6as95nkenn.apps.googleusercontent.com', scope: 'profile email https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/calendar'})
Vue.use(BootstrapVue)
Vue.use(VueMoment);
Vue.use(VueChatScroll)
Vue.use(DatetimePicker)
Vue.use(VueTimepicker)
Vue.use(VueNumber)
Vue.use(VueApexCharts)

Vue.component('v-gravatar', Gravatar);
Vue.component('apexchart', VueApexCharts)

Vue.config.productionTip = false;
Vue.use(modal, { dialog: true, dynamic: true });

new Vue({
  render: h => h(App),
  vuetify,
  store,
  router,
}).$mount("#app");
