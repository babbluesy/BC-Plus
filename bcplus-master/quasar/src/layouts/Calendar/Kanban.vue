<template>
  <div id='SA' class='demo-app container' style="display: block;">
		<div class="container d-flex justify-content-center mt-4">
			<!-- <button class="ml-4" style="height: 30px;" @click="switchingcal"><q-icon size="md" :name="mdiCalendar" /></button> -->
			<q-icon 
				:name="mdiCalendar" 
				class="ml-4 icon-btn"
				@click="switchingcal" 
				size="md" 
			/>             
		</div>
		<div style="width: 100%;">
			<div class="container mt-3">
				<div class="d-flex row" style="flex-wrap: nowrap;">
					<div class="col-md-4" style="min-width: 300px;">
						<div class="p-2 alert alert-secondary" style="opacity: 0.9;">
							<h3>Todo</h3>
							<hr>
							<draggable @change="backlogdnd" class="list-group kanban-box kanban-column" :list="this.TASKS" group="tasks">
								<div class="list-group-item d-flex p-0 b-0 m-0" v-for="(element, idx) in this.TASKS" :key="idx">
									<div class="d-flex" v-if="element.status=='backlog'">
										<q-card class="color-index d-flex justify-content-end mr-2" :style="{ background: element.color }"></q-card>
										<div class="p-2 mt-1">
											<h5 class="kanban-content">{{ element.title }}</h5>
											<p class="m-0" v-if="element.group!=null">{{ element.group }}</p>
											<p class="m-0" v-if="element.group==null">My Schedule</p>

											<!-- <p class="mb-1" v-if="element.allDay==true" >{{ $moment(element.start).format('YYYY-MM-DD') }} - {{ $moment(element.end).add(-1, 'days').format('YYYY-MM-DD') }}</p>
											<p class="mb-1" v-if="element.allDay==false">{{ $moment(element.start).format('YYYY-MM-DD') }} - {{ $moment(element.end).format('YYYY-MM-DD') }}</p> -->
											<p class="mb-1" v-if="element.allDay==true && $moment(element.start).format('YYYY-MM-DD') < $moment(element.end).format('YYYY-MM-DD')" >{{ $moment(element.start).format('YYYY-MM-DD') }} - {{ $moment(element.end).add(-1, 'days').format('YYYY-MM-DD') }}</p>
											<p class="mb-1" v-if="element.allDay==true && $moment(element.start).format('YYYY-MM-DD') >= $moment(element.end).format('YYYY-MM-DD')" >{{ $moment(element.start).format('YYYY-MM-DD') }} - {{ $moment(element.end).format('YYYY-MM-DD') }}</p>
							
											<p class="mb-1" v-if="element.allDay==false">{{ $moment(element.start).format('YYYY-MM-DD') }} - {{ $moment(element.end).format('YYYY-MM-DD') }}</p>
											
											<h6 class="kanban-content">{{ element.description }}</h6>
											<div class="d-flex" style="flex-wrap: wrap;">
												<div v-for="groupMem in element.members" :key="groupMem.id" class="mr-1">
													<div>
														<Gravatar :email="groupMem" :size="20" />
														<q-tooltip>{{ groupMem }}</q-tooltip>
													</div>
												</div>
											</div>
										</div>
									</div> 
								</div>
							</draggable>
						</div>
					</div>

					<div class="col-md-4" style="min-width: 300px;">
						<div class="p-2 alert alert-primary" style="opacity: 0.9;">
							<h3>In Progress</h3>
							<hr>
							<draggable @change="inprogressdnd" class="list-group kanban-box kanban-column" :list="this.TASKS" group="tasks">
								<div class="list-group-item d-flex p-0 b-0 m-0" v-for="(element, idx) in this.TASKS" :key="idx" style="color: #383d41;">
									<div class="d-flex" v-if="element.status=='inprogress'">  
										<q-card class="color-index d-flex justify-content-end mr-2" :style="{ background: element.color }"></q-card>
										<div class="p-2 mt-1">
											<h5 class="kanban-content">{{ element.title }}</h5>
											<p class="m-0" v-if="element.group!=null">{{ element.group }}</p>
											<p class="m-0" v-if="element.group==null">My Schedule</p>
											<!-- <p class="mb-1" v-if="element.allDay==true">{{ $moment(element.start).format('YYYY-MM-DD') }} - {{ $moment(element.end).add(-1, 'days').format('YYYY-MM-DD') }}</p> -->
											<p class="mb-1" v-if="element.allDay==true && $moment(element.start).format('YYYY-MM-DD') < $moment(element.end).format('YYYY-MM-DD')" >{{ $moment(element.start).format('YYYY-MM-DD') }} - {{ $moment(element.end).add(-1, 'days').format('YYYY-MM-DD') }}</p>
											<p class="mb-1" v-if="element.allDay==true && $moment(element.start).format('YYYY-MM-DD') >= $moment(element.end).format('YYYY-MM-DD')" >{{ $moment(element.start).format('YYYY-MM-DD') }} - {{ $moment(element.end).format('YYYY-MM-DD') }}</p>
							
											<p class="mb-1" v-if="element.allDay==false">{{ $moment(element.start).format('YYYY-MM-DD') }} - {{ $moment(element.end).format('YYYY-MM-DD') }}</p>
											<h6 class="kanban-content">{{ element.description }}</h6>
											<div class="d-flex" style="flex-wrap: wrap;">
												<div v-for="groupMem in element.members" :key="groupMem.id" class="mr-1">
													<div>
														<Gravatar :email="groupMem" :size="20" />
														<q-tooltip>{{ groupMem }}</q-tooltip>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</draggable>
						</div>
					</div>

					<div class="col-md-4" style="min-width: 300px;">
						<div class="p-2 alert alert-success" style="opacity: 0.9;">
							<h3>Done</h3>
							<hr>
							<draggable @change="donednd" class="list-group kanban-box kanban-column" :list="this.TASKS" group="tasks">
								<div class="list-group-item d-flex p-0 b-0 m-0" v-for="(element, idx) in this.TASKS" :key="idx" style="text-decoration: line-through; color:gray;">
									<div class="d-flex" v-if="element.status=='done'">  
										<q-card class="color-index d-flex justify-content-end mr-2" :style="{ background: element.color }"></q-card>
											<div class="p-2 mt-1">
												<h5 class="kanban-content">{{ element.title }}</h5>
												<p class="m-0" v-if="element.group!=null">{{ element.group }}</p>
												<p class="m-0" v-if="element.group==null">My Schedule</p>
												<!-- <p class="mb-1" v-if="element.allDay==true">{{ $moment(element.start).format('YYYY-MM-DD') }} - {{ $moment(element.end).add(-1, 'days').format('YYYY-MM-DD') }}</p> -->
											    <p class="mb-1" v-if="element.allDay==true && $moment(element.start).format('YYYY-MM-DD') < $moment(element.end).format('YYYY-MM-DD')" >{{ $moment(element.start).format('YYYY-MM-DD') }} - {{ $moment(element.end).add(-1, 'days').format('YYYY-MM-DD') }}</p>
												<p class="mb-1" v-if="element.allDay==true && $moment(element.start).format('YYYY-MM-DD') >= $moment(element.end).format('YYYY-MM-DD')" >{{ $moment(element.start).format('YYYY-MM-DD') }} - {{ $moment(element.end).format('YYYY-MM-DD') }}</p>
							
												<p class="mb-1" v-if="element.allDay==false">{{ $moment(element.start).format('YYYY-MM-DD') }} - {{ $moment(element.end).format('YYYY-MM-DD') }}</p>
												<h6 class="kanban-content">{{ element.description }}</h6>
												<div class="d-flex" style="flex-wrap: wrap;">
													<div v-for="groupMem in element.members" :key="groupMem.id" class="mr-1">
														<div>
															<Gravatar :email="groupMem" :size="20" />
															<q-tooltip>{{ groupMem }}</q-tooltip>
														</div>
													</div>
												</div>
											</div>
										</div>        
									</div>
								</draggable>
							</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
