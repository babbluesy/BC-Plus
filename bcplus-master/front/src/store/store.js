import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'
import router from '../router/index'
import swal from 'sweetalert'

Vue.use(Vuex)

const moment = require("moment");

let https = require('https')
const instance = axios.create({
  //'http://52.14.190.92/api'
  baseURL: 'https://bcplus.me/api',
  timeout: 120000,
  httpsAgent: new https.Agent({
    rejectUnauthorized: false
  })
});

const store = new Vuex.Store({
  state: {
    events: [],
    groups: [],
    tasks:[],
    userInfo: null,
    isLogin: null,
    isLoginError: null,
    token: null,
    group_name: [],
    month_cnt: 0,
    lun_day: [],
    hol_day: [],
    member_info:[],
    holiday:[],
    googleSch:[],
    chattings:[],
    memos:[],
  },
  getters: {
    EVENTS: state => state.events,
    GROUPS: state => state.groups,
    USER: state => state.userInfo,
    group_name: state => state.group_name,
    LUN_DAYS: state => state.lun_day,
    TASKS: state => state.tasks,
    MEMBER_INFO:state=> state.member_info,
    CHATTINGS:state=>state.chattings,
    MEMOS:state=>state.memos,
  },
    mutations: {
      ADD_EVENT: (state, event) => {           
        state.events.push(event)
      },

      addGoogle:(state,{googleSch})=>{
        googleSch.map(el=>{el.end=moment(el.end).add(0, 'days').format('YYYY-MM-DD')})
        let index = state.events.findIndex(el => el.sid == 'google')
        if(index<0){
          state.googleSch=[]
          let newEvents=state.events;
          state.events=[]
          newEvents.forEach(el=>{state.events.push(el);})
          googleSch.forEach(el=>{
            state.googleSch.push(el);
            state.events.push(el);                    
          }) 
        }
      },
        ADD_USER: (state, event) => {
          state.userInfo=event
        },
        monthInit: (state,{different}) =>{
          state.month_cnt = different
        },
        monthAdd:(state)=>{
          state.month_cnt+=1
        },
        monthMinus:(state)=>{
          state.month_cnt-=1
        },
        LUN_CAL_INFO: (state, days) => {
          state.lun_day=[]
          days.forEach(el=>{
            state.lun_day.push({lunDay:el.lunMonth+'/'+el.lunDay,solDay:el.solYear+'-'+el.solMonth+'-'+el.solDay})
          })
        },
        UPDATE_HOLIDAY:(state,{holi})=>{
          state.holiday=[]             
          if(holi.length===undefined){
            let arr=[]
            arr.push(holi)
            holi=arr                
          }else if(holi===null){
            holi=[]
          }
          holi.forEach(el=>{
          el.locdate=el.locdate.toString()
          let y=el.locdate.substring(0,4)
          let m=el.locdate.substring(4,6);
          let d=el.locdate.substring(6,8);
          let str=y+'-'+m+'-'+d               
          state.holiday.push({                    
            'title': el.dateName,
            'start': new Date(str),
            'color': 'transparent',
            'textColor' : "red",
            'allDay': 'true',
            'description':  el.dateName,
            'durationEditable': false,
            'startEditable': false,
            'group': 'koholday',
            "className": "hol",
          })
          })            
        },
        RE_EVENT: (state, {id, start, end}) => {
          let index = state.events.findIndex(_event => _event.id == id)
          state.events[index].start = moment(start).format('YYYY-MM-DD');
          state.events[index].end = moment(end).format('YYYY-MM-DD');
          state.events[index].realend = moment(end).add(-1, 'days').format('YYYY-MM-DD');
        },
        UPDATE_EVENT: (state, {id, title, description, color, start, end, realend, group}) => {
          let index = state.events.findIndex(_event => _event.id == id)

          state.events[index].title = title;
          state.events[index].description = description;
          state.events[index].color = color;
          state.events[index].start = moment(start).add(+1,'days').format('YYYY-MM-DD');
          state.events[index].end = end;
          state.events[index].realend = realend;
          state.events[index].group = group;
        },
        UPDATE_GROUP: (state, { groupName, groupDetail, hashCode }) => {
          let index = state.groups.findIndex(_group => _group.hashCode == hashCode)

          state.groups[index].groupName = groupName;
          state.groups[index].groupDetail = groupDetail;
        },
        UPDATE_USER: (state, { name, birthDay, pw}) => {
          console.log(state, name, pw, birthDay)
        },
        UPDATE_GRANT: (state, { group }) => {
          let index = state.groups.findIndex(_group => _group.hashCode == group.hashCode)
          state.groups[index].groupMaster = group.groupMaster;
        },
        LEAVE_GROUP:(state, { group })=>{
          let index = state.groups.findIndex(_group => _group.hashCode == group.hashCode)
          let idx = state.group_name.findIndex(el => el === group.groupName)                                    
          if (index > -1) state.groups.splice(index, 1)
          if (idx > -1) state.group_name.splice(idx, 1)
          state.tasks=[]
          state.events=[]
        },
        DELETE_EVENT: (state, {sid}) => {
          const itemToFind = state.events.find(function(item) {return item.sid === sid})
          const idx = state.events.indexOf(itemToFind)
          const taskToFind = state.tasks.find(function(item) {return item.sid === sid})
          const taskIdx = state.tasks.indexOf(taskToFind)
          if (idx > -1) state.events.splice(idx, 1)
          if (taskIdx > -1) state.tasks.splice(taskIdx, 1)
        },
        DELETE_GROUP: (state, {groupid}) => {
          const itemToFind = state.groups.find(function(item) {return item.groupName === groupid})
          const idx = state.groups.indexOf(itemToFind)
          if (idx > -1) state.groups.splice(idx, 1)
          if (idx > -1) state.group_name.splice(idx, 1)
          state.events=[]
          state.tasks=[]
        },
        DELETE_ACCOUNT: (state, {userId}) => {
          console.log(userId)
          state.isLogin = false
          state.isLoginError = false
          state.userInfo = null
          state.token = null
          delete sessionStorage.token
          delete sessionStorage.userId
        },
        ADD_GROUP: (state, {groups}) => {
          state.group_name.push(groups.groupName)
          state.groups.push(groups)
        },
        ADD_GROUPS: (state, {groups}) => {
          state.group_name=[]
          state.groups = []
          groups.forEach(el=>{
            state.groups.push({groupName:el.groupName, groupDetail:el.groupDetail, groupMember:el.groupMember,hashCode:el.hashCode, groupMaster:el.groupMaster})                
            state.group_name.push(el.groupName)
          }) 
        },
        ADD_EVENTS: (state, {schedules}) => {
          state.events=[]
          console.log("sch",schedules)
          state.holiday.forEach(el=>{
            state.events.push(el)
          })
          let cnt=0    
          schedules.map(el=>{if(el.editable){return el}else return el.end=moment(el.end).add(0, 'days').format('YYYY-MM-DD')})
          schedules.forEach(el=>{              
            el.id=cnt++   
            state.events.push(el)
          })
          state.googleSch.forEach(el=>{
            state.events.push(el);                    
          })    
        },
        ADD_TASKS: (state, {tasks}) => {
          state.tasks=[]         
          console.log(tasks, 'add_tasks')
          let cnt=0;
          let name='';   
          tasks.forEach(el=>{            
            el.id=cnt++
            name=el.group;
            state.tasks.push(el)
            }
          )
          let index = state.groups.findIndex(el => el.groupName === name)            
          if(index>=0){
              state.member_info=[]
              state.member_info.push({header: name})
              state.groups[index].groupMember.forEach(el=>{
                  state.member_info.push({ name: el, group: name, avatar: 'https://cdn.vuetifyjs.com/images/lists/1.jpg'})
              }
            )
          }
          state.tasks.sort((a,b)=>new Date(a.rend)-new Date(b.rend))
        },
        UPDATE_MEMBER_INFO:(state,{name,groups})=>{
          let index = state.groups.findIndex(el => el.groupName === name)            
          if(index>=0){
            state.member_info=[]
            state.member_info.push({header: name})
            state.tasks.members=[];
            groups.forEach(el=>{
              state.member_info.push({ name: el, group: name, avatar: 'https://cdn.vuetifyjs.com/images/lists/1.jpg'})
              state.tasks.members.push(el);
            })          
          }
        },
        RESIZE_EVENT: (state, {id, start, end}) => {
          let index = state.events.findIndex(_event => _event.id == id)

          state.events[index].start = moment(start).format('YYYY-MM-DD');
          state.events[index].rstart=moment(start).add(1,'days').format('YYYY-MM-DD');
          state.events[index].end = moment(end).format('YYYY-MM-DD');
          state.events[index].realend = moment(end).add(-1, 'days').format('YYYY-MM-DD');
        },
        
        // 로그인 성공 시
        loginSuccess(state, payload) {
          state.isLogin = true
          state.isLoginError = false
          state.userInfo = payload
        },

        // 로그인 실패 시
        loginError(state) {
          state.isLogin = false
          state.isLoginError = true
        },

        // 로그아웃 mutation
        logout(state) {
          state.isLogin = false
          state.isLoginError = false
          state.userInfo = null
          state.token = null
          delete sessionStorage.token
          delete sessionStorage.userId
          swal('Success', 'Logout Success', 'success')
        },
        ADD_MSG:(state,{data})=>{
          state.chattings=data
          console.log("메시지 가져오기 완료!1",state.chattings)
        },
        UPDATE_MEMO:(state,{memos})=>{
          state.memos=memos
        },
    },
    actions: {
        createEvent:(store, {
          id,
          title,
          description,
          color,
          start,
          end,
          rStart,
          rEnd,
          realend,
          group,
          allDay                     
        }) => {      
          console.log(allDay, 'allDay store')     
            let colorHex={
              'blue':'#2196F3',
              'indigo': '#3F51B5',
              'deep-purple':'#673AB7',
              'cyan':'#00BCD4',
              'green':'#4CAF50',
              'orange': '#FF9800',
              'red':'#F44336'
            }            
            function hashCode(s) {
              for(var i = 0, h = 0; i < s.length; i++)
                h = Math.imul(31, h) + s.charCodeAt(i) | 0;
              
                if (h<0) {h*=-1}
                return h;
            }
            let hash=''
            let people=group
            if(group===null)hash=hashCode(sessionStorage.getItem("username"))
            else hash=group  
            return instance.post('/schedule',
            {
              "sid": id,
              "title": title,
              "description": description,
              "color": colorHex[color],
              "start": start,
              "end": end,
              "rstart": rStart,
              "rend": rEnd,
              "realend": realend,
              "group": hash,
              "allDay":allDay,
              "editable":true,
              "members":[],
              "status":"backlog"
            })
            .then(()=>{
                if(people!=null){
                  instance.get( '/schedule/getschedule/'+people+'/'+ store.state.month_cnt+'/'+sessionStorage.getItem("username"))
                  .then(res1 =>{   
                    instance.get( '/schedule/getTodaySchedule/'+people)
                    .then(res =>{                                
                      store.commit("ADD_TASKS", {tasks: res.data}) 
                      store.commit("ADD_EVENTS", {schedules: res1.data})  
                    })                       
                  })
                }
                else{                                  
                  instance.get( '/schedule/getmyschedule/'+hash+'/'+ store.state.month_cnt+'/'+sessionStorage.getItem("username"))
                  .then(res1 =>{   
                    instance.get( '/schedule/getmyTodaySchedule/'+hash+'/'+sessionStorage.getItem("username"))
                      .then((res) =>{                                
                        res.data.map(el=>{if(el.editable==true){return el.group=null}})                                 
                        store.commit("ADD_TASKS", {tasks: res.data})                   
                        res1.data.map(el=>{if(el.group===people){return el}else return el.end=moment(el.end).add(0, 'days').format('YYYY-MM-DD HH:mm:ss')})
                        res1.data.forEach(el=>el.group=sessionStorage.getItem("username"))
                        store.commit("ADD_EVENTS", {schedules: res1.data})
                        })             
                    })
                }
            })
            .catch((res)=>{
              store.commit('fail', res);
            });
        }
           
        ,
        SHOW_MEMBERS: (state) => {
          state.events.push(event)
        },

        // login
        loginVuex: (store, {userId, pw}) => {
            return instance.post('/user/login',
                {
                  userEmail: userId,
                  userPw: pw
                })
                .then(res => {
                  let token  = res.headers["authorization"]
                  let events = events
                  console.log('loginvuex', events)
                  store.commit('ADD_USER', res.data[0]);
                  sessionStorage.setItem("access-token", token)
                  sessionStorage.setItem("username", res.data[0].userId)
                  sessionStorage.setItem("name", res.data[0].name) 
                  sessionStorage.setItem("showevents", events)
                  sessionStorage.setItem("isGoogle",false)
                  router.push('/calendar')
                })
                .catch(() => {
                  swal('Error', "Incorrect account information",'error')
                  store.commit("user/loginError")
                });
        },

        // logout
        logout: (store) => {
          sessionStorage.removeItem("access-token")
          sessionStorage.removeItem("username")
          store.commit("logout")
        },

        changestatus:(store,{schedule,people})=>{
            // alert("changestatus: "+id+" "+status);
            console.log('schedule', schedule)
            let index = store.state.events.findIndex(_event => _event.sid == schedule.sid) 
            function hashCode(s) {
                for(var i = 0, h = 0; i < s.length; i++)
                    h = Math.imul(31, h) + s.charCodeAt(i) | 0;
                    if(h<0){
                        h*=-1
                     }
                return h;
            }
            store.state.events[index].end = moment(store.state.events[index].end).format("YYYY-MM-DDTHH:mm:ssZ")
            console.log(store.state.events[index].title, schedule.status, schedule.sid, store.state.events[index].end, schedule.group)
            let hash=''
            if(people===null)hash=hashCode(sessionStorage.getItem("username"))    
            console.log('hash', hash)
            return instance.post('/schedule',
            {"sid": schedule.sid,
                "title": store.state.events[index].title,
                "description": store.state.events[index].description,
                "color": store.state.events[index].color,
                "start": store.state.events[index].start,
                "end": store.state.events[index].end,
                "rstart": store.state.events[index].start,
                "rend": store.state.events[index].end,
                "realend": store.state.events[index].realend,
                "group": schedule.group!=null?schedule.group:hash,
                "allDay":store.state.events[index].allDay,
                "editable":true,
                "members":store.state.events[index].members,
                "status":schedule.status
            })
            .then(()=>{
                if(people!=null){
                    instance.get('/schedule/getschedule/'+people+'/'+ store.state.month_cnt+'/'+sessionStorage.getItem("username"))
                    .then(res1 =>{   
                        instance.get( '/schedule/getTodaySchedule/'+people)
                        .then(res =>{                                
                            store.commit("ADD_TASKS", {tasks: res.data}) 
                            store.commit("ADD_EVENTS", {schedules: res1.data})  
                        })                       
                    })
                }
                else{                              
                    instance.get( '/schedule/getmyschedule/'+hash+'/'+ store.state.month_cnt+'/'+sessionStorage.getItem("username"))
                    .then(res1 =>{   
                        instance.get( '/schedule/getmyTodaySchedule/'+hash+'/'+sessionStorage.getItem("username"))
                        .then((res) =>{                                
                            res.data.map(el=>{if(el.editable ==true){return el.group=null}})                              
                            store.commit("ADD_TASKS", {tasks: res.data})                   
                            res1.data.map(el=>{if(el.group===people){return el}else return el.end=moment(el.end).add(0, 'days').format('YYYY-MM-DD HH:mm:ss')})
                            res1.data.forEach(el=>el.group=sessionStorage.getItem("username"))
                            store.commit("ADD_EVENTS", {schedules: res1.data})
                        })             
                    })
                }
                // alert("업데이트 성공!");
            })
            .catch((res)=>{
                store.commit('fail', res);
            });
        },

        ADD_GROUP: (store, {group_name, group_detail, group_members, group_master}) => {
            return instance.post('/group',
            {
              id: {
                date: new Date(),
                timestamp: 0
              },
              groupName: group_name,
              groupMember: group_members,
              messages:[],
              groupDetail: group_detail,
              groupMaster: group_master,

            })
            .then(res =>{
                store.commit("ADD_GROUP", {groups:res.data})
            }).catch((e)=>{
                swal('Warning', "Already exist!", 'warning')
                console.log(e)
            })
        },

        getGroupInfo: () => {
          return instance.get('/group/all/'+ sessionStorage.getItem("username"))
          .then(res =>{
            store.commit("ADD_GROUPS", {groups:res.data})
          })
        },

        updateSchedule: (store, {people}) => {
            return instance.get('/schedule/getschedule/'+people+'/'+ store.state.month_cnt+'/'+sessionStorage.getItem("username"))
            .then(res1 =>{   
              instance.get('/schedule/getTodaySchedule/'+people)
              .then(res =>{                                
                store.commit("ADD_TASKS", {tasks: res.data})
                store.commit("ADD_EVENTS", {schedules: res1.data})     
              })
            })
        },
        updateMySchedule: (store, {people}) => {
          function hashCode(s) {
            for(var i = 0, h = 0; i < s.length; i++)
              h = Math.imul(31, h) + s.charCodeAt(i) | 0;
              if (h<0) {h*=-1}
            return h;
          }
          people=hashCode(people)
          instance.get('/schedule/getmyschedule/'+people+'/'+ store.state.month_cnt+'/'+sessionStorage.getItem("username"))
          .then(res1 =>{   
            instance.get('/schedule/getmyTodaySchedule/'+people+'/'+sessionStorage.getItem("username"))
            .then((res) =>{
              res.data.map(el=>{if(el.editable==true){return el.group=null}})                              
              store.commit("ADD_TASKS", {tasks: res.data})  
              res1.data.map(el=>{if(el.group===people){return el}else return el.end=moment(el.end).add(0, 'days').format('YYYY-MM-DD HH:mm:ss')})
              res1.data.forEach(el=>el.group=sessionStorage.getItem("username"))
              store.commit("ADD_EVENTS", {schedules: res1.data})
            })             
          })
        },
        RE_EVENT: (store, {id, start, end, people,allDay}) => {      
          console.log('!!!')      
          console.log('sdjdjsd', end, allDay) 
            let index = store.state.events.findIndex(_event => _event.id == id)           
            let sid=store.state.events[index].sid                     
            function hashCode(s) {
              for(var i = 0, h = 0; i < s.length; i++)
                  h = Math.imul(31, h) + s.charCodeAt(i) | 0;
                  if (h<0) {h*=-1}
              return h;
            }
            let hash=''
            if(people===null)hash=hashCode(sessionStorage.getItem("username"))       
            return instance.post('/schedule',
            {
              "sid": sid,
              "title": store.state.events[index].title,
              "description": store.state.events[index].description,
              "color": store.state.events[index].color,
              "start": start,
              "end": end,
              "rstart": start,
              "rend": end,
              "realend": store.state.events[index].realend,
              "group": people!=null?people:hash,
              "allDay":allDay,
              "editable":true,
              "members":store.state.events[index].members,
              "status":store.state.events[index].status
            })
            .then(()=>{
                if(people!=null){
                  instance.get('/schedule/getschedule/'+people+'/'+ store.state.month_cnt+'/'+sessionStorage.getItem("username"))
                  .then(res1 =>{   
                    instance.get( '/schedule/getTodaySchedule/'+people)
                    .then(res =>{                                
                      store.commit("ADD_TASKS", {tasks: res.data}) 
                      store.commit("ADD_EVENTS", {schedules: res1.data})  
                    })                       
                  })
                }
                else{                              
                  instance.get( '/schedule/getmyschedule/'+hash+'/'+ store.state.month_cnt+'/'+sessionStorage.getItem("username"))
                  .then(res1 =>{   
                    instance.get( '/schedule/getmyTodaySchedule/'+hash+'/'+sessionStorage.getItem("username"))
                    .then((res) =>{                                
                      res.data.map(el=>{if(el.editable ==true){return el.group=null}})                              
                      store.commit("ADD_TASKS", {tasks: res.data})                   
                      res1.data.map(el=>{if(el.group===people){return el}else return el.end=moment(el.end).add(0, 'days').format('YYYY-MM-DD HH:mm:ss')})
                      res1.data.forEach(el=>el.group=sessionStorage.getItem("username"))
                      store.commit("ADD_EVENTS", {schedules: res1.data})
                    })             
                  })
                }
                
            })
            .catch((res)=>{
                store.commit('fail', res);
            });
        },
    deleteschedule: (store, {sid}) => {
      instance.delete( '/schedule/' + sid)
      .then( ()=>{
        store.commit("DELETE_EVENT", {sid: sid})
      })
    },
    updateschedule: (store, {people, id, title, description, color, start, end, rStart, rEnd, realend, allDay}) => {
        let index = store.state.events.findIndex(_event => _event.id == id)           
        let sid=store.state.events[index].sid                     
        function hashCode(s) {
            for(var i = 0, h = 0; i < s.length; i++)
                h = Math.imul(31, h) + s.charCodeAt(i) | 0;
                if(h<0){
                    h*=-1
                 }
            return h;
        }
        let hash=''
        if(people===null)hash=hashCode(sessionStorage.getItem("username")) 
        let colorHex={
            'blue':'#2196F3',
            'indigo': '#3F51B5',
            'deep-purple':'#673AB7',
            'cyan':'#00BCD4',
            'green':'#4CAF50',
            'orange': '#FF9800',
            'red':'#F44336'
        }  
        return instance.post('/schedule',
        {
            "sid": sid,
            "title": title,
            "description": description,
            "color": colorHex[color],
            "start": start,
            "end": end,
            "rstart": rStart,
            "rend": rEnd,
            "realend": realend,
            "group": people!=null?people:hash,
            "allDay":allDay,
            "members":store.state.events[index].members,
            "editable":true,
            "status":store.state.events[index].status
        })
        .then(()=>{
            if(people!=null){
                instance.get('/schedule/getschedule/'+people+'/'+ store.state.month_cnt+'/'+sessionStorage.getItem("username"))
                .then(res1 =>{   
                    instance.get( '/schedule/getTodaySchedule/'+people)
                    .then(res =>{                                
                        store.commit("ADD_TASKS", {tasks: res.data}) 
                        store.commit("ADD_EVENTS", {schedules: res1.data})  
                    })                       
                })
            }
            else{                              
                instance.get( '/schedule/getmyschedule/'+hash+'/'+ store.state.month_cnt+'/'+sessionStorage.getItem("username"))
                .then(res1 =>{   
                    instance.get( '/schedule/getmyTodaySchedule/'+hash+'/'+sessionStorage.getItem("username"))
                    .then((res) =>{                                
                        res.data.map(el=>{if(el.editable ==true){return el.group=null}})                              
                        store.commit("ADD_TASKS", {tasks: res.data})                   
                        res1.data.map(el=>{if(el.group===people){return el}else return el.end=moment(el.end).add(0, 'days').format('YYYY-MM-DD HH:mm:ss')})
                        res1.data.forEach(el=>el.group=sessionStorage.getItem("username"))
                        store.commit("ADD_EVENTS", {schedules: res1.data})
                    })             
                })
            }
            
        })
        .catch((res)=>{
            store.commit('fail', res);
        });
    },
    getLunInfo: (store) =>{
        
        return instance.get( '/lunar/' + store.state.month_cnt)
        
        .then(res=>{
           let days=res.data.day.filter(el=>el.solWeek=='일')
           store.commit('LUN_CAL_INFO', days)
        })
    },
    getHolidayInfo: (store) =>{   
        return instance.get( '/holiday/' + store.state.month_cnt)        
        .then(res=>{              
           if(!(res.data.days===null)){
            store.commit('UPDATE_HOLIDAY',{holi:res.data.days.day});
           }         
        })
    },
    ShowGroupInfo: (store) => {
        sessionStorage.setItem("groupinfo", store)
        store.commit('ShowGroupInfo', sessionStorage.getItem("groupinfo"))
        instance.get('/group/')
        .then({})
    },   
    invite:(store,{group_code})=>{      
        instance.get( '/group/invite/' + sessionStorage.getItem("username")+'/'+group_code)
        .then(res => {
            store.commit("ADD_GROUP", {groups:res.data})
        }).catch(()=>{
            swal('Error', "Group doesn't exist!", 'error')
        })
    },
    deletegroup: (store, {groupid}) => {
        instance.delete( '/group/' + groupid)
            .then( ()=>{
                store.commit("DELETE_GROUP", {groupid: groupid})
        })
    },
    addMem:(store,{members,sid,people})=>{
        let memberList=[]
        for(let i=0;i<members.length;i++){           
            memberList.push(members[i])
        }
        instance.post('/schedule/addMem',{
            "members":memberList,
            'sid':sid          
        } )
        .then(res => {
            if(res.data==='success'){
                instance.get( '/schedule/getschedule/'+people+'/'+ store.state.month_cnt+'/'+sessionStorage.getItem("username"))
                .then(res1 =>{   
                    instance.get( '/schedule/getTodaySchedule/'+people)
                    .then(res =>{                                
                        store.commit("ADD_TASKS", {tasks: res.data}) 
                        store.commit("ADD_EVENTS", {schedules: res1.data})  
                    })                     
                })
            }
           
        }).catch(()=>{
            swal('Error', "It includes members who have already left.", 'error')
            router().reload();
        })
    },
    editgroup: (store, {groupid, gdtail, group_hash}) => {       
        return instance.put( '/group/' + groupid,
        {
            "groupName": groupid,
            "groupDetail": gdtail,
            "hashCode":group_hash
        })
        .then(()=>{                               
            store.commit('UPDATE_GROUP', { groupName: groupid, groupDetail: gdtail, hashCode: group_hash })                
        })
        .catch((res)=>{
            store.commit('fail', res);
        });
    },
    GRANT_EVENT:(store,{userid,groupid})=>{
        return instance.get('/group/grant/'+userid+'/'+groupid)
        .then((res)=>{
            store.commit('UPDATE_GRANT',{group:res.data});
        }).catch(()=>{
            swal('Warning', "You entered an incorrect ID", 'warning');
        });
    },
    leaveGroup:(store,{groupName})=>{       
        instance.get( '/group/leaveGroup/'+groupName+'/'+sessionStorage.getItem('username'))
        .then((res)=>{
            store.commit('LEAVE_GROUP',{group:res.data});

        }).catch(err=>{
            console.log(err)
        })
    },
    deleteaccount:(store) => {
        function hashCode(s) {
            for(var i = 0, h = 0; i < s.length; i++)
                h = Math.imul(31, h) + s.charCodeAt(i) | 0;
                if(h<0){
                    h*=-1
                 }   
            return h;
        }
        let hash=''       
        hash=hashCode(sessionStorage.getItem("username"))
        let userId=sessionStorage.getItem("username")
        instance.delete('/member/'+ userId+'/'+hash)
        .then(()=>{
            store.commit('DELETE_ACCOUNT',{userId: userId });
        }).catch(err=>{
            console.log('fail',err)
        })
    },
    getMessage:(store,{groupName})=>{
      instance.get("/group/getMessage/"+groupName).then(res=>{
        console.log("msgs",res)
        store.commit('ADD_MSG',{data:res.data });
      })
    },
    getMemberList:(store,{groupName})=>{
        if(!(groupName===null)){
            instance.get('/group/getMemberList/'+groupName).then(res=>{
                store.commit('UPDATE_MEMBER_INFO',{name:groupName,groups:res.data});
    
            })
        }       
    },
    deleteMemo:(store,{memos})=>{
      console.log("memos",memos)
      instance.post('/member/deleteMemo',{
        userId:sessionStorage.getItem('username'),
        memos:memos
      }).then(res=>{
        store.commit('UPDATE_MEMO',{memos:res.data});
      })
    },
    addMemo:(store,{memo,no})=>{
      instance.post('/member/addMemo',{
        userId:sessionStorage.getItem('username'),
        memos:[{
          no:no,
          text: memo
        }],
      }).then(res=>{        
        store.commit('UPDATE_MEMO',{memos:res.data});
      })
    },
    getMemo:(store)=>{     
      instance.get('/member/getMemo/'+sessionStorage.getItem('username'))
      .then(res=>{         
          store.commit('UPDATE_MEMO',{memos:res.data});
      })
    },
    edituserdetail:(store, {name, birthDay, pw})=>{
        instance.post('/member/updateMember',{
            userId:sessionStorage.getItem("username"),
            name: name,
            birthDay: birthDay,
            pw: pw
        })
        .then(()=>{
            store.commit('UPDATE_USER',{name: name, birthDay: birthDay, pw: pw});
        }).catch(err=>{
            console.log('fail',err)
        })
    }
    }
    }
)
export default store;