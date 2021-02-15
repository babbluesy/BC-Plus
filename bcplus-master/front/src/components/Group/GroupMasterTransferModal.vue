<template>
  <v-app id="inspire">
      <div id="SA">
        <div class="rounded border p-2">
          <h3 class="py-3 px-3">Transfer Permission</h3>
          <div class="mt-3 py-3 px-3">
            <h5 id="SA">Enter the user ID</h5>
            <input type="text" class="form-control" v-model="userid" placeholder="Group master will be switch to this user"/>
            <div class="d-flex justify-content-end mt-5">
              <button class="btn text-primary" @click="grant()">Submit</button>
            </div>
          </div>
        </div>
      </div>
  </v-app>
</template>

<script>
import swal from 'sweetalert'

export default {
  name:"Grant",
  props:{
    group:String
  },
  data:function(){
    return{
      userid:null
    } 
  },
  methods:{
    grant(){
      this.$store.dispatch("GRANT_EVENT",{userid:this.userid, groupid:this.group});
      swal('Success', "Your request is approved", 'success')
      this.$emit('close')
    }
  }
}
</script>
<style scoped>
</style>