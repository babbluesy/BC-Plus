<template>
	<q-card id="SA" class="rounded border">
		<div class="p-3 pb-0">
			<div class="d-flex justify-content-between">
				<h3 class="py-3 px-3">Edit User Information</h3>
				<div style="margin-top: 20px; margin-right: 15px;">
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
			<div class="mt-3 px-3">
				<div class="mb-3">
					<h5>User Name</h5>
					<input type="text" class="form-control" v-model="name" placeholder="User Name"/>
				</div>
				<div class="mb-3">
					<h5>User Birthday</h5>
					<input v-model="birthday" type="date" class="form-control"/>
				</div>
				<div class="mb-3">
						<h5>User Password</h5>
						<small>If you do not want to change your password, please leave it blank.</small>
							<input v-model="pastpw" type="password" placeholder="Past Password" class="form-control mr-1 my-1">
							<input v-model="changepw" type="password" placeholder="Change Password" class="form-control mr-1 mb-1">
							<input v-model="changepw2" type="password" placeholder="Confirm Change Password" class="form-control mb-1">
				</div>
				<!-- <div class="d-flex justify-content-end">
					<button class="btn text-primary" @click="EditAccount" :class="{ 'text-light' : this.$q.dark.isActive }">Edit</button>
				</div> -->
				<div class="absolute-bottom-right m-2">
					<button class="btn text-primary" @click="EditAccount" :class="{ 'text-light' : this.$q.dark.isActive }">Edit</button>
					<button class="btn text-danger mr-1" @click="exit" :class="{ 'text-light' : this.$q.dark.isActive }">cancel</button>
				</div>
				<br>
			</div>
		</div>
	</q-card>
</template>

<script>
import { mdiAccountRemove } from '@quasar/extras/mdi-v5'

import swal from 'sweetalert'
import { mapActions } from 'vuex'

export default {
	data (){
		return {
			name: '',
			birthday: null,
			pastpw: null,
			changepw: null,
			changepw2: null,
		}
	},
	props: {
		userId: String,
		user_name: String,
		user_bday: String,
	},
	methods: {
		...mapActions('accounts', ['editUserDetail', 'deleteAccount']),

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

		EditAccount() {
			if(this.name != null) {
				if (this.pastpw == null && this.changepw == null && this.changepw2 == null) {
					swal('Success', 'Your request is approved', 'success')
					this.editUserDetail({
						name: this.name,
						birthDay: this.birthday,
						pw: this.pastpw
					}).then(()=>{
						sessionStorage.setItem("name", this.name)
						swal('Success', 'Your request is approved', 'success').then(()=>{
							this.$router.go()
						})
					})
					this.$emit('close')
				} else if (this.pastpw != null && this.changepw != null && this.changepw2 != null) {
					if (this.changepw == this.changepw2) {
						this.editUserDetail({
								name: this.name,
								birthDay: this.birthday,
								pw: this.changepw
						}).then(()=>{
							sessionStorage.setItem("name", this.name)
							swal('Success', 'Your request is approved', 'success').then(()=>{
									this.$router.go()
							})
						})
						this.$emit('close')
					} else {
						swal('Error', 'Please check your password.', 'error')
					}
				} else {
					swal('Warning', 'Please check your password!', 'warning')
				}
			} else {
				swal('Warning', 'Please check your username and birthday field!', 'warning')
			}
		},
		exit() {
			this.$emit('close')
		}
	},
	created() {
		this.birthday = this.user_bday,
		this.name = this.user_name
		this.mdiAccountRemove = mdiAccountRemove
	}
}
</script>

<style scoped>

</style>