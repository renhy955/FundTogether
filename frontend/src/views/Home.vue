<template>
  <div class="home-container">
    <main class="main-content">
      <div class="hero-section">
        <h1 class="hero-title">发现并支持你喜爱的创新项目</h1>
        <p class="hero-subtitle">在这里，每一个微小的想法都能汇聚成改变世界的力量。</p>
      </div>

      <div class="content-layout">
        <!-- Main Project List -->
        <div class="project-list-section">
          
          <!-- Recommended Section -->
          <div class="section-header" v-if="recommendedProjects.length > 0">
            <h2>为你推荐</h2>
          </div>
          <div class="project-grid" v-loading="recommendLoading" v-if="recommendedProjects.length > 0">
            <el-card 
              v-for="item in recommendedProjects" 
              :key="'rec-' + item.id" 
              class="project-card recommend-card"
              @click="goToDetail(item.id)"
              shadow="hover"
            >
              <div class="recommend-badge">推荐</div>
              <img :src="item.coverImage" class="project-image" />
              <div class="project-info">
                <h3 class="project-title">{{ item.title }}</h3>
                <p class="project-desc">{{ item.summary }}</p>
                
                <div class="progress-container">
                  <el-progress 
                    :percentage="Math.min(100, Math.round((item.currentAmount / item.targetAmount) * 100))" 
                    :stroke-width="8"
                    :color="customColors"
                  />
                </div>
                
                <div class="project-stats">
                  <div class="stat-item">
                    <span class="stat-value">￥{{ item.currentAmount }}</span>
                    <span class="stat-label">已筹金额</span>
                  </div>
                  <div class="stat-item">
                    <span class="stat-value">{{ item.supporterCount }}</span>
                    <span class="stat-label">支持人数</span>
                  </div>
                </div>
              </div>
            </el-card>
          </div>

          <div class="section-header" style="margin-top: 20px;">
            <h2>全部项目</h2>
            <div class="filter-bar">
              <el-input v-model="keyword" placeholder="搜索项目标题或简介" clearable @clear="fetchProjects" @keyup.enter="fetchProjects" style="width: 200px; margin-right: 10px;">
                <template #prefix>
                  <el-icon><Search /></el-icon>
                </template>
              </el-input>
              <el-select v-model="categoryId" placeholder="全部分类" clearable @change="fetchProjects" style="width: 120px; margin-right: 10px;">
                <el-option label="科技创新" :value="1" />
                <el-option label="公益助农" :value="2" />
                <el-option label="文化艺术" :value="3" />
              </el-select>
              <el-select v-model="amountRange" placeholder="目标金额" clearable @change="handleAmountChange" style="width: 120px; margin-right: 10px;">
                <el-option label="1万以下" value="0-10000" />
                <el-option label="1万-5万" value="10000-50000" />
                <el-option label="5万-10万" value="50000-100000" />
                <el-option label="10万以上" value="100000-" />
              </el-select>
              <el-select v-model="progressRange" placeholder="筹款进度" clearable @change="handleProgressChange" style="width: 120px; margin-right: 10px;">
                <el-option label="50%以下" value="0-50" />
                <el-option label="50%-100%" value="50-100" />
                <el-option label="已达标(100%+)" value="100-" />
              </el-select>
              <el-select v-model="sortType" placeholder="排序方式" @change="fetchProjects" style="width: 150px">
                <el-option label="默认排序(热度)" :value="1" />
                <el-option label="最新上线" :value="2" />
                <el-option label="金额最高" :value="3" />
              </el-select>
            </div>
          </div>

          <div class="project-grid" v-loading="loading">
            <el-card 
              v-for="item in projects" 
              :key="item.id" 
              class="project-card"
              @click="goToDetail(item.id)"
              shadow="hover"
            >
              <img :src="item.coverImage" class="project-image" />
              <div class="project-info">
                <h3 class="project-title">{{ item.title }}</h3>
                <p class="project-desc">{{ item.summary }}</p>
                
                <div class="progress-container">
                  <el-progress 
                    :percentage="Math.min(100, Math.round((item.currentAmount / item.targetAmount) * 100))" 
                    :stroke-width="8"
                    :color="customColors"
                  />
                </div>
                
                <div class="project-stats">
                  <div class="stat-item">
                    <span class="stat-value">￥{{ item.currentAmount }}</span>
                    <span class="stat-label">已筹金额</span>
                  </div>
                  <div class="stat-item">
                    <span class="stat-value">{{ item.supporterCount }}</span>
                    <span class="stat-label">支持人数</span>
                  </div>
                </div>
              </div>
            </el-card>
          </div>

          <div class="pagination" v-if="total > 0">
            <el-pagination
              background
              layout="prev, pager, next"
              :total="total"
              :page-size="pageSize"
              v-model:current-page="currentPage"
              @current-change="fetchProjects"
            />
          </div>
          <el-empty v-else-if="!loading" description="暂无在线项目" />
        </div>

        <!-- Sidebar / Leaderboard -->
        <aside class="sidebar">
          <el-card class="leaderboard-card" shadow="never">
            <template #header>
              <div class="leaderboard-header">
                <h3><el-icon><Trophy /></el-icon> 热门项目排行榜</h3>
                <el-select v-model="leaderboardSort" size="small" style="width: 100px" @change="fetchLeaderboard">
                  <el-option label="按热度" :value="1" />
                  <el-option label="按金额" :value="3" />
                </el-select>
              </div>
            </template>
            <div class="leaderboard-list" v-loading="leaderboardLoading">
              <div 
                v-for="(item, index) in leaderboardProjects" 
                :key="item.id"
                class="leaderboard-item"
                @click="goToDetail(item.id)"
              >
                <div class="rank-badge" :class="`rank-${index + 1}`">{{ index + 1 }}</div>
                <div class="lb-info">
                  <div class="lb-title">{{ item.title }}</div>
                  <div class="lb-stats">
                    <span>已筹: ￥{{ item.currentAmount }}</span>
                    <span style="margin-left: 10px;">热度: {{ item.heat || 0 }}</span>
                  </div>
                </div>
              </div>
            </div>
          </el-card>
        </aside>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Search, Trophy } from '@element-plus/icons-vue'
