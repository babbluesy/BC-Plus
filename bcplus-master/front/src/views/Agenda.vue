<template>
  <v-app id="inspire">
    <!-- Right Drawer: Calendar List -->

    <!-- Right Drawer: START -->
    <v-navigation-drawer
      v-model="drawerRight"
      app
      clipped
      right
      id="SA"
      class="py-5"
      :class="{ 'dark': goDark }"
      color="#FBFCFE"
    >
      <v-list dense v-if="!chat">
        <v-list-item>
          <div class="mx-auto">
            <h4>Group List</h4>
          </div>
        </v-list-item>
        <v-list-item>
          <small class="mx-auto text-secondary">Select a group for schedule you want</small>
        </v-list-item>
            
            <!-- 그룹 추가 버튼 -->
        <v-tooltip left>
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              class="mb-10 mr-2"
              fab
              small
              absolute
              bottom
              right
              icon
              @click="addGroupModal"
              v-bind="attrs"
              v-on="on"
            >
              <v-icon medium>mdi-plus-thick</v-icon>
            </v-btn>
          </template>
          <span id="SA">New Group</span>
        </v-tooltip>

        <div v-for="group in group_name" :key="group.id" class="ml-5">
        <!-- 그룹 선택 스위치 시작 -->
          <div class="d-flex">
            <v-list-item-action class="d-flex">
              <v-container fluid>
                <v-switch
                  inset
                  v-model="people"
                  :label="group"
                  :value="group"
                ></v-switch>
              </v-container>
            </v-list-item-action>
        <!-- 그룹 선택 스위치 끝 -->

            <!-- 그룹 정보 시작 -->
            <!-- 링크 활성화 -->
            <v-list-item-action
              v-if="people==group"
              class="d-flex mr-5 my-0"
              style="margin-right: 0px;"
            >
              <div class="d-flex">
                <v-tooltip bottom>
                  <template v-slot:activator="{ on, attrs }">
                    <v-btn
                      v-if="people==group" icon
                      v-bind="attrs"
                      v-on="on"
                      @click="startChat(group)"
                    >
                      <v-icon>mdi-chat-processing-outline</v-icon>
                    </v-btn>
                  </template>
                  <span id="SA">Group Chat</span>
                </v-tooltip>

                <v-tooltip bottom>
                  <template v-slot:activator="{ on, attrs }">
                    <v-btn
                      v-if="people==group" icon
                      v-bind="attrs"
                      v-on="on"
                      @click="GroupInfoModal"
                    >
                      <v-icon>mdi-calendar-check</v-icon>
                    </v-btn>
                  </template>
                  <span id="SA">Group Info</span>
                </v-tooltip>

              </div>
            </v-list-item-action>
          </div>
              <!-- 그룹 정보 끝 -->
        </div>

      </v-list>

      <v-list dense v-else style="height: 100%;">
        <ChatRoom 
          :chatgroup="chatgroup"
          :groups="GROUPS"
          :people="people"
          :chat="chat"             
          @gotoList="gotoList"
          :goDark="goDark"
        />
      </v-list>
    </v-navigation-drawer>
      <!-- Right Drawer: END -->

    <v-app-bar
      app
      clipped-right
      clipped-left
      dark
      src="https://i.ibb.co/ydzrzGz/banner-bg-1-1.jpg"
    >
      <v-tooltip bottom>
        <template v-slot:activator="{ on, attrs }">
          <v-app-bar-nav-icon
            v-bind="attrs"
            v-on="on"
            @click.stop="drawer = !drawer"
            class="mr-5"
          ></v-app-bar-nav-icon>
        </template>
        <span id="SA">ToDo List</span>
      </v-tooltip>
        
      <v-toolbar-title id="PG" class="pr-5 d-flex align-items-center">
        <h2><span class="text-danger font-weight-bold mr-2">BC+</span>Calendar</h2><v-icon small class="pb-4 ml-2">mdi-calendar-month</v-icon>
      </v-toolbar-title>

      <!-- Dark Theme Switch -->
      <div id="SA">
        <v-switch :label="`Dark Theme`" v-model="goDark" class="darkswitch"></v-switch>
      </div>

      <v-tooltip right>
        <template v-slot:activator="{ on, attrs }">
          <v-btn
            v-bind="attrs"
            v-on="on"
            icon
            small
            @click="Tutorial"
          >
            <v-icon small>mdi-help</v-icon>
          </v-btn>
        </template>
        <span id=SA>How to use?</span>
      </v-tooltip>

      <div
        class="dropdown p-2 mr-5 d-flex justify-content-center" 
        style="width: 200px; position: absolute; right: 0;"
        id="SA"
      >
        <div class="d-flex" type="button" id="dropdownMenuButton" data-toggle="dropdown">
          <v-list-item-avatar>
            <Gravatar :email="userId" />
          </v-list-item-avatar>

          <v-list-item-content>
            <v-list-item-title id="NS" class="h5 text-white mr-10 my-0" style="max-width: 120px; display: block; overflow: hidden; text-overflow:ellipsis;">{{ this.userName }}</v-list-item-title>
          </v-list-item-content>
        </div>

        <!-- Account Drop Down: START -->
        <!-- Dropdown Menu: Dark Theme -->
        <div v-if="goDark" class="dropdown-menu rounded" aria-labelledby="dropdownMenuButton" style="background-color: #131E2E; border: solid white 1px;">
          <button class="btn dropdown-item text-primary" @click="MypageModal">My Page</button>
          <button class="btn dropdown-item text-danger" @click="logout">Logout</button>
        </div>
        <!-- Dropdown Menu: Light Theme -->
        <div v-else class="dropdown-menu rounded" aria-labelledby="dropdownMenuButton" style="border: solid 1px">
          <button class="btn dropdown-item text-primary" @click="MypageModal">My Page</button>
          <button class="btn dropdown-item text-danger" @click="logout">Logout</button>
        </div>
          <!-- Account Drop Down: END -->
      </div>

      <v-spacer></v-spacer>

      <div class="d-flex justify-content-end">
        <v-tooltip bottom>
          <template v-slot:activator="{ on, attrs }">
            <v-app-bar-nav-icon
              v-bind="attrs"
              v-on="on"
              @click.stop="drawerRight = !drawerRight"
            ></v-app-bar-nav-icon>
          </template>
          <span id=SA>Group List</span>
        </v-tooltip>
      </div>
    </v-app-bar>
    
    <!-- left drawer: Tasks -->
    <!-- Left Drawer: START -->
    <v-navigation-drawer
      v-model="drawer"
      app
      clipped
      left
      class="p-3"
      :class="{ 'dark': goDark }"
      id="SA"
      width="300px"
      color="#FBFCFE"
    >
      <TodoList 
        :member_info="member_info"
        :people="people"
      />
    </v-navigation-drawer>
    <!-- Left Drawer: END -->

    <v-navigation-drawer
      v-model="left"
      fixed
      temporary
    ></v-navigation-drawer>
    
    <v-main>
      <v-container
        class="fill-height p-0"
        fluid
      >
        <v-row
          align="center"
          justify="center"
        >
          <v-col class="text-center p-0">

        <!-- calendar start -->
        <!-- <v-sheet height="600"> -->
            <div id='SA' v-if="switching" class='demo-app'>
              <div class='demo-app-main'>
                <FullCalendar
                  ref="fullCalendar"
                  class='demo-app-calendar'
                  :options='config'
                ></FullCalendar>
              </div>
            </div>

            <div id='SA' v-if="!switching" class='demo-app mr-4' style="display: block;">
              <div style="height: 30px;">
                <button class="ml-4" style="height: 30px;" @click="switchingcal"><v-icon large>mdi-calendar</v-icon></button>
              </div>
              <div id="SA" style="width: 100%;">
                <div class="container mt-3">
                  <div class="row">
                  </div>
                  <div class="row ml-3">
                    <div class="col-md-4">
                      <div class="p-2 alert alert-secondary" style="opacity: 0.9;">
                        <h3>Todo</h3>
                        <hr>
                        <draggable @change="backlogdnd" class="list-group kanban-column" :list="this.TASKS" group="tasks">
                          <div class="list-group-item d-flex p-0 b-0 m-0" v-for="element in this.TASKS" :key="element.id">
                            <div class="d-flex" v-if="element.status=='backlog'">
                              <v-card class="d-flex justify-content-end mr-2" :color="element.color" style="width: 5px; opacity: 0.7;"></v-card>
                              <div class="p-2 mt-1">
                                <h5>{{ element.title }}</h5>
                                <p class="m-0" v-if="element.group!=null">{{ element.group }}</p>
                                <p class="m-0" v-if="element.group==null">My Schedule</p>
                                <p class="mb-1">{{ $moment(element.start).format('YYYY-MM-DD') }} - {{ $moment(element.end).add(-1, 'days').format('YYYY-MM-DD') }}</p>
                                <h6>{{ element.description }}</h6>
                                <div class="d-flex" style="flex-wrap: wrap;">
                                  <div v-for="groupMem in element.members" :key="groupMem.id" class="mr-1">
                                    <v-tooltip bottom>
                                      <template v-slot:activator="{ on, attrs }">
                                        <div v-bind="attrs" v-on="on"><Gravatar :email="groupMem" :size="20" /></div>
                                      </template>
                                      <span>{{ groupMem }}</span>
                                    </v-tooltip>
                                  </div>
                                </div>
                              </div>
                            </div> 
                          </div>
                        </draggable>
                      </div>
                    </div>

                    <div class="col-md-4">
                      <div class="p-2 alert alert-primary" style="opacity: 0.9;">
                        <h3>In Progress</h3>
                        <hr>
                        <draggable @change="inprogressdnd" class="list-group kanban-column" :list="this.TASKS" group="tasks">
                          <div class="list-group-item d-flex p-0 b-0 m-0" v-for="element in this.TASKS" :key="element.id" style="color: #383d41;">
                            <div class="d-flex" v-if="element.status=='inprogress'">  
                              <v-card class="d-flex justify-content-end mr-2" :color="element.color" style="width: 5px; opacity: 0.7;"></v-card>
                              <div class="p-2 mt-1">
                                <h5>{{ element.title }}</h5>
                                <p class="m-0" v-if="element.group!=null">{{ element.group }}</p>
                                <p class="m-0" v-if="element.group==null">My Schedule</p>
                                <p class="mb-1">{{ $moment(element.start).format('YYYY-MM-DD') }} - {{ $moment(element.end).add(-1, 'days').format('YYYY-MM-DD') }}</p>
                                <h6>{{ element.description }}</h6>
                                <div class="d-flex" style="flex-wrap: wrap;">
                                  <div v-for="groupMem in element.members" :key="groupMem.id" class="mr-1">
                                    <v-tooltip bottom>
                                      <template v-slot:activator="{ on, attrs }">
                                          <div v-bind="attrs" v-on="on"><Gravatar :email="groupMem" :size="20" /></div>
                                      </template>
                                      <span>{{ groupMem }}</span>
                                    </v-tooltip>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </div>
                        </draggable>
                      </div>
                    </div>

                    <div class="col-md-4">
                      <div class="p-2 alert alert-success" style="opacity: 0.9;">
                        <h3>Done</h3>
                        <hr>
                        <draggable @change="donednd" class="list-group kanban-column" :list="this.TASKS" group="tasks">
                          <div class="list-group-item d-flex p-0 b-0 m-0" v-for="element in this.TASKS" :key="element.id" style="text-decoration: line-through; color:gray;">
                            <div class="d-flex" v-if="element.status=='done'">  
                              <v-card class="d-flex justify-content-end mr-2" :color="element.color" style="width: 5px; opacity: 0.7;"></v-card>
                                <div class="p-2 mt-1">
                                  <h5>{{ element.title }}</h5>
                                  <p class="m-0" v-if="element.group!=null">{{ element.group }}</p>
                                  <p class="m-0" v-if="element.group==null">My Schedule</p>
                                  <p class="mb-1">{{ $moment(element.start).format('YYYY-MM-DD') }} - {{ $moment(element.end).add(-1, 'days').format('YYYY-MM-DD') }}</p>
                                  <h6>{{ element.description }}</h6>
                                  <div class="d-flex" style="flex-wrap: wrap;">
                                    <div v-for="groupMem in element.members" :key="groupMem.id" class="mr-1">
                                      <v-tooltip bottom>
                                        <template v-slot:activator="{ on, attrs }">
                                          <div v-bind="attrs" v-on="on"><Gravatar :email="groupMem" :size="20" /></div>
                                        </template>
                                        <span>{{ groupMem }}</span>
                                      </v-tooltip>
                                    </div>
                                  </div>
                                </div>
                              </div>        
                            </div>
                          </draggable>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
                
              <!-- calendar end -->
              
            </v-col>
          </v-row>
        </v-container>
      </v-main>

      <v-navigation-drawer
        v-model="right"
        fixed
        right
        temporary
      ></v-navigation-drawer>

    <v-footer
      padless
    >
      <v-card
        class="flex"
        :class="{ 'dark': goDark }"
        flat
        tile
        color="#FBFCFE"
      >
        <v-card-text class="py-2 text-center">
          <strong>ⓒ{{ new Date().getFullYear() }} BC+ All Rights Reserved.</strong>
        </v-card-text>
      </v-card>
    </v-footer>
  </v-app>
