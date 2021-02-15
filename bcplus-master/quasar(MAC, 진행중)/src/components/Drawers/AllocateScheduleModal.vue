<template>
	<q-card id="SA" class="rounded border p-2" style="min-height: 250px;">
		<label class="typo__label"><legend>Allocate Schedule</legend></label>
		<multiselect 
			v-model="friends" 
			:options="this.selectMembers" 
			:multiple="true" 
			:searchable="false" 
			:close-on-select="false" 
			:clear-on-select="true" 
			placeholder="Pick Group Members!" 
			label="name" 
			track-by="name" 
			:preselect-first="false"
			:custom-label="customLabel" 
			:show-labels="false"
			:maxHeight="200"
		>
			<template slot="tag" slot-scope="{ option, remove }">
				<span class="bg-light p-1 rounded border mx-1">
					<Gravatar class="mb-1 rounded" :email="option.name" :size="20" />
					<span class="option__desc mx-2"><span class="option__title">{{ option.name }}</span><q-icon class="custom__remove ml-1 mb-1" :name="mdiCloseCircle" @click="remove(option)" /></span>
				</span>
			</template>
			<template slot="option" slot-scope="props">
				<div class="d-flex">
					<Gravatar class="mb-1 rounded" :email="props.option.name" :size="20" />
					<div class="option__desc mx-2"><span class="option__title">{{ props.option.name }}</span></div>
				</div>
			</template>
		</multiselect>
		<div class="absolute-bottom-right m-2">
			<button class="btn text-primary" @click="addMem({friends: friends, sid: sid})" :class="{ 'text-light' : this.$q.dark.isActive }">Allocate</button>
			<button class="btn text-danger mr-1" @click="exit" :class="{ 'text-light' : this.$q.dark.isActive }">cancel</button>
		</div>
	</q-card>
</template>

<script>
import Multiselect from 'vue-multiselect'
import Gravatar from 'vue-gravatar'
import { mdiCloseCircle } from '@quasar/extras/mdi-v5'
import { mapActions } from 'vuex'

export default {
	components: {
		Multiselect ,
		Gravatar,
		mdiCloseCircle
	 },
	 props: {
		 member_info: Array,
		 sid: String,
		 people: String,
	 },
	data () {
    return {
			friends: [],
			// allocate_member: [],
			selectMembers: null,
    }
  },
  methods: {
		...mapActions('groups',['addMember']),

		customLabel ({ name }) {
			return `${ name }`
		},

		addMem(data) {
			var allocate_member = []
			for (var i=0; i < data.friends.length; i++) {
				allocate_member.push(data.friends[i].name)
			}
		this.addMember({members: allocate_member, sid: data.sid, people: this.people})
		this.$emit('close')
		},

		exit() {
			this.$emit('close')
		}
	},
	created() {
		this.mdiCloseCircle = mdiCloseCircle
		if (this.member_info.length > 1) {
			this.selectMembers = this.member_info.slice(1)
		} else {
			this.selectMembers = []
		}
	},
}
</script>

<style src="vue-multiselect/dist/vue-multiselect.min.css"></style>
<style lang="scss" scoped>

</style>

<style>
/* .q-field {
	margin: 0 !important;
	padding: 0! important;
}

.q-chip {
	margin: 0 !important;
	padding: 0! important;
}

.q-field__native {
	margin: 0 !important;
	padding: 0! important;
}

.q-chip__icon {
	margin: 0 !important;
}

.q-chip__content {
	margin: 0 !important;
	padding: 0! important;
} */

/* ="q-field__label */
/* .row {
	margin: 0 !important;
	padding: 0! important;
} */
</style>