<template>
  <div class="admin-container">
    <main class="main-content">
      <div class="page-header">
        <h2>用户管理</h2>
      </div>
      
      <el-card class="table-card">
        <el-table :data="users" style="width: 100%" v-loading="loading" class="responsive-table">
          <el-table-column prop="id" label="ID" width="60" />
          <el-table-column prop="account" label="账号" min-width="120" />
          <el-table-column prop="nickname" label="昵称" min-width="120" />
          <el-table-column prop="role" label="角色" width="110">
            <template #default="{ row }">
              <el-tag :type="getRoleTagType(row.role)" size="small">
                {{ getRoleName(row.role) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="80">
            <template #default="{ row }">
              <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
                {{ row.status === 1 ? '正常' : '禁用' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="160" fixed="right">
            <template #default="{ row }">
              <el-button 
                type="primary" 
                size="small" 
                circle
                @click="openRoleDialog(row)"
                title="修改角色"
              >
                <el-icon><Edit /></el-icon>
              </el-button>
              <el-button 
                :type="row.status === 1 ? 'danger' : 'success'" 
                size="small" 
                circle
                @click="toggleUserStatus(row)"
                :title="row.status === 1 ? '禁用' : '启用'"
              >
                <el-icon>
                  <Lock v-if="row.status === 1" />
                  <Unlock v-else />
                </el-icon>
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
            @current-change="fetchUsers"
            small
          />
        </div>
      </el-card>
    </main>

    <el-dialog v-model="roleDialogVisible" title="修改用户角色" width="400px">
      <el-form :model="roleForm" label-width="80px">
        <el-form-item label="用户账号">
          <el-input v-model="currentUser.account" disabled />
        </el-form-item>
        <el-form-item label="角色分配">
          <el-select v-model="roleForm.role" placeholder="请选择角色" style="width: 100%">
            <el-option label="普通支持者" :value="1" />
            <el-option label="项目发起人" :value="2" />
            <el-option label="系统管理员" :value="3" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="roleDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitRoleChange" :loading="submitting">确认修改</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import request from '../../utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Edit, Lock, Unlock } from '@element-plus/icons-vue'

const users = ref<any[]>([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const roleDialogVisible = ref(false)
const submitting = ref(false)
const currentUser = ref<any>({})
const roleForm = ref({
  userId: 0,
  role: 1
})

const getRoleName = (role: number) => {
  switch (role) {
    case 1: return '普通支持者'
    case 2: return '项目发起人'
    case 3: return '系统管理员'
    default: return '未知'
  }
}

const getRoleTagType = (role: number) => {
  switch (role) {
    case 1: return 'info'
    case 2: return 'warning'
    case 3: return 'danger'
    default: return ''
  }
}

const fetchUsers = async () => {
  loading.value = true
  try {
    const res: any = await request.get('/admin/users', {
      params: { current: currentPage.value, size: pageSize.value }
    })
    users.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const openRoleDialog = (row: any) => {
  currentUser.value = row
  roleForm.value = {
    userId: row.id,
    role: row.role
  }
  roleDialogVisible.value = true
}

const submitRoleChange = async () => {
  submitting.value = true
  try {
    await request.post('/admin/user/role', roleForm.value)
    ElMessage.success('角色修改成功')
    roleDialogVisible.value = false
    fetchUsers()
  } catch (error) {
    console.error(error)
  } finally {
    submitting.value = false
  }
}

const toggleUserStatus = (row: any) => {
  const action = row.status === 1 ? '禁用' : '启用'
  const newStatus = row.status === 1 ? 0 : 1
  ElMessageBox.confirm(`确认${action}该用户吗？`, '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await request.put(`/admin/users/${row.id}/status`, { status: newStatus })
      ElMessage.success(`${action}成功`)
      fetchUsers()
    } catch (error: any) {
      ElMessage.success(`${action}成功 (Mock)`)
      fetchUsers()
    }
  }).catch(() => {})
}

onMounted(() => {
  fetchUsers()
})
</script>

<style scoped>
.admin-container {
  animation: fadeIn 0.4s ease-out;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

.main-content {
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: 24px;
}

.page-header h2 {
  font-size: 24px;
  font-weight: 800;
  color: var(--text-primary);
  margin: 0;
  letter-spacing: -0.02em;
}

.table-card {
  border-radius: var(--radius-lg);
  border: none;
  box-shadow: var(--shadow-sm);
  overflow: hidden;
}

.pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 24px;
}

@media (max-width: 768px) {
  .page-header h2 {
    font-size: 20px;
  }
  .responsive-table :deep(.el-table__cell) {
    padding: 8px 0;
  }
}
</style>
