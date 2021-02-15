<template>
  <v-app id="inspire">
    <div id="SA" class="rounded border p-2">
      <br>
      <div class="container wholebox">
        <div class="d-flex justify-content-end mr-2">
          <!-- 계정 수정 및 삭제 -->
          <div v-if="this.ggl=='false'">
            <v-tooltip bottom>
              <template v-slot:activator="{ on, attrs }">
                <v-btn
                  class="mr-2"
                  v-bind="attrs"
                  v-on="on"
                  small
                  icon
                  @click="EditMypage"
                >
                  <v-icon class="outlined" >mdi-pencil</v-icon>
                </v-btn>
              </template>
              <span id="SA">Edit</span>
            </v-tooltip>
          </div>
          <v-tooltip bottom color="white">
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                class="mr-2"
                v-bind="attrs"
                v-on="on"
                small
                icon
                @click="deleteAccount"
              ><v-icon class="outlined text-danger">mdi-account-remove</v-icon></v-btn>
            </template>
            <span id="SA" class="text-danger">Delete Account</span>
          </v-tooltip>
        </div>
        <section>
          <div class="p-3">
            <div class="mb-3 ml-3" style="height: 80px; width: 80px">
              <Gravatar :email="userId" />
            </div>
            <div v-if="userId" class="namearea mt-1 pl-3">
              <h4><strong id="SA" class="font-weight-bold h3">{{ userId }}</strong></h4>
            </div>
            <div v-if="userId" class="namearea mt-1 pl-3">
              <h6 id="SA">{{ user_name }}</h6>
            </div>
            <div v-else id="SA" class="namearea mt-1 pl-3">
              <h4><strong>GUEST USER</strong></h4>
            </div>
            <div class="profilearea pl-3 mb-0">
              <v-icon>mdi-cake-variant</v-icon> {{ this.birth}}
            </div>
          </div>
        </section>
      </div>
      <br>
    </div>
  </v-app>
</template>

<script>
import Gravatar from 'vue-gravatar'
import EditMypage from '@/components/Account/EditMypage.vue'
import swal from 'sweetalert'

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
  name: 'MyPage',
  data: () => ({
    group_list: [],
    user_name: '',
    birth: null,
  }),
  components: {Gravatar},
  props: {
    userId: String,
    ggl: String,
  },
  methods: {
    deleteAccount() {
      swal({
        text: "Are you sure to delete this account?",
        icon:"info",
        buttons: true,
        dangerMode: true,
      })
      .then((willDelete) => {
        if (willDelete) {
          this.$store.dispatch("deleteaccount")
          swal("your request is approved", {
            icon: "success",
          });
          this.$emit('close')
          this.$router.push("/")
        } else {
          this.$emit('close')
        }
      });
    },

    EditMypage() {
      this.$modal.show(EditMypage, {
        userId: this.userId,
        user_name: this.user_name,
        user_bday: this.birth,
        modal: this.$modal},{
            height: 'auto'
      })
      this.$emit('close')
    },

    backtomain() {
      this.$emit('close')
    }
  },

  created: function(){
    this.token=sessionStorage.getItem("access-token")
    instance.get('/member/'+ sessionStorage.getItem("username"))
      .then(res =>{
        sessionStorage.setItem("birth", res.data.birthDay)
        this.group_list = res.data.groups
        this.user_name = res.data.name
        this.birth = this.$moment(res.data.birthDay).format('YYYY-MM-DD');
      })
  }
}

</script>

<style scoped>
  div.mypage {
    border: 3px solid black;
    padding: 100px;
  }

  div.card-header {
    text-align: left;
  }

  h5.card-title {
    text-align: left;
  }

  .bg-cover {
    background-image: url(https://files.123freevectors.com/wp-content/original/110440-black-and-blue-blurred-background-vector.jpg);
    background-size: cover;
    height: 250px;
    border-top-left-radius: 10px 10px;
    border-top-right-radius: 10px 10px;
    width: 100%;
  }

  .settingbtn {
    float: right;
    margin: 10px;
    border: none;
    outline: none;
    background-color: rgba(0,0,0,0);
    padding: 5px;
    border-radius: 5px;
    cursor: pointer;
    color: gray;
  }

  .wholebox {
    padding: 0px;
    border-top-left-radius: 10px 10px;
    border-top-right-radius: 10px 10px;
    border-bottom-left-radius: 10px 10px;
    border-bottom-right-radius: 10px 10px;
  }

  .fsec {
    cursor: pointer;
  }

  .box {
    width: 50px;
    height: 50px; 
    border-radius: 70%;
    overflow: hidden;
  }

  .profile {
      width: 100%;
      height: 100%;
      object-fit: cover;
  }

  .namearea {
    text-align: left;
  }
  
  .profilearea {
    text-align: left;
  }
</style>