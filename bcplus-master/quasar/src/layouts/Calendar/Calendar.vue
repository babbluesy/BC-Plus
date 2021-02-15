<template>
  <q-page-container>
    <q-page padding full-width v-if="switching">
      <FullCalendar
        ref="fullCalendar"
        class="demo-app-calendar"
        :options='config'
      ></FullCalendar>
    </q-page>
    <q-page v-else>
      <Kanban 
        @switchingcal="switchingcal"
        :people="this.people"
      />
    </q-page>
  </q-page-container>
</template>

<!-- Insert this line above script imports  -->
<script>if (typeof module === 'object') {window.module = module; module = undefined;}</script>

<!-- normal script imports etc  -->
<script src="scripts/jquery.min.js"></script>    
<script src="scripts/vendor.js"></script>    

<!-- Insert this line after script imports -->
<script>if (window.module) module = window.module;</script>
<script>
import FullCalendar from '@fullcalendar/vue'
import dayGridPlugin from '@fullcalendar/daygrid'
import timeGridPlugin from '@fullcalendar/timegrid'
import interactionPlugin from '@fullcalendar/interaction'
import googleCalendarPlugin from '@fullcalendar/google-calendar'

import Kanban from './Kanban.vue'
import NewEventModal from '../../components/Events/NewEventModal.vue'
import EventDetailforMobile from '../../components/Events/EventDetailforMobile.vue'
import EventDetailforDesktop from '../../components/Events/EventDetailforDesktop.vue'


import { BPopover } from 'bootstrap-vue'
if (typeof module === 'object')window.$ = window.jQuery = require('jquery');
import $ from 'jquery'
import axios from 'axios'

import { mapGetters, mapActions, mapMutations } from 'vuex'


//  var ipcRenderer = require('electron').ipcRenderer;

let https = require('https')
const instance = axios.create({
  baseURL: 'https://bcplus.me/api',
  timeout: 120000,
  httpsAgent: new https.Agent({
    rejectUnauthorized: false
  })
});

