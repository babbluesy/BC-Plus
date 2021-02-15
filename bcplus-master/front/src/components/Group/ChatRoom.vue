<template>
  <div id="SA" class="container" :class="{ 'dark': goDark }" style="background-color: #FBFCFE; height: 100%;">
		<div class="d-flex justify-content-between">
				<h4>{{ chatgroup }}</h4>
				<v-btn icon @click="gotoList">
						<v-icon>
								mdi-exit-to-app
						</v-icon>
				</v-btn>
		</div>

    <v-divider class="mt-0"></v-divider>

    <div v-chat-scroll="{always: false, smooth : true}" class="bg-transparent" style="height: 70%; overflow: scroll;">
      <div id="messages">
        <div v-for="(chat, idx) in chattings" :key="idx">
          <v-list v-if="chat.name==user_name" class="chat p-2 m-1" color="blue lighten-3" style="color: black; float: right;">
            <p class="m-0">me</p>
            <p class="m-0" style="font-size: 12px;">{{ chat.content }}</p>
          </v-list>
          <v-list v-else class="chat p-2 m-1" color="grey lighten-3" style="color: black; float: left;">
						<p class="m-0">{{ chat.name }}</p>
						<p class="m-0" style="font-size: 12px;">{{ chat.content }}</p>
					</v-list>
        </div>
			</div>
    </div>
            
		<div class="bg-transparent" style="height: 20%;">
			<div class="form-group">
					<label for="chattingarea">chatting..</label>
					<textarea class="form-control" v-model="text" id="chattingarea" placeholder="chatting..." rows="3" @keyup.enter="uploadChat"></textarea>
			</div>
    </div>
  </div>
</template>

<script>
import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'
import { mapGetters } from 'vuex'
import axios from 'axios'
let https = require('https')

const instance = axios.create({
  baseURL: 'https://bcplus.me/api',
  timeout: 120000,
  httpsAgent: new https.Agent({
    rejectUnauthorized: false
  })
});


export default {    
	props: {
			chatgroup: String,
			groups: Array,
			people: String,
			chat: Boolean,       
			goDark: Boolean,
			// group_name: String,
	},
	computed:{
		...mapGetters(["CHATTINGS" ])
	},       
	data() {
		return {
			text:'',
			user_name: '',
			chattings: []
		}
	},
  created() {        
		this.connect();
		this.chattings=this.CHATTINGS

		this.token=sessionStorage.getItem("access-token")
		instance.get('/member/'+ sessionStorage.getItem("username"))
		.then(res =>{
				sessionStorage.setItem("birth", res.data.birthDay)
				this.group_list = res.data.groups
				this.user_name = res.data.name
				this.birth = this.$moment(res.data.birthDay).format('YYYY-MM-DD');
		})
  },
  methods: {
		gotoList() {
			this.$emit('gotoList')
		},

		uploadChat() {          
			this.send()     
			this.text = null
		},

		send() {
			console.log("Send message:" + this.message)

			if (this.stompClient && this.stompClient.connected) {
				let msg = { 
					'groupName':this.people,
					'name':sessionStorage.getItem("name"),
					'content':this.text, 
				}              
				this.stompClient.send("/pub/chat/message", JSON.stringify(msg),{} )     
			}
		},

		connect() {
			const serverURL = "https://bcplus.me/socket"
			let socket = new SockJS(serverURL);
			this.stompClient = Stomp.over(socket);
			console.log(`소켓 연결을 시도합니다. 서버 주소: ${serverURL}`)
			this.stompClient.connect(
				{},
				frame => {
					// 소켓 연결 성공
					this.connected = true;
					console.log('소켓 연결 성공', frame)
					// 서버의 메시지 전송 endpoint를 구독합니다.               
					this.stompClient.subscribe("/sub/chat/room/"+this.people, res => {
						console.log('구독으로 받은 메시지 입니다.', res.body)
						// 받은 데이터를 json으로 파싱하고 리스트에 넣어줍니다.
						console.log("aamsg",res.body)
						this.chattings.push(JSON.parse(res.body))
					});               
				},
				error => {
					// 소켓 연결 실패
					console.log('소켓 연결 실패', error)
					this.connected = false
					}
				);        
    }
  },    
   
}
</script>

<style lang="css" scoped>
.inner {
  position: absolute;
  bottom: 0;
  width: 100%;
    overflow: auto;
}

.chat {
    border-radius: 1rem; 
    width: 80%;
}

.dark {
    background-color: #131E2E !important;
}
</style>