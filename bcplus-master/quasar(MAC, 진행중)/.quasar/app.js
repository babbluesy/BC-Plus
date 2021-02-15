/**
 * THIS FILE IS GENERATED AUTOMATICALLY.
 * DO NOT EDIT.
 *
 * You are probably looking on adding startup/initialization code.
 * Use "quasar new boot <name>" and add it there.
 * One boot file per concern. Then reference the file(s) in quasar.conf.js > boot:
 * boot: ['file', ...] // do not add ".js" extension to it.
 *
 * Boot files are your "main.js"
 **/
import Vue from 'vue'
import './import-quasar.js'
// require(`./themes/app.${__THEME}.styl`)

import App from 'app/src/App.vue'


import createStore from 'app/src/store/index'

import createRouter from 'app/src/router/index'
import GAuth from 'vue-google-oauth2'

import VModal from 'vue-js-modal'
import VueMoment from 'vue-moment'
import { BootstrapVue, IconsPlugin } from 'bootstrap-vue'
import VueTimepicker from 'vue2-timepicker'
import Gravatar from 'vue-gravatar'
import ToggleButton from 'vue-js-toggle-button'
import VueChatScroll from 'vue-chat-scroll'
// import { Plugins } from '@capacitor/core'
import VueApexCharts from 'vue-apexcharts'
import Geocoder from "@pderas/vue2-geocoder"
import VueNumber from 'vue-number-animation'
 

import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
Vue.use(GAuth, {clientId: '367773692104-ldevrvvo0gghju43m8cc1m6as95nkenn.apps.googleusercontent.com', scope: 'profile email https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/calendar'})
Vue.use(VModal, {dynamicDefaults: { dynamic:true, adaptive: true}})
Vue.use(VueMoment)
Vue.use(BootstrapVue)
Vue.use(IconsPlugin)
Vue.use(VueTimepicker)
Vue.use(ToggleButton)
Vue.use(VueChatScroll)
Vue.use(VueApexCharts)
Vue.use(VueNumber)

Vue.use(Geocoder, {
    googleMapsApiKey: 'AIzaSyDca-ggUH7QAOAKRx2m0b3MR0kOV0WFKBk'
})

Vue.component('apexchart', VueApexCharts)
Vue.component('v-gravatar', Gravatar)

export default async function () {
  // create store and router instances
  
  const store = typeof createStore === 'function'
    ? await createStore({Vue})
    : createStore
  
  const router = typeof createRouter === 'function'
    ? await createRouter({Vue, store})
    : createRouter
  
  // make router instance available in store
  store.$router = router
  

  // Create the app instantiation Object.
  // Here we inject the router, store to all child components,
  // making them available everywhere as `this.$router` and `this.$store`.
  const app = {
    router,
    store,
    render: h => h(App)
  }


  
  app.el = '#q-app'
  

  // expose the app, the router and the store.
  // note we are not mounting the app here, since bootstrapping will be
  // different depending on whether we are in a browser or on the server.
  return {
    app,
    store,
    router
  }
}