</template>

<script>
import FullCalendar from '@fullcalendar/vue'
import dayGridPlugin from '@fullcalendar/daygrid'
import timeGridPlugin from '@fullcalendar/timegrid'
import interactionPlugin from '@fullcalendar/interaction'
import googleCalendarPlugin from '@fullcalendar/google-calendar';

import EventDetailModal from '@/components/Event/EventDetailModal'
import NewEventModal from '@/components/Event/NewEventModal'
import AddGroupModal from '@/components/Group/AddGroupModal.vue'
import GroupInfoModal from '@/components/Group/GroupInfoModal.vue'
import MypageModal from '@/components/Account/MypageModal.vue'
import Tutorial from '@/components/Others/Tutorial.vue'
import ChatRoom from '@/components/Group/ChatRoom.vue'
import TodoList from '@/components/Drawer/TodoList.vue'

import swal from 'sweetalert'
import draggable from 'vuedraggable'
import { BPopover } from 'bootstrap-vue'
import Gravatar from 'vue-gravatar'
import $ from 'jquery'
import axios from 'axios'
import qs from "qs";
let url=require('url')
import { mapGetters } from 'vuex'

let https = require('https')
const instance = axios.create({
  baseURL: 'https://bcplus.me/api',
  timeout: 120000,
  httpsAgent: new https.Agent({
    rejectUnauthorized: false
  })
});
const CLIENT_ID = "367773692104-ldevrvvo0gghju43m8cc1m6as95nkenn.apps.googleusercontent.com";
const AUTHORIZE_URI = "https://accounts.google.com/o/oauth2/v2/auth";

