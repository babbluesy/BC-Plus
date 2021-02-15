<template>
  <v-app id="inspire">
    <fieldset>
      <v-card id="SA" class="rounded border">
        <v-container>
          <v-tabs v-model="model" centered>
            <v-tab>CREATE NEW GROUP</v-tab>
            <v-tab>JOIN GROUP</v-tab>
          </v-tabs>
          <v-tabs-items v-model="model" class="rounded">
            <!-- ADD GROUP -->
            <v-tab-item>
              <v-card flat>
                <v-text-field v-model="group_name" :rules="rules" type="text" label="Enter the group name here"></v-text-field>
                <v-textarea v-model="group_detail" type="textarea" label="Enter the group detail here"></v-textarea>
                <div class="d-flex justify-content-end">
                  <button class="btn text-primary" @click="updateGroup">Create</button>
                </div>
              </v-card>
            </v-tab-item>
            <!-- INVITE -->
            <v-tab-item>
              <v-card flat>
                <v-text-field v-model="group_code" type="text" label="Enter the group code here"></v-text-field>
                <div class="d-flex justify-content-end">
                  <div class="d-flex justify-content-end">
                    <button class="btn text-primary" @click="joinGroup">Join</button>
                  </div>
                </div>
              </v-card>
            </v-tab-item>
          </v-tabs-items>
        </v-container>
      </v-card>
    </fieldset>
  </v-app>
</template>

<script>
import swal from 'sweetalert'

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
      model: 'tab-2',
      rules: [v => v.length <= 20 || 'Max 20 characters'],
    }
  },

  methods: {
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
        this.$store.dispatch("ADD_GROUP", {
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
      this.$store.dispatch("invite", {group_code: this.group_code});
      swal('Success', 'Your request is approved', 'success')
      this.$emit('close')
    },
  }
}
</script>

<style scoped>
  .v-application--wrap {
    max-height: 372px !important;
  }
</style>