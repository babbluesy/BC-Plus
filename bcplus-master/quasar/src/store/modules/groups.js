import axios from 'axios'
import router from '../../router/index.js'
import calendar from './calendar.js'

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
	GROUPS: state => state.groups,
	USER: state => state.userInfo,
	GROUP_NAME: state => state.group_name,
	LUN_DAYS: state => state.lun_day,
	TASKS: state => state.tasks,
	MEMBER_INFO:state=> state.member_info,
	CHATTINGS:state=>state.chattings,
	MEMOS:state=>state.memos,
}

// mutations
const mutations = {
	UPDATE_GROUP: (state, { groupName, groupDetail, hashCode }) => {
		let index = state.groups.findIndex(_group => _group.hashCode == hashCode)

		state.groups[index].groupName = groupName;
		state.groups[index].groupDetail = groupDetail;
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

	DELETE_GROUP: (state, {groupid}) => {
		const itemToFind = state.groups.find(function(item) {return item.groupName === groupid})
		const idx = state.groups.indexOf(itemToFind)
		if (idx > -1) state.groups.splice(idx, 1)
		if (idx > -1) state.group_name.splice(idx, 1)
		state.events=[]
		state.tasks=[]
		// calendar.state.events=[]
		// calendar.state.tasks=[]
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

	ADD_MSG:(state,{data})=>{
		state.chattings=data
		
	},

}

// actions
const actions = {
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
			webhooks:[],
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

	getGroupInfo: (store) => {
		return instance.get('/group/all/'+ sessionStorage.getItem("username"))
		.then(res =>{
			// console.log('getgroupinfo', res.data)
			store.commit("ADD_GROUPS", {groups: res.data})
		})
	},

	ShowGroupInfo: (store) => {
		sessionStorage.setItem("groupinfo", store)
		store.commit('ShowGroupInfo', sessionStorage.getItem("groupinfo"))
		instance.get('/group/')
		.then({})
	}, 

	JoinGroup_Invite:(store,{group_code})=>{      
		instance.get( '/group/invite/' + sessionStorage.getItem("username")+'/'+group_code)
		.then(res => {
				store.commit("ADD_GROUP", {groups:res.data})
		}).catch(()=>{
				swal('Error', "Group doesn't exist!", 'error')
		})
	},

	deleteGroup: (store, {groupid}) => {
		instance.delete( '/group/' + groupid)
				.then( ()=>{
						store.commit("DELETE_GROUP", {groupid: groupid})
		})
	},

	addMember:(store,{members,sid,people})=>{
		let memberList=[]
		for(let i=0;i<members.length;i++){           
				memberList.push(members[i])
		}
		
		instance.post('/schedule/addMem',{
			"members":memberList,
			'sid':sid          
		})
		.then(res => {
				if(res.data==='success'){
						instance.get( '/schedule/getschedule/'+people+'/'+ store.state.month_cnt+'/'+sessionStorage.getItem("username"))
						.then(res1 =>{   
								instance.get( '/schedule/getTodaySchedule/'+people)
								.then(res =>{ 
									
										
										calendar.mutations.ADD_TASKS(calendar.state, {tasks: res.data})
										calendar.mutations.ADD_EVENTS(calendar.state, {schedules: res1.data})
										
								})                     
						})
				}
			 
		}).catch(()=>{
				swal('Error', "It includes members who have already left.", 'error')
				// router().reload();
		})
	},

	editGroup: (store, {groupid, gdtail, group_hash}) => {       
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

	getMemberList:(store,{groupName})=>{
		if(!(groupName===null)){
			instance.get('/group/getMemberList/'+groupName).then(res=>{
					store.commit('UPDATE_MEMBER_INFO',{name:groupName,groups:res.data});

			})
		}       
	},
	
	getMessage:(store,{groupName})=>{
		if(groupName!==null){
			instance.get("/group/getMessage/"+groupName).then(res=>{
			
				store.commit('ADD_MSG',{data:res.data });
			})
		}		
	},

	addWebHook: (store,{groupName,webhook})=>{
		console.log(store);
		instance.post('/group/addWebHook',{
			groupName:groupName,
			webhook:webhook
		})
	}

}

export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions,
};  