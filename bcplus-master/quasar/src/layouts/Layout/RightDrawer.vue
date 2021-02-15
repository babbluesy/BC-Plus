<template>
	<q-scroll-area class="fit">
		<div class="mt-5">

			<q-list v-if="!chat">
				<q-item class="pt-0">
					<div class="mx-auto">
						<h4 v-if="people == this.selectGroup && people != null"> {{ selectGroup }} </h4>
						<h4 v-else>Group List</h4>
					</div>
				</q-item>

				<q-item class="mx-0">
					<div v-if="people == this.selectGroup && people != null">
						<div class="d-flex absolute-center px-0">
							<div class="mx-1">
								<q-icon 
									class="icon-btn mr-1"
									:name="mdiChatProcessingOutline" 
									@click="startChat(selectGroup)"
									size="md"
								/>
									<q-tooltip>
										Group Chat
									</q-tooltip>
							</div>

							<div class="mx-1">
								<q-icon 
									class="icon-btn"
									:name="mdiCalendarCheck" 
									@click="GroupInfoModal"
									size="md"
								/>
								<q-tooltip>
									Group Info
								</q-tooltip>
							</div>
						</div>
					</div>
					<small v-else class="mx-auto text-secondary">Select a group for schedule you want</small>
				</q-item>

				<div class="d-flex">
					<q-item class="mx-0">
						<div class="container">
							<div class="mx-2 column">
								<q-toggle
									class="groupToggle mb-2"
									v-for="(group, idx) in GROUP_NAME" :key="idx" 
									:label="group"
									v-model="tmp"
									:val="group"
								/>
							</div>

						</div>
					</q-item>
				</div>
        <!-- </div> -->
			</q-list>

			<q-list dense v-else style="height: 100%;">
				<ChatRoom 
					:chatgroup="chatgroup"
					:groups="GROUPS"
					:people="people"
					:chat="chat"             
					@gotoList="gotoList"
					:goDark="goDark"
				/>
			</q-list>

			<!-- 그룹 추가 버튼 -->
				<div class="fixed-bottom-right py-4 px-4">
					<q-icon
						v-if="!chat"
						class="icon-btn" 
						@click="addGroupModal"
						:name="mdiPlusThick"
						size="lg" 
					/>
					<q-tooltip>
						New Group
					</q-tooltip>
				</div>
		</div>
	</q-scroll-area>
</template>

<script>
import AddGroupModal from '../../components/Groups/AddGroupModal.vue'
import GroupInfoModal from '../../components/Groups/GroupInfoModal.vue'
import ChatRoom from '../../components/Groups/ChatRoom.vue'
 
import { mapGetters, mapActions, mapMutations } from 'vuex'
import { mdiPlusThick, mdiChatProcessingOutline, mdiCalendarCheck } from '@quasar/extras/mdi-v5'

export default {
	data () {
    return {
			// drawerRight: false,
			tmp: [],
			selectGroup: null,
			groupNum: null,
			toggle: false,
			chat: false,
			chatgroup: null,
			people: null,
			goDark: false,
			master: '',
			member_info:this.MEMBER_INFO,
			drawerRightValue: null,
    }
	},
	components: {
		ChatRoom,
	},
	props: {
		drawerRight: Boolean
	},
	computed: {
		...mapGetters('groups', ["EVENTS", "GROUPS", "USER", "GROUP_NAME", "LUN_DAYS", "TASKS", "MEMBER_INFO","CHATTINGS","MEMOS" ]),
	},
	methods: {
		...mapActions('groups', ['getGroupInfo', 'getMessage']),
		...mapActions('calendar', ['updateMySchedule', 'updateSchedule', 'getHolidayInfo']),
		...mapMutations('design', ['changeNumColor']),
		addGroupModal() {
      this.$modal.show(AddGroupModal, {
        groups: this.GROUPS,
        group_name_for_modal: this.GROUP_NAME,
        goDark: this.goDark,
        modal: this.$modal},{
            height: 'auto'
      })
		},
		startChat(group) {
    //   console.log('CHAT!')      
      this.chat = !this.chat
      this.chatgroup = group      
		},
		
		GroupInfoModal() {
      this.$modal.show(GroupInfoModal, {
        groups: this.GROUPS,
        group_name: this.GROUP_NAME,
        people: this.people,
        goDark: this.goDark,
        modal: this.$modal},{
            height: 'auto'
      })
		},
		
		gotoList() {
      this.chat = !this.chat
		},
		
		changePeople(group){
			// console.log(group)
			if (this.people === null) {
				this.people = group
			} else if (this.people !== null && this.people !== group) {
				this.people = group
			} else {
				this.people = null
			}
		},	

		getGroupCal(group, toggle) {
			if (toggle == true) {
				this.people = group
			} else {
				this.people = null
			}
			// console.log(this.people, toggle)
		}
	},
	watch: {
		people() {
			// console.log(this.people, 'watch')
			this.$emit('changePeople', this.people)
			this.getMessage({groupName:this.people}).then(()=>{console.log("!?!?!?!?!")})
		},

		drawerRight() {
			this.drawerRightValue = this.drawerRight
		},

		tmp() {
			if (this.tmp.length == 1) {
				this.people = this.tmp[0]
				this.selectGroup = this.people
			} else if (this.tmp.length > 1) {
				this.people = this.tmp.slice(-1)[0]
				this.tmp = []
				this.tmp.push(this.people)
				this.selectGroup = this.people
			} else {
				this.people = null
				this.selectGroup = null
			}
		}
	},
	created() {
		this.getGroupInfo()
		// console.log(this.GROUP_NAME)
		this.mdiPlusThick = mdiPlusThick
		this.mdiChatProcessingOutline = mdiChatProcessingOutline
		this.mdiCalendarCheck = mdiCalendarCheck
	},
}
</script>

<style lang="scss">
.groupToggle {
	.q-toggle__label.q-anchor--skip {
		width: 140px;
		overflow: hidden; 
		text-overflow:ellipsis; 
		display: -webkit-box; 
		-webkit-line-clamp: 1; 
		-webkit-box-orient: vertical;
		}
}
</style>