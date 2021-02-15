<template>
  <div id="SA">
    <div>
      <div class="">
        <div class="d-flex justify-content-center">
          <q-img
            src="../../assets/PS_A02.png"
            spinner-color="white"
            style="height: 35%; width: 35%;"
          />
        </div>
        <br>
        <div class="d-flex justify-content-center">
          <h1 style="color: #338aff;">My Personal Secretary</h1>
        </div>
        <div class="d-flex justify-content-center ">
          <h2 class="main-title">P.S. Calendar</h2>
        </div>
      </div>

      <div class="d-flex justify-content-center">
        <div class="mobile-btn mobile-btn-bg1 m-3">
          <a href="#" @click="goLogin">
            <q-icon class="m-3 mb-4" :name="mdiLogin" color="white" size="md" />
          </a>
        </div>
        <div class="mobile-btn mobile-btn-bg2 m-3">
          <a href="#" @click="openSignup">
            <q-icon class="m-3 mb-4" :name="mdiAccountPlus" color="white" size="md" />
          </a>
        </div>
        <!-- ios앱은 이거 지우면 됨 -->
        <!-- <div class="mobile-btn mobile-btn-bg3 m-3">
          <a href="#" @click="googleLogin">
            <q-icon class="m-3 mb-4" :name="fabGoogle" color="white" size="md" />
          </a>
        </div> -->
        <!-- ios앱은 이거 지우면 됨 -->
      </div>

      <!-- <div class="mt-4">
        <a class="d-flex justify-content-center" href="https://www.notion.so/P-S-Private-Secretary-2020-10-12-9a6d92ea90a546cc95ffee6ffedd6f2c" style="color: grey;">Why P.S Calendar?</a>
      </div> -->

      <!-- <div class="container d-flex justify-content-center">
        <a href="#Main1"><button class="btn" style="border: 1px solid blue">Why P.S. Calendar?</button></a>
      </div>
    </div>

    <div id="Main1" style="margin-bottom: 2000px;">
      <div class="container d-flex">
        <div>

        </div>
      </div> -->
    </div>
  </div>
</template>

<script>
import { mdiAccountPlus, mdiLogin } from '@quasar/extras/mdi-v5'
import { fabGoogle } from '@quasar/extras/fontawesome-v5'

import LoginModal from '../Accounts/LoginModal.vue'
import SignupModal from '../Accounts/SignupModal.vue'

import axios from 'axios'
import qs from "qs";

// var ipcRenderer = require('electron').ipcRenderer;

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
  redirect_uri: "http://localhost:8080",
  response_type: "code",
  scope: "profile email https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/calendar",
  prompt: "consent",
  access_type : "offline",
  include_granted_scopes:true
});
const loginUrl = AUTHORIZE_URI + "?" + queryStr;
export default {
  name: 'home2',
  components: {
  },
  data() {
    return {

    }
  },
  created() {
    this.mdiAccountPlus = mdiAccountPlus
    this.mdiLogin = mdiLogin
    this.fabGoogle = fabGoogle
  },
  methods: {
    goLogin() {
      this.$modal.show(LoginModal, {
        modal: this.$modal},{
          height: 'auto',
      })
    },
    openSignup() {
      this.$modal.show(SignupModal, {
        modal: this.$modal},{
            height: 'auto'
      })
    },
    goEx() {
      this.$modal.show(HelpModal, {
        modal: this.$modal},{
            height: '700px',
            width: '1200px'
      })
    },
    googleLogin() {
			// ipcRenderer.on('asynchronous-reply', (event, arg) => {
			// 	console.log(arg,"adadwdaw") // "pong"이 출력됩니다.
			// 	instance.post('/token',{ code: arg, redirect_uri: 'http://localhost' }).then(res=>{
			// 		let token=res.headers["authorization"]           
			// 		this.$store.commit('ADD_USER', res.data);
			// 		sessionStorage.setItem("access-token", token)
			// 		sessionStorage.setItem("username", res.data.userId)
			// 		sessionStorage.setItem("name", res.data.name)
			// 		sessionStorage.setItem("isGoogle",true)
			// 		this.$router.push('/calendar')
			// 	})
			// })
      // ipcRenderer.send('asynchronous-message', 'createAuthWindow');
          this.$gAuth.getAuthCode()
          .then(authCode => {
            //on success
            // console.log(authCode)
            instance.post('/token',{ code: authCode, redirect_uri: 'postmessage' }).then(res=>{
              let token=res.headers["authorization"]           
              this.$store.commit('ADD_USER', res.data);
              sessionStorage.setItem("access-token", token)
              sessionStorage.setItem("username", res.data.userId)
              sessionStorage.setItem("name", res.data.name)
              sessionStorage.setItem("isGoogle",true)
              this.$router.push('/calendar')
          })
        })
        .catch(()=>{
          swal('Error', 'chrome://settings/cookies\nPlease allow incognito mode cookie restrictions', 'error')
         })
    }
  }
}
</script>

<style scoped>
/* .main-bg {
	background-image: url('../../assets/mobile_bg.jpg');
	width: 100%;
	height: 100vh;
} */

.social-icons {
	margin: 0;
	position: absolute;
	right: 2.5rem;
	bottom: 2rem;
	width: auto;
}

</style>

<style scoped>
.mobile-btn {
	display: block;
  color: white !important;
  /* background-color: rgba(0, 46, 102, 0.8); */
  border-radius: 100%;
  font-size: 2rem;
  line-height: 4rem;
  height: 4rem;
	width: 4rem;
	padding-right: 5px;
}

.mobile-btn-bg1 {
	background: linear-gradient( to right, #503fff, #338aff );
	/* background: #503fff; */
}

.mobile-btn-bg2 {
	background: linear-gradient( to right, #338aff, #2ec4f9);
	/* background: #21bcff; */
}

.mobile-btn-bg3 {
	background: linear-gradient( to right, #2ec4f9, #94f6d2 );
	/* background: #94f6d2; */
}

.main-title {
	background: -webkit-linear-gradient( #503fff, #94f6d2 );
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent; font-size: 90px;
}

.zoom-move{
  transition: transform .3s ease-out;
}
/* 
.main-page {
  margin: 0px auto;
} */

</style>

<style lang="scss">
.zoom-move{
  transition: transform .3s ease-out;
}
</style>