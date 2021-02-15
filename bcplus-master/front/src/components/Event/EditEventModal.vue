<template>
  <v-app id="inspire">
    <div id="SA" class="rounded border">
      <fieldset>
        <v-card class="p-2">
          <v-container>
            <h4>Edit Schedule</h4>
            <v-text-field v-model="title_tmp" type="text" label="schedule"></v-text-field>
            <v-textarea v-model="description_tmp" type="textarea" label="detail"></v-textarea>

            <div class="mr-2 ml-2 d-flex justify-content-between flex-wrap">
              <div class="d-flex">
                <div>
                  <v-text-field v-model="start_tmp" type="date" label="start date"></v-text-field>
                  <vue-timepicker v-model="start_time_tmp" format="HH:mm" fixed-dropdown-button drop-direction="up" v-if="!allDay_value_tmp">
                    <template v-slot:dropdownButton>
                      <v-icon color="black">mdi-clock</v-icon>
                    </template>
                  </vue-timepicker>
                </div>
                <div>
                  <p class="m-0 ml-2 mr-2"></p>
                </div>
                <div>
                  <v-text-field v-model="end_tmp" type="date" label="end date"></v-text-field> 
                  <vue-timepicker v-model="end_time_tmp" format="HH:mm" fixed-dropdown-button drop-direction="up" v-if="!allDay_value_tmp">
                    <template v-slot:dropdownButton>
                      <v-icon color="black">mdi-clock</v-icon>
                    </template>
                  </vue-timepicker>
                </div>        
              </div>   
            <div>
              <v-checkbox
                v-model="allDay_value_tmp"
                label="Allday Event"
                hide-details
              ></v-checkbox>
            </div>
            </div>
            <br>
            <div class="color-picker">
                <div class="color" v-for="(theme, index) in colors" :key="index" @click="selectColor(theme)" :class="{selected: color_tmp === theme, [theme]: theme}"></div>
            </div>
            <div class="d-flex justify-content-end">
              <button class="btn text-primary" @click="updateEvent">Edit</button>
            </div>
          </v-container>
        </v-card>
      </fieldset>
    </div>
  </v-app>
</template>

<script>
import swal from 'sweetalert'
import VueTimepicker from 'vue2-timepicker/src/vue-timepicker.vue'

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
  components: { VueTimepicker },
  created () {
    console.log(this.start, this.end, this.color, 'editevent')
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

          if (this.$moment.duration(this.end_tmp.diff(this.start_tmp)).asDays() >= 1) {
            console.log('1일이상 차이')
            this.$store.dispatch("updateschedule", {
              id: this.id,
              title: this.title_tmp,
              description: this.description_tmp,
              start: this.start_tmp,
              rStart: this.start_tmp,
              rEnd: this.$moment(this.end_tmp).add('1', 'minutes'),
              end: this.end_tmp,
              realend: this.$moment(this.end_tmp).format('YYYY-MM-DD'),
              color: this.color_tmp,
              people: this.people,
              allDay: true,
            })
          } else {
              this.$store.dispatch("updateschedule", {
                id: this.id,
                title: this.title_tmp,
                description: this.description_tmp,
                start: this.start_tmp,
                rStart: this.start_tmp,
                rEnd: this.end_tmp,
                end: this.end_tmp,
                realend: this.$moment(this.end_tmp).format('YYYY-MM-DD'),
                people: this.people,
                color: this.color_tmp,
                allDay: this.allDay_value_tmp,
              })
            }
          swal('Success', 'Your request is approved', 'success')
          this.$emit('close')
        }
      },
      selectColor(color){
        this.color_tmp = color
      },
    },
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