export default {
  components: {
    FullCalendar,
    Kanban
  },
  props: {
    people: String,
    goDark: Boolean,
    datepicker: String,
  },
  computed: {
    ...mapGetters('calendar', ["EVENTS", "LUN_DAYS", "TASKS"]),
    ...mapGetters('groups', ["GROUPS", "USER", "GROUP_NAME", "MEMBER_INFO", "MEMOS"]),

    changeFilter() {              
      return this.EVENTS.filter((event) => {            
        if ( event.group == this.people || event.color == "transparent"){    
          return event
        } else if (this.people == null ){
          return this.EVENTS
        }
      })
    },

    foundKing() {
      let king
      for(var idx in this.GROUPS) {
        if(this.GROUPS[idx].groupName == this.people) {
          if(sessionStorage.getItem("username")==this.GROUPS[idx].groupMaster) {
            king = this.GROUPS[idx].groupMaster
          }
        }
      }
      return king
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
          left: 'prev',
          center: 'title today dayGridMonth timeGridWeek timeGridDay GoogleBtn kanban',
          right: 'next',
        },
        // titleFormat: { year: 'numeric', month: 'short', day: '2-digit' },
        //  GoogleBtn btn2
        initialView: 'dayGridMonth',
        allDaySlot: true,
        eventDrop: this.reSchedule,
        eventResize: this.resizeEvent,
        select: this.handleSelect,
        eventClick: this.handleEventClick,
        displayEventTime: true,
        googleCalendarApiKey : "AIzaSyDcnW6WejpTOCffshGDDb4neIrXVUA1EAE",
        customButtons: { 
          // GoogleBtn: {
          //   text: '',
          //   click: ()=> {                  
          //     this.googleCal()
          //   },
          // },
          kanban: {
            text: '',
            click: () => {
              this.switching = !this.switching
            },
          },
          prev: { // this overrides the prev button
            text: "PREV",
            click: () => {
              let calendarApi = this.$refs.fullCalendar.getApi()
              calendarApi.prev()
             
              let d = new Date();            
              let nowYear = d.getFullYear()
              let nowMonth = d.getMonth()            
              let str=$( 'h2.fc-toolbar-title' ).text()
              let year=str.substring(str.length-4,str.length)
              let mon=str.substring(0,3)
              let different=this.$moment([year,this.month.indexOf(mon),1]).diff(this.$moment([nowYear, nowMonth, 1]), 'months', true)                
              this.monthInit({different: different})
              if(this.people===null){                 
                this.updateMySchedule({people: sessionStorage.getItem("username"),flag:false})
              } else{
                // this.updateSchedule({people: sessionStorage.getItem("username")}).then(()=>{
                  this.updateSchedule({people: this.people}).then(()=>{
                  this.member_info=this.MEMBER_INFO;         
                });
              }    
              
              // calendarApi.changeView('timeGridWeek')
              this.getLunInfo().then(()=>{
                this.LUN_DAYS.forEach(el=>{
                  $('.fc-daygrid-day[data-date="'+el.solDay+'"]').children('.fc-daygrid-day-frame').children('.fc-daygrid-day-top').children('.holiday-text').remove()
                  $('.fc-daygrid-day[data-date="'+el.solDay+'"]').children('.fc-daygrid-day-frame').children('.fc-daygrid-day-top').prepend("<div class='holiday-text'>"+el.lunDay+"</ div>");
                })
              })
              this.getHolidayInfo()
              
              // this.changeNumColor({goDark: this.goDark})
            }
          },
          next: { // this overrides the next button
            text: "next",
            click: () => {
              let calendarApi = this.$refs.fullCalendar.getApi()
              calendarApi.next();
              let d=new Date();            
              let nowYear=d.getFullYear();
              let nowMonth=d.getMonth();
               
              let str=$( 'h2.fc-toolbar-title' ).text()
              let year=str.substring(str.length-4,str.length)
              let mon=str.substring(0,3)
              
              let different=this.$moment([year,this.month.indexOf(mon),1]).diff(this.$moment([nowYear, nowMonth, 1]), 'months', true)
              this.monthInit({different: different})
              if(this.people===null){                 
                this.updateMySchedule({people: sessionStorage.getItem("username"),flag:false})
              }else{
                  // this.updateSchedule({people: sessionStorage.getItem("username")}).then(()=>{
                  this.updateSchedule({people: this.people}).then(()=>{
                  this.member_info=this.MEMBER_INFO       
                });
              }    
             
              this.getLunInfo().then(()=>{
                this.LUN_DAYS.forEach(el=>{
                  $('.fc-daygrid-day[data-date="'+el.solDay+'"]').children('.fc-daygrid-day-frame').children('.fc-daygrid-day-top').children('.holiday-text').remove()
                  $('.fc-daygrid-day[data-date="'+el.solDay+'"]').children('.fc-daygrid-day-frame').children('.fc-daygrid-day-top').prepend("<div class='holiday-text'>"+el.lunDay+"</ div>");
                })
              })
              this.getHolidayInfo()
             
              // this.changeNumColor({goDark: this.goDark})
            }
          },
          today: { // this overrides the next button
            text: "Today",
            click: () => {
              let calendarApi = this.$refs.fullCalendar.getApi()
              calendarApi.today();
              
              this.monthInit({different:0})
              if(this.people===null){                   
                this.updateMySchedule({people: sessionStorage.getItem("username"),flag:false})
              }else{
                this.updateSchedule({people: this.people}).then(() => {
                  this.member_info=this.MEMBER_INFO    
                });
              }    
                           
              this.getLunInfo().then(() => {
                this.LUN_DAYS.forEach(el=>{
                  $('.fc-daygrid-day[data-date="'+el.solDay+'"]').children('.fc-daygrid-day-frame').children('.fc-daygrid-day-top').children('.holiday-text').remove()
                  $('.fc-daygrid-day[data-date="'+el.solDay+'"]').children('.fc-daygrid-day-frame').children('.fc-daygrid-day-top').prepend("<div class='holiday-text'>"+el.lunDay+"</ div>");
                })
              })
              // this.changeNumColor({goDark: this.goDark})
              this.getHolidayInfo()
              
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
      titlee: '',
      month:['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'],
      newTask: "",
      calWidth: '--webkit',
      switching: true,
      absolute: true,
      overlay: false,
      detailgroup: '',
      master: '',
      // people: null,
      groups: this.GROUPS,
      selectgroup: null,
      show: false,
      focus: '',
      selectedEvent: {},
      selectedElement: null,
      selectedOpen: false,
      // goDark: false,
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

  methods: {
    ...mapActions('calendar', ['RE_EVENT', 'updateMySchedule', 'updateSchedule', 'getLunInfo', 'getHolidayInfo']),
    ...mapMutations('calendar', ['monthInit']),
    ...mapMutations('design', ['changeNumColor', 'removeCustomBtn', 'addHolidayCol', 'addStatusDeco']),
    
    googleCal(){
      this.$gAuth.getAuthCode()
      .then(authCode => {
        return instance.post('/googleCal', {code: authCode, redirect_uri: 'postmessage' }).then(res => {
          this.$store.commit('calendar/addGoogle',{googleSch: res.data}, {root: true})
        })
      })
      // ipcRenderer.on('asynchronous-reply', (event, arg) => {
      //   console.log(arg,"adadwdaw") // "pong"이 출력됩니다.
      //   instance.post('/googleCal', {code: arg, redirect_uri: 'http://localhost' }).then(res => {
      //       console.log(res,"res")
      //       this.$store.commit('calendar/addGoogle',{googleSch: res.data}, {root: true})
      //     })
      // })
      // ipcRenderer.send('asynchronous-message', 'createAuthWindow');
    },

    renderEvent(arg) {
      // console.log(this.$q.dark.isActive, 'darkcal')
      this.changeNumColor({goDark: this.$q.dark.isActive})
      if (!this.$q.screen.lt.sm) {
        let titleStr = arg.event.title
        let contentStr = arg.event.extendedProps.description

        new BPopover({
          propsData: {
            title: titleStr,
            content: contentStr,
            placement: 'auto',
            boundary: 'scrollParent',
            boundaryPadding: 5,
            delay: 500,
            offset: 0,
            triggers: 'hover',
            html: true,
            target: arg.el,    
          },
        }).$mount()
      }

      this.addHolidayCol({ arg: arg, goDark: this.goDark })
      this.addStatusDeco({ arg: arg })

      this.getLunInfo().then(()=>{
        this.LUN_DAYS.forEach(el=>{
          $('.fc-daygrid-day[data-date="'+el.solDay+'"]').children('.fc-daygrid-day-frame').children('.fc-daygrid-day-top').children('.holiday-text').remove()
          $('.fc-daygrid-day[data-date="'+el.solDay+'"]').children('.fc-daygrid-day-frame').children('.fc-daygrid-day-top').prepend("<div class='holiday-text'>"+el.lunDay+"</ div>");
        })
      })
      
    },

    handleSelect(arg) {
    //  console.log(this.$refs.fullCalendar.getApi(), 'refs')
      if (this.userId == this.master) {
        this.$modal.show(NewEventModal, {
          group_name: this.group_name,
          start_cal: arg.start,
          end_cal: arg.end,
          event: arg.event,
          people: this.people,
          dark:this.goDark,
          currentData: this.$refs.fullCalendar.getApi().currentData.currentViewType,
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
          currentData: this.$refs.fullCalendar.getApi().currentData.currentViewType,
          modal: this.$modal},{
              height: 'auto'
          })
      } else {
        swal('Warning', 'Only the head of the group can register the schedule!', 'warning')
        $('.fc-daygrid-bg-harness').children('.fc-highlight').addClass( 'rmvh' )
        $('.fc.fc-highlight').removeClass('rmvh')
      }
    },

    handleEventClick (arg) {
      // console.log(arg)
      if (arg.event.extendedProps.group != 'koholday' && arg.event.extendedProps.sid != 'google') {
        // console.log(this.master, 'asf')

        if (this.$q.screen.lt.sm) {
          this.$modal.show(EventDetailforMobile, {
            groupInfo: this.GROUPS,
            group_name: this.group_name,
            event: arg.event,
            events: this.EVENTS,
            people: this.people,
            master: this.master,
            userId: this.userId,
            modal: this.$modal},{
                height: 'auto',
          })
        } else {
          this.$modal.show(EventDetailforDesktop, {
            groupInfo: this.GROUPS,
            group_name: this.group_name,
            event: arg.event,
            events: this.EVENTS,
            people: this.people,
            master: this.master,
            userId: this.userId,
            modal: this.$modal},{
                height: 'auto',
          })
        }
      }
    },

    resizeEvent(arg) {
      this.RE_EVENT({id: arg.event.id, start: arg.event.start, end: arg.event.end, people: this.people, allDay: arg.event.allDay})        
    },

    reSchedule(arg) {       
      if (arg.event.end === null) {
        let start_time
        start_time = this.$moment(arg.event.start).format('YYYY-MM-DD HH:mm:ss')
        
        let r_end
        r_end = this.$moment(start_time).add(60, 'minutes')
        this.RE_EVENT({id: arg.event.id, start: arg.event.start, end: r_end, people: this.people, allDay: arg.event.allDay})        
      } else {   
        this.RE_EVENT({id: arg.event.id, start: arg.event.start, end: arg.event.end, people: this.people, allDay: arg.event.allDay})        
      } 
    },

    switchingcal() {
			this.switching =! this.switching 
		},
    
  },

  created() {
    this.token=sessionStorage.getItem("access-token")
    this.userId=sessionStorage.getItem("username")
    this.userName=sessionStorage.getItem("name") 
    this.getHolidayInfo()
    
    // this.getGroupInfo()
    this.monthInit({different:0})
    if(this.$q.platform.is.mobile == true ){
      this.updateMySchedule({people: sessionStorage.getItem("username"),flag:false})
    }else{
      this.updateMySchedule({people: sessionStorage.getItem("username"),flag:true})
    }
    
    

    this.switchingKey = 0
  },

  mounted() {
    this.member_info=this.MEMBER_INFO
    this.getLunInfo().then(()=>{
      this.LUN_DAYS.forEach(el=>{
        $('.fc-daygrid-day[data-date="'+el.solDay+'"]').children('.fc-daygrid-day-frame').children('.fc-daygrid-day-top').prepend("<div class='holiday-text'>"+el.lunDay+"</ div>");
      })
    })
  },

  updated() {
    // this.changeNumColor({goDark: this.goDark})
    this.changeNumColor({goDark: this.$q.dark.isActive})
    this.titlee = this.$refs.fullCalendar.$options.calendar.currentData.viewTitle

    this.switchingKey += 1
    if (this.switchingKey == 1) {
      $('.fc-GoogleBtn-button')
        .prepend('<div data-toggle="tooltip" data-placement="top" title="Connect with Google Calendar"><i class="fa fa-google fa-2x"></i></div>')
      $('.fc-kanban-button')
        .prepend('<div data-toggle="tooltip" data-placement="top" title="Go to Kanban"><i class="fa fa-trello fa-2x"></i></div>')
      if (this.goDark==true) {
        $(".fa-trello")
          .attr('style', 'color: white !important')
      } else {
        $(".fa-trello")
          .attr('style', 'color: black !important')
      }

      this.monthInit()

      if(this.people===null) {                   
        this.updateMySchedule({people: sessionStorage.getItem("username"),flag:false})
      } else {
        this.updateSchedule({people: this.people}).then(()=>{
          this.member_info=this.MEMBER_INFO;         
        })
      }    
      this.getLunInfo()
        .then(()=>{
          this.LUN_DAYS.forEach(el=>{
            $('.fc-daygrid-day[data-date="'+el.solDay+'"]').children('.fc-daygrid-day-frame').children('.fc-daygrid-day-top').children('.holiday-text').remove()
            $('.fc-daygrid-day[data-date="'+el.solDay+'"]').children('.fc-daygrid-day-frame').children('.fc-daygrid-day-top').prepend("<div class='holiday-text'>"+el.lunDay+"</ div>");
          })
        })
      
      // this.changeNumColor({goDark: this.goDark})
      this.getHolidayInfo()
    }
  },
  watch: {
    switching() {
      this.switchingKey = 0
    },

    datepicker() {
      let calendarApi = this.$refs.fullCalendar.getApi()
      calendarApi.gotoDate(this.$moment(this.datepicker).add(1,'months').format('YYYY-MM-DD'))

      calendarApi.prev()
             
      let d = new Date();            
      let nowYear = d.getFullYear()
      let nowMonth = d.getMonth()            
      let str=$( 'h2.fc-toolbar-title' ).text()
      let year=str.substring(str.length-4,str.length)
      let mon=str.substring(0,3)
      let different=this.$moment([year,this.month.indexOf(mon),1]).diff(this.$moment([nowYear, nowMonth, 1]), 'months', true)                
      this.monthInit({different: different})
      if(this.people===null){                 
        this.updateMySchedule({people: sessionStorage.getItem("username"),flag:false})
      } else{
        // this.updateSchedule({people: sessionStorage.getItem("username")}).then(()=>{
          this.updateSchedule({people: this.people}).then(()=>{
          this.member_info=this.MEMBER_INFO;         
        });
      }    
      
      // calendarApi.changeView('timeGridWeek')
      this.getLunInfo().then(()=>{
        this.LUN_DAYS.forEach(el=>{
          $('.fc-daygrid-day[data-date="'+el.solDay+'"]').children('.fc-daygrid-day-frame').children('.fc-daygrid-day-top').children('.holiday-text').remove()
          $('.fc-daygrid-day[data-date="'+el.solDay+'"]').children('.fc-daygrid-day-frame').children('.fc-daygrid-day-top').prepend("<div class='holiday-text'>"+el.lunDay+"</ div>");
        })
      })
      this.getHolidayInfo()
    },

    goDark() {
      // this.removeCustomBtn({size: this.$q.screen.lt.sm})
      if (this.goDark === true) {
        this.$q.dark.set(true)
      } else {
        this.$q.dark.set(false)
      }
    },


    people() {
			// console.log(this.people)
			this.getHolidayInfo()
			if(this.people===null){      
				this.updateMySchedule({people: sessionStorage.getItem("username"),flag:false})
			}else{
				this.updateSchedule({people: this.people}).then(()=>{
					this.member_info=this.MEMBER_INFO        
				})
      }

      // this.changeNumColor({goDark: this.goDark})

			for(var idx in this.GROUPS) {
				if(this.GROUPS[idx].groupName == this.people) {
					if(sessionStorage.getItem("username") == this.GROUPS[idx].groupMaster) {
						this.master = this.GROUPS[idx].groupMaster
						break
					} else {
						this.master = ''
					}
				}
			}
    },

  }

}
</script>

<style lang='css' scoped>
.fc {
  width: 100% !important;
}

.status_done {
  text-decoration: line-through !important;
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

select.filter {
  width: 500px !important;
}

</style>

<style lang="scss">
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
    //  margin-left: 0px !important;
    margin: 0px !important;
    padding: 5px !important;
    margin-bottom: 10px !important;
  }
}

.fc-button-mobile {
  margin: 0px !important;
  padding: 5px !important;
  margin-bottom: 10px !important;
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
      }
    }
  }
}

