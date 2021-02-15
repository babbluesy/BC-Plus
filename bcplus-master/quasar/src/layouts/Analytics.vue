<template>
	<div id="SA">
		<div class="container my-4">
			<div class="container">
				<q-avatar size="100px" class="bg-transparent d-flex justify-content-center" style="margin: 0 auto;">
					<Gravatar email="mem" class="bg-transparent" :size="250" />
				</q-avatar>

				<div class="my-3 d-flex justify-content-center">
					<h5 class="mb-0">USERNAME</h5>
					<div>
						<q-icon 
							class="ml-2 mb-1 icon-btn" 
							:name="mdiPencil" 
							@click="gotoEditpage"
						/>
						<q-tooltip>Edit Profile</q-tooltip>
					</div>
				</div>

				<div style="text-align: center;">
					<p>Username: username</p>
					<p>Birthday: 2020-11-11</p>
				</div>
			</div>

			<hr class="my-4" style="width: 50vw;">

			<div class="row d-flex justify-content-center" align="center">
				<div class="col-6 p-0" style="max-width: 400px; height:">
					<div class="m-1 rounded p-2" style="background: #94f6d2;">
						<h3>Total Event</h3>
						<div>
							<number
								class="bold transition"
								:class="{scaleBig: scaleClass}"
								ref="number2"
								:from="numberFrom"
								:format="theFormat"
								animationPaused
								:to="numberTo"
								:duration="duration"
								easing="Power4.easeOut"
								@complete="completed"/>
						</div>
					</div>
				</div>

				<div class="col-6 p-0" style="max-width: 400px;">
					<!-- col-xl-6 col-lg-6 col-md-6 col-sm-6 col-xs-6 -->
					<div class="m-1 rounded p-2" style="background: #2ec4f9;">
						<div>
							<q-icon :name="mdiCake" size="md" />
						</div>
						<div style="font-size: 50px; color: white;">
							7
						</div>
						<h5>10월 경조사 일정</h5>
					</div>
				</div>

				<div class="col-6 p-0" style="max-width: 400px;">
					<div class="m-1 rounded p-2" style="background: #338aff;">
						<div>
							<q-icon :name="mdiShareVariant" size="md" />
						</div>
						<div style="font-size: 50px; color: white;">
							23
						</div>
						<h5>10월 공유된 일정</h5>
						</div>
				</div>

				<div class="col-6 p-0" style="max-width: 400px;">
					<div class="m-1 rounded p-2" style="background: #503fff;">
						<div>
							<q-icon :name="mdiShareVariant" size="md" />
						</div>
						<div style="font-size: 50px; color: white;">
							23
						</div>
						<h5>10월 공유된 일정</h5>
						</div>
				</div>
			</div>
			
			<br>

			<hr class="my-4" style="width: 50vw;">

			<q-splitter
				v-model="splitterModel"
			>

				<template v-slot:before>
					<q-tabs
						v-model="tab"
						vertical
						class="text-teal"
					>
						<q-tab name="mails">
							<div id="chart">
								<apexchart :options="chartOptions" :series="series" width="400"></apexchart>
							</div>
						</q-tab>
						<q-tab name="alarms">
							<div id="chart">
								<apexchart :options="chartOptions" :series="series" width="400"></apexchart>
							</div>
						</q-tab>
					</q-tabs>
				</template>

				<template v-slot:after>
					<q-tab-panels
						v-model="tab"
						animated
						swipeable
						vertical
						transition-prev="jump-up"
						transition-next="jump-up"
					>
						<q-tab-panel name="mails">
							<q-timeline color="secondary">
								<q-timeline-entry heading>
									Timeline heading
								</q-timeline-entry>

								<q-timeline-entry
									title="Event Title"
									subtitle="February 22, 1986"
								>
									<div>
										Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
									</div>
								</q-timeline-entry>

								<q-timeline-entry
									title="Event Title"
									subtitle="February 21, 1986"
									icon="delete"
								>
									<div>
										Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
									</div>
								</q-timeline-entry>
							</q-timeline>
						</q-tab-panel>

						<q-tab-panel name="alarms">
							<div class="text-h4 q-mb-md">Alarms</div>
							<p>Lorem ipsum dolor sit, amet consectetur adipisicing elit. Quis praesentium cumque magnam odio iure quidem, quod illum numquam possimus obcaecati commodi minima assumenda consectetur culpa fuga nulla ullam. In, libero.</p>
							<p>Lorem ipsum dolor sit, amet consectetur adipisicing elit. Quis praesentium cumque magnam odio iure quidem, quod illum numquam possimus obcaecati commodi minima assumenda consectetur culpa fuga nulla ullam. In, libero.</p>
						</q-tab-panel>
					</q-tab-panels>
				</template>

			</q-splitter>
			
			<!-- <div class="container d-flex justify-content-start" style="width: 80%;">

				<div id="chart" class="mr-4">
					<apexchart :options="chartOptions" :series="series" width="420"></apexchart>
				</div>

			</div>

			<div class="container d-flex justify-content-start" style="width: 80%;">

				<div id="chart">
					<apexchart type="bar" height="250" width="370" :options="chartOptions2" :series="series2"></apexchart>
				</div>

			</div> -->
		</div>
	</div>
