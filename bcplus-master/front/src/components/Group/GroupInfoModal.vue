<template>
  <v-app id="inspire">
    <div id="SA" class="gix rounded border p-2">
      <div class="d-flex justify-content-between px-3 my-2">
        <h3 class="py-3 px-3">Group Info</h3>
        <div class="mt-3">
          <v-tooltip v-if="userId==master" bottom>
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                class="mr-2"

                v-bind="attrs"
                v-on="on"
                small
                icon
                @click="GroupMasterTransferModal"
              ><v-icon class="outlined" >mdi-account-arrow-right</v-icon></v-btn>
            </template>
            <span id="SA">Transfer Permission</span>
          </v-tooltip>
          <v-tooltip v-if="userId==master" bottom color="white">
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                class="mr-2"
                v-bind="attrs"
                v-on="on"
                small
                icon
                @click="deleteGroup"
              ><v-icon class="text-danger" >mdi-trash-can-outline</v-icon></v-btn>
            </template>
            <span id="SA" class="text-danger">Delete Group</span>
          </v-tooltip>
          <v-tooltip v-if="userId!=master" bottom color="white">
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                class="mr-2"
                icon
                v-bind="attrs"
                v-on="on"
                @click="leaveGroup"
              ><v-icon class="outlined text-danger" >mdi-exit-run</v-icon></v-btn>
            </template>
            <span id="SA" class="text-danger">Leave This Group</span>
          </v-tooltip>
          <v-tooltip v-if="userId==master" bottom>
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                class="mr-2"
                v-bind="attrs"
                v-on="on"
                small
                icon
                @click="GroupEditModal"
              ><v-icon class="outlined" >mdi-pencil</v-icon></v-btn>
            </template>
            <span id="SA">Edit Group Info</span>
          </v-tooltip>
        </div>
        </div>
        <div class="py-3 px-3 mx-3 my-3">

          <h5 id="SA">Group Name</h5>
          <div v-for="(group,i) in groups" :key="i" class="ml-5">
            <p id="groupN" v-if="group.groupName==people">{{group.groupName}}</p>
          </div>

          <h5>Group Description</h5>
          <div v-for="(group,i) in groups" :key="`A-${i}`" class="ml-5">
            <p id="groupD" v-if="group.groupName==people">{{group.groupDetail}}</p>
          </div>

          <div v-if="userId==master" class="d-flex justify-content-between">
            <h5>Invite Code</h5>
          </div>
            <div>
              <p class="mb-0 ml-5" style="font-size:10px;">If you paste the code in <strong class="text-success">'NEW GROUP' <v-icon small>mdi-arrow-right</v-icon> 'JOIN GROUP'</strong> at the bottom right, you can participate in the group calendar.</p>
            </div>
          <div v-if="userId==master" class="d-flex mt-2">
          
            <div v-for="group in groups" :key="group.id" class="ml-5">
              <input v-if="group.groupName==people" type="text" id="copy_text_input" :value="group.hashCode" class="form-control">
            </div>
            <div>
              <v-tooltip bottom>
                <template v-slot:activator="{ on, attrs }">
                  <v-btn
                    v-bind="attrs"
                    v-on="on"
                    class="cbtn ml-3"
                    @click="copyHash"
                    icon
                  >
                    <v-icon>mdi-content-copy</v-icon>
                  </v-btn>
                </template>
                <span id="SA">Copy Invite Code</span>
              </v-tooltip>
            </div>
          </div>
          
          <!-- Group members when dark theme -->
          <v-list v-if="goDark" subheader class="p-2" style="background-color: #131E2E;">
            
            <v-subheader>Group Members</v-subheader>

            <div style="overflow: auto; max-height: 300px;">
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
                    {{ groupMem }} <v-icon class="text-warning">mdi-crown</v-icon><span class="text-warning">Master</span>
                  </div>
                  <div class="memname" v-else>
                    {{ groupMem }}
                  </div>
                </div>
              </div>
            </div>
            </div>
          </v-list>

          <!-- Group members when light theme -->
          <v-list v-else subheader>
            <v-subheader>Group Members</v-subheader>
            <div style="overflow: auto; max-height: 300px;">
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
                    {{ groupMem }} <v-icon class="text-warning">mdi-crown</v-icon><span class="text-warning">Master</span>
                  </div>
                  <div class="memname" v-else>
                    {{ groupMem }}
                  </div>
                </div>
              </div>
            </div>
            </div>
          </v-list>
        </div>
    </div>
  </v-app>
</template>

<script>
import EditGroupModal from '@/components/Group/EditGroupModal.vue'
import GroupMasterTransferModal from '@/components/Group/GroupMasterTransferModal.vue'

import Gravatar from 'vue-gravatar'
import $ from 'jquery'
import swal from 'sweetalert';
import axios from 'axios'
import { mapGetters } from 'vuex'

let https = require('https')
const instance = axios.create({
  baseURL: 'https://bcplus.me/api',
  timeout: 120000,
  httpsAgent: new https.Agent({
    rejectUnauthorized: false
  })
});

export default {
    data: () => ({
      userId: '',
      master: '',
      userName: '',
    }),
  components: {Gravatar},
  props: {
    groups: Array,
    group_name: Array,
    people: String,
    goDark: Boolean,
  },

  methods: {
    copyHash() {
        $('#copy_text_input').select()
        document.execCommand("copy")
        swal('Success', 'Copied to clipboard', 'success')
    },
    deleteGroup() {
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
            this.$store.dispatch("deletegroup", {
              groupid: this.groups[idx].groupName
            })
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
    leaveGroup(){
      swal({
        text: "Are you sure to leave this group?",
        icon: "warning",
        buttons: true,
        dangerMode: true,
      })
      .then((willDelete) => {
        if (willDelete) {
          this.$store.dispatch('leaveGroup',{groupName:this.people})
          swal("your request is approved", {
            icon: "success",
          });
          this.$emit('close')
        } else {
          this.$emit('close')
        }
      });
    }
  },
  created: function(){  
    this.token=sessionStorage.getItem("access-token")
    this.userId=sessionStorage.getItem("username")
    instance.get('/member/'+ sessionStorage.getItem("username"))
        .then(res =>{
          sessionStorage.setItem("birth", res.data.birthDay)
    })
    this.birth=sessionStorage.getItem("birth")
    this.userName=sessionStorage.getItem("name")

    for(var i in this.groups) {
      if (this.groups[i].groupName == this.people) {
        this.master = this.groups[i].groupMaster
        }
      }
    },
  computed: {
    ...mapGetters(["EVENTS", "GROUPS", "USER","LUN_DAYS", "TASKS", "MEMBER_INFO" ]),
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
</style>