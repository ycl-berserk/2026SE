<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { fetchReviewList } from '../api/leave'

const loading = ref(false)
const rows = ref([])
const router = useRouter()

async function loadData() {
  loading.value = true
  try {
    rows.value = await fetchReviewList(0)
  } catch (error) {
    ElMessage.error(error.message || '加载待审批失败')
  } finally {
    loading.value = false
  }
}

function goDetail(id) {
  router.push(`/review/${id}`)
}

onMounted(loadData)
</script>

<template>
  <el-card>
    <template #header>
      <div class="header">
        <span>待审批请假申请</span>
        <el-button @click="loadData">刷新</el-button>
      </div>
    </template>
    <el-table :data="rows" v-loading="loading" stripe>
      <el-table-column prop="id" label="申请ID" width="120" />
      <el-table-column label="申请人" min-width="160">
        <template #default="{ row }">{{ row.applicantName }}（{{ row.applicantStudentNo }}）</template>
      </el-table-column>
      <el-table-column prop="title" label="标题" min-width="200" />
      <el-table-column prop="leaveStartDate" label="开始日期" width="130" />
      <el-table-column prop="leaveEndDate" label="结束日期" width="130" />
      <el-table-column prop="statusText" label="状态" width="100" />
      <el-table-column label="操作" width="120" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link @click="goDetail(row.id)">审批</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