</template>

<script>
// import VueApexCharts from 'vue-apexcharts'
import { mdiPencil, mdiCake, mdiBikeFast, mdiShareVariant, mdiArrowLeft } from '@quasar/extras/mdi-v5'
import EditMypageModal from '../components/Accounts/EditMypageModal.vue'
import Toolbar from './Layout/Toolbar.vue'
import { mapGetters, mapActions, mapMutations } from 'vuex'

import Gravatar from 'vue-gravatar'
export default {

	components: {       
		Gravatar,
		Toolbar
	},
	mounted() {
		this.playAnimation()
		// console.log('vent', this.EVENTS)
	},
	data: function () {
		return {
			numberFrom: 0,
			numberTo: 5000,
			duration: 5,
			scaleClass: false,
			tab: 'mails',
      splitterModel: 40,

			series: [14, 23, 21, 17, 15, 20, 20],
			chartOptions: {
				plotOptions: {
          pie: {
            startAngle: -90,
            endAngle: 90,
            offsetY: 10
          }
        },
        grid: {
          padding: {
            bottom: -80
          }
        },
        legend: {
          show: false,
          position: 'top'
        },
        responsive: [
          {
            breakpoint: 480,
            options: {
              chart: {
                width: 200
              },
              legend: {
                position: 'bottom'
              }
            }
          }
        ],
				colors: ['#2196F3', '#3F51B5', '#673AB7', '#00BCD4', '#4CAF50', '#FF9800', '#F44336'],
				chart: {
					// width: 380,
					type: 'donut',
					events: {
						dataPointSelection: function(event, chartContext, config) {
							console.log(event, chartContext, config);
						}
					},
				},
				labels: ['Lightblue', 'Blue', 'Purple', 'Teal', 'Green', 'Yellow', 'Red'],
				fill: {
					colors: ['#2196F3', '#3F51B5', '#673AB7', '#00BCD4', '#4CAF50', '#FF9800', '#F44336'],
					opacity: 0.9
				},
				stroke: {
					show: false
				},
				yaxis: {
					show: false
				},
				tooltip: {
					fillSeriesColor: true,
					maker: {
						show: true
					}
				},
			},

			series2: [{data: [400, 430, 448]}],
			chartOptions2: {
				chart: {
					type: 'bar',
					height: 300,
				},
				annotations: {
				},
				plotOptions: {
					bar: {
						horizontal: true,
					}
				},
				dataLabels: {
					enabled: true
				},
				xaxis: {
					categories: ['June', 'July', 'August'],
				},
				grid: {
					xaxis: {
						lines: {
							show: false
						}
					}
				},
				yaxis: {
					// reversed: true,
					axisTicks: {
						show: false
					}
				}
			},

		}
	},
	methods: {
		...mapActions('calendar', ['updateMySchedule']),
		generateData(count, yrange) {
				var i = 0;
				var series = [];
				while (i < count) {
					var x = (i + 1).toString();
					var y = Math.floor(Math.random() * (yrange.max - yrange.min + 1)) + yrange.min;
					series.push({
						x: x,
						y: y
					});
					i++;
				}
				return series;
			},
		updateChart() {
		const max = 90;
		const min = 20;
		const newData = this.series[0].data.map(() => {
			return Math.floor(Math.random() * (max - min + 1)) + min
		})
		// In the same way, update the series option
		this.series = [{
			data: newData
		}]
		},
		
		theFormat(number) {
				return number.toFixed(2);
			},
			completed() {
				console.log("Animation ends!");
				this.scaleClass = true;
			},
			playAnimation() {
				console.log("Animation starts!");
				this.scaleClass = false;
				this.$refs.number2.play();
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
	},
 	created() {
		this.mdiPencil = mdiPencil
		this.mdiCake = mdiCake
		this.mdiBikeFast = mdiBikeFast
		this.mdiShareVariant = mdiShareVariant
		this.mdiArrowLeft = mdiArrowLeft
		this.updateMySchedule({people: sessionStorage.getItem("username"),flag:true})
		console.log('vent', this.EVENTS)
	},
	computed: {
		...mapGetters('calendar', ["EVENTS", "TASKS"]),
	}
}
</script>

<style scoped>
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
</style>