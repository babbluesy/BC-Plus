<template>
  <div id="SA" class="rounded border">
		<q-card class="my-card">

      <q-card-section class="text-white" :style="{ background: this.color }">
				<div class="d-flex justify-content-between">
					<div class="text-h7 mt-1 eventDetail-title">{{ event.title }}</div>
					<div class="d-flex">
						<q-icon
							@click="deleteForm"
							v-if="(this.master==this.userId_tmp && this.people!=null) || (this.people==null && event.startEditable==true)"
							:name="mdiTrashCanOutline"
							size="sm" 
							class="icon-btn m-1 mb-0"
						/>
            <!-- edit 버튼 -->
						<q-icon 
							@click="changeForm" 
							v-if="(this.master==this.userId_tmp && this.people!=null) || (this.people==null && event.startEditable==true)" 
							:name="mdiPencil" 
							size="sm" 
							class="icon-btn m-1 mb-0"
						/>
            <!-- edit 버튼 -->
					</div>
				</div>
      </q-card-section>

      <q-card-section class="p-3 mx-0">
				<q-tabs
					v-model="tab"
					dense
					class="text-grey"
					align="justify"
					narrow-indicator
				>
					<q-tab name="Detail" label="Detail" />
					<q-tab name="Weather" label="Weather" />
					<q-tab name="Location" label="Location" />
				</q-tabs>

      	<q-separator />

				<q-tab-panels v-model="tab" animated>
					<q-tab-panel name="Detail">
						<div v-if="this.people==null">
							<div class="pt-0 mb-2" v-if="event.startEditable==true">
								<p><span class="font-weight-bold">{{ event.extendedProps.group }}</span> 's private schedule</p>
							</div>

							<div v-else class="pt-0 mb-2" v-for="group in this.TASKS" :key="group.sid">
								<div class="pt-0" v-if="group.sid == event.extendedProps.sid">
										<p v-if="group.group"><span class="font-weight-bold">Group {{ group.group }}</span> schedule</p>
										<p v-else><span class="font-weight-bold">{{ event.extendedProps.group }}</span> 's private schedule</p>
									</div>
								</div>
						</div>
			
						<div v-else class="pt-0">
							<p><span class="font-weight-bold">Group {{ this.people }}</span> schedule</p>
						</div>

							<div>
								<div class="text-subtitle2 pb-2 pt-1">
										<div class="mr-2"><strong>Title: </strong></div>
										<div class="text-h7 eventDetail-content">{{ event.title }}</div>
								</div>

								<div class="text-subtitle2 pb-2 pt-1">
										<div class="mr-2"><strong>Date: </strong></div>
										<div class="text-subtitle2">{{ start }}  -  {{ end }}</div>
								</div>

								<div class="text-subtitle2 pb-2 pt-1">
										<div class="mr-2 eventDetail-content"><strong>Description: </strong></div>
										{{ event.extendedProps.description }}
								</div>

								<div v-if="event.extendedProps.members.length!=0" class="py-2 mx-0">
										<q-separator class="mb-2 mx-0" inset style="width: 100%;" />
										members
								</div>

								<div class="d-flex mb-2" style="flex-wrap: wrap;">
										<div v-for="member in event.extendedProps.members" :key="member.id" class="d-flex pb-2 pt-1 mx-1" style="width: 20px;">
											<div>
													<Gravatar :email="member" :size="20" />
														<q-tooltip>
																<span>{{ member }}</span>
														</q-tooltip>
											</div>
										</div>
								</div>
							</div>
					</q-tab-panel>

					<q-tab-panel name="Weather">
						<div class="py-0">
              <div class="d-flex mb-3" v-if="analysis_data">
                <q-icon :name="mdiMapMarker" style="margin-top: 2px;" />
                <p>{{ analysis_data.location_lst[0].loc }}</p>
              </div>

          <h5>Weather</h5>

          <div v-if="!analysis_data" class="d-flex justify-content-center">
            <div style="text-align: center;">
              <q-spinner-hourglass
                size="3em"
                :style="{ color: color }"
              />
              <p :style="{ color: color }">Analyzing user data...</p>
            </div>
          </div>
          <div v-else>
            <div class="d-flex" align="center">
              <div class="container px-0" style="text-align: center; width: 60%;">
                <div class="d-flex justify-content-center">
                  <h5>{{ this.$moment(new Date()).format('MM/DD')}}</h5>
                  <q-chip color="green" class="mx-0 mt-0 ml-1" size="sm" style="color: white;">Today</q-chip>

                </div>
                <div class="d-flex justify-content-center">
                  <!-- <h1 class="m-0 d-flex" style="font-size: 5rem;"><i class="las la-sun"></i></h1> -->
                  <!-- <h5 class="px-2 mt-4 mb-0 pt-1">{{ this.analysis_data.weather.today.temp }}˚</h5> -->
                  <h2>{{ this.analysis_data.weather.today.temp }}˚</h2>
                </div>
                <div class="d-flex justify-content-center">
                  <p><i class="las la-temperature-low" style="color: #4d4dff;"></i>{{ this.analysis_data.weather.today.min_temp }} /</p>
                  <p class="ml-1"><i class="las la-temperature-high" style="color: #ff4d4d;"></i> {{ this.analysis_data.weather.today.max_temp }}</p>
                </div>
                <p style="font-size: 0.9rem;">{{ this.analysis_data.weather.today.cast_txt }}</p>
              </div>

              <!-- <div style="width: 40%;">
                <div class="bg-transparent pt-1 pl-3">
                  <div class="container px-0 py-1" style="text-align: center;">
                    <p style="font-size: 1.1rem;">{{ this.$moment(new Date()).add(1, 'days').format('MM/DD')}}</p>
                    <div class="d-flex justify-content-center">
                      <p><i class="las la-temperature-low" style="color: blue;"></i>{{ this.analysis_data.weather.D1.temp_D1_AM }} /</p> 
                      <p class="ml-1"><i class="las la-temperature-high" style="color: red;"></i>{{ this.analysis_data.weather.D1.temp_D1_PM }}</p>
                    </div>
                  </div>

                  <div class="container px-0 py-1" style="text-align: center;">
                    <p style="font-size: 1.1rem;">{{ this.$moment(new Date()).add(2, 'days').format('MM/DD')}}</p>
                    <div class="d-flex justify-content-center">
                      <p><i class="las la-temperature-low" style="color: blue;"></i>{{ this.analysis_data.weather.D2.temp_D2_AM }} /</p> 
                      <p class="ml-1"><i class="las la-temperature-high" style="color: red;"></i>{{ this.analysis_data.weather.D2.temp_D2_PM }}</p>
                    </div>
                  </div>
                </div>
              </div> -->
            </div>
          </div>
							</div>
					</q-tab-panel>

					<q-tab-panel name="Location">
							<div class="py-0">
                <div class="d-flex mb-3" v-if="analysis_data">
                  <q-icon :name="mdiMapMarker" style="margin-top: 2px;" />
                  <p>{{ analysis_data.location_lst[0].loc }}</p>
                </div>
								<div>
									<h5>Location</h5>
                  <div v-if="!analysis_data" class="d-flex justify-content-center">
                    <div style="text-align: center;">
                      <q-spinner-hourglass
                        size="3em"
                        :style="{ color: color }"
                      />
                      <p :style="{ color: color }">Analyzing user data...</p>
                    </div>
                  </div>
                  <div v-else>
                    <div class="row location-box">
                      <div class="col-6 mb-1" v-for="(analysis, idx) in analysis_data.restaurant.rest_lst" :key="idx" style="overflow: hidden;">
                        <q-img class="rounded-borders" :src="analysis.img_path" style="height: 100px !important; overflow: hidden;"/>
                        <p style="font-size: 0.9rem; !important">{{ analysis.rest_name }}</p>
                        <p style="font-size: 0.8rem;">★ {{ analysis.rest_rate }}</p>
                        <!-- <p style="font-size: 0.8rem;">{{ analysis.rest_category }}</p> -->
                      </div>
										</div>
                  </div>
								</div>
							</div>
					</q-tab-panel>

				</q-tab-panels>

      </q-card-section>

    </q-card>
  </div>
