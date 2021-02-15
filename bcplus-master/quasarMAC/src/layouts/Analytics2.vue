<template>
  <div id="SA">

		<div id="SA">
			<q-toolbar class="text-white header-color">
				<q-btn flat round dense :icon="$q.screen.lt.sm ? 'list' : 'menu'" color="transparent"/>
				<q-toolbar-title class="d-flex pt-2 pl-0 icon-btn" @click="$router.push('calendar')">
					<h3 v-if="!this.$q.screen.lt.sm" class=""><span class="font-weight-bold">P</span><span style="font-size: 10px;">ersonal</span><span class="font-weight-bold">S</span><span style="font-size: 10px;">ecretary</span> Calendar</h3>
					<h3 v-else class=""><span class="font-weight-bold">P.S.</span></h3>
					<div class="ml-4">
        </div>
				</q-toolbar-title>

				<b-dropdown size="lg" variant="link" toggle-class="text-decoration-none" no-caret>
          <template #button-content>
            <!-- &#x1f50d;<span class="sr-only">Search</span> -->
            <q-item class="bg-transparent">
              <q-avatar class="m-0 bg-transparent">
                <Gravatar :email="userId" class="bg-transparent" />
              </q-avatar>
              <q-item-section id="NS" v-if="!$q.screen.lt.sm" class="h5 text-white mr-10 mt-1 ml-2 toggle-username">{{ userName }}</q-item-section>
            </q-item>
          </template>
					<b-dropdown-item variant="primary" @click="$router.push('/calendar')">Calendar</b-dropdown-item>
          <b-dropdown-item variant="danger" @click="gotoLogout">Logout</b-dropdown-item>
        </b-dropdown>
				<q-btn flat round dense :icon="$q.screen.lt.sm ? 'group' : 'menu'" color="transparent"/>
			</q-toolbar>
		</div>

		<div v-if="!$q.screen.lt.sm" class="container d-flex my-2" style="height: 100vh;">
			<div class="p-1" style="width: 25%;">
				<div class="rounded p-2 mb-2 border" style="height: 30%; background: #fafffc;" :class="{ 'mypage-dark' : $q.dark.isActive == true }">
					<div class="my-4" align="center">
						<q-avatar class="bg-transparent" style="width: 75%; height: 75%;">
							<Gravatar :email="userId" class="bg-transparent" :size="250" />
						</q-avatar>

						<div class="my-2 d-flex justify-content-center">
							<h5 class="mb-0">{{ userName }}</h5>
							<div>
								<q-icon 
										class="ml-2 mb-1 icon-btn" 
										:name="mdiPencil" 
										@click="gotoEditpage"
								/>
								<q-tooltip>Edit Profile</q-tooltip>
							</div>
						</div>

						<div>
								<!-- <p>Username: {{ userName }}</p> -->
								<p>Birthday: {{ this.birth }}</p>
						</div>
					</div>
				</div>

				<div class="p-2 rounded border" style="height: 65%; background: #e9fbfa;" :class="{ 'mypage-dark' : $q.dark.isActive == true }">
					<h5 class="my-3" align="center">Today's Schedule</h5>

					<q-scroll-area v-if="dayEvents.length !=0" style="height: 85%;">
					<div v-for="(event, idx) in dayEvents" :key="idx" class="my-2">
					
						<q-card v-if="event.allDay" class="p-2 d-flex rounded border">
							<q-icon :name="mdiCalendarToday" size="md" class="my-2" style="text-align: center;" :style="{ color : event.color }" />
							<div class="mx-2">
								<p class="todayEvent-title-desktop">{{ event.title }}</p>
								<p class="todayEvent-title-desktop">{{ event.description }}</p>
							</div>
						</q-card>
						<q-card v-else class="p-2 d-flex rounded border">
							<q-icon :name="mdiClockOutline" size="md" class="my-2" style="text-align: center;" :style="{ color : event.color }" />
							<div class="mx-2">
								<div class="d-flex">
									<h5 class="mr-1 mb-0 pl-2" style="font-weight: 600;">{{ $moment(event.start).format("HH:mm") }}</h5>
									<p> - {{ $moment(event.end).format("HH:mm") }}</p>
								</div>
								<p class="todayEvent-title-desktop">{{ event.title }}</p>

							</div>
						</q-card>
						
					</div>
					</q-scroll-area>
					<div v-else align="center">
						<h5 style="margin-top: 50px; color: #B0B0B0 !important;">NO DATA AVAILABLE</h5>
					</div>
				</div>
			</div>

			<div class="p-1" style="width: 75%;">
				<div class="row m-0 mb-2" style="height: 30%; text-align: center;">
					<div class="col-3 pt-2 rounded" style="background: #e9fbfa; border: 1px solid lightgray;" :class="{ 'mypage-dark' : $q.dark.isActive == true }">
						<q-icon :name="mdiCalendar" size="md" class="my-2" />
						<h1 class="absolute-center" style="font-size: 4rem;">
							{{ totalEventNum }}
						</h1>
						<h5 class="absolute-bottom mb-4">Total Events</h5>
					</div>
					<div class="col-3 p-2 rounded" style="background: #e6f8fe; border: 1px solid lightgray;" :class="{ 'mypage-dark' : $q.dark.isActive == true }">
						<q-icon :name="fabTrello" size="md" class="my-2" />
						<h1 class="absolute-center" style="font-size: 4rem;">{{ doneEventNum }}</h1>
						<h5 class="absolute-bottom mb-4">Kanban Done</h5>
					</div>
					<div class="col-3 p-2 rounded" style="background: #e6f1ff; border: 1px solid lightgray;" :class="{ 'mypage-dark' : $q.dark.isActive == true }">
						<q-icon :name="mdiShareVariant" size="md" class="my-2" />
						<h1 class="absolute-center" style="font-size: 4rem;">{{ groupEventNum }}</h1>
						<h5 class="absolute-bottom mb-4">Group Schedule</h5>
					</div>
					<div class="col-3 p-2 rounded" style="background: #e8e6ff; border: 1px solid lightgray;" :class="{ 'mypage-dark' : $q.dark.isActive == true }">
						<q-icon :name="mdiSeal" size="md" class="my-2" />
						<div class="absolute-center">
							<q-spinner-hourglass
								v-if="this.freqResult==null"
				size="3em"
				/>
							<h5 v-else v-for="(freqWord, idx) in freqResult" :key="idx" class="mb-0">{{ freqWord.word }}</h5>
						</div>
						<h5 class="absolute-bottom mb-4">Keywords</h5>
					</div>

				</div>

				<div class="rounded border m-0" style="height: 65%;">
					
					<q-splitter
					v-model="splitterModel"
					class="mx-0"
					style="height: 100%;"
				>
				
					<template v-slot:before>
						<q-tabs
							v-model="tab"
							vertical
							class="text-grey"
						>
							<q-tab name="mails" style="height: 50%;">
								<div style="margin-top: 170px; position: relative; margin-right: 0px; bottom: 20px;">
									<apexchart v-if="totalEventNum != 0" type="treemap" :options="chartOptions" :series="series"></apexchart>
									<h5 :class="{ 'chart-darkmode' : $q.dark.isActive == true }" style="color: black;">Color Chart Events</h5>
									<div v-if="totalEventNum == 0" align="center">
										<h3 style="color: #B0B0B0 !important">NO DATA AVAILABLE</h3>
									</div>
								</div>
							</q-tab>
							<q-tab name="alarms" style="height: 50%;">
								<div style="margin-top: 140px;">
									<div id="chart" style="margin-left: 10px;">
										<apexchart type="radialBar" :options="chartOptions2" :series="series2"></apexchart>
										<h5 :class="{ 'chart-darkmode' : $q.dark.isActive == true }" style="color: black;">Kanban Task Achievement Rate</h5>
									</div>
								</div>
							</q-tab>
						</q-tabs>
					</template>

					<template v-slot:after class="px-0" :class="{ 'scroll-dark' : $q.dark.isActive == true }">
						<q-scroll-area style="height: 100%;">
							<q-tab-panels
								v-model="tab"
								animated
								swipeable
								vertical
								transition-prev="jump-up"
								transition-next="jump-up"
							>
							
								<q-tab-panel name="mails" style="">
									<q-timeline color="secondary" layout="loose">
											
											<q-timeline-entry heading>
												<div class="d-flex justify-content-center">
													<p :style="{ color: colorEventTitle }" style="filter: brightness(1.5); font-weight: 600;">{{ colorEventTitle }}</p><p class="ml-2"> Color Event</p>
												</div>
											</q-timeline-entry>
											
											<div v-if="totalEventNum != 0">
												<q-timeline-entry
													v-for="(event, idx) in colChartEvent" :key="idx"
													:title="event.title"
													:subtitle="$moment(event.start).format('YYYY-MM-DD')"
													:color="colEvent[idx]"
													:side="colPosition[idx]"
												>
													<p>
														{{ event.description }}
													</p>
												</q-timeline-entry>
											</div>

											<div v-else align="center" style="height: 50%;">
												<h3 style="color: #B0B0B0 !important;">NO DATA AVAILABLE</h3>
											</div>

												
									</q-timeline>

									
								</q-tab-panel>

								<q-tab-panel name="alarms" style="height: 100%;">
									<q-timeline color="secondary" layout="loose">
											

											<q-timeline-entry heading>
												Now in progress
											</q-timeline-entry>
											
											<div v-if="progressEvent.length != 0">
												<q-timeline-entry
													v-for="(event, idx) in progressEvent" :key="idx"
													:title="event.title"
													:subtitle="!event.allDay ? event.start.substring(0, 10) : $moment(event.start).format('YYYY-MM-DD')"
													:color="colProEvent[idx]"
													:side="colProPosition[idx]"
												>
													<p>
														{{ event.description }}
													</p>
												</q-timeline-entry>
											</div>
											<div v-else align="center" style="height: 50%;">
												<h3 style="color: #B0B0B0;">NO DATA AVAILABLE</h3>
											</div>
									</q-timeline>

								</q-tab-panel>
								
							</q-tab-panels>
						</q-scroll-area>
					</template>

				
				</q-splitter>
				
				</div>
			</div>
		</div>

		<div v-else>
			<q-card>
        <q-tabs
          v-model="tab2"
          dense
          class="text-grey"
          active-color="primary"
          indicator-color="primary"
          align="justify"
          narrow-indicator
        >
          <q-tab name="mypage" label="MYPAGE" />
					<q-tab name="today" label="TODAY" />
          <q-tab name="color" label="COLOR" />
          <q-tab name="kanban" label="KANBAN" />
        </q-tabs>

        <q-separator />

        <q-tab-panels v-model="tab2" animated>
          <q-tab-panel name="mypage">
            <div align="center">
							<q-avatar size="100px" class="bg-transparent">
								<Gravatar :email="userId" class="bg-transparent" :size="250" />
							</q-avatar>

							<div class="my-2 d-flex justify-content-center">
								<h5 class="mb-0">{{ userName }}</h5>
								<div>
									<q-icon 
											class="ml-2 mb-1 icon-btn" 
											:name="mdiPencil" 
											@click="gotoEditpage"
									/>
									<q-tooltip>Edit Profile</q-tooltip>
								</div>
							</div>

							<div>
									<!-- <p>Username: {{ userName }}</p> -->
									<p>Birthday: {{ this.birth }}</p>
							</div>
						</div>

						<hr>

						<div>
							<div class="row m-0 mb-2" style="text-align: center;">
								<div class="col-6 pt-2 rounded" style="background: #e9fbfa; border: 1px solid lightgray; min-height: 160px;" :class="{ 'mypage-dark' : $q.dark.isActive == true }">
									<q-icon :name="mdiCalendar" size="md" class="my-2" />
									<h3 class="my-2">
										{{ totalEventNum }}
									</h3>
									<h5 class="absolute-bottom my-2">Total Events</h5>
								</div>
								<div class="col-6 p-2 rounded" style="background: #e6f8fe; border: 1px solid lightgray; min-height: 160px;" :class="{ 'mypage-dark' : $q.dark.isActive == true }">
									<q-icon :name="fabTrello" size="md" class="my-2" />
									<h3 class="my-2">{{ doneEventNum }}</h3>
									<h5 class="absolute-bottom my-2">Kanban Done</h5>
								</div>
								<div class="col-6 p-2 rounded" style="background: #e6f1ff; border: 1px solid lightgray; min-height: 160px;" :class="{ 'mypage-dark' : $q.dark.isActive == true }">
									<q-icon :name="mdiShareVariant" size="md" class="my-2" />
									<h3 class="my-2">{{ groupEventNum }}</h3>
									<h5 class="absolute-bottom my-2">Group Schedule</h5>
								</div>
								<div class="col-6 p-2 rounded" style="background: #e8e6ff; border: 1px solid lightgray; min-height: 160px;" :class="{ 'mypage-dark' : $q.dark.isActive == true }">
									<q-icon :name="mdiSeal" size="md" class="my-2" />
									<div class="my-2">
										<q-spinner-hourglass
											v-if="this.freqResult==null"
											size="2em"
										/>
										<p v-else v-for="(freqWord, idx) in freqResult" :key="idx" class="mb-0">{{ freqWord.word }}</p>
									</div>
									<h5 class="absolute-bottom my-2">Keywords</h5>
								</div>

							</div>
						</div>
          </q-tab-panel>

          <q-tab-panel name="today" style="height: 90vh; word-break:break-all;">
            <div class="p-2 mb-3" :class="{ 'mypage-dark' : $q.dark.isActive == true }">
							<h3 class="my-3" align="center">Today's Schedule</h3>

							<!-- <q-scroll-area style="height: 85%;"> -->
								<div v-if="dayEvents.length !=0">
									<div v-for="(event, idx) in dayEvents" :key="idx" class="my-2">
							
										<q-card v-if="event.allDay" class="p-2 d-flex rounded border">
											<q-icon :name="mdiCalendarToday" size="md" class="my-2" style="text-align: center;" :style="{ color : event.color }" />
											<div class="mx-2">
												<p class="todayEvent-title-mobile">{{ event.title }}</p>
												<p class="todayEvent-title-mobile">{{ event.description }}</p>
											</div>
										</q-card>
										<q-card v-else class="p-2 d-flex rounded border">
											<q-icon :name="mdiClockOutline" size="md" class="my-2" style="text-align: center;" :style="{ color : event.color }" />
											<div class="mx-2">
												<div class="d-flex">
													<h5 class="mr-1 mb-0 pl-2" style="font-weight: 600;">{{ $moment(event.start).format("HH:mm") }}</h5>
													<p> - {{ $moment(event.end).format("HH:mm") }}</p>
												</div>
												<p class="todayEvent-title-mobile">{{ event.title }}</p>

											</div>
										</q-card>
								
									</div>
								</div>
								<div v-else align="center">
									<h5 style="margin-top: 50px; color: #B0B0B0 !important;">NO DATA AVAILABLE</h5>
								</div>
											
							<!-- </q-scroll-area> -->
						</div>
				</q-tab-panel>

				<q-tab-panel name="color" style="height: 90vh;">
					<div style="">
							<apexchart v-if="totalEventNum != 0" type="treemap" :options="chartOptions" :series="series"></apexchart>
							<!-- <h5 :class="{ 'chart-darkmode' : $q.dark.isActive == true }" class="mb-4" style="color: black; text-align: center;">Color Chart Events</h5> -->
						</div>
						<div v-if="totalEventNum == 0" align="center">
							<div class="d-flex justify-content-center my-4">
								<h3 :style="{ color: colorEventTitle }" style="filter: brightness(1.5); font-weight: 600;">{{ colorEventTitle }}</h3><h3 class="ml-2"> Color Event</h3>
							</div>
							<h5 style="color: #B0B0B0 !important; margin-top: 45px; ">NO DATA AVAILABLE</h5>
						</div>

						<q-timeline v-else color="secondary" layout="loose">
										
							<q-timeline-entry heading>
								<div class="d-flex justify-content-center">
									<p :style="{ color: colorEventTitle }" style="filter: brightness(1.5); font-weight: 600;">{{ colorEventTitle }}</p><p class="ml-2"> Color Event</p>
								</div>
							</q-timeline-entry>

								<q-timeline-entry
									v-for="(event, idx) in colChartEvent" :key="idx"
									:title="event.title"
									:subtitle="$moment(event.start).format('YYYY-MM-DD')"
									:color="colEvent[idx]"
									:side="colPosition[idx]"
								>
									<p>
										{{ event.description }}
									</p>
								</q-timeline-entry>
							
					</q-timeline>
          </q-tab-panel>

					<q-tab-panel name="kanban" style="height: 90vh;">
            			<div style="">
							<div id="chart" class="my-4" style="margin-left: 10px;">
								<apexchart type="radialBar" :options="chartOptions2" :series="series2"></apexchart>
								<h5 :class="{ 'chart-darkmode' : $q.dark.isActive == true }" style="color: black; text-align: center;">Kanban Task Achievement Rate</h5>
							</div>
						</div>

						<q-timeline color="secondary" layout="loose">
							<q-timeline-entry heading>
								Now in progress
							</q-timeline-entry>
						
							<div v-if="progressEvent.length != 0">
								<q-timeline-entry
									v-for="(event, idx) in progressEvent" :key="idx"
									:title="event.title"
									:subtitle="!event.allDay ? event.start.substring(0, 10) : $moment(event.start).format('YYYY-MM-DD')"
									:color="colProEvent[idx]"
									:side="colProPosition[idx]"
								>
									<p>
										{{ event.description }}
									</p>
								</q-timeline-entry>
							</div>

							<div v-else align="center" style="height: 50%;">
								<h5 style="color: #B0B0B0;">NO DATA AVAILABLE</h5>
							</div>
								
							
						</q-timeline>
          </q-tab-panel>
        </q-tab-panels>
      </q-card>
		</div>
  </div>
