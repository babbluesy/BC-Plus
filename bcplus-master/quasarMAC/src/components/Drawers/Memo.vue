<template>
  <div class="q-pa-md">
		<div class="d-flex add-memo icon-btn justify-content-center m-2 p-2" @click="switchAddMemo">
			<q-icon :name="mdiPlus" style="color: green;" />
			<q-icon :name="fasStickyNote" style="color: #ffc107;" />
			<q-tooltip>
				Add Memo
			</q-tooltip>
		</div>
		<div v-if="this.switchMemo" class="border rounded">
			<q-input class="m-0 p-0" v-model="text_memo" type="textarea" label="Insert Memo..." />
			<div class="d-flex justify-content-end">
				<button class="btn text-primary mt-2" @click="addMemoToFront" :class="{ 'text-light' : this.$q.dark.isActive }" >Create</button>
			</div>
		</div>

		<q-separator class="my-4" inset />

		<div v-for="(memo, idx) in MEMOS" :key="idx" class="mb-2">
			<q-card class="my-card post-memo p-1 border">
				<div class="d-flex justify-content-end">
					<q-icon 
						class="icon-btn mt-1" 
						:name="mdiClose" 
						@click="deleteMemoToFront(idx)"
					/>
				</div>
				<q-card-section class="pt-0" style="word-break: break-all;">
					{{ memo.text }}
				</q-card-section>
			</q-card>
		</div>
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'
import { mdiClose, mdiPlus } from '@quasar/extras/mdi-v5'
import { fasStickyNote } from '@quasar/extras/fontawesome-v5'

export default {
	components: {
		mdiClose,
		mdiPlus
	},
	data() {
		return {
			switchMemo: false,
			text_memo: ''
		}
	},
	computed: {
		...mapGetters('calendar', ["MEMOS"]),
	},
	methods: {
		...mapActions('calendar', ["deleteMemo", "addMemo", "getMemo"]),

		switchAddMemo() {
			this.switchMemo = !this.switchMemo
		},

		addMemoToFront() {
			this.addMemo({
        memo: this.text_memo,
        no: this.MEMOS.length
			})    
			this.text_memo = ''
			this.switchMemo = !this.switchMemo
    },

    deleteMemoToFront(idx) {
			// console.log('delete')
      this.MEMOS.splice(idx, 1)
			this.deleteMemo({memos: this.MEMOS})
		},
		
	},
	created() {
		this.getMemo()
		this.mdiClose = mdiClose
		this.mdiPlus = mdiPlus
		this.fasStickyNote = fasStickyNote
	}
}
</script>

<style scoped>
.add-memo {
	border: 2px solid grey; 
	border-radius: 1rem; 
	border-style: dotted;
}

.post-memo {
	background: #ffc107; 
	opacity: 0.9; 
	color: #121212;
}
</style>