<template>
<q-card id="SA">
  <div class="rounded border p-2">
    <div class="d-flex justify-content-between">
      <h3 class="py-3 px-3">Group Info</h3>
      <div class="mt-3">
				<div>
					<q-icon 
						:name="mdiCheck"
						class="icon-btn mr-2"
						@click="editGroupToFront"
						size="md"
					/>
					<q-tooltip>
						Edit Group Info
					</q-tooltip>
				</div>
        </div>
        </div>
        <div class="py-3 px-3">

          <h5 id="SA">Group Name</h5>
          <div class="ml-5">
            <p>{{ group_name }}</p>
          </div>

          <h5>Group Description</h5>
          <textarea cols="30" rows="10" v-model="gDetail" class="form-control"></textarea>

        </div>
    </div>
	</q-card>
</template>

<script>
import swal from 'sweetalert'
import { mdiCheck } from '@quasar/extras/mdi-v5'
import { mapActions } from 'vuex'

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
	components: {
		mdiCheck
	},
  methods: {
		...mapActions("groups", ["editGroup"]),

    editGroupToFront() {
      if (this.group_name == '') {
        swal('Warning', 'Please enter group name!', 'warning')
      } else if (this.group_detail == '') {
        swal('Warning', 'Please enter group detail!', 'warning')
      } else {
					this.editGroup({userid: this.group_master, gdtail: this.gDetail, groupid: this.group_name, group_hash: this.group_hash})
        swal('Success', 'Your request is approved!', 'success')
        this.$emit('close')
      }
    },   
  },
   created(){  
			 this.gDetail=this.group_detail  
			 this.mdiCheck = mdiCheck     
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