</template>

<script>
import EditEventModal from '../../components/Events/EditEventModal.vue'
import EventDetailforMobile from './EventDetailforMobile.vue'
import EventDetailforDesktop from './EventDetailforDesktop.vue'

import { mdiPencil, mdiTrashCanOutline, mdiMapMarker } from '@quasar/extras/mdi-v5'
import { laSun } from '@quasar/extras/line-awesome'
import { Plugins } from '@capacitor/core'

import { mapGetters, mapActions } from 'vuex'
import Gravatar from 'vue-gravatar'
import swal from 'sweetalert'
import axios from 'axios'
import Geocoder from "@pderas/vue2-geocoder"

let https = require('https')
const instance = axios.create({
  baseURL: 'https://bcplus.me/api',
  timeout: 120000,
  httpsAgent: new https.Agent({
    rejectUnauthorized: false
  })
});

const { Geolocation } = Plugins

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
  data() {
    return {
      coord: null,
      latitude: null,
      longitude: null,
      analysis_data: null,
      roof: [1, 2, 3, 4, 5, 6],
      tab: 'Detail',
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
      userId_tmp:'',
      slide: 1
    }
  },
  components: { 
    Gravatar,
    EventDetailforMobile,
    EventDetailforDesktop
  },
  methods: {
		...mapActions('calendar', ['deleteschedule']),

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
            // this.$store.dispatch("deleteschedule", {
						this.deleteschedule({
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
    },
    getCurrentPosition(title, description) {
      Geolocation.getCurrentPosition().then(position => {
        console.log('Current', position);
        this.position = position

        this.latitude  = position.coords.latitude;
        this.longitude = position.coords.longitude;

        this.$geocoder.setDefaultMode('lat-lng');
          var latLngObj = {
              lat: this.latitude,
              lng: this.longitude,
              result_type: 'administrative_area_level_1'
          }
            this.$geocoder.send(latLngObj, response => { 
              this.coord = response.results[0].formatted_address
              console.log(title, description)
              console.log(this.coord)

              axios.post('https://bcplus.me/analysis/MemoAnalysis', {
              text: title + ',' + description,
              coord: this.coord
            })
              .then(res => {
                this.analysis_data = res.data
                console.log('data', this.analysis_data)
              }) .catch(res => {
                console.log(err)
              })
            })
      });
    }
  },
  computed: {
    ...mapGetters('calendar', ["EVENTS", "GROUPS", "USER", "group_name", "LUN_DAYS", "TASKS" ]),
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
      if( this.event.durationEditable == false) {
        this.end = this.$moment(this.event.extendedProps.rend).format('YYYY-MM-DD HH:mm')
      } else {
        this.end = this.$moment(this.event.end).format('YYYY-MM-DD HH:mm')
      }
      
      console.log(this.event, 's')
    }
    
    // render시 날짜표기수정
    this.realend = this.$moment(this.event.realend).format('YYYY-MM-DD');
    // DB에 넣을때(선택)     
    // this.end = this.$moment(this.event.end).format('YYYY-MM-DD');
    this.printend = this.$moment(this.event.end).add(-1, 'days').format('YYYY-MM-DD');
    this.selectgroup = this.event.extendedProps.group
    
  },
  created() {
    this.color = this.event.backgroundColor;
    this.start = this.$moment(this.event.start).format('YYYY-MM-DD');
    this.printend=this.$moment(this.event.end).add(-1, 'days').format('YYYY-MM-DD');
    this.realend = this.$moment(this.event.realend).add(-1, 'days').format('YYYY-MM-DD');
    this.token=sessionStorage.getItem("access-token")
    this.userId_tmp=sessionStorage.getItem("username")
    
    this.colorHex = {
      '#2196F3': 'blue',
      '#3F51B5': 'indigo',
      '#673AB7': 'deep-purple',
      '#00BCD4': 'cyan',
      '#4CAF50': 'green',
      '#FF9800': 'orange',
      '#F44336': 'red',
    }   
  
    this.getCurrentPosition(this.event.title, this.event.extendedProps.description)
    
    instance.get('/member/'+ sessionStorage.getItem("username"))
      .then(res =>{
        sessionStorage.setItem("userId", res.data.userId)
      })
		this.userId_tmp=sessionStorage.getItem("userId")
		
		this.mdiPencil = mdiPencil
    this.mdiTrashCanOutline = mdiTrashCanOutline
    this.mdiMapMarker = mdiMapMarker
  }
};
</script>

<style scoped>
.icon-btn {
	cursor: pointer;
}

P {
  margin-bottom: 0px;
}

.q-tabs__content {
  margin: 0 !important;
}

.location-box {
	height:290px; 
	overflow:hidden;
}

.location-box:hover {
	overflow-y:auto;
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
.eventDetail-content {
  word-break:break-all;
}

.eventDetail-title {
  text-overflow:ellipsis;
  white-space:nowrap;
  word-wrap:normal;
  width:300px;
  overflow:hidden;
}
</style>

<style lang="scss">
.q-chip__content {
  bottom: 2px;
}
</style>