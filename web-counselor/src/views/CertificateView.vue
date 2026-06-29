<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import request from '../api/http'

const list = ref([])
const loading = ref(false)
const detailVisible = ref(false)
const currentItem = ref(null)
const rejectDialogVisible = ref(false)
const rejectReason = ref('')
const approveDialogVisible = ref(false)
const approveTarget = ref(null)
const certificateFileId = ref(null)
const certificateFileName = ref('')
const certificateUploading = ref(false)
const approving = ref(false)

const statusMap = { 0: '待审批', 1: '审批中', 2: '已通过', 3: '已驳回' }
const statusType = { 0: 'warning', 1: 'primary', 2: 'success', 3: 'danger' }

async function fetchList(status) {
  loading.value = true
  try {
    const params = status !== undefined ? { status } : {}
    const res = await request.get('/api/admin/certificates', { params })
    list.value = res || []
  } catch (e) {
    ElMessage.error('加载列表失败')
  } finally {
    loading.value = false
  }
}

function showDetail(row) {
  currentItem.value = row
  detailVisible.value = true
}

function openApprove(row) {
  approveTarget.value = row
  certificateFileId.value = null
  certificateFileName.value = ''
  approveDialogVisible.value = true
}

function beforePdfUpload(file) {
  const isPdf = file.type === 'application/pdf' || /\.pdf$/i.test(file.name)
  if (!isPdf) {
    ElMessage.warning('请上传 PDF 文件')
  }
  return isPdf
}

async function uploadCertificatePdf(options) {
  certificateUploading.value = true
  try {
    const formData = new FormData()
    formData.append('file', options.file)
    formData.append('bizType', 'certificate-pdf')
    const result = await request.post('/api/files/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    })
    certificateFileId.value = result.id
    certificateFileName.value = result.originName || options.file.name
    ElMessage.success('证明 PDF 上传成功')
    options.onSuccess?.(result)
  } catch (e) {
    ElMessage.error(e.message || '证明 PDF 上传失败')
    options.onError?.(e)
  } finally {
    certificateUploading.value = false
  }
}

async function handleApprove() {
  if (!approveTarget.value) {
    return
  }
  if (!certificateFileId.value) {
    ElMessage.warning('请先上传证明 PDF')
    return
  }
  approving.value = true
  try {
    await request.post(`/api/admin/certificates/${approveTarget.value.id}/approve`, {
      certificateFileId: certificateFileId.value,
    })
    ElMessage.success('已通过')
    approveDialogVisible.value = false
    detailVisible.value = false
    await fetchList()
  } catch (e) {
    ElMessage.error(e.message || '操作失败')
  } finally {
    approving.value = false
  }
}

function openReject(row) {
  currentItem.value = row
  rejectReason.value = ''
  rejectDialogVisible.value = true
}

async function handleReject() {
  if (!rejectReason.value.trim()) {
    ElMessage.warning('请填写驳回原因')
    return
  }
  try {
    await request.post(`/api/admin/certificates/${currentItem.value.id}/reject`, { rejectReason: rejectReason.value.trim() })
    ElMessage.success('已驳回')
    rejectDialogVisible.value = false
    detailVisible.value = false
    await fetchList()
  } catch (e) {
    ElMessage.error(e.message || '操作失败')
  }
}

async function downloadCertificatePdf(fileId) {
  if (!fileId) {
    return
  }
  const token = localStorage.getItem('accessToken')
  if (!token) {
    ElMessage.error('未登录')
    return
  }
  const baseURL = import.meta.env.VITE_API_BASE_URL || ''
  try {
    const response = await axios.get(`${baseURL}/api/files/${fileId}/download`, {
      responseType: 'blob',
      headers: { Authorization: token },
    })
    const blobUrl = window.URL.createObjectURL(response.data)
    const link = document.createElement('a')
    link.href = blobUrl
    link.download = ''
    document.body.appendChild(link)
    link.click()
    link.remove()
    window.URL.revokeObjectURL(blobUrl)
  } catch (e) {
    ElMessage.error(e.message || '证明下载失败')
  }
}

onMounted(() => fetchList())
</script>

<template>
  <div class="page">
    <div class="page-header">
      <h2>电子证明审批</h2>
    </div>

    <el-table :data="list" v-loading="loading" stripe empty-text="暂无证明申请">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="title" label="证明标题" min-width="180" />
      <el-table-column prop="templateType" label="类型" width="120" />
      <el-table-column label="申请人" width="120">
        <template #default="{ row }">{{ row.userId }}</template>
      </el-table-column>
      <el-table-column label="状态" width="90">
        <template #default="{ row }">
          <el-tag :type="statusType[row.status] || 'info'" size="small">{{ statusMap[row.status] || '未知' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="submitTime" label="提交时间" width="180" />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link @click="showDetail(row)">详情</el-button>
          <el-button v-if="row.status === 0" type="success" link @click="openApprove(row)">通过</el-button>
          <el-button v-if="row.status === 0" type="danger" link @click="openReject(row)">驳回</el-button>
          <el-button v-if="row.certificateFileId" type="primary" link @click="downloadCertificatePdf(row.certificateFileId)">下载证明</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="detailVisible" title="证明详情" width="520px">
      <el-descriptions v-if="currentItem" :column="1" border>
        <el-descriptions-item label="标题">{{ currentItem.title }}</el-descriptions-item>
        <el-descriptions-item label="类型">{{ currentItem.templateType }}</el-descriptions-item>
        <el-descriptions-item label="理由">{{ currentItem.reason || '无' }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ statusMap[currentItem.status] }}</el-descriptions-item>
        <el-descriptions-item label="提交时间">{{ currentItem.submitTime }}</el-descriptions-item>
        <el-descriptions-item v-if="currentItem.certificateFileId" label="证明 PDF">
          <el-button type="primary" link @click="downloadCertificatePdf(currentItem.certificateFileId)">下载证明</el-button>
        </el-descriptions-item>
        <el-descriptions-item v-if="currentItem.rejectReason" label="驳回原因">{{ currentItem.rejectReason }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
        <el-button v-if="currentItem && currentItem.status === 0" type="success" @click="openApprove(currentItem)">通过</el-button>
        <el-button v-if="currentItem && currentItem.status === 0" type="danger" @click="openReject(currentItem)">驳回</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="approveDialogVisible" title="通过申请并上传证明" width="460px">
      <div class="approve-panel">
        <div class="approve-title">{{ approveTarget?.title }}</div>
        <el-upload
          :show-file-list="false"
          :http-request="uploadCertificatePdf"
          :before-upload="beforePdfUpload"
          :disabled="certificateUploading"
          accept="application/pdf,.pdf"
        >
          <el-button :loading="certificateUploading">上传证明 PDF</el-button>
        </el-upload>
        <div v-if="certificateFileName" class="file-name">{{ certificateFileName }}</div>
      </div>
      <template #footer>
        <el-button @click="approveDialogVisible = false">取消</el-button>
        <el-button type="success" :loading="approving" @click="handleApprove">确认通过</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="rejectDialogVisible" title="驳回申请" width="420px">
      <el-input v-model="rejectReason" type="textarea" :rows="4" placeholder="请填写驳回原因" />
      <template #footer>
        <el-button @click="rejectDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="handleReject">确认驳回</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.page { padding: 24px; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-header h2 { margin: 0; font-size: 20px; }
.approve-panel { display: flex; flex-direction: column; gap: 12px; }
.approve-title { font-weight: 600; color: #303133; }
.file-name { color: #409eff; font-size: 13px; }
</style>
