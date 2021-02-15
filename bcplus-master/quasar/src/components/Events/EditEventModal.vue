<template>
  <q-card id="SA" class="q-pa-md rounded border">
    <div class="q-gutter-md m-0">
      <h4>Edit Schedule</h4>
			<div class="container m-0 p-0">
				<q-input class="m-0 p-0" v-model="title_tmp" label="insert schedule title here" />
				<q-input class="m-0 p-0" v-model="description_tmp" type="textarea" label="insert schedule detail here" />
			</div>

			<div class="q-gutter-md m-0 d-flex justify-content-between flex-wrap">
				<div class="d-flex">
					<div>
						<q-input v-model="start_tmp" type="date" label="start date" />
						<vue-timepicker v-model="start_time_tmp" format="HH:mm" fixed-dropdown-button drop-direction="up" v-if="!allDay_value_tmp">
							<template v-slot:dropdownButton>
								<q-icon :name="mdiClock" />
							</template>
						</vue-timepicker>
					</div>

					<div>
						<p class="m-0 ml-2 mr-2"></p>
					</div>

					<div>
						<q-input v-model="end_tmp" type="date" label="end date" /> 
						<vue-timepicker v-model="end_time_tmp" format="HH:mm" fixed-dropdown-button drop-direction="up" v-if="!allDay_value_tmp">
							<template v-slot:dropdownButton>
								<q-icon :name="mdiClock" />
							</template>
						</vue-timepicker>
					</div>
				</div>

				<div>
					<q-checkbox
						v-model="allDay_value_tmp"
						label="Allday Event"
            class="m-0 mr-4 mt-3"
					/>
				</div>
			</div>

			<br>
			
			<div class="color-picker m-0">
					<div class="color" v-for="(theme, index) in colors" :key="index" @click="selectColor(theme)" :class="{selected: color_tmp === theme, [theme]: theme}"></div>
			</div>
			<div class="d-flex justify-content-end">
				<button class="btn text-primary" @click="updateEvent" :class="{ 'text-light' : this.$q.dark.isActive }">Create</button>
        <button class="btn text-danger" @click="exit" :class="{ 'text-light' : this.$q.dark.isActive }">cancel</button>
			</div>
    </div>
  </q-card>
</template>

<script>
import { mdiClock } from '@quasar/extras/mdi-v5'
import swal from 'sweetalert'
import VueTimepicker from 'vue2-timepicker/src/vue-timepicker.vue'
import { mapActions } from 'vuex'

export default {
  props: {
    id: String,
    title: String,
    description: String,
    color: String,
    start: String,
    end: String,
    rstart: String,
    rend: String,
    realend: String,
    group_name: Array,
    selectgroup: String,
    event: Object,
    people: String,
  },
  data: () => ({
    allDay_value_tmp: null,
    colors: ['blue', 'indigo', 'deep-purple', 'cyan', 'green', 'orange', 'red'],
    group: '',
    title_tmp:'',
    description_tmp:'',
    start_tmp:'',
    end_tmp:'',
    start_time_tmp: '00:00',
    end_time_tmp: '00:00',
    color_tmp:'',    
  }),
  components: { 
    VueTimepicker,
    mdiClock 
  },
  created () {
    this.mdiClock = mdiClock

    this.allDay_value_tmp = this.event.allDay
    this.title_tmp=this.title
    this.description_tmp=this.description
    this.start_tmp = this.$moment(this.start).format('YYYY-MM-DD');      
    this.end_tmp = this.$moment(this.rend).format('YYYY-MM-DD');
    this.start_time_tmp = this.$moment(this.start).format('HH:mm');
    this.end_time_tmp = this.$moment(this.rend).format('HH:mm');
    this.color_tmp = this.color
  },
  methods: {
    ...mapActions('calendar', ['updateschedule','updateMySchedule']),

    updateEvent () {
      if (this.title_tmp == '') {
        swal('Warning', 'Please enter group name!', 'warning')
      } else if (this.selectgroup == null) {
        swal('Warning', 'Please select group!', 'warning')
      } else if (this.color_tmp == '') {
        swal('Warning', 'Please select schedule color!', 'warning')
      } else {
          var real_start = this.start_tmp + ' ' + this.start_time_tmp
          var real_end = this.end_tmp + ' ' + this.end_time_tmp

          this.start_tmp = this.$moment(real_start, 'YYYY-MM-DD HH:mm:ss')
          this.end_tmp= this.$moment(real_end, 'YYYY-MM-DD HH:mm:ss')

           if (this.$moment.duration(this.end_tmp.diff(this.start_tmp)).asMinutes() < 0) {
            swal('Warning', 'Please check your schedule time!', 'warning')
          } else {
            if (this.$moment.duration(this.end_tmp.diff(this.start_tmp)).asDays() >= 1) {
              // console.log('1일이상 차이', this.$moment(this.end_tmp).add('1', 'minutes'), this.end_tmp)
              // this.$store.dispatch("updateschedule", {              
              this.updateschedule({
                id: this.id,
                title: this.title_tmp,
                description: this.description_tmp,
                start: this.start_tmp,
                rStart: this.start_tmp,
                rEnd: this.$moment(this.end_tmp).add('1', 'days'),
                end: this.end_tmp,
                realend: this.$moment(this.end_tmp).format('YYYY-MM-DD'),
                color: this.color_tmp,
                people: this.people,
                allDay: true,
              })
            } else {
              // this.$store.dispatch("updateschedule", {
              this.updateschedule({
                id: this.id,
                title: this.title_tmp,
                description: this.description_tmp,
                start: this.start_tmp,
                rStart: this.start_tmp,
                rEnd: this.end_tmp,
                end: this.end_tmp,
                realend: this.$moment(this.end_tmp).format('YYYY-MM-DD'),                
                color: this.color_tmp,
                people: this.people,
                allDay: this.allDay_value_tmp,
              })
            }
            swal('Success', 'Your request is approved', 'success')
            this.$emit('close')
          }
        }
      },
      selectColor(color){
        this.color_tmp = color
      },

      exit() {
			  this.$emit('close')
		  }
    },
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
.vue__time-picker {
  width: 9em !important;
}

.vue__time-picker input.display-time {
  width: 9em !important;
}
</style>