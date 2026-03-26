<template>
  <div class="stats-dashboard">
    <div class="header">
      <h2>数据统计大盘</h2>
      <el-button type="primary" @click="exportData">导出数据 (CSV)</el-button>
    </div>
    <el-row :gutter="20" class="summary-cards">
      <el-col :xs="24" :sm="12" :md="6">
        <el-card shadow="hover">
          <div class="card-title">总用户数</div>
          <div class="card-value">{{ stats.totalUsers }}</div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <el-card shadow="hover">
          <div class="card-title">总项目数</div>
          <div class="card-value">{{ stats.totalProjects }}</div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <el-card shadow="hover">
          <div class="card-title">总筹款金额</div>
          <div class="card-value">￥{{ stats.totalAmount }}</div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <el-card shadow="hover">
          <div class="card-title">成功项目数</div>
          <div class="card-value">{{ stats.successfulProjects }}</div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20" class="charts-row">
      <el-col :xs="24" :md="12">
        <el-card>
          <div ref="pieChartRef" class="chart"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :md="12">
        <el-card>
          <div ref="barChartRef" class="chart"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import request from '../../utils/request'
import { ElMessage } from 'element-plus'

const pieChartRef = ref<HTMLElement | null>(null)
const barChartRef = ref<HTMLElement | null>(null)

const stats = ref({
  totalUsers: 120,
  totalProjects: 45,
  totalAmount: 156000,
  successfulProjects: 12
})

const exportData = () => {
  const csvContent = `data:text/csv;charset=utf-8,Type,Value
Total Users,${stats.value.totalUsers}
Total Projects,${stats.value.totalProjects}
Total Amount,${stats.value.totalAmount}
Successful Projects,${stats.value.successfulProjects}`
  
  const encodedUri = encodeURI(csvContent)
  const link = document.createElement("a")
  link.setAttribute("href", encodedUri)
  link.setAttribute("download", "stats_export.csv")
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
  ElMessage.success('数据导出成功')
}

onMounted(async () => {
  try {
    const res: any = await request.get('/admin/stats')
    if (res.data) {
      stats.value = res.data
    }
  } catch (error) {
    console.warn('Using mock data for stats dashboard')
  } finally {
    initCharts()
  }
})

const initCharts = () => {
  const chartInstances: echarts.ECharts[] = []

  if (pieChartRef.value) {
    const pieChart = echarts.init(pieChartRef.value)
    pieChart.setOption({
      title: { text: '项目分类占比', left: 'center' },
      tooltip: { trigger: 'item' },
      legend: { bottom: '0%', textStyle: { fontSize: 10 } },
      series: [
        {
          name: '分类',
          type: 'pie',
          radius: ['40%', '70%'],
          avoidLabelOverlap: false,
          label: { show: false },
          data: [
            { value: 15, name: '科技' },
            { value: 10, name: '公益' },
            { value: 8, name: '农业' },
            { value: 7, name: '文化' },
            { value: 5, name: '教育' }
          ]
        }
      ]
    })
    chartInstances.push(pieChart)
  }

  if (barChartRef.value) {
    const barChart = echarts.init(barChartRef.value)
    barChart.setOption({
      title: { text: '近6个月筹款趋势' },
      tooltip: { trigger: 'axis' },
      grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
      xAxis: {
        type: 'category',
        data: ['1月', '2月', '3月', '4月', '5月', '6月']
      },
      yAxis: { type: 'value' },
      series: [
        {
          data: [12000, 20000, 15000, 80000, 70000, 110000],
          type: 'bar',
          name: '金额 (元)',
          itemStyle: { color: '#409EFF' }
        }
      ]
    })
    chartInstances.push(barChart)
  }

  window.addEventListener('resize', () => {
    chartInstances.forEach(chart => chart.resize())
  })
}
</script>

<style scoped>
.stats-dashboard {
  padding: 20px;
}
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
.summary-cards {
  margin-bottom: 20px;
}
.summary-cards .el-col {
  margin-bottom: 20px;
}
.card-title {
  font-size: 14px;
  color: #909399;
}
.card-value {
  font-size: 24px;
  font-weight: bold;
  margin-top: 10px;
}
.charts-row .el-col {
  margin-bottom: 20px;
}
.chart {
  height: 400px;
}

@media (max-width: 768px) {
  .stats-dashboard {
    padding: 10px;
  }
  .header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  .header h2 {
    font-size: 18px;
    margin: 0;
  }
  .chart {
    height: 300px;
  }
}
</style>
