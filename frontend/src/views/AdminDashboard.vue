<template>
  <div class="admin-dashboard">
    <el-container class="admin-container">
      <el-aside :width="isMobile ? '100%' : '200px'" class="admin-sidebar">
        <el-menu
          :default-active="activeMenu"
          :mode="isMobile ? 'horizontal' : 'vertical'"
          class="admin-menu"
          router
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409eff"
        >
          <el-menu-item index="/admin/dashboard">
            <span>数据大盘</span>
          </el-menu-item>
          <el-menu-item index="/admin/users">
            <span>用户管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/projects">
            <span>项目审计</span>
          </el-menu-item>
          <el-menu-item index="/admin/notices">
            <span>系统通知</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-main class="admin-main">
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { computed, ref, onMounted, onUnmounted } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const activeMenu = computed(() => route.path)

const isMobile = ref(false)

const checkMobile = () => {
  isMobile.value = window.innerWidth <= 768
}

onMounted(() => {
  checkMobile()
  window.addEventListener('resize', checkMobile)
})

onUnmounted(() => {
  window.removeEventListener('resize', checkMobile)
})
</script>

<style scoped>
.admin-dashboard {
  /* subtract header height to prevent scrollbar */
  height: calc(100vh - 64px);
  margin: -30px -20px; /* remove main layout padding */
}
.admin-container {
  height: 100%;
}
.admin-sidebar {
  background-color: #304156;
  transition: width 0.3s;
}
.admin-menu {
  border-right: none;
}
.admin-main {
  padding: 0;
  background-color: #f0f2f5;
  overflow-y: auto;
}

@media (max-width: 768px) {
  .admin-container {
    flex-direction: column;
  }
  .admin-sidebar {
    height: auto;
  }
  .admin-menu {
    display: flex;
    overflow-x: auto;
    white-space: nowrap;
  }
  .admin-menu::-webkit-scrollbar {
    display: none;
  }
}
</style>
