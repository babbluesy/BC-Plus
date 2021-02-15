import axios from 'axios'
// import router from '../../router/index.js'
const moment = require("moment");

let https = require('https')
const instance = axios.create({
  baseURL: 'https://bcplus.me/api',
  timeout: 120000,
  httpsAgent: new https.Agent({
    rejectUnauthorized: false
  })
})

// state
const state = {
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
}

// getters
const getters = {
	EVENTS: state => state.events,
	GROUPS: state => state.groups,
	USER: state => state.userInfo,
	group_name: state => state.group_name,
	LUN_DAYS: state => state.lun_day,
	TASKS: state => state.tasks,
	MEMBER_INFO:state=> state.member_info,
	CHATTINGS:state=>state.chattings,
	MEMOS:state=>state.memos,
}

// mutations
const mutations = {

	ADD_EVENT: (state, event) => {           
		state.events.push(event)
	},
	FILTER_HOLY:(state,{events})=>{
		state.events=[]
		state.events=events
	},

	addGoogle:(state, { googleSch }) => {	
		googleSch.map(el => { el.end = moment(el.end).add(0, 'days').format('YYYY-MM-DD') })
		let index = state.events.findIndex(el => el.sid == 'google')
		if (index < 0) {
			state.googleSch=[]
			let newEvents=state.events
			state.events=[]
			newEvents.forEach(el => { state.events.push(el) })
			googleSch.forEach(el => {
				state.googleSch.push(el)
				state.events.push(el)                  
			}) 
		}
	},
    
	monthInit: (state, { different }) => {
		state.month_cnt = different
	},

	monthAdd: (state) => {
		state.month_cnt += 1
	},

	monthMinus: (state) => {
		state.month_cnt -= 1
	},

	LUN_CAL_INFO: (state, days) => {
		state.lun_day=[]
		days.forEach(el => {
			state.lun_day.push({lunDay:el.lunMonth+'/'+el.lunDay,solDay:el.solYear+'-'+el.solMonth+'-'+el.solDay})
		})
	},

	UPDATE_HOLIDAY: (state,{holi}) => {
		state.holiday=[]             
		if (holi.length === undefined) {
			let arr=[]
			arr.push(holi)
			holi=arr                
		} else if (holi === null) {
			holi=[]
		}
		holi.forEach(el => {
			el.locdate = el.locdate.toString()
			let y = el.locdate.substring(0,4)
			let m = el.locdate.substring(4,6)
			let d = el.locdate.substring(6,8)
			let str = y+'-'+m+'-'+d               
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
		state.events[index].start = moment(start).format('YYYY-MM-DD')
		state.events[index].end = moment(end).format('YYYY-MM-DD')
		state.events[index].realend = moment(end).add(-1, 'days').format('YYYY-MM-DD')
	},

	UPDATE_EVENT: (state, {id, title, description, color, start, end, realend, group}) => {
		let index = state.events.findIndex(_event => _event.id == id)

		state.events[index].title = title
		state.events[index].description = description
		state.events[index].color = color
		state.events[index].start = moment(start).add(+1,'days').format('YYYY-MM-DD')
		state.events[index].end = end
		state.events[index].realend = realend
		state.events[index].group = group
	},

	DELETE_EVENT: (state, {sid}) => {
		const itemToFind = state.events.find(function(item) {return item.sid === sid})
		const idx = state.events.indexOf(itemToFind)
		const taskToFind = state.tasks.find(function(item) {return item.sid === sid})
		const taskIdx = state.tasks.indexOf(taskToFind)
		if (idx > -1) state.events.splice(idx, 1)
		if (taskIdx > -1) state.tasks.splice(taskIdx, 1)
	},

	ADD_EVENTS: (state, {schedules}) => {
		state.events = []
		state.holiday.forEach(el => {
			state.events.push(el)
		})

		let cnt=0    
		schedules.map(el => { if (el.editable) { return el } else return el.end = moment(el.end).add(0, 'days').format('YYYY-MM-DD') })
		schedules.forEach(el => {              
			el.id=cnt++   
			state.events.push(el)
		})
		state.googleSch.forEach(el => {
			state.events.push(el)                   
		})    
		state.events.sort((a,b)=>new Date(a.start)-new Date(b.start))
	},

	RESIZE_EVENT: (state, {id, start, end}) => {
		let index = state.events.findIndex(_event => _event.id == id)

		state.events[index].start = moment(start).format('YYYY-MM-DD')
		state.events[index].rstart=moment(start).add(1,'days').format('YYYY-MM-DD')
		state.events[index].end = moment(end).format('YYYY-MM-DD')
		state.events[index].realend = moment(end).add(-1, 'days').format('YYYY-MM-DD')
	},

	ADD_TASKS: (state, {tasks}) => {
		state.tasks=[]         
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

	UPDATE_MEMO:(state,{memos}) => {
		state.memos = memos
	},
}

// actions
const actions = {
  createEvent:(store, {id, title, description, color, start, end, rStart, rEnd, realend, group, allDay}) => {       
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
					if (h<0) {h *= -1}
			return h
		}

		 
		let hash=''
		let people=group
		if (group === null) hash = hashCode(sessionStorage.getItem("username"))
		else hash = group 
		
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
				if(people != null){
					instance.get( '/schedule/getschedule/'+people+'/'+ store.state.month_cnt+'/'+sessionStorage.getItem("username"))
					.then(res1 => {   
						instance.get( '/schedule/getTodaySchedule/'+people)
						.then(res => {                                
							store.commit("ADD_TASKS", {tasks: res.data}) 
							store.commit("ADD_EVENTS", {schedules: res1.data})  
						})                       
					})
				}
				else{                                  
					instance.get( '/schedule/getmyschedule/'+hash+'/'+ store.state.month_cnt+'/'+sessionStorage.getItem("username"))
					.then(res1 =>{   
						instance.get('/schedule/getmyTodaySchedule/'+hash+'/'+sessionStorage.getItem("username"))
							.then((res) =>{                                
								res.data.map(el => {if(el.editable == true){return el.group = null}})                                 
								store.commit("ADD_TASKS", {tasks: res.data})                   
								res1.data.map(el => {if(el.group === people){return el} else return el.end = moment(el.end).add(0, 'days').format('YYYY-MM-DD HH:mm:ss')})
								res1.data.forEach(el => el.group = sessionStorage.getItem("username"))
								store.commit("ADD_EVENTS", {schedules: res1.data})
						})             
					})
				}
			})
			.catch((res)=>{
				store.commit('fail', res);
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

	updateMySchedule: (store, {people,flag}) => {
		function hashCode(s) {
			for(var i = 0, h = 0; i < s.length; i++)
				h = Math.imul(31, h) + s.charCodeAt(i) | 0
				if (h < 0) {h *= -1}
			return h
		}	

		people = hashCode(people)

		instance.get('/schedule/getmyschedule/'+people+'/'+ store.state.month_cnt+'/'+sessionStorage.getItem("username"))
			.then(res1 =>{   
				instance.get('/schedule/getmyTodaySchedule/'+people+'/'+sessionStorage.getItem("username"))
				.then((res) =>{
					res.data.map(el => {if (el.editable == true) {return el.group = null}})                              
					store.commit("ADD_TASKS", {tasks: res.data})
					if(flag){
						let num=3600000
						let num1=5000						
						Notification.requestPermission().then();
						
						let nowM=new Date().getTime();
						let str='';
						let cnt=0
						res.data.forEach(el=>{
							let tM=new Date(el.rend).getTime();
							// console.log(nowM+" "+tM)
							let diffMille=tM-nowM;							

							if(diffMille>0&&el.status!=='done'){
								diffMille-=num;
								if(diffMille>0){
									setTimeout(() => {var notification = new Notification('Incomplete Schedule', { body: el.title, icon: 'icons/favicon-32x32.png'});},diffMille)
								}else{									
									cnt++									
								}
							}												
						}) 
						
							if(cnt>=1)setTimeout(() => {var notification = new Notification('Incomplete Schedule', { body: '완료되지않은 '+cnt+'개의 일정이 있습니다.', icon: 'icons/favicon-32x32.png'});},7000)
					
					}  
					res1.data.map(el => {if (el.group === people) {return el} else return el.end = moment(el.end).add(0, 'days').format('YYYY-MM-DD HH:mm:ss')})
					res1.data.forEach(el => el.group=sessionStorage.getItem("username"))
					store.commit("ADD_EVENTS", {schedules: res1.data})
				})             
			})
	},

	RE_EVENT: (store, {id, start, end, people,allDay}) => {
			let index = store.state.events.findIndex(_event => _event.id == id)           
			let sid=store.state.events[index].sid 

			function hashCode(s) {
				for(var i = 0, h = 0; i < s.length; i++)
						h = Math.imul(31, h) + s.charCodeAt(i) | 0;
						if (h<0) {h *= -1}
				return h
			}

			let hash=''

			if (people === null) hash = hashCode(sessionStorage.getItem("username"))       
			
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
				.then(() => {
						if (people != null) {
							instance.get('/schedule/getschedule/'+people+'/'+ store.state.month_cnt+'/'+sessionStorage.getItem("username"))
							.then(res1 =>{   
								instance.get( '/schedule/getTodaySchedule/'+people)
								.then(res =>{                                
									store.commit("ADD_TASKS", {tasks: res.data}) 
									store.commit("ADD_EVENTS", {schedules: res1.data})  
								})                       
							})
						}
						else {                              
							instance.get( '/schedule/getmyschedule/'+hash+'/'+ store.state.month_cnt+'/'+sessionStorage.getItem("username"))
							.then(res1 => {   
								instance.get( '/schedule/getmyTodaySchedule/'+hash+'/'+sessionStorage.getItem("username"))
								.then((res) => {                                
									res.data.map(el => {if (el.editable == true) {return el.group = null}})                              
									store.commit("ADD_TASKS", {tasks: res.data})                   
									res1.data.map(el=>{if (el.group === people) {return el} else return el.end = moment(el.end).add(0, 'days').format('YYYY-MM-DD HH:mm:ss')})
									res1.data.forEach(el => el.group = sessionStorage.getItem("username"))
									store.commit("ADD_EVENTS", {schedules: res1.data})
								})             
							})
						}
				})
				.catch((res) => {
						store.commit('fail', res);
				});
	},

	deleteschedule: (store, {sid}) => {
		instance.delete( '/schedule/' + sid)
		.then(() => {
			store.commit("DELETE_EVENT", {sid: sid})
		})
	},

	updateschedule: (store, {people, id, title, description, color, start, end, rStart, rEnd, realend, allDay}) => {
		let index = store.state.events.findIndex(_event => _event.id == id)           
		let sid=store.state.events[index].sid     
          
		function hashCode(s) {
			for(var i = 0, h = 0; i < s.length; i++)
					h = Math.imul(31, h) + s.charCodeAt(i) | 0;
					if (h<0) {h *= -1}
			return h
		}

		
		let hash = ''
		if(people === null) hash = hashCode(sessionStorage.getItem("username")) 
		
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
		.then(() => {
				if(people != null){
					instance.get('/schedule/getschedule/'+people+'/'+ store.state.month_cnt+'/'+sessionStorage.getItem("username"))
						.then(res1 => {   
								instance.get( '/schedule/getTodaySchedule/'+people)
								.then(res => {                                
										store.commit("ADD_TASKS", {tasks: res.data}) 
										store.commit("ADD_EVENTS", {schedules: res1.data})  
								})                       
						})
				}
				else{                              
					instance.get( '/schedule/getmyschedule/'+hash+'/'+ store.state.month_cnt+'/'+sessionStorage.getItem("username"))
					.then(res1 => {   
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
		.catch((res) => {
			store.commit('fail', res)
		})
	},

	getLunInfo: (store) => {
		return instance.get( '/lunar/' + store.state.month_cnt)
		.then(res => {
			let days=res.data.day.filter(el => el.solWeek == '일')
			store.commit('LUN_CAL_INFO', days)
		})
	},

	getHolidayInfo: (store) =>{
		
		return instance.get( '/holiday/' + store.state.month_cnt)        
		.then(res => {              
			if(!(res.data.days === null)){
				store.commit('UPDATE_HOLIDAY',{holi:res.data.days.day})
			}         
		}).catch(res=>{
			console.log("fail",res)
		})
	},

	changeStatus:(store,{schedule,people})=>{
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

		let hash=''		
		if(people===null||typeof people == "undefined")hash=hashCode(sessionStorage.getItem("username"))           
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

	deleteMemo:(store, {memos}) => {

		instance.post('/member/deleteMemo',{
			userId: sessionStorage.getItem('username'),
			memos: memos
		}).then(res => {
			store.commit('UPDATE_MEMO',{memos:res.data})
		})
	},

	addMemo:(store, {memo, no}) => {
		instance.post('/member/addMemo',{
			userId:sessionStorage.getItem('username'),
			memos:[{
				no: no,
				text: memo
			}],
		}).then(res => {        
			store.commit('UPDATE_MEMO',{memos:res.data})
		})
	},

	getMemo:(store) => {     
		instance.get('/member/getMemo/'+sessionStorage.getItem('username'))
		.then(res=>{         
				store.commit('UPDATE_MEMO',{memos:res.data})
		})
	},
}

export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions,
};  