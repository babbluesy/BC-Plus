<template>
  <div id="SA">
    <q-toolbar class="text-white">
      <q-btn flat round dense @click="switchLeftDrawer" :icon="$q.screen.lt.sm ? 'list' : 'menu'" />
      <q-toolbar-title class="d-flex pt-2 pl-0 icon-btn" @click="gotoMain">
        <h3 v-if="!this.$q.screen.lt.sm" class=""><span class="font-weight-bold">P</span><span style="font-size: 10px;">ersonal</span><span class="font-weight-bold">S</span><span style="font-size: 10px;">ecretary</span> Calendar</h3>
        <h3 v-else class=""><span class="font-weight-bold">P.S.</span></h3>
        <div class="ml-4">
          <q-toggle 
            v-if="!this.$q.screen.lt.sm"
            flat 
            round
            v-model="goDark" 
            @click="goDarkMode"
            :icon="$q.dark.isActive ? 'nights_stay' : 'wb_sunny'"
            style="font-size: 1rem;"
            color="grey"
          />
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
          <b-dropdown-item variant="primary" @click="gotoAnalytics">Analytics</b-dropdown-item>
          <b-dropdown-item varient="secondary" v-if="$q.screen.lt.sm">
            <q-toggle 
              :label="`Dark Mode`"
              flat 
              round
              v-model="goDark" 
              @click="goDarkMode"
              :icon="$q.dark.isActive ? 'nights_stay' : 'wb_sunny'"
              style="font-size: 1rem;"
              color="grey"
            />
          </b-dropdown-item>
          <b-dropdown-item variant="danger" @click="gotoLogout">Logout</b-dropdown-item>
        </b-dropdown>
      <q-btn flat round dense @click="switchRightDrawer" :icon="$q.screen.lt.sm ? 'group' : 'menu'" />
    </q-toolbar>
  </div>
</template>

<script>
import MypageModal from '../../components/Accounts/MypageModal.vue'

import Gravatar from 'vue-gravatar'
import { mapActions, mapMutations } from 'vuex'

export default {
  data() {
    return{
      goDark: false,
      userId: null,
      userName: this.USERNAME,
    }
  },
  components: {
    Gravatar,
  },
  // props: {
  //   analytics: Boolean
  // },
  methods: {
    ...mapActions('accounts', ['Logout']),
    ...mapMutations('design', ['changeNumColor']),
    ...mapMutations('calendar', ['monthInit']),

    switchLeftDrawer() {
      this.$emit('switchLeftDrawer')
    },

    switchRightDrawer() {
      this.$emit('switchRightDrawer')
    },

    // MypageModal() {
    //   this.$modal.show(MypageModal,
    //   { userId: this.userId,
    //     ggl: sessionStorage.getItem("isGoogle"),
    //     modal: this.$modal},
    //     {height: 'auto'})
    // },
    gotoAnalytics() {
      this.monthInit({different: 0})
      this.$router.push('/analytics')
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
    goDarkMode() {
      this.$emit('goDarkMode')
    },
    gotoMain() {
      this.$router.push('/calendar')
    }
  },
  watch: {
    goDark() {       
      // console.log('adfasdf', this.$q.dark.isActive,  this.goDark)
      this.$q.dark.isActive = this.goDark
      this.changeNumColor({goDark: this.goDark})
      this.goDarkMode()
    },
  },
  created() {
    this.token=sessionStorage.getItem("access-token")
    this.userId=sessionStorage.getItem("username")
    this.userName=sessionStorage.getItem("name")
    this.goDark = this.$q.dark.isActive
  },
}
</script>

<style scoped>
.dropdown-dark {
  background-color: #192734; 
  border: 1px solid white !important;
  filter: brightness(130%);
}

</style>

<style lang="scss">
.q-toolbar {
  margin: 0 !important;
}

.q-btn__wrapper {
  margin: 0 !important; 
}

.q-btn__content {
  margin: 0 !important; 
}

.q-avatar__content {
  margin: 0 !important;
}

.toggle-username {
  max-width: 120px; 
  display: block; 
  overflow: hidden; 
  text-overflow:ellipsis;
}

.dropdown-toggle-no-caret {
  padding: 0px !important;
}
// button#__BVID__18__BV_toggle_ {
//   padding: 0px !important;
// }
</style>