import axios from 'axios'
import router from '../../router/index.js'
import swal from 'sweetalert'

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
	userInfo: null,
	events: [],
	isLogin: null,
  isLoginError: null,
}

// getters
const getters = {
  
}

// mutations
const mutations = {
  ADD_USER: (state, event) => {
		state.userInfo=event
	},

	loginSuccess(state, payload) {
		state.isLogin = true
		state.isLoginError = false
		state.userInfo = payload
	},

	LoginError(state) {
		state.isLogin = false
		state.isLoginError = true
	},

	logout(state) {
		state.isLogin = false
		state.isLoginError = false
		state.userInfo = null
		state.token = null
		delete sessionStorage.token
		delete sessionStorage.userId
		swal('Success', 'Logout Success', 'success')
	},
}

// actions
const actions = {
  Login: (store, {userId, pw}) => {
    return instance.post('/user/login',{
			userEmail: userId,
			userPw: pw
		})
			.then(res => {
				let token  = res.headers["authorization"]
				store.commit('ADD_USER', res.data[0]);
				sessionStorage.setItem("access-token", token)
				sessionStorage.setItem("username", res.data[0].userId)
				sessionStorage.setItem("name", res.data[0].name) 
				sessionStorage.setItem("isGoogle",false)
				router.push('/calendar')
			})
			.catch(() => {
				swal('Error', "Incorrect account information",'error')
				store.commit("LoginError")
			});
	},

	Logout: (store) => {
		sessionStorage.removeItem("access-token")
		sessionStorage.removeItem("username")
		store.commit("logout")
	},

	deleteAccount: (store) => {
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

	editUserDetail:(store, {name, birthDay, pw})=>{
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

export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions,
};  