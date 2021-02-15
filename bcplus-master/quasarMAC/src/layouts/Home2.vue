<template>
  <div id="SA" class="q-app">
    <q-layout view="lHh Lpr lff" container style="height: 100vh" class="shadow-2 rounded-borders">

      <q-drawer
        v-model="drawer"
        show-if-above
        :width="200"
      >
        <q-scroll-area style="height: calc(100% - 150px); margin-top: 150px; border-right: 1px solid #ddd">
          <q-list padding>

            <q-card class="bg-transparent" style="width: 290px; box-shadow: none;">
              <!-- <date-pick 
                v-model="date"
                class="customDatepicker"
                :hasInputElement="false"
                :inputAttributes="{size: 2}"
              ></date-pick> -->
              <!-- <b-calendar 
                v-model="date" 
                @context="onContext" 
                locale="en-US"
                selected-variant="success"
                today-variant="info"
                nav-button-variant="primary"
                :hide-header="true"
                width="50px"
              ></b-calendar> -->
            </q-card>

            <q-item clickable :active="CalendarPage" v-ripple @click="ActiveCalendar">
              <q-item-section avatar>
                <q-icon :name="mdiCalendar" />
              </q-item-section>

              <q-item-section>
                Calendar
              </q-item-section>
            </q-item>

            <q-item :active="KanbanPage" clickable v-ripple @click="ActiveKanban">
              <q-item-section avatar>
                <q-icon :name="mdiTrello" />
              </q-item-section>

              <q-item-section>
                Kanban
              </q-item-section>
            </q-item>

            <!-- <q-item clickable v-ripple>
              <q-item-section avatar>
                <q-icon :name="mdiGoogleAnalytics" />
              </q-item-section>

              <q-item-section>
                Analytics
              </q-item-section>
            </q-item> -->

            <q-item clickable v-ripple @click="ActiveGroup">
              <q-item-section avatar>
                <q-icon :name="mdiAccountMultiple" />
              </q-item-section>

              <q-item-section>
                Groups
              </q-item-section>
            </q-item>

            <q-item clickable v-ripple @click="ActiveTask">
              <q-item-section avatar>
                <q-icon :name="mdiClipboardListOutline" />
              </q-item-section>

              <q-item-section>
                Tasks
              </q-item-section>
            </q-item>

            <q-item clickable v-ripple @click="ActiveMemo">
              <q-item-section avatar>
                <q-icon :name="fasStickyNote" />
              </q-item-section>

              <q-item-section>
                Memos
              </q-item-section>
            </q-item>

            <q-item clickable v-ripple @click="open('left')">
              <q-item-section avatar>
                <q-icon :name="mdiCalendarSearch"/>
              </q-item-section>

              <q-item-section>
                Datepicker
              </q-item-section>
            </q-item>

            <q-dialog v-model="dialog" :position="position">
              <q-card class="bg-transparent" style="width: 290px; box-shadow: none;">
                <date-pick 
                  v-model="datepicker"
                  class="customDatepicker"
                  :hasInputElement="false"
                ></date-pick>
              </q-card>
            </q-dialog>

            <q-item clickable v-ripple @click="gotoMypage">
              <q-item-section avatar>
                <q-icon :name="mdiCog" />
              </q-item-section>

              <q-item-section>
                Settings
              </q-item-section>
            </q-item>

            <q-item clickable v-ripple @click="gotoLogout">
              <q-item-section avatar>
                <q-icon :name="mdiLogout" />
              </q-item-section>

              <q-item-section>
                Logout
              </q-item-section>
            </q-item>
          </q-list>
        </q-scroll-area>

        <q-img class="absolute-top header-color" style="height: 150px">
          <div class="absolute-bottom bg-transparent">
            <q-avatar size="56px" class="q-mb-sm">
              <Gravatar :email="userId" class="bg-transparent" />
            </q-avatar>
            <div class="text-weight-bold">{{ this.userId }}</div>
            <div>{{ this.userName }}</div>
          </div>
        </q-img>
      </q-drawer>
      
      <q-drawer
        side="right"
        v-model="DrawerPage"
        bordered
        :width="280"
        :breakpoint="1250"
        background="#131E2E"
      >
        <!-- <div>hello world</div> -->
        <RightDrawer 
          @changePeople="changePeople"
          v-show="GroupPage"
        />

        <LeftDrawer 
          @changePeople="changePeople"
          v-show="TaskPage"
        />

        <Memo 
          v-show="MemoPage"
        />
      </q-drawer>

      <!-- <q-drawer
        side="right"
        v-model="TaskPage"
        bordered
        :width="250"
        :breakpoint="1250"
        background="#131E2E"
      >
        <LeftDrawer 
          @changePeople="changePeople"
        />
      </q-drawer> -->

      <!-- <q-page-container> -->
        <!-- <q-page padding> -->
          <Calendar 
            v-if="CalendarPage"
            :openDrawer="openDrawer"
            :people="people"
            :datepicker="datepicker"
          />
      <q-page-container v-if="KanbanPage">
        <q-page>
          <Kanban 
          />
        </q-page>
      </q-page-container>

          <Analytics 
            v-if="CalendarPage"
          />
        <!-- </q-page> -->
      <!-- </q-page-container> -->
    </q-layout>
  </div>