td.fc-daygrid-day.fc-day.fc-day-sat{
  .fc-daygrid-day-frame.fc-scrollgrid-sync-inner{
    .fc-daygrid-day-top{
      .fc-daygrid-day-number{
        color: blue;
      }
    }
  }
}

td.fc-daygrid-day.fc-day{
  .fc-daygrid-day-frame.fc-scrollgrid-sync-inner{
    .fc-daygrid-day-top{
      .fc-daygrid-day-number{
          color: gray;
      }
    }
  }
}

td.fc-daygrid-day.fc-day.fc-day-sun{
  .fc-daygrid-day-frame.fc-scrollgrid-sync-inner{
    .fc-daygrid-day-top{
      justify-content: space-between;
      flex-direction: row;
    }
  }
}

td.fc-daygrid-day{
  .fc-daygrid-day-frame.fc-scrollgrid-sync-inner.birthday{
    .fc-daygrid-day-top{
      justify-content: space-between;
      flex-direction: row;
    }
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

.koreaholday {
  color: red !important;
}

div.fc-header-toolbar.fc-toolbar.fc-toolbar-ltr {
  margin-bottom: 0;
}

i.fa.fa-birthday-cake {
  margin-left: 5px;
  margin-top: 5px;
}

.notholday {
  color: white !important;
}
.rmvh {
  background: none !important;
}
.ui-datepicker-trigger{
  border-top-right-radius: 4px;
  border-bottom-right-radius: 4px;
  background: transparent;
  border: none;
  margin: 0!important;
  padding: 0!important;
  height: 30px!important;
}
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
  font-size: 0.7rem !important;
  // display: block !important;
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

.fc-kanban-button {
  padding: 0 !important;
  // margin-bottom: 10px !important;
  background-color: transparent !important;
  // display: block !important;
  font-size: 0.7rem !important;
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
  // min-width: 100%;
 }

.fc-button {
  background: transparent !important;
  color: black !important;
}

.fc table {
  width: 100% !important;
}

.fc-daygrid-body {
  width: 100% !important;
}

.goDarkCal {
  background: #121212;
}

.popover-header {
  color: black !important;
}

.fc-toolbar-title {
  justify-content: center !important;
  display: flex !important;
  font-size: 18px !important;
  // flex-wrap: wrap !important;
}
.fc-GoogleBtn-button-mobile {
  margin-bottom: 20px !important
}

.fc-prev-button {
  font-size: 0.7rem !important;
}

.fc-next-button {
  font-size: 0.7rem !important;
}

</style>