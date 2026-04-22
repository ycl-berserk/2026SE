<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { fetchReviewList } from '../api/leave'

const loading = ref(false)
const activeStatus = ref(2)
const rows = ref([])
const router = useRouter()

async function loadData() {
  loading.value = true
  try {
    rows.value = await fetchReviewList(activeStatus.value)
  } catch (error) {
    ElMessage.error(error.message || '加载已处理列表失败')
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
        <span>已处理请假申请</span>
        <div class="actions">
          <el-select v-model="activeStatus" style="width: 140px" @change="loadData">
            <el-option label="已通过" :value="2" />
            <el-option label="已驳回" :value="3" />
          </el-select>
          <el-button @click="loadData">刷新</el-button>
        </div>
      </div>
    </template>
    <el-table :data="rows" v-loading="loading" stripe>
      <el-table-column prop="id" label="申请ID" width="120" />
      <el-table-column label="申请人" min-width="160">
        <template #default="{ row }">{{ row.applicantName }}（{{ row.applicantStudentNo }}）</template>
      </el-table-column>
      <el-table-column prop="title" label="标题" min-width="220" />
      <el-table-column prop="statusText" label="状态" width="100" />
      <el-table-column prop="submitTime" label="提交时间" min-width="180" />
      <el-table-column label="详情" width="120" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link @click="goDetail(row.id)">查看</el-button>
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

.actions {
  display: flex;
  gap: 10px;
}
</style>