const queryStr = qs.stringify({
  client_id: CLIENT_ID,
  redirect_uri: "http://localhost:8082",
  response_type: "code",
  scope: "profile email https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/calendar",
  
});
const loginUrl = AUTHORIZE_URI + "?" + queryStr;
let queryData = url.parse(window.location.href, true).query;
console.log(queryData)
let auth_code=queryData.code;
console.log(auth_code)
export default {
  name: "Agenda",
  components: {
    FullCalendar,        
    Gravatar,
    draggable,
    ChatRoom,
    TodoList
  },
  props: {
    source: String,
  },  
  computed: {
    ...mapGetters(["EVENTS", "GROUPS", "USER", "group_name", "LUN_DAYS", "TASKS", "MEMBER_INFO","CHATTINGS","MEMOS" ]),

    matchingScheduleGroup() {
      let scheduleGroup
      for(var i in this.EVENTS) {
        for(var j in this.TASKS) {
          if(this.EVENTS[i].sid == this.TASKS[j].sid) {
            scheduleGroup = this.EVENTS[i].group
          }
        }
      }
      return scheduleGroup
    },

    changeFilter() {              
      return this.EVENTS.filter((event) => {            
        if ( event.group == this.people || event.color == "transparent"){    
          return event
        } else if (this.people == null ){
          return this.EVENTS
        }
      })
    },

    config() {
      return {
        ...this.configOptions,
        ...this.eventHandlers,
      }
    },

    configOptions () {
      return {
        editable: true,
        selectable: true,
        dayMaxEvents: 3,
        events: this.changeFilter,
        weekends: true,
        plugins: [timeGridPlugin, dayGridPlugin, timeGridPlugin, interactionPlugin, googleCalendarPlugin],
        headerToolbar: {
          left: 'prev,next today',
          center: 'title',
          right: 'dayGridMonth timeGridWeek timeGridDay GoogleBtn btn2',
        },
        initialView: 'dayGridMonth',
        allDaySlot: true,
        eventDrop: this.reSchedule,
        eventResize: this.resizeEvent,
        select: this.handleSelect,
        eventClick: this.handleEventClick,
        displayEventTime: true,
        googleCalendarApiKey : "AIzaSyDcnW6WejpTOCffshGDDb4neIrXVUA1EAE",
        customButtons: { 
          GoogleBtn: {
            text: '',
            click: ()=> {                  
            this.googleCal()
            },
          },
          btn2: {
            text: '',
            click: () => {
              this.switching = !this.switching
            },
          },
          prev: { // this overrides the prev button
            text: "PREV",
            click: () => {
              let d=new Date();            
              let nowYear=d.getFullYear();
              let nowMonth=d.getMonth();            
              let str=$( 'h2.fc-toolbar-title' ).text()
              let year=str.substring(str.length-4,str.length)
              let mon=str.substring(0,3)
              // console.log(nowYear+" "+nowMonth+" "+year+" "+this.month.indexOf(mon))
              let different=this.$moment([year,this.month.indexOf(mon),1]).diff(this.$moment([nowYear, nowMonth, 1]), 'months', true)                
              this.$store.commit("monthInit",{different:different})
              if(this.people===null){                 
                this.$store.dispatch("updateMySchedule", {people: sessionStorage.getItem("username")})
              }else{
                this.$store.dispatch("updateSchedule", {people: this.people}).then(()=>{
                  this.member_info=this.MEMBER_INFO;         
                });
              }    
              let calendarApi = this.$refs.fullCalendar.getApi();
              calendarApi.prev();
              this.$store.dispatch("getLunInfo").then(()=>{
                this.LUN_DAYS.forEach(el=>{
                  $('.fc-daygrid-day[data-date="'+el.solDay+'"]').children('.fc-daygrid-day-frame').children('.fc-daygrid-day-top').children('.holiday-text').remove()
                  $('.fc-daygrid-day[data-date="'+el.solDay+'"]').children('.fc-daygrid-day-frame').children('.fc-daygrid-day-top').prepend("<div class='holiday-text'>"+el.lunDay+"</ div>");
                })
              })
              this.$store.dispatch("getHolidayInfo")
              this.changeNumColor();   
            }
          },
          next: { // this overrides the next button
            text: "next",
            click: () => {
              let d=new Date();            
              let nowYear=d.getFullYear();
              let nowMonth=d.getMonth();            
              let str=$( 'h2.fc-toolbar-title' ).text()
              let year=str.substring(str.length-4,str.length)
              let mon=str.substring(0,3)
              console.log(nowYear+" "+nowMonth+" "+year+" "+this.month.indexOf(mon))
              let different=this.$moment([year,this.month.indexOf(mon),1]).diff(this.$moment([nowYear, nowMonth, 1]), 'months', true)                
              this.$store.commit("monthInit",{different:different})
              if(this.people===null){                 
                this.$store.dispatch("updateMySchedule", {people: sessionStorage.getItem("username")})
              }else{
                this.$store.dispatch("updateSchedule", {people: this.people}).then(()=>{
                  this.member_info=this.MEMBER_INFO;         
                });
              }    
              let calendarApi = this.$refs.fullCalendar.getApi();
              calendarApi.next();
              this.$store.dispatch("getLunInfo").then(()=>{
                this.LUN_DAYS.forEach(el=>{
                  $('.fc-daygrid-day[data-date="'+el.solDay+'"]').children('.fc-daygrid-day-frame').children('.fc-daygrid-day-top').children('.holiday-text').remove()
                  $('.fc-daygrid-day[data-date="'+el.solDay+'"]').children('.fc-daygrid-day-frame').children('.fc-daygrid-day-top').prepend("<div class='holiday-text'>"+el.lunDay+"</ div>");
                })
              })
              this.changeNumColor();
              this.$store.dispatch("getHolidayInfo")   
            }
          },
          today: { // this overrides the next button
            text: "Today",
            click: () => {
              this.$store.commit("monthInit",{different:0})
              if(this.people===null){                   
                this.$store.dispatch("updateMySchedule", {people: sessionStorage.getItem("username")})
              }else{
                this.$store.dispatch("updateSchedule", {people: this.people}).then(()=>{
                  this.member_info=this.MEMBER_INFO;         
                });
              }    
              let calendarApi = this.$refs.fullCalendar.getApi();
              calendarApi.today();
              this.$store.dispatch("getLunInfo").then(()=>{
                this.LUN_DAYS.forEach(el=>{
                  $('.fc-daygrid-day[data-date="'+el.solDay+'"]').children('.fc-daygrid-day-frame').children('.fc-daygrid-day-top').children('.holiday-text').remove()
                  $('.fc-daygrid-day[data-date="'+el.solDay+'"]').children('.fc-daygrid-day-frame').children('.fc-daygrid-day-top').prepend("<div class='holiday-text'>"+el.lunDay+"</ div>");
                })
              })
              this.changeNumColor(); 
              this.$store.dispatch("getHolidayInfo")  
            }
          }
          }
        }
      },
      eventHandlers() {
      return {
        eventDidMount: this.renderEvent,
      }
    },
  },

    data() {       
      return {
        month:['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'],
        chat: false,
        chatgroup: null,
        newTask: "",
        arrBacklog: [],
        arrInProgress:[],
        arrDone: [],
        switching: true,
        absolute: true,
        overlay: false,
        detailgroup: '',
        master: '',
        calendargroup: null,
        people: null,
        groups: this.GROUPS,
        selectgroup: null,
        show: false,
        focus: '',
        selectedEvent: {},
        selectedElement: null,
        selectedOpen: false,
        goDark: false,
        colors: ['#1976d2', '#3F51B5', '#673AB7', '#00BCD4', '#4CAF50', '#FF9800', '#F44336'],
        names: ['Meeting', 'Holiday', 'PTO', 'Travel', 'Event', 'Conference', 'Party'],
        dialog1: false,
        TaskDialog: false,
        drawerRight: null,
        drawer: null,
        right: false,
        left: false,
        friends: [],
        member_info:this.MEMBER_INFO,
        sid:'',
        title: '',
        value: '',
        miniVariant: false,
        name: null,
        details: null,
        start: null,
        end: null,
        color: null,
        token: null,
        userId: null,
        lun_days:this.LUN_DAYS,
        userName: this.USERNAME,
      }
    },

    watch: {
      switching() {
        this.switchingKey = 0
      },

      people() {
        this.$store.dispatch("getHolidayInfo")
        if(this.people===null){      
          this.$store.dispatch("updateMySchedule", {people: sessionStorage.getItem("username")})
        }else{
          this.$store.dispatch("updateSchedule", {people: this.people}).then(()=>{
            this.member_info=this.MEMBER_INFO;         
          });
        }

        this.changeNumColor()

        for(var idx in this.GROUPS) {
          if(this.GROUPS[idx].groupName == this.people) {
            if(sessionStorage.getItem("username")==this.GROUPS[idx].groupMaster) {
              this.master = this.GROUPS[idx].groupMaster
              break
            } else {
              this.master = ''
            }
          }
        }
        this.$store.dispatch("getMessage", {groupName:this.people}).then(()=>{console.log("!?!?!?!?!")})     
      },

      goDark() {       
        this.$vuetify.theme.dark = this.goDark
        this.changeNumColor();
      },

      MEMBER_INFO() {
        this.member_info=this.MEMBER_INFO;
      }
    },

    methods: {
      Tutorial() {
        this.$modal.show(Tutorial, {
          modal: this.$modal},{
            width: '1000px',
            height: 'auto',
        })
      },

      googleCal(){
        if (!auth_code) {
          window.location.assign(loginUrl)
        }
        // this.$gAuth.getAuthCode()
        //   .then(authCode => {
        //     return instance.post('/googleCal', { code: authCode, redirect_uri: 'postmessage' }).then(res=>{
        //       this.$store.commit("addGoogle",{googleSch:res.data});
        //       })
        //   })
      },

      gg() {
        let scheduleGroup
        for(var i in this.EVENTS) {
          for(var j in this.TASKS) {
            if(this.EVENTS[i].sid == this.TASKS[j].sid) {
              scheduleGroup = this.EVENTS[i].group
            }
          }
        }
        return scheduleGroup
      },

      changeNumColor() {
        if(this.goDark==true){
          $("td.fc-daygrid-day.fc-day")
            .children(".fc-daygrid-day-frame.fc-scrollgrid-sync-inner")
              .children(".fc-daygrid-day-top")
                .children(".fc-daygrid-day-number").css("color","white")
          $("td.fc-daygrid-day.fc-day.fc-day-sat")
            .children(".fc-daygrid-day-frame.fc-scrollgrid-sync-inner")
              .children(".fc-daygrid-day-top")
                .children(".fc-daygrid-day-number").css("color","#6666ff")
          $("td.fc-daygrid-day.fc-day.fc-day-sun")
            .children(".fc-daygrid-day-frame.fc-scrollgrid-sync-inner")
              .children(".fc-daygrid-day-top")
                .children(".fc-daygrid-day-number").css("color","hotpink")
          $("th.fc-col-header-cell.fc-day.fc-day-sun")
            .children(".fc-scrollgrid-sync-inner")
              .children('.fc-col-header-cell-cushion').css("color","hotpink")
          $("th.fc-col-header-cell.fc-day.fc-day-sat")
            .children(".fc-scrollgrid-sync-inner")
              .children('.fc-col-header-cell-cushion').css("color","#6666ff")
          $("a.fc-daygrid-day-number.koreaholday")
            .attr('style','color: hotpink !important')
          $("div.fc-event-title.fc-sticky.koreaholday")
            .attr('style','color: hotpink !important')
          $(".fa-trello").attr('style', 'color: white !important')

          $(".fc-daygrid-dot-event.status_done").attr('style','text-decoration: line-through white double')
          $("div.fc-event-title.status_done").attr('style','text-decoration: line-through double')

          $('.fc-daygrid-dot-event').children('.fc-event-title').attr('style', 'color: white')
          $('.fc-daygrid-dot-event').children('.fc-event-time').attr('style', 'color: white')  

          $('.fc-button').attr('style', 'background: transparent !important')
          $('.fc-button').attr('style', 'color: white !important')

        } else {
          $("td.fc-daygrid-day.fc-day")
            .children(".fc-daygrid-day-frame.fc-scrollgrid-sync-inner")
              .children(".fc-daygrid-day-top")
                .children(".fc-daygrid-day-number").css("color","black")
          $("td.fc-daygrid-day.fc-day.fc-day-sat")
            .children(".fc-daygrid-day-frame.fc-scrollgrid-sync-inner")
              .children(".fc-daygrid-day-top")
                .children(".fc-daygrid-day-number").css("color","blue")
          $("td.fc-daygrid-day.fc-day.fc-day-sun")
            .children(".fc-daygrid-day-frame.fc-scrollgrid-sync-inner")
              .children(".fc-daygrid-day-top")
                .children(".fc-daygrid-day-number").css("color","red")
          $("th.fc-col-header-cell.fc-day.fc-day-sun")
            .children(".fc-scrollgrid-sync-inner")
              .children('.fc-col-header-cell-cushion').css("color","red")
          $("div.fc-event-title.fc-sticky.koreaholday").attr('style','color: red !important')
          $("th.fc-col-header-cell.fc-day.fc-day-sat")
            .children(".fc-scrollgrid-sync-inner")
              .children('.fc-col-header-cell-cushion').css("color","blue")
          $(".fa-trello").attr('style', 'color: black !important')
          
          $('.fc-daygrid-dot-event').children('.fc-event-title').attr('style', 'color: black')
          $('.fc-daygrid-dot-event').children('.fc-event-time').attr('style', 'color: black')

          $("div.fc-event-time.status_done").attr('style','text-decoration: line-through black double')
          $("div.fc-event-title.status_done").attr('style','text-decoration: line-through black double')

          $('.fc-button').attr('style', 'background: transparent !important')
          $('.fc-button').attr('style', 'color: black !important')
        }
      },

      renderEvent(arg) {
        console.log('renderEvent')
        let titleStr = arg.event.title
        let contentStr = arg.event.extendedProps.description
      
        new BPopover({
          propsData: {
            title: titleStr,
            content: contentStr,
            placement: 'top',
            boundary: 'scrollParent',
            boundaryPadding: 5,
            delay: 500,
            offset: 0,
            triggers: 'hover',
            html: true,
            target: arg.el,    
          },
        }).$mount()

        if (arg.event.extendedProps.group == "koholday"){
          $('.fc-daygrid-day[data-date="'+arg.event.startStr+'"]')
            .children('.fc-daygrid-day-frame')
              .children('.fc-daygrid-day-top')
                .children('.fc-daygrid-day-number')
                  .addClass( 'koreaholday' );
          $('.fc-daygrid-day[data-date="'+arg.event.startStr+'"]')
            .children('.fc-daygrid-day-frame')
              .children('.fc-daygrid-day-events')
                .children('.fc-daygrid-event-harness')
                  .children('.fc-daygrid-event.hol')
                    .children('.fc-event-main')
                      .children('.fc-event-main-frame')
                        .children('.fc-event-title-container')
                          .children('.fc-event-title.fc-sticky')
                            .addClass( 'koreaholday' );        
          if(this.goDark==true){
            $("a.fc-daygrid-day-number.koreaholday")
              .attr('style','color: hotpink !important')
            $("div.fc-event-title.fc-sticky.koreaholday")
              .attr('style','color: hotpink !important')
          }
        }

        if (arg.event.extendedProps.status == "done") {
          arg.el.className+=arg.el.className+" status_done"
          $('.fc-daygrid-day-frame').children('.fc-daygrid-day-events')
            .children('.fc-daygrid-event-harness')
              .children('.fc-daygrid-event.status_done')
                .children('.fc-event-main')
                  .children('.fc-event-main-frame')
                    .children('.fc-event-title-container')
                      .children('.fc-event-title.fc-sticky')
                        .addClass( 'status_done' );  
          
          $('.fc-daygrid-day-frame').children('.fc-daygrid-day-events')
            .children('.fc-daygrid-event-harness')
              .children('.fc-daygrid-event.status_done')
                .children('.fc-event-time').addClass( 'status_done' );
          
          $('.fc-daygrid-day-frame').children('.fc-daygrid-day-events')
            .children('.fc-daygrid-event-harness')
              .children('.fc-daygrid-event.status_done')
                .children('.fc-event-title').addClass( 'status_done' );

          // $('.fc-popover-body').children('.fc-daygrid-event-harness')
          //   .children('.fc-daygrid-event.status_done')
          //     .children('.fc-event-main')
          //       .children('.fc-event-main-frame')
          //         .children('.fc-event-title-container')
          //           .children('.fc-event-title.fc-sticky')
          //             .addClass( 'status_done' );
          // $("div.fc-event-title.fc-sticky.status_done")
          //   .attr('style','text-decoration: line-through black double')
          
          $(".fc-event-time.status_done").attr('style','text-decoration: line-through black double')
          $(".fc-event-title.status_done").attr('style','text-decoration: line-through black double')
          
        }
      },

    handleSelect(arg) {
      console.log(arg.start, arg.end, typeof(arg.start), 'handleselect')
      if (this.userId == this.master) {
        this.$modal.show(NewEventModal, {
          group_name: this.group_name,
          start_cal: arg.start,
          end_cal: arg.end,
          event: arg.event,
          people: this.people,
          dark:this.goDark,
          modal: this.$modal},{
              height: 'auto'
          })
      } else if (this.people == null) {
        this.$modal.show(NewEventModal, {
          group_name: this.group_name,
          start_cal: arg.start,
          end_cal: arg.end,
          event: arg.event,
          people: this.people,
          goDark:this.goDark,
          modal: this.$modal},{
              height: 'auto'
          })
      } else {
        swal('Warning', 'Only the head of the group can register the schedule!', 'warning')
        $('.fc-daygrid-bg-harness').children('.fc-highlight').addClass( 'rmvh' )
        // $('.fc').children('$fc-highlight.rmvh').css("background", 'none')
        $('.fc.fc-highlight').removeClass('rmvh')
        // $('.fc').children('$fc-highlight').css("background", 'none')
        // this.$router().go()
      }
    },

    handleEventClick (arg) {
      console.log('evnt click', arg.event)
      if (arg.event.extendedProps.group != 'koholday' && arg.event.extendedProps.sid != 'google') {
        this.$modal.show(EventDetailModal, {
            groupInfo: this.GROUPS,
            group_name: this.group_name,
            event: arg.event,
            events: this.EVENTS,
            people: this.people,
            master: this.master,
            userId: this.userId,
            modal: this.$modal},{
                height: 'auto',
                width: '344px'
        })
      }
    },

    resizeEvent(arg) {
      this.$store.dispatch("RE_EVENT", {id:arg.event.id,start:arg.event.start,end:arg.event.end,people:this.people, allDay: arg.event.allDay})
    },

    reSchedule(arg) {       
      if (arg.event.end === null) {
        let start_time
        start_time = this.$moment(arg.event.start).format('YYYY-MM-DD HH:mm:ss')
        
        let r_end
        r_end = this.$moment(start_time).add(60, 'minutes')
        this.$store.dispatch("RE_EVENT", {id:arg.event.id,start:arg.event.start,end:r_end.toDate(),people:this.people, allDay: arg.event.allDay})
      } else {   
        this.$store.dispatch("RE_EVENT", {id:arg.event.id,start:arg.event.start,end:arg.event.end,people:this.people, allDay: arg.event.allDay})
      } 
    },

    viewDay({ date }) {
      this.focus = date
      this.type = 'day'
    },

    setToday() {
      this.focus = ''
    },

    addGroupModal() {
      this.$modal.show(AddGroupModal, {
        groups: this.GROUPS,
        group_name_for_modal: this.group_name,
        goDark: this.goDark,
        modal: this.$modal},{
            height: 'auto'
      })
    },

    GroupInfoModal() {
      this.$modal.show(GroupInfoModal, {
        groups: this.GROUPS,
        group_name: this.group_name,
        people: this.people,
        goDark: this.goDark,
        modal: this.$modal},{
            height: 'auto'
      })
    },

    logout() {
      this.$store.dispatch("logout", {
        token: sessionStorage.getItem("access-token"),
        userId: sessionStorage.getItem("username")
      })
      .then(() => this.$router.push("/"))
    },

    MypageModal() {
      this.$modal.show(MypageModal,
      { userId: this.userId,
        ggl: sessionStorage.getItem("isGoogle"),
        modal: this.$modal},
        {height: 'auto'})
    },

    switchingcal() {
      this.switching =! this.switching 
    },

    backlogdnd(evt) {
      if(!(evt.added===undefined)){
        evt.added.element.status = 'backlog'     
        this.$store.dispatch('changestatus',{schedule: evt.added.element,people:this.people});
      }       
    },

    inprogressdnd(evt) {
      if(!(evt.added===undefined)){
        console.log("dawdaw",evt.added)
        evt.added.element.status = 'inprogress'     
        this.$store.dispatch('changestatus',{schedule: evt.added.element,people:this.people});
      }     
    },

    donednd(evt) {
      if(!(evt.added===undefined)){
        evt.added.element.status = 'done'    
        this.$store.dispatch('changestatus',{schedule: evt.added.element,people:this.people});
      }      
    },

    startChat(group) {
      console.log('CHAT!')      
      this.chat = !this.chat
      this.chatgroup = group      
    },

    gotoList() {
      this.chat = !this.chat
    },
    
  },

  created() {
    this.token=sessionStorage.getItem("access-token")
    this.userId=sessionStorage.getItem("username")
    this.userName=sessionStorage.getItem("name")
    this.$store.dispatch("getHolidayInfo")
    this.$store.dispatch("getGroupInfo")       
    this.$store.commit("monthInit",{different:0})
    this.$store.dispatch("updateMySchedule", {people: sessionStorage.getItem("username")})
    if(!auth_code){
            instance.post('/googleCal', { code: auth_code, redirect_uri: 'http://localhost:8082/calendar/' }).then(res=>{
              console.log("url 읽기 성공",res)
              this.$store.commit("addGoogle",{googleSch:res.data});
            })                 
    }    
    this.switchingKey = 0
  },

  mounted() {
    this.member_info=this.MEMBER_INFO;
    this.$store.dispatch("getLunInfo").then(()=>{
      this.LUN_DAYS.forEach(el=>{
        $('.fc-daygrid-day[data-date="'+el.solDay+'"]').children('.fc-daygrid-day-frame').children('.fc-daygrid-day-top').prepend("<div class='holiday-text'>"+el.lunDay+"</ div>");
      })
    })
  },

  updated() {
    this.switchingKey += 1
    if (this.switchingKey == 1) {
      $('.fc-GoogleBtn-button')
        .prepend('<div data-toggle="tooltip" data-placement="top" title="Connect with Google Calendar"><i class="fa fa-google fa-2x"></i></div>')
      $('.fc-btn2-button')
        .prepend('<div data-toggle="tooltip" data-placement="top" title="Go to Kanban"><i class="fa fa-trello fa-2x"></i></div>')
      if (this.goDark==true) {
        $(".fa-trello")
          .attr('style', 'color: white !important')
      } else {
        $(".fa-trello")
          .attr('style', 'color: black !important')
      }

      this.$store.commit("monthInit")

      if(this.people===null) {                   
        this.$store.dispatch("updateMySchedule", {people: sessionStorage.getItem("username")})
      } else {
        this.$store.dispatch("updateSchedule", {people: this.people}).then(()=>{
          this.member_info=this.MEMBER_INFO;         
        })
      }    

      this.$store.dispatch("getLunInfo")
        .then(()=>{
          this.LUN_DAYS.forEach(el=>{
            $('.fc-daygrid-day[data-date="'+el.solDay+'"]').children('.fc-daygrid-day-frame').children('.fc-daygrid-day-top').children('.holiday-text').remove()
            $('.fc-daygrid-day[data-date="'+el.solDay+'"]').children('.fc-daygrid-day-frame').children('.fc-daygrid-day-top').prepend("<div class='holiday-text'>"+el.lunDay+"</ div>");
          })
        })
      
      this.changeNumColor(); 
      this.$store.dispatch("getHolidayInfo") 
    }
  },
}
</script>

