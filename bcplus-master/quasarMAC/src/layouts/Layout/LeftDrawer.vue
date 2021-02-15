<template>
  <q-scroll-area class="fit">
    <div v-if="!this.switchValue">
      <div class="mx-auto mt-5 pb-2 d-flex justify-content-between align-items-center">
        <div class="d-flex">
          <!-- task title -->
          <div class="ml-4" v-if="this.people!=null">
            <!-- <q-icon class="mr-2" :name="mdiClipboardListOutline" /> -->
            <h4 class="mb-0">ToDo List</h4>
            <small>Selected Group: <span class="font-weight-bold">{{this.people}}</span></small>
          </div>
          <div v-else>
            <h4 id="SA" class="ml-4 mb-0">My ToDo List</h4>
          </div>
        </div>

        <!-- <div class="d-flex">

          <h4 class="mb-0">
            <q-icon
              class="mr-2 icon-btn" 
              :name="mdiCalendar" 
              style="font-size: 1.1em; margin-top: 2px;"
              @click="open('left')"
            />
            <q-tooltip>
              Full Calendar
            </q-tooltip>
          </h4>
          
          <q-dialog v-model="dialog" :position="position">
            <q-card class="bg-transparent" style="width: 290px; box-shadow: none;">
              <date-pick 
                v-model="date"
                class="customDatepicker"
                :hasInputElement="false"
              ></date-pick>
            </q-card>
          </q-dialog>

          <h4 class="mb-0" style="color: #ffc107;">
            <q-icon
              class="mr-4 icon-btn" 
              :name="fasStickyNote" 
              @click="switchMemoAndTodo"
            />
            <q-tooltip>
              My Memo
            </q-tooltip>
          </h4>
        </div> -->
      </div>

      <div>
        <TodoList 
          :people="people"
        />
      </div>
    </div>


    <div v-else>
      <div class="mx-auto mt-5 pb-2 d-flex justify-content-between align-items-center">
        <div class="d-flex">
          <h4 id="SA" class="ml-4 mb-0"><q-icon class="mr-2" :name="fasStickyNote" style="color: #ffc107;" />My Memo</h4>
        </div>

        <div>
          <h4 class="mb-0">
            <q-icon
              class="mr-4 icon-btn" 
              :name="mdiClipboardListOutline" 
              @click="switchMemoAndTodo"
            />
            <q-tooltip>
              Todo List
            </q-tooltip>
          </h4>
        </div>
      </div>

      <div>
        <Memo />
      </div>
    </div>
  </q-scroll-area>
</template>

<script>
import TodoList from '../../components/Drawers/TodoList.vue'
import Memo from '../../components/Drawers/Memo.vue'

import { mapGetters, mapActions } from 'vuex'
import Gravatar from 'vue-gravatar'
import { mdiClipboardListOutline, mdiCrown, mdiCalendar } from '@quasar/extras/mdi-v5'
import { fasStickyNote } from '@quasar/extras/fontawesome-v5'
import { QDate } from 'quasar'

import DatePick from 'vue-date-pick'
import 'vue-date-pick/dist/vueDatePick.css'

export default {
  props: {
    people: String,
    member_info: Array,
    drawerLeft: Boolean,
  },
  components: {       
    Gravatar,
    TodoList,
    Memo,
    DatePick
  },
  data() {
    return {
      // drawerLeft: false,
      dialog: false,
      memo_menu: false,
      text_memo: '',
      friends: [],
      switchValue: false,
      drawerLeftValue: null,
      dialog: false,
      position: 'top',
      date: this.$moment(new Date()).format('YYYY-MM-DD')
      // this.$moment(new Date()).format('YYYY-MM-DD').toString()
    }
  },
  methods: {
    switchMemoAndTodo() {
      this.switchValue = !this.switchValue
    },
    open (position) {
      this.position = position
      this.dialog = true
      if(this.$q.screen.lt.sm || this.$q.screen.lt.md) {
        this.$emit('closeLeftDrawer')
      }
    },
    // gotoDate() {
    //   console.log(this.date)
    //   this.$emit('gotoDate', this.date)
    // }
  },
  watch: {
    // drawerLeft() {
		// 	this.drawerLeftValue = this.drawerLeft
    // }
    date() {
      // console.log(this.date)
      this.$emit('gotoDate', this.date)
    }
  },
  created() {
    this.mdiClipboardListOutline = mdiClipboardListOutline
    this.mdiCrown = mdiCrown
    this.fasStickyNote = fasStickyNote
    this.drawerLeftValue = this.drawerLeft
    this.mdiCalendar = mdiCalendar
  }

}
</script>

<style lang="scss">

</style>