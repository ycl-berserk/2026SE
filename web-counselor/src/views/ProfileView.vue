<script setup>
import { computed, onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { fetchCurrentUser } from '../api/auth'

const loading = ref(false)
const user = ref(readCachedUser())

const roleMap = {
  admin: { label: '管理员', type: 'danger' },
  counselor: { label: '辅导员', type: 'warning' },
  student: { label: '学生', type: 'success' },
  cadre: { label: '学生骨干', type: 'primary' },
}

const accountTypeMap = {
  admin: '管理员账号',
  counselor: '辅导员账号',
  student: '学生账号',
}

const statusMap = {
  0: { label: '待审核', type: 'warning' },
  1: { label: '正常', type: 'success' },
  2: { label: '已禁用', type: 'danger' },
}

const roleItems = computed(() => {
  const roles = user.value?.roles || []
  return roles.length ? roles.map((role) => roleMap[role] || { label: role, type: 'info' }) : [{ label: '未分配', type: 'info' }]
})

const primaryRole = computed(() => {
  if (user.value?.roles?.includes('admin')) {
    return '管理员'
  }
  if (user.value?.roles?.includes('counselor')) {
    return '辅导员'
  }
  return '用户'
})

const statusInfo = computed(() => {
  const status = user.value?.status
  if (status === undefined || status === null) {
    return null
  }
  return statusMap[status] || { label: String(status), type: 'info' }
})

const infoRows = computed(() => [
  { label: '姓名', value: user.value?.realName },
  { label: '登录账号', value: user.value?.studentNo },
  { label: '账号类型', value: accountTypeMap[user.value?.accountType] || user.value?.accountType },
  { label: '联系电话', value: user.value?.phone },
  { label: '邮箱', value: user.value?.email },
  { label: '所属班级', value: user.value?.className },
  { label: '认证身份', value: user.value?.authType },
  { label: '创建时间', value: formatDate(user.value?.createdAt) },
  { label: '更新时间', value: formatDate(user.value?.updatedAt) },
])

function readCachedUser() {
  try {
    const saved = localStorage.getItem('currentUser')
    return saved ? JSON.parse(saved) : null
  } catch (_) {
    return null
  }
}

function emptyText(value) {
  return value === undefined || value === null || value === '' ? '暂无' : value
}

function formatDate(value) {
  if (!value) {
    return ''
  }
  const date = new Date(value)
  if (Number.isNaN(date.getTime())) {
    return value
  }
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
  })
}

async function loadProfile() {
  loading.value = true
  try {
    const currentUser = await fetchCurrentUser()
    user.value = currentUser
    localStorage.setItem('currentUser', JSON.stringify(currentUser))
  } catch (error) {
    ElMessage.error(error.message || '获取个人信息失败')
  } finally {
    loading.value = false
  }
}

onMounted(loadProfile)
</script>

<template>
  <div class="profile-page" v-loading="loading">
    <section class="profile-summary">
      <div class="avatar-wrap">
        <el-avatar :size="72" :src="user?.avatarUrl" icon="UserFilled" />
      </div>
      <div class="summary-main">
        <div class="name-row">
          <h3>{{ emptyText(user?.realName) }}</h3>
          <el-tag v-if="statusInfo" :type="statusInfo.type" size="small">{{ statusInfo.label }}</el-tag>
        </div>
        <p>{{ primaryRole }} · {{ emptyText(user?.studentNo) }}</p>
        <div class="role-tags">
          <el-tag v-for="role in roleItems" :key="role.label" :type="role.type" size="small" effect="light">
            {{ role.label }}
          </el-tag>
        </div>
      </div>
    </section>

    <el-card shadow="never" class="profile-card">
      <template #header>
        <div class="section-title">基本信息</div>
      </template>
      <div class="info-grid">
        <div v-for="item in infoRows" :key="item.label" class="info-item">
          <span class="info-label">{{ item.label }}</span>
          <span class="info-value">{{ emptyText(item.value) }}</span>
        </div>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.profile-page {
  max-width: 1040px;
}

.profile-summary {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 28px 32px;
  margin-bottom: 16px;
  background: #fff;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
}

.avatar-wrap {
  flex-shrink: 0;
}

.summary-main {
  min-width: 0;
}

.name-row {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
  margin-bottom: 6px;
}

.name-row h3 {
  margin: 0;
  color: #1f2937;
  font-size: 24px;
  font-weight: 700;
}

.summary-main p {
  margin: 0 0 12px;
  color: #606266;
  font-size: 14px;
}

.role-tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.profile-card {
  border-radius: 8px;
}

.section-title {
  color: #1f2937;
  font-size: 16px;
  font-weight: 600;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  border-top: 1px solid #ebeef5;
  border-left: 1px solid #ebeef5;
}

.info-item {
  display: grid;
  grid-template-columns: 132px minmax(0, 1fr);
  min-height: 54px;
  border-right: 1px solid #ebeef5;
  border-bottom: 1px solid #ebeef5;
}

.info-label,
.info-value {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  line-height: 1.5;
}

.info-label {
  color: #606266;
  background: #fafafa;
  font-weight: 500;
}

.info-value {
  min-width: 0;
  color: #1f2937;
  overflow-wrap: anywhere;
}

@media (max-width: 768px) {
  .profile-summary {
    align-items: flex-start;
    padding: 20px;
  }

  .info-grid {
    grid-template-columns: 1fr;
  }
}
</style>
