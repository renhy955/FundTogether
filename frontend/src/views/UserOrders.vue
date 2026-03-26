<template>
  <div class="user-container">
    <main class="main-content">
      <div class="page-header">
        <h2>我的支持</h2>
      </div>

      <el-row :gutter="20" class="stats-row" style="margin-bottom: 20px;">
        <el-col :span="12">
          <el-card shadow="hover">
            <div class="stat-box">
              <div class="stat-title">累计支持金额</div>
              <div class="stat-value highlight">￥{{ stats.totalAmount || 0 }}</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card shadow="hover">
            <div class="stat-box">
              <div class="stat-title">支持项目数</div>
              <div class="stat-value">{{ stats.projectCount || 0 }}</div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      
      <el-card>
        <el-table :data="orders" style="width: 100%" v-loading="loading">
          <el-table-column prop="orderNo" label="订单号" width="200" />
          <el-table-column prop="projectId" label="项目ID" width="100" />
          <el-table-column prop="amount" label="支持金额" width="120">
            <template #default="{ row }">
              <span style="color: #f56c6c; font-weight: bold;">￥{{ row.amount }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="message" label="留言" show-overflow-tooltip />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="row.status === 1 ? 'success' : (row.status === 0 ? 'warning' : 'info')">
                {{ row.status === 1 ? '已支付' : (row.status === 0 ? '待支付' : '已取消') }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="payChannel" label="支付渠道" width="120" />
          <el-table-column prop="createdAt" label="支持时间" width="180" />
          <el-table-column label="操作" width="100" fixed="right">
            <template #default="{ row }">
              <el-button type="primary" link @click="router.push(`/projects/${row.projectId}`)">
                查看项目
              </el-button>
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
            @current-change="fetchOrders"
          />
        </div>
      </el-card>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import request from '../utils/request'
import { useRouter } from 'vue-router'

const router = useRouter()

const orders = ref<any[]>([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const stats = ref<any>({})

const fetchStats = async () => {
  try {
    const res = await request.get('/order/my-stats')
    stats.value = res.data
  } catch (error) {
    console.error(error)
  }
}

const fetchOrders = async () => {
  loading.value = true
  try {
    const res = await request.get('/order/my-list', {
      params: { current: currentPage.value, size: pageSize.value }
    })
    orders.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchStats()
  fetchOrders()
})
</script>

<style scoped>
.user-container {
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

.stat-box {
  text-align: center;
  padding: 20px;
}
.stat-title {
  font-size: 16px;
  color: #909399;
  margin-bottom: 10px;
}
.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
}
.stat-value.highlight {
  color: #f56c6c;
}
</style>
