<template>
  <div class="layout-container">
    <header class="app-header">
      <div class="logo" @click="router.push('/home')" tabindex="0" @keydown.enter="router.push('/home')">FundTogether</div>
      <nav class="nav-links" aria-label="Main Navigation">
        <span v-if="userStore.token" class="user-greeting">
          欢迎, {{ userStore.userInfo?.nickname || userStore.userInfo?.account }} ({{ getRoleName(userStore.userInfo?.role) }})
        </span>
        <el-button type="primary" plain @click="router.push('/projects')">探索项目</el-button>
        <el-button v-if="!userStore.token" type="primary" @click="router.push('/login')">登录 / 注册</el-button>
        <template v-else>
          <el-button v-if="userStore.userInfo?.role === 3" type="warning" plain @click="router.push('/admin')">管理员后台</el-button>
          <el-button v-if="userStore.userInfo?.role === 2" type="success" plain @click="router.push('/sponsor/projects')">我的项目</el-button>
          <el-button type="primary" plain @click="router.push('/user/orders')">我的支持</el-button>
          <el-button type="info" plain @click="router.push('/user/messages')">消息通知</el-button>
          <el-button type="info" plain @click="router.push('/user/profile')">个人中心</el-button>
          <el-button type="danger" @click="logout">退出登录</el-button>
        </template>
      </nav>
    </header>
    <main class="app-main">
      <router-view />
    </main>
  </div>
</template>

<script setup lang="ts">
import { useUserStore } from '../store/user'
import { useRouter } from 'vue-router'
import { ElMessage, ElNotification } from 'element-plus'
import { onMounted, onUnmounted, watch } from 'vue'

const userStore = useUserStore()
const router = useRouter()

let ws: WebSocket | null = null

const initWebSocket = () => {
  if (ws) {
    ws.close()
  }
  const userId = userStore.userInfo?.id
  if (!userId) return

  ws = new WebSocket(`ws://localhost:8080/ws/${userId}`)

  ws.onopen = () => {
    console.log('WebSocket connected')
  }

  ws.onmessage = (event) => {
    try {
      const message = event.data
      ElNotification({
        title: '新消息通知',
        message: message,
        type: 'info',
        duration: 5000
      })
    } catch (e) {
      console.error('Failed to handle websocket message', e)
    }
  }

  ws.onclose = () => {
    console.log('WebSocket disconnected')
  }

  ws.onerror = (error) => {
    console.error('WebSocket error:', error)
  }
}

onMounted(() => {
  if (userStore.token) {
    initWebSocket()
  }
})

watch(() => userStore.token, (newVal) => {
  if (newVal) {
    initWebSocket()
  } else {
    if (ws) {
      ws.close()
      ws = null
    }
  }
})

onUnmounted(() => {
  if (ws) {
    ws.close()
  }
})

const getRoleName = (role: number | undefined) => {
  switch (role) {
    case 1: return '普通支持者'
    case 2: return '项目发起人'
    case 3: return '系统管理员'
    default: return '游客'
  }
}

const logout = () => {
  userStore.logout()
  ElMessage.success('已退出登录')
  router.push('/login')
}
</script>

<style scoped>
.layout-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f5f7fa;
}

.app-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 40px;
  height: 64px;
  background-color: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  position: sticky;
  top: 0;
  z-index: 100;
  flex-shrink: 0;
}

.logo {
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
  cursor: pointer;
  transition: opacity 0.2s;
  outline: none;
}

.logo:hover, .logo:focus {
  opacity: 0.8;
}

.nav-links {
  display: flex;
  align-items: center;
  gap: 15px;
  flex-wrap: wrap;
}

.user-greeting {
  font-size: 14px;
  color: #606266;
  margin-right: 10px;
}

.app-main {
  flex: 1;
  padding: 30px 20px;
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
  box-sizing: border-box;
}

/* 响应式适配 */
@media (max-width: 768px) {
  .app-header {
    padding: 0 20px;
    height: auto;
    min-height: 64px;
    flex-direction: column;
    padding-top: 10px;
    padding-bottom: 10px;
    gap: 10px;
  }
  .nav-links {
    justify-content: center;
  }
  .user-greeting {
    display: none; /* 移动端隐藏欢迎语节省空间 */
  }
  .app-main {
    padding: 15px 10px;
  }
}
</style>
