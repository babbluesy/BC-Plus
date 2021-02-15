<template>
  <div class="q-pa-md">
		<div
			v-for="group in this.GROUPS" 
			:key="group.groupName"
			class="mb-2 rounded"
		>
			<div v-if="peopleOn == group.groupName">
				<q-icon size="sm" class="text-warning" :name="mdiCrown" /><span class="text-warning">Master</span>  {{group.groupMaster}}
				<br><small class="text-secondary">Past group schedules are not display here.</small>
			</div>
		</div>
		<div v-if="this.TASKS" bordered class="rounded-borders">
			<q-list v-for="task in this.TASKS" :key="task.sid">
				<div class="ml-1 my-1 small" v-if="!peopleOn" :class="{ 'task-exapansion-done' : task.status=='done' }">
					<div v-if="task.group">
						Group: {{task.group}}
					</div>
					<div v-else>
						My Private Schedule
					</div>
				</div>
				<q-expansion-item
					@click="getMember"
					class="mb-2 task-expansion-color border rounded"
					:class="{ 'task-exapansion-done': task.status=='done' }"
					:style="{ background: task.color }"
				>
					<template v-slot:header>
						<q-item-section class="ml-2">
							<div class="d-flex justify-content-between">
								<div style="max-width: 130px;">
									<span class="task-title task-title py-1">{{ task.title }}</span>
									 <!-- :class="{ 'task-title-done' : task.status=='done' }" -->
								</div>
								<div class="task-title py-1">
									Month:
									{{task.end.substring(5,7)}}
								</div>
							</div>
						</q-item-section>
					</template>
					
					<q-card v-if="sid=task.sid" expand-icon="mdi-menu-down">
						<q-card-section v-if="task.sid">
							<!-- <div v-if="task.status=='done'"> -->
							<div v-if="task.allDay==true">
								 <!-- class="task-title-done" -->
								<ul v-if="$moment(task.start).format('YYYY-MM-DD') < $moment(task.end).format('YYYY-MM-DD')" class="p-0">{{ $moment(task.start).format('YYYY-MM-DD') }} - {{ $moment(task.end).add('-1', 'days').format('YYYY-MM-DD') }}</ul>
								<ul v-else class="p-0">{{ $moment(task.start).format('YYYY-MM-DD') }} - {{ $moment(task.end).format('YYYY-MM-DD') }}</ul>
								<ul class="task_description p-0">Memo: {{ task.description }}</ul>
								<ul class="p-0" v-if="task.group!=null">Group: {{ task.group }}</ul>
							</div>
							<div v-else>
								<ul class="p-0">{{ $moment(task.start).format('YYYY-MM-DD') }} - {{ $moment(task.end).format('YYYY-MM-DD') }}</ul>
								<ul class="task_description p-0">Memo: {{ task.description }}</ul>
								<ul class="p-0" v-if="task.group!=null">Group: {{ task.group }}</ul>
							</div>
							<q-separator class="my-2" />

							<div class="d-flex justify-content-between">
								<div class="d-flex" style="flex-wrap: wrap;">
									<div class="p-0 mr-1" v-for="member in task.members" :key="member.id">
										<div>
											<Gravatar :email="member" :size="20" />
											<q-tooltip>{{ member }}</q-tooltip>
										</div>
									</div>
								</div>

								<div v-if="foundKing" class="d-flex justify-content-end">
									<q-icon 
									:name="mdiAccountPlus" 
									class="icon-btn"
									@click="switchAllocateModal(task)"
									size="sm"
									/>
										<q-tooltip>Allocate Schedule</q-tooltip>
								</div>
							</div>
					</q-card-section>
				</q-card>
				</q-expansion-item>
			</q-list>
		</div>
	</div>
</template>

<script>
import AllocateScheduleModal from './AllocateScheduleModal.vue'
import { mapGetters, mapActions } from 'vuex'
import Gravatar from 'vue-gravatar'
import { mdiAccountPlus, mdiCrown } from '@quasar/extras/mdi-v5'

export default {
	components: {       
		Gravatar,
		mdiAccountPlus
	},
	props: {
		people: String
	},
	data() {
    return {
      // drawerLeft: false,
      dialog: false,
      memo_menu: false,
      text_memo: '',
      friends: [],
			switchValue: false,
    }
	},
	computed: {
    ...mapGetters('calendar', ["TASKS"]),
    ...mapGetters('groups', ["GROUPS", "group_name", "MEMBER_INFO"]),

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
    ...mapActions('groups',['getMemberList']),

    getMember() {     
    	this.getMemberList({groupName: this.people})
    },

    remove(item) {
      const index = this.friends.indexOf(item.name)
      if (index >= 0) this.friends.splice(index, 1)
    },
		
		switchAllocateModal(task) {
			this.$modal.show(AllocateScheduleModal, {
				// group_name: this.group_name,
				member_info: this.MEMBER_INFO,
				sid: task.sid,
				people: this.people,
				modal: this.$modal},{
						height: 'auto'
			})
		}
  },
  created() {
		this.mdiAccountPlus = mdiAccountPlus
		this.mdiCrown = mdiCrown
  }
}
</script>

<style scoped>
ul {
	margin: 0;
}
.task_description {
  display: block; 
  overflow: hidden; 
  text-overflow:ellipsis;
}
.task-title {
  font-weight: 600;
  font-size: 0.9rem;
  /* display: block; 
  overflow: hidden; 
  text-overflow:ellipsis; */
  overflow: hidden; 
  text-overflow:ellipsis; 
  display: -webkit-box; 
  -webkit-line-clamp: 1; 
  -webkit-box-orient: vertical;
}

.task-title-done {
  text-decoration: line-through;
}

.task-expansion-color {
  filter: brightness(110%);
  opacity: 0.9;
}

.task-exapansion-done {
  /* filter: brightness(55%) !important; */
  opacity: 0.3 !important;
}
</style>

<style lang="scss">
.q-expansion-item__toggle-icon {
	color: lightgray;
	margin-right: 5px;
}
</style>