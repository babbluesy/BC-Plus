<template>
	<v-app id="inspire">
		<div id="SA" class="container">
			<div class="d-flex">
				<div class="mt-10" style="margin: 40px;">
					<Gravatar class="mt-5" email="mem" :size="200" />
					<div align="center">
						<h5 class="mt-10">USERNAME</h5>
					</div>
				</div>
				<div id="chart">
					<apexchart type="heatmap" width="800" height="350" :options="chartOptions" :series="series"></apexchart>
				</div>
			</div>

			<div class="d-flex" align="center">
				<div class="m-1 pt-10 container bg-primary rounded" style="width: 300px; height: 250px;">
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
				<div class="m-1 pt-5 container bg-secondary rounded" style="width: 300px; height: 250px;">
					<div>
						<v-icon x-large>
							mdi-cake
						</v-icon>
					</div>
					<div style="font-size: 50px; color: white;">
						7
					</div>
					<h3>10월 경조사 일정</h3>
					<p class="m-0">이번달은</p>
					<p>선물값이 많이 나가겠네요!</p>
				</div>
				<div class="m-1 pt-5 container bg-warning rounded" style="width: 300px; height: 250px;">
					<div>
						<v-icon x-large>
							mdi-bike-fast
						</v-icon>
					</div>
					<div style="font-size: 50px; color: white;">
						4
					</div>
					<h3>10월 운동계획</h3>
					<p class="m-0">이번 주말에는</p>
					<p>가볍게 러닝 한 시간 어떠세요?</p>
				</div>
				<div class="m-1 pt-5 container bg-success rounded" style="width: 300px; height: 250px;">
					<div>
						<v-icon x-large>
							mdi-share-variant
						</v-icon>
					</div>
					<div style="font-size: 50px; color: white;">
						23
					</div>
					<h3>10월 공유된 일정</h3>
					<p class="m-0">이번 달은</p>
					<p>많이 바쁜 한 달이 되겠네요!</p>
				</div>
			</div>

			<v-divider></v-divider>
			<div class="d-flex justify-content-between">
				<div id="chart">
					<apexchart type="polarArea" :options="chartOptions2" :series="series2" width="400"></apexchart>
				</div>
				<div>
			<apexchart width="600" height="300" type="bar" :options="chartOptions3" :series="series3"></apexchart>
		</div>
			</div>

			<br>
	</div>
	</v-app>
	
</template>

<script>
// import VueApexCharts from 'vue-apexcharts'
import Gravatar from 'vue-gravatar'
export default {
//  name: 'MypageChart',
//  components: {
//           apexchart: VueApexCharts,
//         },
components: {       
    Gravatar,
	},
	mounted() {
		this.playAnimation()
	},
 data: function () {
  return {
		numberFrom: 0,
    numberTo: 5000,
    duration: 5,
		scaleClass: false,

		series2: [14, 23, 21, 17, 15],
          chartOptions2: {
            chart: {
              type: 'polarArea',
            },
            stroke: {
              colors: ['#fff']
            },
            fill: {
              opacity: 0.8
            },
            responsive: [{
              breakpoint: 480,
              options: {
                chart: {
                  width: 200
                },
                legend: {
                  position: 'bottom'
                }
              }
            }]
          },

		series: [
			{
				name: 'Jan',
				data: this.generateData(30, {
					min: -30,
					max: 55
				})
			},
			{
				name: 'Feb',
				data: this.generateData(30, {
					min: -30,
					max: 55
				})
			},
			{
				name: 'Mar',
				data: this.generateData(30, {
					min: -30,
					max: 55
				})
			},
			{
				name: 'Apr',
				data: this.generateData(30, {
					min: -30,
					max: 55
				})
			},
			{
				name: 'May',
				data: this.generateData(30, {
					min: -30,
					max: 55
				})
			},
			{
				name: 'Jun',
				data: this.generateData(30, {
					min: -30,
					max: 55
				})
			},
			{
				name: 'Jul',
				data: this.generateData(30, {
					min: -30,
					max: 55
				})
			},
			{
				name: 'Aug',
				data: this.generateData(30, {
					min: -30,
					max: 55
				})
			},
			{
				name: 'Sep',
				data: this.generateData(30, {
					min: -30,
					max: 55
				})
			}
		],
          chartOptions: {
            chart: {
              height: 350,
              type: 'heatmap',
            },
            plotOptions: {
              heatmap: {
                shadeIntensity: 0.5,
                radius: 0,
                useFillColorAsStroke: true,
                colorScale: {
                  ranges: [{
                      from: -30,
                      to: 5,
                      name: 'low',
                      color: '#00A100'
                    },
                    {
                      from: 6,
                      to: 20,
                      name: 'medium',
                      color: '#128FD9'
                    },
                    {
                      from: 21,
                      to: 45,
                      name: 'high',
                      color: '#FFB200'
                    },
                    {
                      from: 46,
                      to: 55,
                      name: 'extreme',
                      color: '#FF0000'
                    }
                  ]
                }
              }
            },
            dataLabels: {
              enabled: false
            },
            stroke: {
              width: 1
            },
            // title: {
            //   text: 'HeatMap Chart with Color Range'
            // },
          },
   chartOptions3: {
    chart: {
      id: 'vuechart-example',
    },
    xaxis: {
      categories: [1991, 1992, 1993, 1994, 1995, 1996, 1997, 1998],
    },
   },
   series3: [{
    name: 'MypageCharts',
    data: [30, 40, 45, 50, 49, 60, 70, 81]
   }]
  }
 },
 methods: {
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
    }
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
</style>