import { mdiCalendar } from '@quasar/extras/mdi-v5'
 import draggable from 'vuedraggable'
import Gravatar from 'vue-gravatar'
import { mapGetters, mapActions } from 'vuex'

export default {
	components: {      
		Gravatar,
		draggable
	},
	props: {
		people: String
	},
	data() {
		return {

		}
	},
	computed: {
		...mapGetters('calendar', ["EVENTS", "LUN_DAYS", "TASKS", "MEMBER_INFO", "CHATTINGS"]),
	},
	methods: {
		...mapActions('calendar', ["changeStatus"]),

		switchingcal() {
			// this.switching =! this.switching 
			this.$emit('switchingcal')
		},
		
		backlogdnd(evt) {
      if(!(evt.added===undefined)){
        evt.added.element.status = 'backlog'     
				// this.$store.dispatch('changestatus',{schedule: evt.added.element,people:this.people});
				this.changeStatus({schedule: evt.added.element,people:this.people})
      }       
    },

    inprogressdnd(evt) {
      if(!(evt.added===undefined)){
        // console.log("dawdaw",evt.added)
        evt.added.element.status = 'inprogress'     
        this.changeStatus({schedule: evt.added.element,people:this.people})
      }     
    },

    donednd(evt) {
      if(!(evt.added===undefined)){
        evt.added.element.status = 'done'    
      	this.changeStatus({schedule: evt.added.element,people:this.people})
      }      
    },
	},
	created() {
		this.mdiCalendar = mdiCalendar
	}
}
</script>

<style scoped>
.demo-app {
  display: flex;
  font-size: 14px;
}

.kanban-column {
  min-height: 500px;
  /* max-height: 450px; */
	/* height: 100%; */
  overflow: auto;
  text-align: left;
}

.list-group-item {
  border: 0 !important;
}

.color-index {
	width: 5px; 
	opacity: 0.7;
}

.kanban-box {
	max-height:500px; 
	overflow:hidden;
}

.kanban-box:hover {
	overflow-y:auto;
}

.kanban-content {
	width: 200px;
	overflow: hidden; 
	text-overflow:ellipsis; 
	display: -webkit-box; 
	-webkit-line-clamp: 1; 
	-webkit-box-orient: vertical;
}

/* width */
::-webkit-scrollbar {
  width: 5px;
}

/* Track */
::-webkit-scrollbar-track {
  background: transparent;
}

/* Handle */
::-webkit-scrollbar-thumb {
  background: #888;
}

/* Handle on hover */
::-webkit-scrollbar-thumb:hover {
  background: #555;
}
</style>