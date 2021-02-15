<template>
  <q-card id="SA" class="gix rounded border p-2">
    <!-- <q-icon :name="mdiClose" @click="exit" class="absolute-right m-2" /> -->
    <div class="d-flex justify-content-between px-3 my-2">
      <h3 class="py-3 px-3">Group Info</h3>
      <div class="d-flex mt-3">
        <div class="mx-1" v-if="userId==master">
          <q-icon 
            class="icon-btn"
            :name="mdiAccountArrowRight"
            @click="GroupMasterTransferModal"
            size="sm"
          />
          <q-tooltip>Transfer Permission</q-tooltip>
        </div>
        <div class="mx-1" v-if="userId==master">
          <q-icon 
            class="icon-btn"
            :name="mdiTrashCanOutline"
            @click="deleteGroupToFront"
            size="sm"
          />
          <q-tooltip>Delete Group</q-tooltip>
        </div>
        <div class="mx-1" v-if="userId!=master">
          <q-icon 
            class="icon-btn"
            :name="mdiExitRun"
            @click="leaveGroupToFront"
            size="sm"
          />
          <q-tooltip>Leave This Group</q-tooltip>
        </div>
        <div class="mx-1" v-if="userId==master">
          <q-icon 
            class="icon-btn"
            :name="mdiPencil"
            @click="GroupEditModal"
            size="sm"
          />
          <q-tooltip>Edit Group Info</q-tooltip>
        </div>
      </div>
      </div>
      <div class="py-3 px-3 mx-3 my-3">

        <div class="d-flex">
          <h5 id="SA">Group Name:</h5>
          <div v-for="(group,i) in groups" :key="i" class="ml-3">
            <p id="groupN" v-if="group.groupName==people">{{group.groupName}}</p>
          </div>
        </div>

        <div class="d-flex">
          <h5>Group Description: </h5>
          <div v-for="(group,i) in groups" :key="`A-${i}`" class="ml-3">
            <p id="groupD" v-if="group.groupName==people">{{group.groupDetail}}</p>
          </div>
        </div>

        <div v-if="userId==master" class="d-flex justify-content-between">
          <h5>Invite Code</h5>
        </div>
          <div>
            <p class="mb-0 ml-5" style="font-size:10px;">If you paste the code in <strong class="text-success">'NEW GROUP' <q-icon class="text-success" :name="mdiArrowRight"/> 'JOIN GROUP'</strong> at the bottom right, you can participate in the group calendar.</p>
          </div>
        <div v-if="userId==master" class="d-flex mt-2">
        
          <div v-for="group in groups" :key="group.id">
            <input class="form-control ml-5" :class="{'mobile-hashcode' : $q.screen.lt.sm}" v-if="group.groupName==people" type="text" id="copy_text_input" :value="group.hashCode">
          </div>
          <div>
            <div>
              <q-icon 
                class="icon-btn ml-2 mt-1"
                :name="mdiContentCopy" 
                @click="copyHash"
                size="md"
              />
              <q-tooltip>Copy Invite Code</q-tooltip>
            </div>
          </div>
        </div>

        <!-- Group members when dark theme -->
        <q-list class="p-2">
          
          <h5>Group Members</h5>

          <div style="overflow: auto; max-height: 200px;">
            <div
              v-for="group in groups" 
              :key="group.id"
            >
          
              <div v-if="group.groupName==people">
                <div class="d-flex memlist" v-for="(groupMem,i) in group.groupMember" :key="i">
                  <div class="box ml-3" style="background: #BDBDBD;">
                    <Gravatar class="profile" :email="groupMem" :size="50" />
                  </div>
                  <div class="memname" v-if="groupMem==iAmMaster">
                    {{ groupMem }} 
                    <q-icon :name="mdiCrown" class="text-warning" /><span class="text-warning">Master</span>
                  </div>
                  <div class="memname" v-else>
                    {{ groupMem }}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </q-list>
      </div>
      <button class="btn text-primary absolute-bottom-right m-3" @click="exit" :class="{ 'text-light' : this.$q.dark.isActive }">Close</button>
  </q-card>
</template>

<script>
import EditGroupModal from './EditGroupModal.vue'
import GroupMasterTransferModal from './GroupMasterTransferModal.vue'
import { mdiCheck, mdiClose, mdiAccountArrowRight, mdiTrashCanOutline, mdiExitRun, mdiPencil, mdiContentCopy, mdiCrown, mdiArrowRight } from '@quasar/extras/mdi-v5'

import Gravatar from 'vue-gravatar'
import $ from 'jquery'
import swal from 'sweetalert';
import axios from 'axios'
import { mapGetters, mapActions } from 'vuex'

