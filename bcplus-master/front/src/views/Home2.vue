<template>
  <div id="SA">
    <!-- v-if="this.$vuetify.breakpoint.xs" -->
    <div id="home2">
      <div class="overlay">
        <video playsinline="playsinline" autoplay="autoplay" muted="muted" loop="loop">
          <source src="@/assets/main1.mp4" type="video/mp4">
        </video>
        <div class="masthead" style="width: 35rem;">
          <div class="masthead-bg"></div>
          <div class="container h-100">
            <div class="row h-100">
              <div class="col-12 my-auto">
                <div class="masthead-content text-white py-5 py-md-0">
                  <h3 id="SA" class="mb-3"><span id="SA" class="text-danger h1 font-weight-bold">B</span>USINESS <span id="SA" class="text-danger h1 font-weight-bold">C</span>ALENDAR</h3>
                  <h1 id="PG" class="text-danger">BC+</h1>
                  <h5 id="SA" style="font-weight: 350;">Connect <span id="SA" class="text-warning">Business</span> and <span id="SA" class="text-success">Calendar</span></h5>
                  <br>
                  <strong>Business Group Calendar</strong>
                  <p id="SA" class="mb-5 font-weight-bold d-flex justify-content-center">for<span id="SA" class="font-italic ml-2">Office Workers</span></p>
                  <div id="SA" class="input-group input-group-newsletter">
                    <button class="btn rounded text-white mt-5" type="button" id="submit-button" @click="goEx" style="font-size: 1.1rem; background-color: #131E2E;">What is <strong style="color: red;">BC+</strong> ?</button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="social-icons">
          <ul class="list-unstyled text-center">
            <li class="list-unstyled-item my-3">
              <a style="text-decoration: none;" href="#" @click="goLogin">
                <v-icon large dark>mdi-login</v-icon>
              </a>
            </li>
            <li class="list-unstyled-item my-3">
              <a style="text-decoration: none;" href="#" @click="openSignup">
                <v-icon large dark>mdi-account-plus</v-icon>
              </a>
            </li>
            <li class="list-unstyled-item my-3">
              <a style="text-decoration: none;" href="#" @click="googleLogin">
                <i class="fa fa-google" ></i>
              </a>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script src="js/coming-soon.min.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js">
  </script>
  <script src="https://apis.google.com/js/client:platform.js?onload=start" async defer>
  </script>
<script>
import SignupModal from '@/components/Account/SignupModal.vue'
import LoginModal from '@/components/Account/LoginModal.vue'
import HelpModal from '@/components/Others/HelpModal.vue'
import axios from 'axios'
import swal from 'sweetalert'
import qs from "qs";
let url=require('url')
let https = require('https')
const instance = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 120000,
  httpsAgent: new https.Agent({
    rejectUnauthorized: false
  })
});
const CLIENT_ID = "367773692104-ldevrvvo0gghju43m8cc1m6as95nkenn.apps.googleusercontent.com";
const AUTHORIZE_URI = "https://accounts.google.com/o/oauth2/v2/auth";

const queryStr = qs.stringify({
  client_id: CLIENT_ID,
  redirect_uri: "http://localhost:8080/api/token",
  response_type: "code",
  scope: "profile email https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/calendar",
  prompt: "consent",
  access_type : "offline",
  include_granted_scopes:true
});
const loginUrl = AUTHORIZE_URI + "?" + queryStr;
let queryData = url.parse(window.location.href, true).query;
console.log(queryData)
let auth_code=queryData.code;
console.log(auth_code)

export default {
  name: 'home2',
  components: {
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
    googleLogin()
    {
        //    this.$gAuth.getAuthCode()
        //   .then(authCode => {
        //     //on success
        //     console.log(authCode)
        //     instance.post('/token',{ code: authCode, redirect_uri: 'postmessage' }).then(res=>{
        //       let token=res.headers["authorization"]           
        //       this.$store.commit('ADD_USER', res.data);
        //       sessionStorage.setItem("access-token", token)
        //       sessionStorage.setItem("username", res.data.userId)
        //       sessionStorage.setItem("name", res.data.name)
        //       sessionStorage.setItem("isGoogle",true)
        //       this.$router.push('/calendar')
        //   })
        // })
        // .catch(()=>{
        //   swal('Error', 'chrome://settings/cookies\nPlease allow incognito mode cookie restrictions', 'error')
        // })
        alert("인증시작!!!!")
       
        if (!auth_code) {
          window.location.assign(loginUrl)
        }
    }
  },
  created (){
  //   if(auth_code){
  //  instance.post('/token',{ code: auth_code, redirect_uri: 'http://localhost:8082/' }).then(res=>{
  //             let token=res.headers["authorization"]           
  //             this.$store.commit('ADD_USER', res.data);
  //             sessionStorage.setItem("access-token", token)
  //             sessionStorage.setItem("username", res.data.userId)
  //             sessionStorage.setItem("name", res.data.name)
  //             sessionStorage.setItem("isGoogle",true)
  //             this.$router.push('/calendar')
  //   })
  //   }
  },
}
</script>

