<template>
  <div id="app" style="height: 500px;">
    <div id="SA" class="container main-container">
      <h1>Login</h1>
      <div class="container">
        <div class="container">
          <div>
            <input v-model="userId" maxlength="25" type="text" placeholder="Enter Account ID" class="form-control" name="userId" required>
          </div>
          <div>
            <input @keyup.enter="login" v-model="pw" type="password" placeholder="Enter Password" class="form-control" name="pw" required>
          </div>
          <button class="btnlgn btn-light btn-lg mb-0 text-white" style="background-color: #131E2E;" @click="login">Login</button>
          <div class="col four">
            <a href="#" class="btn btn-google-plus btn-lg mt-0" @click="googleLogin"><i class="fa fa-google"></i>  Sign in with Google</a>	
          </div>
        </div>
      </div>
      <hr class="line1 mb-4">
      <div>
        <p class="mb-0">New to BC+? Create an account.</p>
        <button type="button" class="btn btn-success btn-lg" @click="SignupModal">signup</button>
      </div>
    </div>
  </div>  
</template>

<script>
import SignupModal from '@/components/Account/SignupModal.vue'
import swal from 'sweetalert';
import axios from 'axios'
import * as tunnel from 'tunnel';

const agent = tunnel.httpsOverHttp({
  proxy: {
    host: '52.14.190.92',
    port: 8080,
  },
});
const instance = axios.create({
  baseURL: 'https://bcplus.me/api',
  timeout: 120000,
  httpsAgent: agent,
});

export default {
  name: 'Login',
  components: {
  },

  data(){
    return {
      birthDay: '',
      groups: [],
      id: {
        date: new Date(),
        timestamp: 0
      },
      name: '',
      pw: '',
      userId: '',
      token:'',
      isInit: false,
      isSignIn: false
    };
  },

  methods: {
    login() {
      if (this.userId == '') {
        swal('Warning', 'Please enter your ID', 'warning')
      } else if (this.pw == '') {
        swal('Warning', 'Please enter your password', 'warning')
      } else {
        this.$store.dispatch("loginVuex", {
          userId: this.userId,
          pw: this.pw
        })
        this.$emit('close')
      }
    },

    SignupModal() {
      this.$modal.show(SignupModal, {
        modal: this.$modal},{
          height: 'auto',
        }
      )
      this.$emit('close')
    },

    googleLogin()
    {
       this.$gAuth.getAuthCode()
      .then(authCode => {
        instance.post('/token',{ code: authCode, redirect_uri: 'postmessage' }).then(res=>{
          let token=res.headers["authorization"]           
          this.$store.commit('ADD_USER', res.data);
          sessionStorage.setItem("access-token", token)
          sessionStorage.setItem("username", res.data.userId)
          sessionStorage.setItem("name", res.data.name)
          this.$emit('close')
          this.$router.push('/calendar')
      })
    })
    }
  }
}
</script>

<style scoped>
/* Full-width inputs */
input {
  width: 300px;
  padding: 12px 20px;
  margin: 8px 0;
  display: inline-block;
  border: 1px solid #ccc;
  box-sizing: border-box;
}

/* Set a style for all buttons */
button {
  margin: 8px 0;
  border: none;
  cursor: pointer;
  width: 300px;
}

/* Add a hover effect for buttons */
button:hover {
  opacity: 0.8;
}

/* Add padding to containers */
.container {
  padding: 16px;
}

.main-container {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.line1 {
  height:1;
  width: 300px;
  margin: 0 auto;
}

.btn.btn-google-plus{
  margin: 8px 0;
  border-color: lightgray;
  cursor: pointer;
  width: 300px;
  }

.btn.btn-google-plus:active{
  top: 3px; 
  outline: none; 
  -webkit-box-shadow: none; 
  box-shadow: none;
}

.btnlgn {
  background-color: #002f76;
  color: white;
}
</style>