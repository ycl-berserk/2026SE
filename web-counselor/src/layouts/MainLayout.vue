<script setup>
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

function navTo(path) {
  if (route.path !== path) {
    router.push(path)
  }
}
</script>

<template>
  <el-container class="layout">
    <el-aside class="aside" width="220px">
      <div class="logo">辅导员审批端</div>
      <el-menu :default-active="route.path" class="menu">
        <el-menu-item index="/review/pending" @click="navTo('/review/pending')">待审批</el-menu-item>
        <el-menu-item index="/review/processed" @click="navTo('/review/processed')">已处理</el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">学院学生服务平台 · 审批管理</el-header>
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.layout {
  min-height: 100vh;
}

.aside {
  background: #0f172a;
  color: #fff;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  padding: 0 20px;
  font-weight: 700;
}

.menu {
  border-right: none;
}

.menu :deep(.el-menu-item) {
  color: #dbeafe;
}

.menu :deep(.el-menu-item.is-active) {
  background: #1d4ed8;
  color: #fff;
}

.header {
  background: #fff;
  border-bottom: 1px solid #e5e7eb;
  display: flex;
  align-items: center;
  font-weight: 600;
  color: #334155;
}

.main {
  background: #f4f6fb;
}
</style>
