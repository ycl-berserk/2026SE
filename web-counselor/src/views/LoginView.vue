<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

function loginAsDemoCounselor() {
  localStorage.removeItem('accessToken')
  router.push('/review/pending')
}
</script>

<template>
  <div class="page">
    <el-card class="card">
      <h2>辅导员审批端登录</h2>
      <p class="desc">开发环境可直接进入审批页面（后端启用自动登录）。</p>
      <el-button type="primary" @click="loginAsDemoCounselor">进入审批系统</el-button>
    </el-card>
  </div>
</template>

<style scoped>
.page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
}

.card {
  width: 420px;
}

.desc {
  color: #64748b;
  margin-bottom: 20px;
}
</style>
