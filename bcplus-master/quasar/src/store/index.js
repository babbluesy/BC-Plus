import Vue from 'vue'
import Vuex from 'vuex'

import accounts from './modules/accounts.js'
import calendar from './modules/calendar.js'
import design from './modules/design.js'
import groups from './modules/groups.js'
// import example from './module-example'

Vue.use(Vuex)

export default function (/* { ssrContext } */) {
  const Store = new Vuex.Store({
    modules: {
      accounts,
      calendar,
      design,
      groups
      // example
    },

    // enable strict mode (adds overhead!)
    // for dev mode only
    strict: process.env.DEBUGGING
  })

  return Store
}
