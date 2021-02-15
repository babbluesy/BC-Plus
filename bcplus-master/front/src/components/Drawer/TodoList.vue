<template>
  <div>
    <div class="mx-auto my-5 pb-2 d-flex justify-content-between align-items-center">
      <div class="d-flex">
        <!-- task title -->
        <div v-if="this.people!=null">
          <h4 class="ml-4 mb-0"><v-icon class="mr-4">mdi-clipboard-list-outline</v-icon>ToDo List</h4>
          <small class="ml-15">Selected Group: <span class="font-weight-bold">{{this.people}}</span></small>
        </div>
        <div v-else>
          <h4 id="SA" class="ml-4 mb-0"><v-icon class="mr-4">mdi-clipboard-list-outline</v-icon>My ToDo List</h4>
        </div>
      </div>

      <div>
        <v-menu
          :close-on-content-click="false"
          :nudge-width="200"
          offset-x
          v-model="memo_menu"
        >
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              icon
              v-bind="attrs"
              v-on="on"
            >
              <v-icon>mdi-file-document-edit-outline</v-icon>
            </v-btn>
          </template>

          <v-card width="400px" style="overflow: auto;">
            <v-list>
              <v-list-item class="d-flex justify-content-between">
                <v-expansion-panels>
                  <v-expansion-panel>
                    <v-expansion-panel-header>
                      MEMO
                    </v-expansion-panel-header>
                    <v-expansion-panel-content>
                      <v-textarea v-model="text_memo"></v-textarea>
                      <v-btn @click="addMemo">Save</v-btn>
                    </v-expansion-panel-content>
                  </v-expansion-panel>
                </v-expansion-panels>
              </v-list-item>
            </v-list>

            <v-divider></v-divider>

            <v-list v-for="(memo, idx) in MEMOS" :key="idx">
              <v-list-item color="yellow">
                <div class="d-flex justify-content-right">
                  <v-btn icon @click="deleteMemo(idx)">
                    <v-icon>
                      mdi-delete
                    </v-icon>
                  </v-btn>
                  <v-btn icon @click="editMemo">
                    <v-icon>
                      mdi-pencil
                    </v-icon>
                  </v-btn>
                </div>
                <v-list-item-action>
                  {{ memo.text }}
                </v-list-item-action>
              </v-list-item>

              <v-divider></v-divider>
            </v-list>

            <v-spacer></v-spacer>
            
            <v-card-actions>
              <v-btn
                text
                color="primary"
                @click="memo_menu = false"
              >
                Cancel
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-menu>
      </div>
    </div>

    <!-- Task Card: START -->
    <v-expansion-panels class="mb-6" v-if="this.TASKS">
      <div
        v-for="group in this.GROUPS" 
        :key="group.groupName"
        class="mb-2 rounded"
      >
        <div v-if="peopleOn == group.groupName">
          <v-icon class="text-warning">mdi-crown</v-icon><span class="text-warning">Master</span>  {{group.groupMaster}}
          <br><small class="text-secondary">Past group schedules are not display here.</small>
        </div>
      </div>

      <v-expansion-panel
        @click="getMember"
        v-for="task in this.TASKS" 
        :key="task.sid"
        class="mb-2 rounded"
      >
        <div class="ml-3 my-1 small" v-if="!peopleOn">
          <div v-if="task.group">
            Group: {{task.group}}
          </div>
          <div v-else>
            My Private Schedule
          </div>
        </div>
            
        <v-expansion-panel-header :color="task.color" v-if="sid=task.sid" expand-icon="mdi-menu-down">
          <span v-if="task.status=='done'" class="task_title py-1" style="width: 150px; text-decoration: line-through"><strong>{{ task.title }}</strong></span>
          <span v-else class="task_title py-1" style="width: 150px;"><strong>{{ task.title }}</strong></span>
          <div class="small">
            <strong>Month:</strong>
            <strong>{{task.end.substring(5,7)}}</strong>
          </div>
        </v-expansion-panel-header>
            
        <v-expansion-panel-content v-if="task.sid">
          <br>
          <div v-if="task.status=='done'" style="text-decoration: line-through">
            <ul class="p-0">{{ $moment(task.start).format('YYYY-MM-DD') }} - {{ $moment(task.end).add('-1', 'days').format('YYYY-MM-DD') }}</ul>
            <ul class="task_description p-0">Memo: {{ task.description }}</ul>
            <ul class="p-0" v-if="task.group!=null">Group: {{ task.group }}</ul>
          </div>
          <div v-else>
            <ul class="p-0">{{ $moment(task.start).format('YYYY-MM-DD') }} - {{ $moment(task.end).format('YYYY-MM-DD') }}</ul>
            <ul class="task_description p-0">Memo: {{ task.description }}</ul>
            <ul class="p-0" v-if="task.group!=null">Group: {{ task.group }}</ul>
          </div>
          <hr>
          <div class="d-flex" style="flex-wrap: wrap;">
            <div class="p-0 mr-1" v-for="mem in task.members" :key="mem.id">
              <v-tooltip bottom>
                <template v-slot:activator="{ on, attrs }">
                  <div v-bind="attrs" v-on="on"><Gravatar :email="mem" :size="20" /></div>
                </template>
                <span>{{ mem }}</span>
              </v-tooltip>
            </div>
          </div>
              
          <ul class="d-flex justify-content-end" v-if="foundKing">
            <v-row justify="end">
              <v-tooltip right>
                <template v-slot:activator="{ on, attrs }">
                  <v-btn
                    @click.stop="dialog = true"
                    fab
                    x-small
                    icon
                    v-bind="attrs"
                    v-on="on"
                  >
                    <v-icon>
                      mdi-account-plus
                    </v-icon>
                  </v-btn>
                </template>
                <span id="SA">Allocate Schedule</span>
              </v-tooltip>
              
              <v-dialog
                v-model="dialog"
                max-width="500"
                max-height="500"
              >
                <v-card class="rounded border p-2">
                  <v-col cols="12">
                    <legend id="SA">Allocate Schedule</legend>
                    <v-col id= "SA" cols="12">
                      <v-autocomplete           
                        v-model="friends"
                        :disabled="false"
                        :items="member_info"
                        filled
                        chips
                        label="Select"
                        item-text="name"
                        item-value="name"
                        multiple
                      >
                        <template v-slot:selection="data">
                          <v-chip
                            id="SA"
                            v-bind="data.attrs"
                            :input-value="data.selected"
                            close
                            @click="data.select"
                            @click:close="remove(data.item)"
                          >
                            <v-avatar left>
                              <Gravatar :email="data.item.name" />
                            </v-avatar>
                              {{ data.item.name }}
                          </v-chip>                          
                        </template>   

                        <template v-slot:item="data">                 
                          <template v-if="typeof data.item !== 'object'">
                            <v-list-item-content v-text="data.item"></v-list-item-content>                  
                          </template>
                          <template v-else>
                            <v-list-item-avatar>
                              <Gravatar :email="data.item.name" />
                            </v-list-item-avatar>
                            <v-list-item-content>
                              <v-list-item-title v-html="data.item.name"></v-list-item-title>
                              <v-list-item-subtitle v-html="data.item.group"></v-list-item-subtitle>
                            </v-list-item-content>
                          </template>                                  
                        </template>                             
                      </v-autocomplete>
                    </v-col>
                  </v-col>

                  <v-card-actions id="SA" class="mr-5">
                    <v-spacer></v-spacer>
                    <button class="btn text-danger mr-2" @click="dialog = false">cancel</button>
                    <button class="btn text-primary" @click="addMem({friends: friends, sid: task.sid}); dialog = false;">Allocate</button>
                  </v-card-actions>
                </v-card>
              </v-dialog>
            </v-row>
          </ul>
        </v-expansion-panel-content>
      </v-expansion-panel>
    </v-expansion-panels>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Gravatar from 'vue-gravatar'

