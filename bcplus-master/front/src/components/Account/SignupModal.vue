<template>
  <div id="SA" class="rounded">
    <div class="modal-dialog" role="document">
      <div class="modal-content" style="width: 600px; border: none;">
        <div class="modal-header">
          <h5 class="modal-title pl-4" id="signupModalLabel">SignUp</h5>
          <button type="button" class="close" data-dismiss="modal" aria-label="Close" @click="exitsu">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <div class="modal-body p-5">
          <div class="form-group">
            <div class="input-group mb-1">
              <div class="input-group-prepend">
                <span class="input-group-text"><i class="fa fa-user" aria-hidden="true"></i></span>
              </div>
              <input v-model="userId" maxlength="25" type="text" class="form-control" id="userId" ref="userId" aria-describedby="emailHelp" placeholder="Account ID" required>
            </div>
          </div>
          <div class="form-group">
            <div class="input-group mb-1">
              <div class="input-group-prepend">
                <span class="input-group-text"><i class="fa fa-user" aria-hidden="true"></i></span>
              </div>
              <input v-model="name" maxlength="30" type="text" class="form-control" id="name" ref="name" aria-describedby="emailHelp" placeholder="Name" required>
            </div>
          </div>
          <div class="form-group">
            <div class="input-group">
              <div class="input-group-prepend">
                <span class="input-group-text"><i class="fa fa-lock" aria-hidden="true"></i></span>
              </div>
              <input v-model="pw" type="password" class="form-control" id="pw" ref="pw" placeholder="Password" required>
            </div> 
          </div>
          <div class="form-group">
            <div class="input-group">
              <div class="input-group-prepend">
                <span class="input-group-text"><i class="fa fa-lock" aria-hidden="true"></i></span>
              </div>
              <input v-model="pw2" type="password" class="form-control" id="pw2" ref="pw2" placeholder="Confirm Password" required>
            </div> 
          </div>      
          <div class="form-group">
            <div class="input-group">
              <div class="input-group-prepend">
                <span class="input-group-text bthday"><i class="fa fa-birthday-cake" aria-hidden="true"></i></span>
              </div>
              <input @keyup.enter="checkHandler" v-model="birthDay" type="date" class="form-control" id="birthDay" ref="birthDay" placeholder="Birthday" required>
            </div> 
          </div> 
          <div class="text-right">
              <!-- 버튼 클릭 시 checkHandler 메서드 호출 -->
              <button type="submit" class="btn w-100" @click="checkHandler">Submit</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import $ from 'jquery';
import swal from 'sweetalert';
import axios from 'axios'
let https = require('https')
const instance = axios.create({
  baseURL: 'http://52.14.190.92/api',
  timeout: 120000,
  httpsAgent: new https.Agent({
    rejectUnauthorized: false
  })
});
export default {
  name: 'SignupModal',
  props: {
    items2: Array,
    
  },
  data() {
    return {
      birthDay: '',
      groups: [
        
      ],
      id: {
        date: new Date(),
        timestamp: 0
      },
      name: '',
      pw: '',
      pw2: '',
      userId: ''
    }
  },
  methods:{
    signupclose(){
      $('#signupModal .close').click();
    },
    checkHandler() {
      // 사용자 입력값 체크하기
      // 작성자, 제목, 내용 
      let isOk = true;
      let isPw = false;
      let birthValid = true;

      if(!this.name || !this.userId || !this.pw || !this.pw2 ){
        isOk = false;
      }
      if ( this.pw !== this.pw2 ){
        isPw = true;
      }
      if (this.birthDay.substring(0, 4) < 1850 || this.birthDay.substring(0, 4) > new Date().getFullYear()) {
        birthValid = false;
      }
      if( isPw == false && isOk && birthValid ){ // 글 작성 진행.
        this.createHandler();
      } else if (isPw){
        swal("Error", 'Passwords are not matching', "error")
      } else if (isOk != true){
        swal("Warning", 'Please fill in the blanks.', 'warning')
      } else if (birthValid != true) {
        swal("Warning", 'Please check the birthday form', 'warning')
      }
    },
    exitsu(){
      this.$emit('close')
    },
    createHandler() {              
      instance.post('/member',
      {
        birthDay: this.birthDay,
        groups: [],
        id: {
          date: new Date(),
          timestamp: 0
        },
        name: this.name,
        pw: this.pw,
        userId: this.userId          
      }
      ).then(({data})=>{
        if (data == 'member insert success') {
          $('#signupModal .close').click();
          swal("Good job!", "Thanks For Sign Up!", "success");
          this.$emit('close')    
        } else {
          swal("Error", 'Oops! Some Error Occurred', 'error')
        }
      }).catch(()=>{
        swal("Warning", "Already Exists", 'warning')
      });
    }
  },
}

</script>

<style scoped>
#logo{
  width:22px;
  height:22px;
  margin-bottom:3px;
}
.bthday {
    padding-left: 4px;
    padding-right: 9px;
    padding-top: 4px;
}
.modal-dialog {
    margin: 0;
}
.modal-body {
    height: 446px;
}
.btn {
  background-color: #131E2E;
  color: white;
}

.btn:hover {
  background-color: #0F274A;
  color: white;
  font-weight: bold;
}
</style>