</template>

<script>
import { mdiPencil, mdiCake, mdiCalendar, mdiShareVariant, mdiArrowLeft, mdiSeal, mdiCalendarToday, mdiClockOutline } from '@quasar/extras/mdi-v5'
import { fabTrello } from '@quasar/extras/fontawesome-v5'
import EditMypageModal from '../components/Accounts/EditMypageModal.vue'
import { mapGetters, mapActions } from 'vuex'
import axios from 'axios'
import Gravatar from 'vue-gravatar'
import Toolbar from './Layout/Toolbar.vue'

let https = require('https')
const instance = axios.create({
  baseURL: 'https://bcplus.me/api',
  timeout: 120000,
  httpsAgent: new https.Agent({
    rejectUnauthorized: false
  })
})

export default {
	components: {       
		Gravatar,
		EditMypageModal,
		Toolbar
	},
	data() {
		return {
			dayEvents: [],
			graphArr: [1, 1, 1, 1, 1, 1, 1],
			doneEventNum: 0,
			kanbanProgress: 1,
			groupEventNum: 0,
			totalEventNum: 0,
			redEventNum: 0,
			blueEventNum: 0,
			orangeEventNum: 0,
			greenEventNum: 0,
			indigoEventNum: 0,
			dpurpleEventNum: 0,
			cyanEventNum: 0,
			birth: null,
			colorEventTitle: "All",
			colEvent: [],
			colEventTime: [],
			colPosition: ['left'],
			progressEvent: [],
			colProEvent: [],
			colProPosition: ['left'],
			colTitleColor: null,
			freqText: '',
			freqResult: null,

			tab: 'mails',
			tab2: 'mypage',
      splitterModel: 50,

			series: [
				{
					data: [
						{
							x: '',
							y: 0
						},
						{
							x: '',
							y: 0
						},
						{
							x: '',
							y: 0
						},
						{
							x: '',
							y: 0
						},
						{
							x: '',
							y: 0
						},
						{
							x: '',
							y: 0
						},
						{
							x: 'Cyan',
							y: 0
						},
					]
				}
			],
				chartOptions: {
					legend: {
						show: false
					},
					chart: {
						height: 200,
						type: 'treemap',
						toolbar: {
							show: false
						},
						events: {
							dataPointSelection: (event, chartContext, config) => {
								this.goChart(config)
								// console.log(event, chartContext, config);
							}
						},
					},
					colors: [
						'#F44336',
						'#2196F3',
						'#FF9800',
						'#4CAF50',
						'#3F51B5',
						'#673AB7',
						'#00BCD4',

					],
					plotOptions: {
						treemap: {
							distributed: true,
							enableShades: false
						}
					},
					fill: {
						opacity: 0.8,
					},
					states: {
						hover: {
								filter: {
										type: 'none',
								}
						},
						active: {
							allowMultipleDataPointsSelection: false,
							filter: {
									type: 'none',
							}
    				},
				}
					
				},
			series2: [0],
			chartOptions2: {
				chart: {
					type: 'radialBar',
					offsetY: -20,
					sparkline: {
						enabled: true
					}
				},
				plotOptions: {
					radialBar: {
						startAngle: -90,
						endAngle: 90,
						track: {
							background: "#e7e7e7",
							strokeWidth: '97%',
							margin: 5, // margin is in pixels
							dropShadow: {
								enabled: true,
								top: 2,
								left: 0,
								color: '#999',
								opacity: 1,
								blur: 2
							}
						},
						dataLabels: {
							name: {
								show: false
							},
							value: {
								color: "#503fff",
								offsetY: -2,
								fontSize: '22px'
							}
						}
					}
				},
				grid: {
					padding: {
						top: -10
					}
				},
				fill: {
					type: "gradient",
    gradient: {
      shadeIntensity: 1,
      opacityFrom: 0.7,
      opacityTo: 0.9,
      colorStops: [
        {
          offset: 10,
          color: "#94f6d2",
          opacity: 1
        },
        {
          offset: 40,
          color: "#2ec4f9",
          opacity: 1
        },
        {
          offset: 70,
          color: "#338aff",
          opacity: 1
        },
        {
          offset: 100,
          color: "#503fff",
          opacity: 1
        }
			]
		}
				},
				labels: ['Average Results'],
			},

		}
	},
	computed: {
		...mapGetters('calendar', ["EVENTS", "TASKS"]),
	},
	methods: {
		...mapActions('calendar', ["updateMySchedule"]),		
		...mapActions('accounts', ['Logout']),
		
		goChart(config) {
			this.colChartEvent = []
			this.colEvent = []
			this.colPosition = ['left']

			if (config.dataPointIndex == 0) {
				this.colorEventTitle = "Red"
				for (var i=0; i < this.EVENTS.length; i++ ) {
					if ( this.EVENTS[i].color == "#F44336") {
						this.colChartEvent.push(this.EVENTS[i])
						this.colEvent.push("red")
					} 
				}
			} else if (config.dataPointIndex == 1) {
				this.colorEventTitle = "Blue"
				for (var i=0; i < this.EVENTS.length; i++ ) {
					if ( this.EVENTS[i].color == "#2196F3") {
						this.colChartEvent.push(this.EVENTS[i])
						this.colEvent.push("blue")
					} 
				}
			} else if (config.dataPointIndex == 2) {
				this.colorEventTitle = "Orange"
				for (var i=0; i < this.EVENTS.length; i++ ) {
					if ( this.EVENTS[i].color == "#FF9800") {
						this.colChartEvent.push(this.EVENTS[i])
						this.colEvent.push("orange")
					} 
				}
			} else if (config.dataPointIndex == 3) {
				this.colorEventTitle = "Green"
				for (var i=0; i < this.EVENTS.length; i++ ) {
					if ( this.EVENTS[i].color == "#4CAF50") {
						this.colChartEvent.push(this.EVENTS[i])
						this.colEvent.push("green")
					} 
				}
			} else if (config.dataPointIndex == 4) {
				this.colorEventTitle = "Indigo"
				for (var i=0; i < this.EVENTS.length; i++ ) {
					if ( this.EVENTS[i].color == "#3F51B5") {
						this.colChartEvent.push(this.EVENTS[i])
						this.colEvent.push("indigo")
					} 
				}
			} else if (config.dataPointIndex == 5) {
				this.colorEventTitle = "Purple"
				for (var i=0; i < this.EVENTS.length; i++ ) {
					if ( this.EVENTS[i].color == "#673AB7") {
						this.colChartEvent.push(this.EVENTS[i])
						this.colEvent.push("purple")
					} 
				}
			} else {
				this.colorEventTitle = "Cyan"
				for (var i=0; i < this.EVENTS.length; i++ ) {
					if ( this.EVENTS[i].color == "#00BCD4") {
						this.colChartEvent.push(this.EVENTS[i])
						this.colEvent.push("cyan")
					} 
				}
			}

			for (var i=0; i < this.colChartEvent.length; i++) {
				if (i+1 < this.colChartEvent.length) {
						// console.log('z',this.EVENTS[i].start)
						if (this.$moment(this.colChartEvent[i].start).format("YYYY-MM-DD") == this.$moment(this.colChartEvent[i+1].start).format("YYYY-MM-DD")) {
							this.colPosition.push(this.colPosition[i])
						} else {
							if (this.colPosition[i] == 'left') {
								this.colPosition.push('right')
							} else {
								this.colPosition.push('left')
							}
						}
					}
			}
		},
		gotoEditpage() {
			this.$modal.show(EditMypageModal, {
				userId: this.userId,
				user_name: this.user_name,
				user_bday: this.birth,
				modal: this.$modal},{
						height: 'auto'
			})
		},
		gotoLogout() {
      this.Logout({
        token: sessionStorage.getItem("access-token"),
        userId: sessionStorage.getItem("username")
      })
      .then(() => {
        this.$q.dark.set(false)
        this.$router.push("/")
      })
    },
	},
	mounted() {
		// this.playAnimation()
		
		this.updateMySchedule({people: sessionStorage.getItem("username"), flag:true})
	},
	created() {
		this.mdiPencil = mdiPencil
		this.mdiCake = mdiCake
		this.mdiCalendar = mdiCalendar
		this.mdiShareVariant = mdiShareVariant
		this.mdiArrowLeft = mdiArrowLeft
		this.fabTrello = fabTrello
		this.mdiSeal = mdiSeal
		this.mdiCalendarToday = mdiCalendarToday
		this.mdiClockOutline = mdiClockOutline
		
		this.userId=sessionStorage.getItem("username")
		this.userName=sessionStorage.getItem("name")
		this.token=sessionStorage.getItem("access-token")
    instance.get('/member/'+ sessionStorage.getItem("username"))
		.then(res =>{
			sessionStorage.setItem("birth", res.data.birthDay)
			this.group_list = res.data.groups
			this.user_name = res.data.name
			this.birth = this.$moment(res.data.birthDay).format('YYYY-MM-DD');
		})
	},
	watch: {
		EVENTS() {
			// console.log(this.$q.dark.isActive, 'dark')
			// const today = this.$moment(new Date()).format("YYYY-MM-DD HH:mm:ss")
			let filterdEvents=this.EVENTS.filter(el=>el.color!='transparent')
			
			this.totalEventNum = filterdEvents.length

			//holyday 제외
			
			

			this.colChartEvent = filterdEvents


			if (this.TASKS.length != 0) {

				for (var j=0; j < this.TASKS.length; j++ ) {

					this.freqText += this.TASKS[j].title + ', '

					if (this.TASKS[j].status == 'done') {
						this.doneEventNum += 1
					} else if (this.TASKS[j].status == 'inprogress') {
						this.progressEvent.push(this.TASKS[j])
					}
				}

				// console.log(this.freqText)
				axios.post('https://bcplus.me/analysis/show_freq', {
					text: this.freqText,
				})
					.then(res => {
						// console.log('data', res.data)
						this.freqResult = res.data.freq_lst
						if (this.freqResult.length > 3) {
							this.freqResult = this.freqResult.slice(0, 3)
						} else if ( res.data.freq_lst.length == 0 ) {
							this.freqResult = [{ word : 'NO DATA AVAILABLE'}]
						}
						// console.log(this.freqResult)
					}) .catch(res => {
						this.freqResult = [{ word : 'NO DATA AVAILABLE'}]
						// console.log(err)
					})
			} else {
				this.freqResult = [{ word : 'NO DATA AVAILABLE'}]
			}
			

			for (var i=0; i < this.progressEvent.length; i++ ) {

				if ( this.progressEvent[i].color == "#F44336") {
					this.colProEvent.push("red")
					// colPosition

					if (i+1 < this.progressEvent.length) {
						// console.log('z',this.EVENTS[i].start)
						if (this.$moment(this.progressEvent[i].start).format("YYYY-MM-DD") == this.$moment(this.progressEvent[i+1].start).format("YYYY-MM-DD")) {
							this.colProPosition.push(this.colProPosition[i])
						} else {
							if (this.colProPosition[i] == 'left') {
								this.colProPosition.push('right')
							} else {
								this.colProPosition.push('left')
							}
						}
					}
				} else if ( this.progressEvent[i].color == "#2196F3" ) {
					this.colProEvent.push("blue")
					if (i+1 < this.progressEvent.length) {
						// console.log('z',this.EVENTS[i].start)
						if (this.$moment(this.progressEvent[i].start).format("YYYY-MM-DD") == this.$moment(this.progressEvent[i+1].start).format("YYYY-MM-DD")) {
							this.colProPosition.push(this.colProPosition[i])
						} else {
							if (this.colProPosition[i] == 'left') {
								this.colProPosition.push('right')
							} else {
								this.colProPosition.push('left')
							}
						}
					}
				} else if ( this.progressEvent[i].color == "#FF9800" ) {
					this.colProEvent.push("orange")
					if (i+1 < this.progressEvent.length) {
						// console.log('z',this.EVENTS[i].start)
						if (this.$moment(this.progressEvent[i].start).format("YYYY-MM-DD") == this.$moment(this.progressEvent[i+1].start).format("YYYY-MM-DD")) {
							this.colProPosition.push(this.colProPosition[i])
						} else {
							if (this.colProPosition[i] == 'left') {
								this.colProPosition.push('right')
							} else {
								this.colProPosition.push('left')
							}
						}
					}
				} else if ( this.progressEvent[i].color == "#4CAF50" ) {
					this.colProEvent.push("green")
					if (i+1 < this.progressEvent.length) {
						// console.log('z',this.EVENTS[i].start)
						if (this.$moment(this.progressEvent[i].start).format("YYYY-MM-DD") == this.$moment(this.progressEvent[i+1].start).format("YYYY-MM-DD")) {
							this.colProPosition.push(this.colProPosition[i])
						} else {
							if (this.colProPosition[i] == 'left') {
								this.colProPosition.push('right')
							} else {
								this.colProPosition.push('left')
							}
						}
					}
				} else if ( this.progressEvent[i].color == "#3F51B5" ) {
					this.colProEvent.push("indigo")
					if (i+1 < this.progressEvent.length) {
						// console.log('z',this.EVENTS[i].start)
						if (this.$moment(this.progressEvent[i].start).format("YYYY-MM-DD") == this.$moment(this.progressEvent[i+1].start).format("YYYY-MM-DD")) {
							this.colProPosition.push(this.colProPosition[i])
						} else {
							if (this.colProPosition[i] == 'left') {
								this.colProPosition.push('right')
							} else {
								this.colProPosition.push('left')
							}
						}
					}
				} else if ( this.progressEvent[i].color == "#673AB7" ) {
					this.colProEvent.push("purple")
					if (i+1 < this.progressEvent.length) {
						// console.log('z',this.EVENTS[i].start)
						if (this.$moment(this.progressEvent[i].start).format("YYYY-MM-DD") == this.$moment(this.progressEvent[i+1].start).format("YYYY-MM-DD")) {
							this.colProPosition.push(this.colProPosition[i])
						} else {
							if (this.colProPosition[i] == 'left') {
								this.colProPosition.push('right')
							} else {
								this.colProPosition.push('left')
							}
						}
					}
				} else {
					this.colProEvent.push("cyan")
					if (i+1 < this.progressEvent.length) {
						// console.log('z',this.EVENTS[i].start)
						if (this.$moment(this.progressEvent[i].start).format("YYYY-MM-DD") == this.$moment(this.progressEvent[i+1].start).format("YYYY-MM-DD")) {
							this.colProPosition.push(this.colProPosition[i])
						} else {
							if (this.colProPosition[i] == 'left') {
								this.colProPosition.push('right')
							} else {
								this.colProPosition.push('left')
							}
						}
					}
				}
			}

			if (this.TASKS.length > 0) {
				this.kanbanProgress = (this.doneEventNum / this.TASKS.length).toFixed(4)
				// console.log('pro', this.kanbanProgress, this.doneEventNum / this.TASKS.length, this.doneEventNum , this.TASKS.length)
				this.series2 = [Math.round( (this.doneEventNum / this.TASKS.length).toFixed(4) * 100)]
			} else {
				this.series2 = [0]
			}

			

			for (var i=0; i < filterdEvents.length; i++ ) {

				if ( filterdEvents[i].editable == false ) {
					this.groupEventNum += 1
				}

				if ( filterdEvents[i].color == "#F44336") {
					this.colEvent.push("red")
					this.redEventNum += 1
					// colPosition

					if (i+1 < filterdEvents.length) {
						// console.log('z',this.EVENTS[i].start)
						if (this.$moment(filterdEvents[i].start).format("YYYY-MM-DD") == this.$moment(filterdEvents[i+1].start).format("YYYY-MM-DD")) {
							this.colPosition.push(this.colPosition[i])
						} else {
							if (this.colPosition[i] == 'left') {
								this.colPosition.push('right')
							} else {
								this.colPosition.push('left')
							}
						}
					}
				} else if ( filterdEvents[i].color == "#2196F3" ) {
					this.colEvent.push("blue")
					this.blueEventNum += 1
					if (i+1 < filterdEvents.length) {
						if (this.$moment(filterdEvents[i].start).format("YYYY-MM-DD") == this.$moment(filterdEvents[i+1].start).format("YYYY-MM-DD")) {
							this.colPosition.push(this.colPosition[i])
						} else {
							if (this.colPosition[i] == 'left') {
								this.colPosition.push('right')
							} else {
								this.colPosition.push('left')
							}
						}
					}
				} else if ( filterdEvents[i].color == "#FF9800" ) {
					this.colEvent.push("orange")
					this.orangeEventNum += 1
					if (i+1 < filterdEvents.length) {
						if (this.$moment(filterdEvents[i].start).format("YYYY-MM-DD") == this.$moment(filterdEvents[i+1].start).format("YYYY-MM-DD")) {
							this.colPosition.push(this.colPosition[i])
						} else {
							if (this.colPosition[i] == 'left') {
								this.colPosition.push('right')
							} else {
								this.colPosition.push('left')
							}
						}
					}
				} else if ( filterdEvents[i].color == "#4CAF50" ) {
					this.colEvent.push("green")
					this.greenEventNum += 1
					if (i+1 < filterdEvents.length) {
						if (this.$moment(filterdEvents[i].start).format("YYYY-MM-DD") == this.$moment(filterdEvents[i+1].start).format("YYYY-MM-DD")) {
							this.colPosition.push(this.colPosition[i])
						} else {
							if (this.colPosition[i] == 'left') {
								this.colPosition.push('right')
							} else {
								this.colPosition.push('left')
							}
						}
					}
				} else if ( filterdEvents[i].color == "#3F51B5" ) {
					this.colEvent.push("indigo")
					this.indigoEventNum += 1
					if (i+1 < filterdEvents.length) {
						if (this.$moment(filterdEvents[i].start).format("YYYY-MM-DD") == this.$moment(filterdEvents[i+1].start).format("YYYY-MM-DD")) {
							this.colPosition.push(this.colPosition[i])
						} else {
							if (this.colPosition[i] == 'left') {
								this.colPosition.push('right')
							} else {
								this.colPosition.push('left')
							}
						}
					}
				} else if ( filterdEvents[i].color == "#673AB7" ) {
					this.colEvent.push("purple")
					this.dpurpleEventNum += 1
					if (i+1 < filterdEvents.length) {
						if (this.$moment(filterdEvents[i].start).format("YYYY-MM-DD") == this.$moment(filterdEvents[i+1].start).format("YYYY-MM-DD")) {
							this.colPosition.push(this.colPosition[i])
						} else {
							if (this.colPosition[i] == 'left') {
								this.colPosition.push('right')
							} else {
								this.colPosition.push('left')
							}
						}
					}
				} else {
					this.colEvent.push("cyan")
					this.cyanEventNum += 1
					if (i+1 <filterdEvents.length) {
						if (this.$moment(filterdEvents[i].start).format("YYYY-MM-DD") == this.$moment(filterdEvents[i+1].start).format("YYYY-MM-DD")) {
							this.colPosition.push(this.colPosition[i])
						} else {
							if (this.colPosition[i] == 'left') {
								this.colPosition.push('right')
							} else {
								this.colPosition.push('left')
							}
						}
					}
				}
			}

			this.graphArr = [
				{ data: 
				[{x: "", y: this.redEventNum}, {x: '', y: this.blueEventNum}, {x: '', y: this.orangeEventNum}, {x: '', y: this.greenEventNum}, {x: '', y: this.indigoEventNum}, {x: '', y: this.dpurpleEventNum}, {x: '', y: this.cyanEventNum}]
				}]
			this.series = this.graphArr

			var today = this.$moment(new Date()).format("YYYY-MM-DD")
			for (var k=0; k <filterdEvents.length; k++) {
				// console.log(today)
				if (today <= this.$moment(filterdEvents[k].rend).format("YYYY-MM-DD") && today >= this.$moment(filterdEvents[k].start).format("YYYY-MM-DD")) {
					// console.log("!", this.$moment(filterdEvents[k].rend).format("YYYY-MM-DD"), filterdEvents[k])
					this.dayEvents.push(filterdEvents[k])
				}
			}
		}
	}
}
</script>