export default {
  props: {
    people: String,
    member_info: Array,
  },
  components: {       
    Gravatar,
  },
  data() {
    return {
      dialog: false,
      memo_menu: false,
      text_memo: '',
      friends: [],
    }
  },
  computed: {
    ...mapGetters(["GROUPS", "group_name", "TASKS","MEMOS"]),

    peopleOn() {
      const peopleOn = this.people
      return peopleOn
    },
    
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
  methods: {
    addMem(data) {
      console.log('addmem', data)
      this.$store.dispatch("addMem",{members:data.friends,sid:data.sid,people:this.people})
    },

    getMember() {     
    this.$store.dispatch('getMemberList',{groupName:this.people});
    },

    addMemo() {
      this.$store.dispatch("addMemo",{
        memo:  this.text_memo,
        no: this.MEMOS.length
      })    
    },

    deleteMemo(idx) {
      alert(idx)
      this.MEMOS.splice(idx, 1)
      this.$store.dispatch("deleteMemo",{memos:this.MEMOS})
    },

    editMemo() {

    },

    remove(item) {
      const index = this.friends.indexOf(item.name)
      if (index >= 0) this.friends.splice(index, 1)
    },

    getMemo(){     
      this.$store.dispatch("getMemo")
    },
  },
  created() {
    this.getMemo()
  }

}
</script>

<style scoped>
ul {
	margin: 0;
}
</style>