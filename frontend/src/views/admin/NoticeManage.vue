<template>
  <div class="notice-manage">
    <el-card class="notice-card">
      <template #header>
        <div class="header">
          <span>系统通知管理</span>
          <el-button type="primary" @click="openDialog()">发布新通知</el-button>
        </div>
      </template>

      <el-table :data="notices" v-loading="loading" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="标题" />
        <el-table-column prop="content" label="内容" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '已发布' : '草稿' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180" />
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button v-if="row.status === 0" type="success" size="small" @click="publishNotice(row.id)">发布</el-button>
            <el-button type="danger" size="small" @click="deleteNotice(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" title="新建系统通知" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="标题" required>
          <el-input v-model="form.title" placeholder="请输入通知标题" />
        </el-form-item>
        <el-form-item label="内容" required>
          <el-input v-model="form.content" type="textarea" :rows="4" placeholder="请输入通知内容" />
        </el-form-item>
        <el-form-item label="直接发布">
          <el-switch v-model="form.publish" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitNotice">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../utils/request'

const notices = ref<any[]>([])
const loading = ref(false)
const dialogVisible = ref(false)

const form = ref({
  title: '',
  content: '',
  publish: true
})

const fetchNotices = async () => {
  loading.value = true
  try {
    const res: any = await request.get('/admin/notices')
    notices.value = res.data?.records || res.data || []
  } catch (e) {
    ElMessage.error('获取通知列表失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchNotices()
})

const openDialog = () => {
  form.value = { title: '', content: '', publish: true }
  dialogVisible.value = true
}

const submitNotice = async () => {
  if (!form.value.title || !form.value.content) {
    ElMessage.warning('请填写标题和内容')
    return
  }
  try {
    await request.post('/admin/notices', {
      title: form.value.title,
      content: form.value.content,
      status: form.value.publish ? 1 : 0
    })
    ElMessage.success('保存成功')
    dialogVisible.value = false
    fetchNotices()
  } catch (e) {
    ElMessage.error('保存失败')
  }
}

const publishNotice = async (id: number) => {
  try {
    await request.put(`/admin/notices/${id}/publish`)
    ElMessage.success('发布成功')
    fetchNotices()
  } catch (e) {
    ElMessage.error('发布失败')
  }
}

const deleteNotice = async (id: number) => {
  ElMessageBox.confirm('确认删除该通知吗？', '提示', { type: 'warning' }).then(async () => {
    try {
      await request.delete(`/admin/notices/${id}`)
      ElMessage.success('删除成功')
      fetchNotices()
    } catch (e) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}
</script>

<style scoped>
.notice-manage {
  padding: 0;
  max-width: 1200px;
  margin: 0 auto;
  animation: fadeIn 0.4s ease-out;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

.notice-card {
  border-radius: var(--radius-lg);
  border: none;
  box-shadow: var(--shadow-sm);
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header span {
  font-size: 20px;
  font-weight: 800;
  color: var(--text-primary);
}

@media (max-width: 768px) {
  .notice-manage {
    padding: 0;
  }
  .header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }
}
</style>
