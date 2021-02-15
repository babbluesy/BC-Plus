<template>
  <div id="q-app" class="fixed-center">
    <div v-if="this.$q.screen.lt.sm">
      <MainforMobile />
    </div>
    <div v-else class="fixed-center">
      <MainforDesktop />
    </div>
  </div>
</template>

<script>
import MainforMobile from '../components/Main/MainforMobile.vue'
import MainforDesktop from '../components/Main/MainforDesktop.vue'

import { Platform } from 'quasar'
export default {
  components: {
    MainforMobile,
    MainforDesktop
  },
}
</script>

<style scoped>

</style>