import request from '../utils/request'

const router = useRouter()

const customColors = [
  { color: '#f56c6c', percentage: 20 },
  { color: '#e6a23c', percentage: 40 },
  { color: '#5cb87a', percentage: 60 },
  { color: '#1989fa', percentage: 80 },
  { color: '#6f7ad3', percentage: 100 },
]

const projects = ref<any[]>([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(12)
const total = ref(0)
const sortType = ref(1)
const keyword = ref('')
const categoryId = ref<number | null>(null)
const amountRange = ref('')
const progressRange = ref('')

const minAmount = ref<number | null>(null)
const maxAmount = ref<number | null>(null)
const minProgress = ref<number | null>(null)
const maxProgress = ref<number | null>(null)

// Recommended
const recommendedProjects = ref<any[]>([])
const recommendLoading = ref(false)

const fetchRecommended = async () => {
  recommendLoading.value = true
  try {
    const res = await request.get('/projects/recommend')
    recommendedProjects.value = res.data
  } catch (error) {
    console.error(error)
  } finally {
    recommendLoading.value = false
  }
}

// Leaderboard
const leaderboardProjects = ref<any[]>([])
const leaderboardLoading = ref(false)
const leaderboardSort = ref(1)

const handleAmountChange = (val: string) => {
  if (!val) {
    minAmount.value = null
    maxAmount.value = null
  } else {
    const parts = val.split('-')
    minAmount.value = parts[0] ? Number(parts[0]) : null
    maxAmount.value = parts[1] ? Number(parts[1]) : null
  }
  fetchProjects()
}

const handleProgressChange = (val: string) => {
  if (!val) {
    minProgress.value = null
    maxProgress.value = null
  } else {
    const parts = val.split('-')
    minProgress.value = parts[0] ? Number(parts[0]) : null
    maxProgress.value = parts[1] ? Number(parts[1]) : null
  }
  fetchProjects()
}

const fetchLeaderboard = async () => {
  leaderboardLoading.value = true
  try {
    const res = await request.get('/public/project/list', {
      params: {
        current: 1,
        size: 5,
        sortType: leaderboardSort.value
      }
    })
    leaderboardProjects.value = res.data.records
  } catch (error) {
    console.error(error)
  } finally {
    leaderboardLoading.value = false
  }
}

const fetchProjects = async () => {
  loading.value = true
  try {
    const res = await request.get('/public/project/list', {
      params: {
        current: currentPage.value,
        size: pageSize.value,
        sortType: sortType.value,
        keyword: keyword.value || undefined,
        categoryId: categoryId.value || undefined,
        minAmount: minAmount.value !== null ? minAmount.value : undefined,
        maxAmount: maxAmount.value !== null ? maxAmount.value : undefined,
        minProgress: minProgress.value !== null ? minProgress.value : undefined,
        maxProgress: maxProgress.value !== null ? maxProgress.value : undefined
      }
    })
    projects.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const goToDetail = (id: number) => {
  router.push(`/projects/${id}`)
}

onMounted(() => {
  fetchRecommended()
  fetchProjects()
  fetchLeaderboard()
})
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  background-color: #f5f7fa;
}

.hero-section {
  text-align: center;
  padding: 60px 20px;
  background: linear-gradient(135deg, #409eff 0%, #3a8ee6 100%);
  color: white;
  border-radius: 12px;
  margin-bottom: 30px;
  box-shadow: 0 4px 15px rgba(64, 158, 255, 0.3);
}

.hero-title {
  font-size: 36px;
  margin: 0 0 15px 0;
}

.hero-subtitle {
  font-size: 18px;
  opacity: 0.9;
  margin: 0;
}

.main-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 30px 20px;
}

.content-layout {
  display: flex;
  gap: 30px;
}

.project-list-section {
  flex: 1;
  min-width: 0;
}

.sidebar {
  width: 300px;
  flex-shrink: 0;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-header h2 {
  margin: 0;
  font-size: 24px;
  color: #303133;
}

.filter-bar {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.project-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.project-card {
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
  border-radius: 8px;
  overflow: hidden;
  position: relative;
}

.recommend-card {
  border: 1px solid #e6a23c;
  box-shadow: 0 4px 12px rgba(230, 162, 60, 0.15);
}

.recommend-badge {
  position: absolute;
  top: 10px;
  right: -25px;
  background-color: #e6a23c;
  color: white;
  padding: 4px 25px;
  font-size: 12px;
  font-weight: bold;
  transform: rotate(45deg);
  z-index: 1;
}

.project-card:hover {
  transform: translateY(-5px);
}

.project-image {
  width: 100%;
  height: 180px;
  object-fit: cover;
}

.project-info {
  padding: 15px;
}

.project-title {
  font-size: 16px;
  font-weight: bold;
  margin: 0 0 10px 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  color: #303133;
}

.project-desc {
  font-size: 13px;
  color: #606266;
  margin: 0 0 15px 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.progress-container {
  margin-bottom: 15px;
}

.project-stats {
  display: flex;
  justify-content: space-between;
}

.stat-item {
  display: flex;
  flex-direction: column;
}

.stat-value {
  font-size: 15px;
  font-weight: bold;
  color: #303133;
}

.stat-label {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 40px;
}

/* Leaderboard */
.leaderboard-card {
  border-radius: 8px;
}

.leaderboard-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.leaderboard-header h3 {
  margin: 0;
  font-size: 16px;
  color: #303133;
  display: flex;
  align-items: center;
  gap: 5px;
}

.leaderboard-item {
  display: flex;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #ebeef5;
  cursor: pointer;
  transition: background-color 0.2s;
}

.leaderboard-item:hover {
  background-color: #f5f7fa;
}

.leaderboard-item:last-child {
  border-bottom: none;
}

.rank-badge {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background-color: #909399;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: bold;
  margin-right: 12px;
  flex-shrink: 0;
}

.rank-1 { background-color: #f56c6c; }
.rank-2 { background-color: #e6a23c; }
.rank-3 { background-color: #e6a23c; opacity: 0.8; }

.lb-info {
  flex: 1;
  min-width: 0;
}

.lb-title {
  font-size: 14px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.lb-stats {
  font-size: 12px;
  color: #909399;
}

/* Responsive adjustments */
@media (max-width: 992px) {
  .content-layout {
    flex-direction: column;
  }
  .sidebar {
    width: 100%;
  }
}

@media (max-width: 768px) {
  .hero-section {
    padding: 40px 15px;
  }
  .hero-title {
    font-size: 28px;
  }
  .hero-subtitle {
    font-size: 14px;
  }
  .section-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }
  .filter-bar {
    width: 100%;
  }
  .filter-bar .el-input,
  .filter-bar .el-select {
    width: 100% !important;
    margin-right: 0 !important;
  }
  .project-grid {
    grid-template-columns: 1fr;
  }
  .project-image {
    height: 200px;
  }
}
</style>
