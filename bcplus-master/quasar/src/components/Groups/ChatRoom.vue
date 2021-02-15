<template>
  <div id="SA" class="container" :class="{ 'dark': goDark }">
		<div class="d-flex justify-content-between">
				<h4>{{ chatgroup }}</h4>
				<q-icon 
					:name="mdiExitToApp" 
					@click="gotoList"
					class="icon-btn"
					size="sm"
				/>
		</div>

    <q-separator />

		<!-- <q-scroll-area v-chat-scroll="{always: false, smooth : true}" class="bg-transparent"> -->
    <div v-chat-scroll="{always: false, smooth : true}" class="bg-transparent message-box" style="height: 60vh;">
      <div id="messages">
				<!-- <q-scroll-area v-chat-scroll="{always: true, smooth : true}" class="bg-transparent" style="height: 60vh;"> -->
					<!-- <div> -->
						<!-- <q-list v-if="chat.name==user_name" class="chat my-chat p-2 m-1">
							<p class="m-0">me</p>
							<p class="m-0" style="font-size: 12px; word-break:break-all;">{{ chat.content }}</p>
						</q-list> -->
            <div class="q-pa-md row justify-center">
              <div style="width: 100%; max-width: 210px" v-for="(chat, idx) in chattings" :key="idx">
        
              <q-chat-message
                v-if="chat.name==user_name"
                name="me"
                :text="[chat.content]"
                class="chat-pop"
                bg-color="blue-4"
                sent
              />
              <q-chat-message
                v-else
                :name="[chat.name]"
                :text="[chat.content]"
                bg-color="grey-4"
              />
              </div>
            </div>
						<!-- <q-list v-else class="chat other-chat p-2 m-1">
							<p class="m-0">{{ chat.name }}</p>
							<p class="m-0" style="font-size: 12px; word-break:break-all;">{{ chat.content }}</p>
						</q-list> -->
					<!-- </div> -->
				<!-- </q-scroll-area> -->
			</div>
    </div>
		<!-- </q-scroll-area> -->
    
		<br>

		<div class="bg-transparent" style="height: 20%;">
			<div class="form-group">
				<textarea class="form-control" v-model="text" id="chattingarea" placeholder="chatting..." rows="3" @keyup.enter="uploadChat"></textarea>
			</div>
    </div>
  </div>
</template>

<script>
import { mdiExitToApp } from '@quasar/extras/mdi-v5'

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
		...mapGetters('groups', ["CHATTINGS"])
	},       
	data() {
		return {
			text:'',
			user_name: '',
			chattings: []
		}
	},
  created() {        
		this.mdiExitToApp = mdiExitToApp
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
			this.stompClient.disconnect();
			this.$emit('gotoList')
		},

		uploadChat() {          
			this.send()     
			this.text = null
		},

		send() {
			// console.log("Send message:" + this.message)
			


			if (this.stompClient && this.stompClient.connected) {
				this.text=this.text.replace(/\r\n/g,"");
				// console.log("보낸 메시지:",this.text)
				if(this.text!=="\n"){
					let msg = { 
					'groupName':this.people,
					'name':sessionStorage.getItem("name"),
					'content':this.text, 
				    }              
				    this.stompClient.send("/pub/chat/message", JSON.stringify(msg),{} ) 
				}				
			}
		},
		
 
		connect() {
			if(this.people!==null){
				const serverURL = "https://bcplus.me/socket"
				//let socket = new SockJS(serverURL);
				let socket = new SockJS(serverURL);
				this.stompClient = Stomp.over(socket);
				// console.log(`소켓 연결을 시도합니다. 서버 주소: ${serverURL}`)
				this.stompClient.connect(
					{},
					frame => {
						// 소켓 연결 성공
						this.connected = true;
						// console.log('소켓 연결 성공', frame)
						// 서버의 메시지 전송 endpoint를 구독합니다.               
						this.stompClient.subscribe("/sub/chat/room/"+this.people, res => {
							// console.log('구독으로 받은 메시지 입니다.', res.body)
							// 받은 데이터를 json으로 파싱하고 리스트에 넣어줍니다.
							// console.log("aamsg",res.body)
							this.chattings.push(JSON.parse(res.body))
						});               
					},
					error => {
						// 소켓 연결 실패
						// console.log('소켓 연결 실패', error)
						this.connected = false
						}
					);  
			}		
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

.my-chat {
	color: black; 
	float: right; 
	background: skyblue;
}

.other-chat {
	color: black; 
	float: left; 
	background: lightgrey;
}

.dark {
  background-color: #131E2E !important;
}

.message-box {
	height:300px; overflow:hidden;
}

.message-box:hover {
	overflow-y:auto;
}

/* width */
::-webkit-scrollbar {
  width: 5px;
}

/* Track */
::-webkit-scrollbar-track {
  background: transparent;
}

/* Handle */
::-webkit-scrollbar-thumb {
  background: #888;
}

/* Handle on hover */
::-webkit-scrollbar-thumb:hover {
  background: #555;
}
</style>

<style lang="scss">
.q-message-text:last-child {
  min-height: 30px !important;
  font-size: 0.8rem !important;
}
</style>