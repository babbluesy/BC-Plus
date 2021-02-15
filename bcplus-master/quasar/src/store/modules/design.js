import axios from 'axios'
// import router from '../../router/index.js'

let https = require('https')
const instance = axios.create({
  baseURL: 'https://bcplus.me/api',
  timeout: 120000,
  httpsAgent: new https.Agent({
    rejectUnauthorized: false
  })
});

// state
const state = {
  // rightDrawer: true
}

// getters
const getters = {
    // RIGHTDRAWER: state => state.rightDrawer,
}

// mutations
const mutations = {
    // changeRigthDrawer(state) {
    //   state.rightDrawer = !state.rightDrawer
    // },
    removeCustomBtn(state, { size }) {
      if ( size ) {
        $('.fc-button').addClass('fc-button-mobile')
        $('.fc-GoogleBtn-button').addClass('fc-GoogleBtn-button-mobile')
        $('.fc-btn2-button').attr('style', 'display: none !important')
        $('.fc-GoogleBtn-button').attr('style', 'display: none !important')
      } else {
        $('.fc-button').removeClass('fc-button-mobile')
        $('.fc-GoogleBtn-button').removeClass('fc-GoogleBtn-button-mobile')
      }
    },

    changeNumColor(state, { goDark }) {
        if(goDark==true){
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

    addHolidayCol(state, { arg, goDark }) {
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
        if(goDark==true){
          $("a.fc-daygrid-day-number.koreaholday")
            .attr('style','color: hotpink !important')
          $("div.fc-event-title.fc-sticky.koreaholday")
            .attr('style','color: hotpink !important')
        }
      }
    },

    addStatusDeco(state, { arg }) {
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
        
        $(".fc-event-time.status_done").attr('style','text-decoration: line-through black double')
        $(".fc-event-title.status_done").attr('style','text-decoration: line-through black double')
        
      }
    }
}

// actions
const actions = {

}

export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions,
};  