<template>
  <div id="SA">
    <q-card class="rounded border">
      <q-card-section>

        <q-tabs
          v-model="model"
          dense
          class="text-grey"
          active-color="primary"
          indicator-color="primary"
          align="center"
          narrow-indicator
        >
          <q-tab name="CREATE_NEW_GROUP" label="CREATE NEW GROUP" />
          <q-tab name="JOIN_GROUP" label="JOIN GROUP" />
        </q-tabs>

        <q-separator />

        <q-tab-panels v-model="model" animated>
          <q-tab-panel name="CREATE_NEW_GROUP">
            <div class="text-h6">CREATE NEW GROUP</div>
              <q-input class="m-0 p-0" v-model="group_name" label="Enter the group name here" />
              <q-input class="m-0 p-0" v-model="group_detail" type="textarea" label="Enter the group detail here" />
              <div class="d-flex justify-content-end mt-3">
                <button class="btn text-primary pb-0" @click="updateGroup" :class="{ 'text-light' : this.$q.dark.isActive }">Create</button>
                <button class="btn text-danger pb-0" @click="exit" :class="{ 'text-light' : this.$q.dark.isActive }">cancel</button>
              </div>
          </q-tab-panel>

          <q-tab-panel name="JOIN_GROUP">
            <div class="text-h6">JOIN GROUP</div>
              <q-input class="m-0 p-0" v-model="group_code" label="Enter the group code here" />
              <div class="d-flex justify-content-end mt-3">
                <button class="btn text-primary pb-0" @click="joinGroup" :class="{ 'text-light' : this.$q.dark.isActive }">Join</button>
                <button class="btn text-danger pb-0" @click="exit" :class="{ 'text-light' : this.$q.dark.isActive }">cancel</button>
              </div>
          </q-tab-panel>

        </q-tab-panels>
      </q-card-section>
    </q-card>
  </div>
</template>

<script>
import swal from 'sweetalert'
import { mapGetters, mapActions } from 'vuex'

export default {
  props: {
    groups: Array,
    group_name_for_modal: Array,
    userId: String,
    goDark: Boolean,
  },

  data() {
    return {
      group_name: '',
      group_detail: '',
      group_members: [],
      group_code:'',
      model: 'CREATE_NEW_GROUP',
      rules: [v => v.length <= 20 || 'Max 20 characters'],
    }
  },

  computed: {
    ...mapGetters('groups', ["GROUPS", "USER", "group_name", "MEMBER_INFO", "MEMOS"]),
  },

  methods: {
    ...mapActions('groups', ["ADD_GROUP", "JoinGroup_Invite"]),

    updateGroup() {
      if (this.group_name == '') {
          swal('Warning', 'Group name is necessary', 'warning')
      } else if (this.group_name.length > 20) {
          swal('Warning', 'Max 20 characters', 'warning')
      } else if (this.group_name_for_modal.includes(this.group_name)) {
        swal('Warning', 'Group name is already exists.', 'warning')
      } else if (this.group_detail == '') {
          swal('Warning', 'Group information is necessary', 'warning')
      } else {
          this.ADD_GROUP({
            group_name: this.group_name,
            group_detail: this.group_detail,
            group_members: [sessionStorage.getItem("username")],
            group_master: this.userId,
          }
        )
        this.$emit('close')
        swal('Success', 'Your request is approved', 'success')
      }
    },

    joinGroup() {
      this.JoinGroup_Invite({group_code: this.group_code})
      swal('Success', 'Your request is approved', 'success')
      this.$emit('close')
    },

    exit() {
			this.$emit('close')
		}
  }
}
</script>

<style scoped>
  /* .v-application--wrap {
    max-height: 372px !important;
  } */
  .q-tabs__content {
    margin: 0 !important;
  }
</style>

<style lang="scss">
  .q-tabs__content {
    margin: 0 !important;
  }
</style>