<style scoped>
html {
  height: 100%;
}

#home2{
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
  z-index: 1;
}

h1,
h2,
h3,
h4,
h5,
h6 {
  font-weight: 700;
}

video {
  position: fixed;
  top: 50%;
  left: 50%;
  min-width: 100%;
  min-height: 100%;
  width: auto;
  height: auto;
  transform: translateX(-50%) translateY(-50%);
  opacity: 0.7;
  z-index: 0;
}

@media (pointer: coarse) and (hover: none) {
  body video {
    display: none;
  }
}

.overlay {
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
  background-color:rgba(0, 46, 102, 0.3);
  z-index: 1;
}

.masthead {
  position: relative;
  overflow: hidden;
  padding-bottom: 3rem;
  z-index: 2;
}

.masthead .masthead-bg {
  position: absolute;
  top: 0;
  bottom: 0;
  right: 0;
  left: 0;
  width: 100%;
  min-height: 35rem;
  height: 100%;
  background-color: rgba(0, 46, 102, 0.9);
  transform: skewY(4deg);
  transform-origin: bottom right;
}

.masthead .masthead-content h1 {
  font-size: 2.5rem;
}

.masthead .masthead-content p {
  font-size: 1.2rem;
}

.masthead .masthead-content p strong {
  font-weight: 700;
}

.masthead .masthead-content .input-group-newsletter input {
  height: auto;
  font-size: 1rem;
  padding: 1rem;
}

.masthead .masthead-content .input-group-newsletter button {
  font-size: 0.8rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 1px;
  padding: 1rem;
}

@media (min-width: 768px) {
  .masthead {
    height: 100%;
    min-height: 0;
    width: 40.5rem;
    padding-bottom: 0;
  }
  .masthead .masthead-bg {
    min-height: 0;
    transform: skewX(-8deg);
    transform-origin: top right;
  }
  .masthead .masthead-content {
    padding-left: 3rem;
    padding-right: 10rem;
  }
  .masthead .masthead-content h1 {
    font-size: 3.5rem;
  }
  .masthead .masthead-content p {
    font-size: 1.3rem;
  }
}

.social-icons {
  position: absolute;
  margin-bottom: 2rem;
  width: 100%;
  z-index: 2;
}

.social-icons ul {
  margin-top: 2rem;
  width: 100%;
  text-align: center;
}

.social-icons ul > li {
  margin-left: 1rem;
  margin-right: 1rem;
  display: inline-block;
}

.social-icons ul > li > a {
  display: block;
  color: white;
  background-color: rgba(0, 46, 102, 0.8);
  border-radius: 100%;
  font-size: 2rem;
  line-height: 4rem;
  height: 4rem;
  width: 4rem;
}

@media (min-width: 768px) {
  .social-icons {
    margin: 0;
    position: absolute;
    right: 2.5rem;
    bottom: 2rem;
    width: auto;
  }
  .social-icons ul {
    margin-top: 0;
    width: auto;
  }
  .social-icons ul > li {
    display: block;
    margin-left: 0;
    margin-right: 0;
    margin-bottom: 2rem;
  }
  .social-icons ul > li:last-child {
    margin-bottom: 0;
  }
  .social-icons ul > li > a {
    transition: all 0.2s ease-in-out;
    font-size: 2rem;
    line-height: 4rem;
    height: 4rem;
    width: 4rem;
  }
  .social-icons ul > li > a:hover {
    background-color: #002E66;
  }
}

.btn-secondary {
  background-color: #cd9557;
  border-color: #cd9557;
}

.btn-secondary:active, .btn-secondary:focus, .btn-secondary:hover {
  background-color: #ba7c37 !important;
  border-color: #ba7c37 !important;
}

.input {
  font-weight: 300 !important;
}
</style>