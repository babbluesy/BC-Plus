<template>
  <div id="SA" class="q-app">
    <q-layout view="hHh LpR lfr" container style="height: 100vh;" class="shadow-2 rounded-borders">

      <q-header class="header-color"> 
        <Toolbar 
          @switchLeftDrawer="switchLeftDrawer"
          @switchRightDrawer="switchRightDrawer"
          @goDarkMode="goDarkMode"
        />
      </q-header>
      
      <q-drawer
        side="left"
        v-model="drawerLeft"
        show-if-above
        bordered
        :width="290"
        :breakpoint="1250"
        background="#131E2E"
      >
        <LeftDrawer 
          :people="people"
          @gotoDate="gotoDate"
          @closeLeftDrawer="closeLeftDrawer"
        />
      </q-drawer>

      <q-drawer
        side="right"
        v-model="drawerRight"
        show-if-above
        bordered
        :width="290"
        :breakpoint="1250"
        background="#131E2E"
      >
        <RightDrawer 
          @changePeople="changePeople"
        />
      </q-drawer>

      <Calendar 
        :people="people"
        :goDark="goDark"
        :datepicker="datepicker"
      />

    </q-layout>
  </div>
</template>
<script>
import Toolbar from './Layout/Toolbar.vue'
import LeftDrawer from './Layout/LeftDrawer.vue'
import RightDrawer from './Layout/RightDrawer.vue'
import Calendar from './Calendar/Calendar.vue'

export default {
	name: 'Home',
	components: {
    Calendar,
    Toolbar,
    LeftDrawer,
    RightDrawer
	},
	data () {
    return {
      drawerLeft: true,
      drawerRight: true,
      people: null,
      goDark: false,
      datepicker: this.$moment(new Date()).format('YYYY-MM-DD')
    }
  },
  methods: {
    changePeople(changePeopleVal) {
      // console.log('changepeople', changePeopleVal)
      this.people = changePeopleVal
    },
    switchLeftDrawer() {
      // console.log('left')
      this.drawerLeft = !this.drawerLeft
    },
    switchRightDrawer() {
      // console.log('right')
      this.drawerRight = !this.drawerRight
    },
    goDarkMode() {
      this.goDark = !this.goDark
    },
    gotoDate(date) {
      this.datepicker = this.$moment(date).format('YYYY-MM-DD')
      // this.$moment(date).format('YYYY-MM-DD')
    },
    closeLeftDrawer() {
      this.drawerLeft = !this.drawerLeft
    }
  },
}
</script>

<style lang="scss">
main.v-main {
  min-height: 100vh;
  height: 100%;
  // width: 100% !important;
}

.header-color {
  // background: linear-gradient( to right, #503fff,  #94f6d2 );
  background: linear-gradient( to right, #4032CC,  #85DDBD );
  // #76C4A8
  // #4032CC
  // filter: brightness(80%);
}
</style>