<style lang='css' scoped>
.status_done {
  text-decoration: line-through !important;
}
.kanban-column {
  min-height: 450px;
  max-height: 450px;
  overflow: auto;
  text-align: left;
}
h2 {
  margin: 0;
  font-size: 24px;
  text-shadow: 2px 2px 5px black;
}
ul {
  margin: 0;
  padding: 0 0 0 1.5em;
}
li {
  margin: 1.5em 0;
  padding: 0;
}
b { /* used for event dates/times */
  margin-right: 3px;
}
.demo-app {
  display: flex;
  font-size: 14px;
}
.demo-app-sidebar {
  width: 300px;
  line-height: 1.5;
  background: #eaf9ff;
  border-right: 1px solid #d3e2e8;
}
.demo-app-sidebar-section {
  padding: 2em;
}
.demo-app-main {
  flex-grow: 1;
  margin: 1em;
  margin-top: 0;
}
.fc { /* the calendar root */
  margin: 0 auto;
  margin-left: 5px;
}
select.filter {
  width: 500px !important;
}
.v-expansion-panel-header {
  border-radius: inherit;
}
</style>

<style lang="scss">
main.v-main {
  min-height: 100vh;
  height: 100%;
}
label.v-label.theme--light {
  margin-left: 10px !important;
  margin-bottom: 0 !important;
  display: block !important; 
  overflow: hidden; 
  text-overflow:ellipsis;
}
div.v-input__slot {
  margin-bottom: 0;
  margin-top: 15px;
}
label.v-label.theme--dark {  
  margin-left: 10px !important;
  margin-bottom: 0 !important;
  display: block !important; 
  overflow: hidden; 
  text-overflow: ellipsis;
}
button.fc-today-button.fc-button.fc-button-primary{
  padding-bottom: 0px;
  background-color: green;
  width: 60px;
  text-align: center;
  vertical-align: middle;
}
.fc {
  .fc-button-primary {
    background-color: white;
    color: black;
    border: none;
    // width: 40px;
    height: 30px;
    padding-top: 0;
    margin: 3px;
    margin-top: 10px;
    margin-bottom: 10px;
  }
}
.fc {
  .fc-button {
     margin-left: 0px !important;
  }
}
th.fc-col-header-cell.fc-day.fc-day-sat{
  .fc-scrollgrid-sync-inner{
    .fc-col-header-cell-cushion{
      color: blue;
    }
  }
}
th.fc-col-header-cell.fc-day.fc-day-sun{
  .fc-scrollgrid-sync-inner{
    .fc-col-header-cell-cushion{
      color: red;
    }
  }
}
th.fc-col-header-cell.fc-day{
  .fc-scrollgrid-sync-inner{
    .fc-col-header-cell-cushion{
      color: grey;
    }
  }
}
td.fc-daygrid-day.fc-day.fc-day-sun{
  .fc-daygrid-day-frame.fc-scrollgrid-sync-inner{
    .fc-daygrid-day-top{
      .fc-daygrid-day-number{
        color: red;
      };
    }
  }
}
td.fc-daygrid-day.fc-day.fc-day-sat{
  .fc-daygrid-day-frame.fc-scrollgrid-sync-inner{
    .fc-daygrid-day-top{
      .fc-daygrid-day-number{
        color: blue;
      };
    }
  }
}
td.fc-daygrid-day.fc-day{
  .fc-daygrid-day-frame.fc-scrollgrid-sync-inner{
    .fc-daygrid-day-top{
      .fc-daygrid-day-number{
          color: gray;
          
      };
    }
  }
}
td.fc-daygrid-day.fc-day.fc-day-sun{
  .fc-daygrid-day-frame.fc-scrollgrid-sync-inner{
    .fc-daygrid-day-top{
      justify-content: space-between;
      flex-direction: row;
      };
    }
  }
