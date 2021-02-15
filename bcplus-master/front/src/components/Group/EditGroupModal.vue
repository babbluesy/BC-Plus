<template>
<v-app id="inspire">
  <div id="SA" class="rounded border p-2">
    <div class="d-flex justify-content-between">
      <h3 class="py-3 px-3">Group Info</h3>
      <div class="mt-3">
        <v-tooltip bottom color="white">
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              class="mr-2"
              color="white"
              v-bind="attrs"
              v-on="on"
              icon
              @click="editGroup"
            ><v-icon class="text-success outlined" >mdi-check</v-icon></v-btn>
            </template>
            <span id="SA" class="text-dark">Edit Group Info</span>
        </v-tooltip>
        </div>
        </div>
        <div class="py-3 px-3">

          <h5 id="SA">Group Name</h5>
          <div class="ml-5">
            <p>{{group_name}}</p>
          </div>

          <h5>Group Description</h5>
          <textarea cols="30" rows="10" v-model="gDetail"   class="form-control"></textarea>

        </div>
    </div>
  </v-app>
</template>

<script>
import swal from 'sweetalert'

export default {
   data: () => ({
      gDetail:"입력값없음",
    }),
  props: {
    group_name: String,
    group_detail: String,
    group_master: String,
    group_hash: String,
  },
  methods: {
    editGroup() {
      if (this.group_name == '') {
        swal('Warning', 'Please enter group name!', 'warning')
      } else if (this.group_detail == '') {
        swal('Warning', 'Please enter group detail!', 'warning')
      } else {
        this.$store.dispatch("editgroup", {
          userid: this.group_master,
          gdtail: this.gDetail,
          groupid: this.group_name,
          group_hash: this.group_hash
          })
        swal('Success', 'Your request is approved!', 'success')
        this.$emit('close')
      }
    },   
  },
   created(){  
       this.gDetail=this.group_detail       
    },
    
}

</script>

<style scoped>
  .profile {
      width: 100%;
      height: 100%;
      object-fit: cover;
  }
  .box {
    width: 50px;
    height: 50px; 
    border-radius: 70%;
    overflow: hidden;
  }
</style>