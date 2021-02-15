<template>
<v-app id="inspire">
    <div id="SA" class="rounded border">
        <div class="p-3 pb-0">
            <h3 class="py-3 px-3">Edit User Information</h3>
            <div class="mt-3 py-3 px-3">
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
                    <div class="d-flex justify-content-end">
                        <button class="btn text-primary" @click="joinGroup">Edit</button>
                    </div>
                <br>
            </div>
        </div>
    </div>
</v-app>
</template>

<script>
import swal from 'sweetalert'

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
        joinGroup() {
            if(this.name != null) {
                if (this.pastpw == null && this.changepw == null && this.changepw2 == null) {
                swal('Success', 'Your request is approved', 'success')
                this.$store.dispatch("edituserdetail", {
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
                        this.$store.dispatch("edituserdetail", {
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
    },
    created() {
        this.birthday = this.user_bday,
        this.name = this.user_name
    }
}
</script>

<style scoped>

</style>