td.fc-daygrid-day{
  .fc-daygrid-day-frame.fc-scrollgrid-sync-inner.birthday{
    .fc-daygrid-day-top{
      justify-content: space-between;
      flex-direction: row;
      };
    }
  }
.holiday-text{
  padding-top: 7px;
  padding-left: 5px;
  font-size: 11px;
}
.fc-daygrid-event{
  opacity: 0.75;
}
.v-expansion-panel{
    opacity: 0.75;
}
.koreaholday {
  color: red !important;
}
div.fc-header-toolbar.fc-toolbar.fc-toolbar-ltr {
  margin-bottom: 0;
}
.theme--dark.v-application {
  background: #0B1D38;
  /* #0B1D38 */
  /* #131E2E */
  /* #0F274A */
}
i.fa.fa-birthday-cake {
  margin-left: 5px;
  margin-top: 5px;
}
.v-application--wrap {
  min-height: auto;
}
.notholday {
  color: white !important;
}
.rmvh {
  background: none !important;
}
.ui-datepicker-trigger{border-top-right-radius: 4px;
  border-bottom-right-radius: 4px;
  background: transparent;
    border: none;
    margin: 0!important;
    padding: 0!important;
    height: 30px!important;}
  .ui-datepicker-trigger .input-group-addon:last-child {
    border-left: 1px solid #ccc;
  border-left: 1px solid #ccc;
    border-top-right-radius: 4px;
    border-bottom-right-radius: 4px;
}
.fa-google {
  background: conic-gradient(from -45deg, #ea4335 110deg, #4285f4 90deg 180deg, #34a853 180deg 270deg, #fbbc05 270deg) 73% 55%/150% 150% no-repeat;
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  -webkit-text-fill-color: transparent;
}
.fc-GoogleBtn-button {
  margin-right: 5px !important;
  margin-bottom: 10px !important;
  border: none !important;
  outline: none !important;
  background-color: transparent !important;
}
.fc-event-title {
  display: block; 
  overflow: hidden; 
  text-overflow:ellipsis;
}
.fc-popover {
  color: black;
}
.fc-popover-body {
  background-color: white;
}
.task_description {
  display: block; 
  overflow: hidden; 
  text-overflow:ellipsis;
}
.task_title {
  display: block; 
  overflow: hidden; 
  text-overflow:ellipsis;
}
.fc-btn2-button {
  padding: 0 !important;
  margin-bottom: 10px !important;
  background-color: transparent !important;
}
.list-group-item {
  border: 0 !important;
}

.fc-daygrid-dot-event .fc-event-title {
  color: black;
}
.fc-daygrid-dot-event .fc-event-time {
  color: black;
}
.dark {
  background-color: #131E2E !important;
}

.fc .fc-view-harness {
  height: 100vh !important;
}

.fc-button {
  background: transparent !important;
  color: black !important;
}
</style>