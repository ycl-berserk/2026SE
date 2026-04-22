<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { approveLeave, fetchReviewDetail, rejectLeave } from '../api/leave'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const submitting = ref(false)
const detail = ref(null)
const comment = ref('')

const canReview = computed(() => detail.value && detail.value.status === 0)

async function loadData() {
  loading.value = true
  try {
    detail.value = await fetchReviewDetail(route.params.id)
  } catch (error) {
    ElMessage.error(error.message || '加载详情失败')
  } finally {
    loading.value = false
  }
}

async function doApprove() {
  if (!comment.value.trim()) {
    ElMessage.warning('请先填写审批意见')
    return
  }
  submitting.value = true
  try {
    await approveLeave(route.params.id, comment.value.trim())
    ElMessage.success('已审批通过')
    await loadData()
  } catch (error) {
    ElMessage.error(error.message || '审批失败')
  } finally {
    submitting.value = false
  }
}

async function doReject() {
  if (!comment.value.trim()) {
    ElMessage.warning('请先填写驳回原因')
    return
  }
  submitting.value = true
  try {
    await rejectLeave(route.params.id, comment.value.trim())
    ElMessage.success('已驳回申请')
    await loadData()
  } catch (error) {
    ElMessage.error(error.message || '驳回失败')
  } finally {
    submitting.value = false
  }
}

onMounted(loadData)
</script>

<template>
  <el-card v-loading="loading">
    <template #header>
      <div class="header">
        <span>申请详情 #{{ route.params.id }}</span>
        <el-button @click="router.back()">返回</el-button>
      </div>
    </template>

    <el-descriptions v-if="detail" :column="2" border>
      <el-descriptions-item label="申请人">{{ detail.applicantName }}（{{ detail.applicantStudentNo }}）</el-descriptions-item>
      <el-descriptions-item label="状态">{{ detail.statusText }}</el-descriptions-item>
      <el-descriptions-item label="标题">{{ detail.title }}</el-descriptions-item>
      <el-descriptions-item label="联系电话">{{ detail.contactPhone }}</el-descriptions-item>
      <el-descriptions-item label="请假日期">{{ detail.leaveStartDate }} 至 {{ detail.leaveEndDate }}</el-descriptions-item>
      <el-descriptions-item label="提交时间">{{ detail.submitTime }}</el-descriptions-item>
      <el-descriptions-item label="请假事由" :span="2">{{ detail.reason }}</el-descriptions-item>
      <el-descriptions-item v-if="detail.rejectReason" label="驳回原因" :span="2">{{ detail.rejectReason }}</el-descriptions-item>
    </el-descriptions>

    <div class="timeline" v-if="detail && detail.timelines && detail.timelines.length">
      <h4>审批时间线</h4>
      <el-timeline>
        <el-timeline-item
          v-for="item in detail.timelines"
          :key="`${item.action}-${item.createdAt}`"
          :timestamp="item.createdAt"
        >
          {{ item.actionText }} · {{ item.operatorName }} · {{ item.comment }}
        </el-timeline-item>
      </el-timeline>
    </div>

    <div class="review-box" v-if="canReview">
      <h4>审批操作</h4>
      <el-input
        v-model="comment"
        type="textarea"
        :rows="4"
        placeholder="请输入审批意见或驳回原因"
      />
      <div class="ops">
        <el-button type="success" :loading="submitting" @click="doApprove">通过</el-button>
        <el-button type="danger" :loading="submitting" @click="doReject">驳回</el-button>
      </div>
    </div>
  </el-card>
</template>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.timeline {
  margin-top: 24px;
}

.review-box {
  margin-top: 24px;
}

.ops {
  margin-top: 12px;
}
</style>
