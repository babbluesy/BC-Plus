<template>
  <div id="NS" class="rounded border">
    <div id="carouselExampleControls" class="carousel slide" data-ride="carousel">
      <div class="carousel-inner">
        <div class="carousel-item active">
          <img class="homeimg" src="@/assets/m2.gif" style="width: 1200px; opacity: 0.7;">
          <div class="carousel-caption d-none d-md-block mb-4">
            <div class="carousel-caption d-none d-md-block">
              <h3><strong>쉽고 빠르게 일정을 관리해 보세요!</strong></h3>
              <hr style="width: 70px; height: 4px; margin: 0px auto; text-align: center;">
              <h6>간편한 스케줄 등록과 TODO LIST를 통해 쉽고 빠르게 일정을 관리할 수 있습니다.</h6>
              <h6><strong>GOOGLE 캘린더</strong>와의 연동을 통해 더욱 효과적으로 일정을 관리하세요! </h6>
            </div>
          </div>
        </div>
        <div class="carousel-item">
          <img class="homeimg" src="@/assets/m3.gif" style="width: 1200px; opacity: 0.7;">
          <div class="carousel-caption d-none d-md-block mb-4">
            <div class="carousel-caption d-none d-md-block">
              <h3><strong>목적에 맞게 캘린더 그룹을 분류해 보세요!</strong></h3>
              <hr style="width: 70px; height: 4px; margin: 0px auto; text-align: center;">
              <h6>뒤죽박죽 일정이 섞여 정신없는 캘린더 대신, 그룹 캘린더는 어떠세요?</h6>
              <h6>개인, 친목, 업무 등 다양한 목적에 맞게 깔끔하게 캘린더를 관리하실 수 있습니다.</h6>
            </div>
          </div>
        </div>
        <div class="carousel-item">
          <img class="homeimg" src="@/assets/m4.gif" style="width: 1200px; opacity: 0.7;">
          <div class="carousel-caption d-none d-md-block mb-4">
            <div class="carousel-caption d-none d-md-block">
              <h3><strong>다양한 사람들과 캘린더를 공유해 보세요!</strong></h3>
              <hr style="width: 70px; height: 4px; margin: 0px auto; text-align: center;">
              <h6>BC+의 캘린더 공유기능을 통해 유저들과 캘린더를 공유할 수 있습니다.</h6>
              <h6>또한 일정 할당 기능을 통해 내 캘린더로 그룹의 일정을 옮겨올 수 있습니다.</h6>
            </div>
          </div>
        </div>
      </div>
      <a class="carousel-control-prev" href="#carouselExampleControls" role="button" data-slide="prev">
        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
        <span class="sr-only">Previous</span>
      </a>
      <a class="carousel-control-next" href="#carouselExampleControls" role="button" data-slide="next">
        <span class="carousel-control-next-icon" aria-hidden="true"></span>
        <span class="sr-only">Next</span>
      </a>
    </div>
  </div>
</template>

<script>
export default {
  data () {
    return {
    }
  },
}
</script>

<style scoped>
h3 {
    color: black;
}
h6 {
    color: black;
}
</style>