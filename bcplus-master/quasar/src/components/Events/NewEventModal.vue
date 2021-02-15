<template>
	<q-card id="SA" class="q-pa-md rounded border">
		<div class="q-gutter-md m-0">
			<h4>New Schedule</h4>
			<div v-if="people" class="small ml-3">
				Selected Group: {{ this.people }}
			</div>
			<div class="container m-0 p-0">
				<q-input class="m-0 p-0" v-model="title" label="insert schedule title here" />
				<q-input class="m-0 p-0" v-model="description" type="textarea" label="insert schedule detail here" />
			</div>

			<div class="q-gutter-md m-0 d-flex justify-content-between flex-wrap">
				<div class="d-flex">
					<div>
						<q-input v-model="start" type="date" label="start date" />
						<vue-timepicker v-model="start_time" format="HH:mm" fixed-dropdown-button drop-direction="up" v-if="!allDay_value">
							<template v-slot:dropdownButton>
								<!-- <v-icon color="black">mdi-clock</v-icon> -->
                <q-icon :name="mdiClock" />
							</template>
						</vue-timepicker>
					</div>
          
					<div>
						<p class="m-0 ml-2 mr-2"></p>
					</div>

					<div>
						<q-input v-model="rend" type="date" label="end date" /> 
						<vue-timepicker v-model="end_time" format="HH:mm" fixed-dropdown-button drop-direction="up" v-if="!allDay_value">
							<template v-slot:dropdownButton>
								<q-icon :name="mdiClock" />
							</template>
						</vue-timepicker>
					</div>
				</div>   

				<div>
					<q-checkbox
						v-model="allDay_value"
						label="Allday Event"
            class="m-0 mr-4 mt-3"
					/>
				</div>
			</div>

			<br>
			
			<div class="color-picker m-0">
				<div class="color" v-for="(theme, index) in colors" :key="index" @click="selectColor(theme)" :class="{selected: color === theme, [theme]: theme}"></div>
			</div>
			<div class="d-flex justify-content-end">
				<button class="btn text-primary" @click="updateEvent" :class="{ 'text-light' : this.$q.dark.isActive }">Create</button>
        <button class="btn text-danger" @click="exit" :class="{ 'text-light' : this.$q.dark.isActive }">cancel</button>
			</div>
		</div>
	</q-card>  
</template>

<script>
import { mapGetters, mapActions } from 'vuex'
import { mdiClock } from '@quasar/extras/mdi-v5'
import swal from 'sweetalert'
import VueTimepicker from 'vue2-timepicker/src/vue-timepicker.vue'

export default {
    name: "NewEventModal",
    props: {
      start_cal: Date,
      end_cal: Date,
      event: Object,
      group_name: Array,
      people: String,
      goDark:Boolean,
      currentData: String,
    },
    computed: {
      ...mapGetters(["EVENTS", "GROUPS"]),
    },
    components: { 
      VueTimepicker, 
      mdiClock 
    },
    data: () => ({
      allDay_value: true,
      title: "",
      description: "",
      start: null,
      start_time: '00:00',
      end_time: '23:59',
      end: {},
      realend: {},
      color: '',
      colors: ['blue', 'indigo', 'deep-purple', 'cyan', 'green', 'orange', 'red'],
      group: '',
      selectgroup: '',
      rstart: {},
      rend: null,
    }),
    methods: {
			...mapActions('calendar', ['createEvent']),

      updateEvent () {
        if (this.title == '') {
          swal('Warning', 'Please enter group name!', 'warning')
        } else if (this.color == '') {
          swal('Warning', 'Please select schedule color!', 'warning')
        } else {
          var real_start = this.start + ' ' + this.start_time
          var real_end = this.rend + ' ' + this.end_time

          this.start = this.$moment(real_start, 'YYYY-MM-DD HH:mm:ss')
          this.rend = this.$moment(real_end, 'YYYY-MM-DD HH:mm:ss')

          // console.log(this.$moment.duration(this.rend.diff(this.start)).asMinutes())

          if (this.$moment.duration(this.rend.diff(this.start)).asMinutes() < 0) {
            swal('Warning', 'Please check your schedule time!', 'warning')
          } else {
            if (this.$moment.duration(this.rend.diff(this.start)).asDays() >= 1) {
            this.createEvent({
              id: (new Date()).getTime(),
              title: this.title,
              description: this.description,
              start: this.start,
              rStart: this.start,
              rEnd: this.$moment(this.rend).add('1', 'minutes'),
              // rEnd: this.$moment(this.rend).add('1', 'days'),
              end: this.rend,
              realend: this.$moment(this.end).format('YYYY-MM-DD'),
              group: this.people,
              color: this.color,
              allDay: true,
            })
          } else {
           
            this.createEvent({
              id: (new Date()).getTime(),
              title: this.title,
              description: this.description,
              start: this.start,
              rStart: this.start,
              rEnd: this.rend,
              end: this.rend,
              realend: this.$moment(this.end).format('YYYY-MM-DD'),
              group: this.people,
              color: this.color,
              allDay: this.allDay_value,
            })
          }
          this.$emit('close')
          }
        }
        
      },
      selectColor(color){
        this.color = color
      },

      exit() {
			  this.$emit('close')
		  }
    },
    mounted() {
      this.selectgroup = this.people
      this.start = this.$moment(this.start_cal).format('YYYY-MM-DD')
      // console.log(this.currentData)
      if (this.currentData == 'dayGridMonth') {
        this.rend = this.$moment(this.end_cal).add(-1, 'days').format('YYYY-MM-DD')
      } else {
        // console.log(this.end_cal)
        this.rend = this.$moment(this.end_cal).format('YYYY-MM-DD')
        this.start_time = this.$moment(this.start_cal).format('HH:mm')
        this.end_time = this.$moment(this.end_cal).format('HH:mm')
      }
      
    },
    created() {
      this.mdiClock = mdiClock
    }
};

</script>

<style scoped>

.color-picker {
  display: flex;
  justify-content: space-around;
  margin-bottom: 0px;
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

<style lang="scss">
.q-field__control-container {
	margin: 0 !important;
	padding: 0 !important;
}

.q-field__control {
	margin: 0 !important;
	padding: 0 !important;
}

.q-textarea.q-field--labeled .q-field__native {
  padding-top: 30px !important;
}

.vue__time-picker {
  width: 9em !important;
}

.vue__time-picker input.display-time {
  width: 9em !important;
}
</style>