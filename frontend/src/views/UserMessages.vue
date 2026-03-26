<template>
  <div class="messages-container">
    <main class="main-content">
      <div class="page-header">
        <h2>消息通知</h2>
      </div>
      
      <el-card>
        <el-tabs v-model="activeTab">
          <el-tab-pane label="全部消息" name="all">
            <el-table :data="messages" style="width: 100%" v-loading="loading">
              <el-table-column prop="type" label="类型" width="120">
                <template #default="{ row }">
                  <el-tag :type="getTypeTag(row.type)">{{ getTypeName(row.type) }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="title" label="标题" width="200" />
              <el-table-column prop="content" label="内容" show-overflow-tooltip />
              <el-table-column prop="createdAt" label="时间" width="180" />
              <el-table-column prop="isRead" label="状态" width="100">
                <template #default="{ row }">
                  <el-tag :type="row.isRead ? 'info' : 'danger'">{{ row.isRead ? '已读' : '未读' }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="120" fixed="right">
                <template #default="{ row }">
                  <el-button v-if="!row.isRead" type="primary" link @click="markAsRead(row.id)">标记已读</el-button>
                  <el-button v-if="row.relatedId && row.type === 2" type="primary" link @click="router.push(`/projects/${row.relatedId}`)">查看项目</el-button>
                </template>
              </el-table-column>
            </el-table>

            <div class="pagination">
              <el-pagination
                background
                layout="prev, pager, next"
                :total="total"
                :page-size="pageSize"
                v-model:current-page="currentPage"
                @current-change="fetchMessages"
              />
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-card>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import request from '../utils/request'
import { useRouter } from 'vue-router'

const router = useRouter()
const activeTab = ref('all')
const messages = ref<any[]>([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const getTypeTag = (type: number) => {
  const map: any = { 1: 'warning', 2: 'success', 3: 'info', 4: 'primary' }
  return map[type] || 'info'
}

const getTypeName = (type: number) => {
  const map: any = { 1: '系统公告', 2: '项目动态', 3: '评论回复', 4: '订单通知' }
  return map[type] || '其他'
}

const fetchMessages = async () => {
  loading.value = true
  try {
    const res = await request.get('/message/my-list', {
      params: { current: currentPage.value, size: pageSize.value }
    })
    messages.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const markAsRead = async (id: number) => {
  try {
    await request.put(`/message/${id}/read`)
    fetchMessages()
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => {
  fetchMessages()
})
</script>

<style scoped>
.messages-container {
  min-height: 100vh;
  background-color: #f5f7fa;
}
.main-content {
  max-width: 1200px;
  margin: 30px auto;
}
.page-header {
  margin-bottom: 20px;
}
.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>