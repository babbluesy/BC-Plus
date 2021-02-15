<template>
  <v-app id="inspire">
    <fieldset>
      <v-card id="SA" class="py-3 px-3 rounded border">
        <v-container id="SA">
          <h4>New Schedule</h4>
          <div v-if="people" class="small ml-3">
            Selected Group: {{ this.people }}
          </div>
          <v-text-field v-model="title" type="text" label="insert schedule title here"></v-text-field>
          <v-textarea v-model="description" type="textarea" label="insert schedule detail here"></v-textarea>
          <div class="mr-2 ml-2 d-flex justify-content-between flex-wrap">
            <div class="d-flex">
              <div>
                <v-text-field v-model="start" type="date" label="start date"></v-text-field>
                <vue-timepicker v-model="start_time" format="HH:mm" fixed-dropdown-button drop-direction="up" v-if="!allDay_value">
                  <template v-slot:dropdownButton>
                    <v-icon color="black">mdi-clock</v-icon>
                  </template>
                </vue-timepicker>

              </div>
              <div>
                <p class="m-0 ml-2 mr-2"></p>
              </div>
              <div>
                <v-text-field v-model="rend" type="date" label="end date"></v-text-field> 
                <vue-timepicker v-model="end_time" format="HH:mm" fixed-dropdown-button drop-direction="up" v-if="!allDay_value">
                  <template v-slot:dropdownButton>
                    <v-icon color="black">mdi-clock</v-icon>
                  </template>
                </vue-timepicker>

              </div>
              
            </div>   
            <div>
              <v-checkbox
                v-model="allDay_value"
                label="Allday Event"
                hide-details
              ></v-checkbox>
            </div>
          </div>
  
          <br>
          
          <div class="color-picker">
            <div class="color" v-for="(theme, index) in colors" :key="index" @click="selectColor(theme)" :class="{selected: color === theme, [theme]: theme}"></div>
          </div>
          <div class="d-flex justify-content-end">
            <button class="btn text-primary" @click="updateEvent">Create</button>
          </div>
        </v-container>
      </v-card>
    </fieldset>    
  </v-app>
</template>

<script>
import { mapGetters } from 'vuex'
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
    },
    computed: {
      ...mapGetters(["EVENTS", "GROUPS"]),
    },
    components: { VueTimepicker },
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

            if (this.$moment.duration(this.rend.diff(this.start)).asDays() >= 1) {
              this.$store.dispatch("createEvent", {
                id: (new Date()).getTime(),
                title: this.title,
                description: this.description,
                start: this.start,
                rStart: this.start,
                rEnd: this.$moment(this.rend).add('1', 'minutes'),
                end: this.rend,
                realend: this.$moment(this.end).format('YYYY-MM-DD'),
                group: this.people,
                color: this.color,
                allDay: true,
              })
            } else {
              this.$store.dispatch("createEvent", {
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
      },
      selectColor(color){
        this.color = color
      }
    },
    mounted() {
      this.selectgroup = this.people;
      this.start = this.$moment(this.start_cal).format('YYYY-MM-DD');
      this.rend = this.$moment(this.end_cal).add(-1, 'days').format('YYYY-MM-DD');
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