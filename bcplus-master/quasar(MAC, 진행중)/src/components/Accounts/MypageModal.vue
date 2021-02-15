<template>
	<q-card id="SA" class="rounded border p-2">
		<br>
		<div class="container wholebox">
			<div class="d-flex justify-content-end mr-2">
				<!-- 계정 수정 및 삭제 -->
				<div v-if="this.ggl=='false'">
					<q-icon 
						class="icon-btn m-1"
						@click="EditMypage"
						:name="mdiPencil" 
						size="sm"
					/>
					<q-tooltip>
						Edit
					</q-tooltip>
				</div>

				<div>
					<q-icon 
						class="icon-btn m-1 text-danger"
						@click="del_Account"
						:name="mdiAccountRemove" 
						size="sm"
					/>
					<q-tooltip class="text-danger">
						Delete Account
					</q-tooltip>
				</div>
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
							<q-icon :name="mdiCakeVariant"/> {{ this.birth }}
					</div>
				</div>
			</section>
		</div>
		<br>
	</q-card>
</template>

<script>
import Gravatar from 'vue-gravatar'
import EditMypageModal from '../../components/Accounts/EditMypageModal.vue'
import swal from 'sweetalert'
import { mdiPencil, mdiAccountRemove, mdiCakeVariant } from '@quasar/extras/mdi-v5'
import { mapActions } from 'vuex'
import axios from 'axios'

let https = require('https')
const instance = axios.create({
  baseURL: 'https://bcplus.me/api',
  timeout: 120000,
  httpsAgent: new https.Agent({
    rejectUnauthorized: false
  })
})

export default {
  name: 'MyPage',
  data: () => ({
    group_list: [],
    user_name: '',
    birth: null,
  }),
  components: { Gravatar },
  props: {
    userId: String,
    ggl: String,
  },
  methods: {
		...mapActions('accounts', ['deleteAccount']),
		
    del_Account() {
      swal({
        text: "Are you sure to delete this account?",
        icon:"info",
        buttons: true,
        dangerMode: true,
      })
      .then((willDelete) => {
        if (willDelete) {
          this.deleteAccount()
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
      this.$modal.show(EditMypageModal, {
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

  created() {
    this.token=sessionStorage.getItem("access-token")
    instance.get('/member/'+ sessionStorage.getItem("username"))
		.then(res =>{
			sessionStorage.setItem("birth", res.data.birthDay)
			this.group_list = res.data.groups
			this.user_name = res.data.name
			this.birth = this.$moment(res.data.birthDay).format('YYYY-MM-DD');
		})

		this.mdiPencil = mdiPencil
		this.mdiAccountRemove = mdiAccountRemove
		this.mdiCakeVariant = mdiCakeVariant
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

	.icon-btn {
		cursor: pointer;
	}
</style>