<style scoped>
p {
	margin-bottom: 0px;
}
.scaleBig {
  font-size: 45px;
}
.bold {
  font-weight: bold;
  font-size: 35px;
	color: white;
}
.transition {
  transition: all 0.3s ease-in;
}

p {
	margin-bottom: 0px;
}

.header-color {
  background: linear-gradient( to right, #4032CC,  #85DDBD );
}

.q-tabs--vertical .q-tab {
	padding: 0px !important;
	overflow: hidden;
}

.q-tabs {
	margin: 0px !important;
}

.chart-darkmode {
	color: white !important;
}

.mypage-dark {
	background: none !important;
}

.header-color {
  background: linear-gradient( to right, #4032CC,  #85DDBD );
}

.todayEvent-title-mobile {
	width: 250px;
	padding-left: 10px;
	word-break:break-all;
}

.todayEvent-title-desktop {
	width: 180px;
	padding-left: 10px;
	word-break:break-all;
}
</style>

<style lang="scss">
.q-timeline__dot {
	.q-icon {
		margin: 0px;
	}
}

.q-timeline__title {
	font-size: 1.2rem;
	font-weight: 600;
	margin-bottom: 5px;
}

.q-timeline__content {
	// border: 1px solid gray;
	padding-bottom: 0px;
	margin: 7px;
	margin-left: 0px;
	padding: 5px;
	border-radius: 0.4rem;
}

.q-timeline__dot {
	opacity: 0.8 !important;
}

div.q-splitter__panel.q-splitter__after.col {
	padding-right: 0px;
	padding-left: 0px;
	padding-top: 5px;
	padding-bottom: 5px;
}

div.q-splitter__panel.q-splitter__after.col {
	.q-scrollarea.q-scrollarea--dark {
		background: #192734;
	}
}

.q-timeline__content {
	word-break:break-all;
}
</style>