let https = require('https')
const instance = axios.create({
  baseURL: 'https://bcplus.me/api',
  timeout: 120000,
  httpsAgent: new https.Agent({
    rejectUnauthorized: false
  })
});

export default {
    data (){
      return {
        userId: '',
        master: '',
        userName: '',
        webhook:'',
      }      
    },
  components: { Gravatar },
  props: {
    groups: Array,
    group_name: Array,
    people: String,
    goDark: Boolean,
  },

  methods: {
    ...mapActions('groups',["deleteGroup", "leaveGroup", "addWebHook"]),

    copyHash() {
        $('#copy_text_input').select()
        document.execCommand("copy")
        swal('Success', 'Copied to clipboard', 'success')
    },
    deleteGroupToFront() {
        swal({
          text: "Are you sure to delete this group?",
          icon: "warning",
          buttons: true,
          dangerMode: true,
        })
        .then((willDelete) => {
          if (willDelete) {
            let g = $("#groupN").text()
            const itemToFind = this.groups.find(function(item) {return item.groupName === g})
            const idx = this.groups.indexOf(itemToFind)
            // this.$store.dispatch("deleteGroup", {
            //   groupid: this.groups[idx].groupName
            // })
            this.deleteGroup({groupid: this.groups[idx].groupName})
            swal("your request is approved", {
              icon: "success",
            }).then(()=>{
                this.$router.go()
            })
            this.$emit('close')
          } else {
            this.$emit('close')
          }
        });
      },
    // 그룹 정보 수정 모달
    GroupEditModal() {
      let group_n = $("#groupN").text();
      let group_d = $("#groupD").text();
      let group_h = $('input[id=copy_text_input]').val();

      const itemToFind = this.groups.find(function(item) {return item.groupName === group_n})
      const idx = this.groups.indexOf(itemToFind)
      let group_m = this.groups[idx].groupMember[0]

      this.$modal.show(EditGroupModal, {
        group_name: group_n,
        group_detail: group_d,
        group_master: group_m,
        group_hash: group_h,
        modal: this.$modal},{
            height: 'auto'
      })
      this.$emit('close')
     
    },

    GroupMasterTransferModal() {
      this.$modal.show(GroupMasterTransferModal, {
        group:this.people,
        modal: this.$modal},{
            height: 'auto'
      })
      this.$emit('close')
    },

    leaveGroupToFront(){
      swal({
        text: "Are you sure to leave this group?",
        icon: "warning",
        buttons: true,
        dangerMode: true,
      })
      .then((willDelete) => {
        if (willDelete) {
          // this.$store.dispatch('leaveGroup',{groupName:this.people})
          this.leaveGroup({groupName: this.people})
          swal("your request is approved", {
            icon: "success",
          });
          this.$emit('close')
        } else {
          this.$emit('close')
        }
      });
    },

    addWebHookEvent(){
      this.addWebHook({groupName: this.people, webhook: this.webhook})
      swal("your request is approved", {
            icon: "success",
          });
      this.$emit('close')
    },

    exit() {
			this.$emit('close')
		}
  },
  created() {  
    this.mdiAccountArrowRight = mdiAccountArrowRight
    this.mdiTrashCanOutline = mdiTrashCanOutline
    this.mdiExitRun = mdiExitRun
    this.mdiPencil = mdiPencil
    this.mdiContentCopy = mdiContentCopy
    this.mdiCrown = mdiCrown
    this.mdiArrowRight = mdiArrowRight
    this.mdiClose = mdiClose
    this.mdiCheck = mdiCheck

    this.token=sessionStorage.getItem("access-token")
    this.userId=sessionStorage.getItem("username")

    for(var i in this.groups) {
      if (this.groups[i].groupName == this.people) {
        this.master = this.groups[i].groupMaster
        }
      }
    },
  computed: {
    ...mapGetters('calendar', ["EVENTS", "LUN_DAYS", "TASKS"]),
    ...mapGetters('groups', ["USER", "MEMBER_INFO", "MEMOS"]),

    iAmMaster() {
      const groupKing = this.master
      return groupKing
    }
  },
  mounted() {

  }
}

</script>

<style scoped>
  .gix {
    max-height: 700px;
  }
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
  .form-control {
    width: 400px;
    margin-bottom: 20px;
  }
  .memname {
    margin-top: 12px;
    margin-left: 10px;
  }
  .memlist {
    margin-bottom: 10px;
  }
  .mobile-hashcode {
    font-size: 12px !important;
    width: 180px !important;
  }
</style>