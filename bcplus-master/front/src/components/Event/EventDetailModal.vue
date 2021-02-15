<template>
  <div class="rounded">
    <v-app id="inspire">
      <v-card
        id="SA"
        width="344"
        class="mx-auto rounded border"
      >
        <v-app-bar
          dark
          :color="color"
          height="65px"
        >
        <v-list-item class="pl-0" style="display: block; overflow: hidden; text-overflow:ellipsis;">
          <v-list-item-content>
            <v-list-item-title class="headline mt-1" style="font-size: 1rem !important;">{{ event.title }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
        <v-spacer></v-spacer>
        <v-card-actions class="p-0" style="width: 80px;">
          <v-btn
            text
            @click="changeForm"
            icon
            v-if="this.master==this.userId_tmp && this.people!=null"
            class="p-0 m-0"
          >
            <v-icon>
              mdi-pencil
            </v-icon>
          </v-btn>
          <v-btn
            text
            @click="deleteForm"
            icon
            v-if="this.master==this.userId_tmp && this.people!=null"
            class="p-0 m-0"
          >
            <v-icon>
              mdi-trash-can-outline
            </v-icon>
          </v-btn>
          <v-btn
            text
            @click="changeForm"
            icon
            v-if="this.people==null && event.startEditable==true"
            class="p-0 m-0"
          >
            <v-icon>
              mdi-pencil
            </v-icon>
          </v-btn>
          <v-btn
            text
            @click="deleteForm"
            v-if="this.people==null && event.startEditable==true"
            icon
            class="p-0 m-0"
          >
            <v-icon>
              mdi-trash-can-outline
            </v-icon>
          </v-btn>
          <v-spacer></v-spacer>
          </v-card-actions>
        </v-app-bar>

        <div class="d-flex">
          <v-card-text class="pb-0">
            {{ start }}  -  {{ end }}
          </v-card-text>
        </div>

        <!-- 누구 일정인지 표시 -->
        <div v-if="this.people==null">
          <div class="pt-0" v-if="event.startEditable==true">
            <v-card-text class="pt-0">
              <div>
                <p class="pb-0 mb-0">
                  <span class="font-weight-bold">{{ event.extendedProps.group }}</span> 's private schedule
                </p>
              </div>
            </v-card-text>
          </div>
          <div v-else class="pt-0" v-for="group in this.TASKS" :key="group.sid">
            <v-card-text class="pt-0" v-if="group.sid == event.extendedProps.sid">
              <div v-if="group.group">
                <p class="pb-0 mb-0">
                  <span class="font-weight-bold">Group {{ group.group }}</span> schedule
                </p>
              </div>
              <div v-else>
                <p class="pb-0 mb-0">
                  <span class="font-weight-bold">{{ event.extendedProps.group }}</span> 's private schedule
                </p>
              </div>
            </v-card-text>
          </div>
        </div>
        
        <div v-else>
          <div class="pt-0">
            <v-card-text class="pt-0">
              <div>
                <p class="pb-0 mb-0">
                  <span class="font-weight-bold">Group {{ this.people }}</span> schedule
                </p>
              </div>
            </v-card-text>
          </div>
        </div>

        <v-card-text class="pb-2 pt-1 mb-2">
          {{ event.extendedProps.description }}
        </v-card-text>
        <v-card-text v-if="event.extendedProps.members.length!=0" class="pb-2 pt-1">
          <hr>
          members
        </v-card-text>
        <div class="d-flex mb-2" style="flex-wrap: wrap;">
          <v-card-text v-for="mem in event.extendedProps.members" :key="mem.id" class="d-flex pb-2 pt-1" style="width: 20px;">
            <v-tooltip bottom>
              <template v-slot:activator="{ on, attrs }">
                <div v-bind="attrs" v-on="on"><Gravatar :email="mem" :size="20" /></div>
              </template>
              <span>{{ mem }}</span>
            </v-tooltip>
          </v-card-text>
        </div>
      </v-card>
    </v-app>
  </div>
</template>

<script>
import EditEventModal from '@/components/Event/EditEventModal.vue'
import { mapGetters } from 'vuex'
import Gravatar from 'vue-gravatar'
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
  props: {
    event: Object,
    groupInfo: Array,
    people: String,
    master: String,
    userId: String,
    events: Array,
    detailgroup: String,
  },
  data: () => ({
    title: "",
    description: "",
    start: {},
    end: {},
    realend: {},
    color: '',
    colors: ['#2196F3', '#3F51B5', '#673AB7', '#00BCD4', '#4CAF50', '#FF9800', '#F44336'],
    colorHex: null,
    rstart: {},
    rend: {},
    group: '',
    printend: '',
    curevnt: null,
    userId_tmp:''
  }),
  components: { Gravatar },
  methods: {
    changeForm() {
      if (this.people==null) {
        this.$modal.show(EditEventModal, {
          group_name: [this.userId_tmp],
          id: this.event.id,
          title: this.title,
          description: this.description,
          color: this.colorHex[this.color],
          start: this.start,
          end: this.$moment(this.end).add(-1, 'days').format('YYYY-MM-DD HH:mm'),
          rstart: this.start,
          rend: this.end,
          realend: this.$moment(this.end).format('YYYY-MM-DD'),
          selectgroup: this.selectgroup,
          colors: this.colors,
          event: this.event,
          people: this.people,
          modal: this.$modal
          },{ height: "auto" })
        this.$emit('close')
        } else{
          this.$modal.show(EditEventModal, {
            group_name: this.group_name,
            id: this.event.id,
            title: this.title,
            description: this.description,
            color: this.colorHex[this.color],
            start: this.start,
            end: this.$moment(this.end).add(-1, 'days').format('YYYY-MM-DD HH:mm'),
            rstart: this.start,
            rend: this.end,
            realend: this.$moment(this.end).format('YYYY-MM-DD'),
            selectgroup: this.selectgroup,
            colors: this.colors,
            event: this.event,
            people: this.people,
            modal: this.$modal}, {
              height: "auto"
          })
        this.$emit('close')
      }
    },
    deleteForm() {
      swal({
        text: "Are you sure to delete this schedule?",
        icon:"info",
        buttons: true,
          dangerMode: true,
        })
        .then((willDelete) => {
          if (willDelete) {
            this.$store.dispatch("deleteschedule", {
              sid: this.event.extendedProps.sid
            })
            swal("Your request is approved", {
              icon: "success",
            })
              this.$emit('close')
          } else {
              this.$emit('close')
            }
        });
      }
  },
  computed: {
    ...mapGetters(["EVENTS", "GROUPS", "USER", "group_name", "LUN_DAYS", "TASKS", "MEMBER_INFO" ]),
    foundKing() {
      let king
      for(var idx in this.GROUPS) {
        if(this.GROUPS[idx].groupName == this.people) {
          if(sessionStorage.getItem("username")==this.GROUPS[idx].groupMaster) {
            king = this.GROUPS[idx].groupMaster
          }
        }
      }
      return king
    },
  },
  mounted() {
      this.title = this.event.title;
      this.description = this.event.extendedProps.description;
      if ( this.event.allDay == true ) {
        this.start = this.$moment(this.event.start).format('YYYY-MM-DD')
        if (this.event.end === null) {
          this.end = this.start
        } else {
          this.end = this.$moment(this.event.end).add(-1, 'days').format('YYYY-MM-DD')
        }
      } else {
        this.start = this.$moment(this.event.start).format('YYYY-MM-DD HH:mm')
        this.end = this.$moment(this.event.end).format('YYYY-MM-DD HH:mm')
      }
      
      // render시 날짜표기수정
      this.realend = this.$moment(this.event.realend).format('YYYY-MM-DD');
      // DB에 넣을때(선택)     
      // this.end = this.$moment(this.event.end).format('YYYY-MM-DD');
      this.printend = this.$moment(this.event.end).add(-1, 'days').format('YYYY-MM-DD');
      this.selectgroup = this.event.extendedProps.group;
  },
  created() {
    this.color = this.event.backgroundColor;
    this.start = this.$moment(this.event.start).format('YYYY-MM-DD');
    this.printend=this.$moment(this.event.end).add(-1, 'days').format('YYYY-MM-DD');
    this.realend = this.$moment(this.event.realend).add(-1, 'days').format('YYYY-MM-DD');
    this.token=sessionStorage.getItem("access-token")
    this.userId_tmp=sessionStorage.getItem("username")
    
    instance.get('/member/'+ sessionStorage.getItem("username"))
      .then(res =>{
        sessionStorage.setItem("userId", res.data.userId)
      })
    this.userId_tmp=sessionStorage.getItem("userId")

    this.colorHex = {
      '#2196F3': 'blue',
      '#3F51B5': 'indigo',
      '#673AB7': 'deep-purple',
      '#00BCD4': 'cyan',
      '#4CAF50': 'green',
      '#FF9800': 'orange',
      '#F44336': 'red',
    }   
  }
};
</script>

<style scoped>
.color-picker {
  display: flex;
  justify-content: space-around;
  margin-bottom: 20px;
}
.color-picker > .color {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  border: 1.5px solid whitesmoke;
  cursor: pointer;
}
.color.selected{
  box-shadow: 0 2px 3px 1px rgba(0, 0, 0, 0.2);
  border: 3px solid rgba(0, 0, 0, 0.4);
}
.color.red {
    background: #F44336;
}
.color.blue {
    background: #2196F3;
}
.color.orange {
    background: #FF9800;
}
.color.green {
    background: #4CAF50;
}
.color.indigo {
    background: #3F51B5;
}
.color.deep-purple {
    background: #673AB7;
}
.color.cyan {
    background: #00BCD4;
}
</style>