</template>

<script>
import { mdiCalendar, mdiAccountMultiple, mdiGoogleAnalytics, mdiCog, mdiTrello, mdiClipboardListOutline, mdiCalendarSearch, mdiLogout } from '@quasar/extras/mdi-v5'
import { fasStickyNote } from '@quasar/extras/fontawesome-v5'

import Gravatar from 'vue-gravatar'
import Calendar from './Calendar/Calendar.vue'
import Kanban from './Calendar/Kanban.vue'
import RightDrawer from './Layout/RightDrawer.vue'
import LeftDrawer from './Layout/LeftDrawer.vue'
import MypageModal from '../components/Accounts/MypageModal.vue'
import Memo from '../components/Drawers/Memo.vue'

import { QDate } from 'quasar'
import { mapActions, mapMutations } from 'vuex'

import DatePick from 'vue-date-pick'
import 'vue-date-pick/dist/vueDatePick.css'

export default {
  data () {
    return {
      drawer: false,
      userId: null,
      userName: null,
      CalendarPage: true,
      KanbanPage: false,
      DrawerPage: false,
      GroupPage: false,
      MemoPage: false,
      openDrawer: false,
      people: null,
      datepicker: this.$moment(new Date()).format('YYYY-MM-DD'),
      // date: this.$moment(new Date()).format('YYYY-MM-DD'),
      dialog: false,
    }
  },
  components: {
    Gravatar,
    Calendar,
    Kanban,
    RightDrawer,
    LeftDrawer,
    DatePick,
    MypageModal,
    Memo
  },
  methods: {
    ...mapActions('accounts', ['Logout']),

    gotoLogout() {
      this.Logout({
        token: sessionStorage.getItem("access-token"),
        userId: sessionStorage.getItem("username")
      })
      .then(() => {
        // this.$q.dark.set(false)
        this.$router.push("/")
      })
    },

    changePeople(changePeopleVal) {
      // console.log('changepeople', changePeopleVal)
      this.people = changePeopleVal
    },
    ActiveCalendar() {
      this.CalendarPage = true
      this.KanbanPage = false
    },
    ActiveKanban() {
      this.KanbanPage = true
      this.CalendarPage = false
    },
    ActiveGroup() {
      if (this.TaskPage) {
        this.TaskPage = false
        this.GroupPage = true
      } else if (this.MemoPage){
        this.MemoPage = false
        this.GroupPage = true
      } else {
        this.DrawerPage = !this.DrawerPage
        this.GroupPage = !this.GroupPage
      }
      this.openDrawer = !this.openDrawer      
    },
    ActiveTask() {
      if (this.GroupPage) {
        this.TaskPage = true
        this.GroupPage = false
      } else if (this.MemoPage) {
        this.MemoPage = false
        this.TaskPage = true
      } else {
        this.DrawerPage = !this.DrawerPage
        this.TaskPage = !this.TaskPage
      }
      // this.DrawerPage = !this.DrawerPage
      // this.TaskPage = !this.TaskPage
      this.openDrawer = !this.openDrawer      
    },
    ActiveMemo() {
      if (this.GroupPage) {
        this.MemoPage = true
        this.GroupPage = false
      } else if (this.TaskPage) {
        this.MemoPage = true
        this.TaskPage = false
      } else {
        this.DrawerPage = !this.DrawerPage
        this.MemoPage = !this.MemoPage
      }
      this.openDrawer = !this.openDrawer
    },
    gotoDate(date) {
      this.datepicker = this.$moment(date).format('YYYY-MM-DD')
      // this.$moment(date).format('YYYY-MM-DD')
    },
    changePeople(changePeopleVal) {      
      this.people = changePeopleVal
    },
    open (position) {
      this.position = position
      this.dialog = true
      // if(this.$q.screen.lt.sm || this.$q.screen.lt.md) {
      //   this.$emit('closeLeftDrawer')
      // }
    },
    gotoMypage() {
      this.$modal.show(MypageModal,
      { userId: this.userId,
        ggl: sessionStorage.getItem("isGoogle"),
        modal: this.$modal},
        {height: 'auto'})
    }
  },
  watch: {
    date() {
      // console.log(this.date)
      // this.$emit('gotoDate', this.date)
    }
  },
  created() {
    this.mdiCalendar = mdiCalendar
    this.mdiGoogleAnalytics = mdiGoogleAnalytics
    this.mdiAccountMultiple = mdiAccountMultiple
    this.mdiCog = mdiCog
    this.mdiTrello = mdiTrello 
    this.mdiClipboardListOutline = mdiClipboardListOutline
    this.fasStickyNote = fasStickyNote
    this.mdiCalendarSearch = mdiCalendarSearch
    this.mdiLogout = mdiLogout

    this.token=sessionStorage.getItem("access-token")
    this.userId=sessionStorage.getItem("username")
    this.userName=sessionStorage.getItem("name")
  }
}
</script>

<style lang="scss">
.q-item--clickable {
    margin-left: 0 !important;
}

.header-color {
  // background: linear-gradient( to right, #503fff,  #94f6d2 );
  background: linear-gradient( to top, #4032CC,  #85DDBD );
  // #76C4A8
  // #4032CC
  // filter: brightness(80%);
}
</style>