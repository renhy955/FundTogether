<template>
  <div class="user-profile-container">
    <el-card class="profile-card">
      <template #header>
        <div class="card-header">
          <span>个人资料与设置</span>
        </div>
      </template>

      <el-tabs v-model="activeTab">
        <!-- 基础信息 -->
        <el-tab-pane label="基础信息" name="profile">
          <el-form :model="profileForm" label-width="100px" style="max-width: 500px; margin-top: 20px;">
            <el-form-item label="昵称">
              <el-input v-model="profileForm.nickname" placeholder="请输入昵称" />
            </el-form-item>
            <el-form-item label="个人简介">
              <el-input v-model="profileForm.bio" type="textarea" placeholder="请输入个人简介" />
            </el-form-item>
            <el-form-item label="头像URL">
              <el-input v-model="profileForm.avatar" placeholder="请输入头像URL" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleUpdateProfile">保存修改</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 修改密码 -->
        <el-tab-pane label="修改密码" name="password">
          <el-form :model="passwordForm" label-width="100px" style="max-width: 500px; margin-top: 20px;">
            <el-form-item label="原密码" prop="oldPassword">
              <el-input v-model="passwordForm.oldPassword" type="password" show-password />
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
              <el-input v-model="passwordForm.newPassword" type="password" show-password />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleUpdatePassword">修改密码</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 支付方式 -->
        <el-tab-pane label="支付方式" name="payment">
          <div style="margin-top: 20px;">
            <el-button type="primary" @click="showPaymentDialog = true" style="margin-bottom: 20px;">绑定新支付方式</el-button>
            
            <el-table :data="paymentMethods" border style="width: 100%">
              <el-table-column prop="type" label="支付类型">
                <template #default="{ row }">
                  <el-tag v-if="row.type === 1" type="success">微信</el-tag>
                  <el-tag v-else-if="row.type === 2" type="primary">支付宝</el-tag>
                  <el-tag v-else-if="row.type === 3" type="warning">银行卡</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="account" label="账号" />
              <el-table-column prop="name" label="真实姓名" />
              <el-table-column prop="bankName" label="银行名称" />
              <el-table-column prop="createdAt" label="绑定时间" />
            </el-table>
          </div>
        </el-tab-pane>

        <!-- 实名认证 -->
        <el-tab-pane label="实名认证" name="auth">
          <div style="margin-top: 20px; max-width: 600px;">
            <el-alert
              v-if="authInfo && authInfo.status === 0"
              title="实名认证审核中"
              type="info"
              show-icon
              style="margin-bottom: 20px;"
            />
            <el-alert
              v-if="authInfo && authInfo.status === 1"
              title="实名认证已通过"
              type="success"
              show-icon
              style="margin-bottom: 20px;"
            />
            <el-alert
              v-if="authInfo && authInfo.status === 2"
              title="实名认证被驳回"
              :description="'原因: ' + authInfo.auditReason"
              type="error"
              show-icon
              style="margin-bottom: 20px;"
            />

            <el-form :model="authForm" label-width="100px" :disabled="authInfo && (authInfo.status === 0 || authInfo.status === 1)">
              <el-form-item label="真实姓名" required>
                <el-input v-model="authForm.realName" placeholder="请输入真实姓名" />
              </el-form-item>
              <el-form-item label="身份证号" required>
                <el-input v-model="authForm.idCard" placeholder="请输入身份证号" />
              </el-form-item>
              <el-form-item label="身份证正面" required>
                <el-input v-model="authForm.idCardFront" placeholder="请输入身份证正面图片URL" />
              </el-form-item>
              <el-form-item label="身份证反面" required>
                <el-input v-model="authForm.idCardBack" placeholder="请输入身份证反面图片URL" />
              </el-form-item>
              <el-form-item v-if="!authInfo || authInfo.status === 2">
                <el-button type="primary" @click="submitAuth">提交审核</el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 绑定支付方式弹窗 -->
    <el-dialog v-model="showPaymentDialog" title="绑定支付方式" width="500px">
      <el-form :model="paymentForm" label-width="100px">
        <el-form-item label="支付类型" required>
          <el-select v-model="paymentForm.type" placeholder="请选择">
            <el-option label="微信" :value="1" />
            <el-option label="支付宝" :value="2" />
            <el-option label="银行卡" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="账号" required>
          <el-input v-model="paymentForm.account" placeholder="请输入账号" />
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input v-model="paymentForm.name" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item label="银行名称" v-if="paymentForm.type === 3">
          <el-input v-model="paymentForm.bankName" placeholder="请输入银行名称" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showPaymentDialog = false">取消</el-button>
          <el-button type="primary" @click="handleBindPaymentMethod">确认绑定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { updateProfile, updatePassword, bindPaymentMethod, getPaymentMethods } from '../api/user'
import request from '../utils/request'
import { useUserStore } from '../store/user'
import { useRouter } from 'vue-router'

const activeTab = ref('profile')
const userStore = useUserStore()
const router = useRouter()

const profileForm = ref({
  nickname: userStore.userInfo?.nickname || '',
  bio: '',
  avatar: userStore.userInfo?.avatar || ''
})

const passwordForm = ref({
  oldPassword: '',
  newPassword: ''
})

const paymentMethods = ref([])
const showPaymentDialog = ref(false)
const paymentForm = ref({
  type: 1,
  account: '',
  name: '',
  bankName: ''
})

const authInfo = ref<any>(null)
const authForm = ref({
  realName: '',
  idCard: '',
  idCardFront: '',
  idCardBack: ''
})

const fetchAuthInfo = async () => {
  try {
    const res = await request.get('/user/auth-info')
    authInfo.value = res.data
    if (res.data) {
      authForm.value.realName = res.data.realName || ''
      authForm.value.idCard = res.data.idCard || ''
      authForm.value.idCardFront = res.data.idCardFront || ''
      authForm.value.idCardBack = res.data.idCardBack || ''
    }
  } catch (error) {
    console.error(error)
  }
}

const submitAuth = async () => {
  if (!authForm.value.realName || !authForm.value.idCard || !authForm.value.idCardFront || !authForm.value.idCardBack) {
    ElMessage.warning('请填写完整')
    return
  }
  try {
    await request.post('/user/auth-info', authForm.value)
    ElMessage.success('提交成功，等待审核')
    fetchAuthInfo()
  } catch (error: any) {
    console.error(error)
  }
}

const fetchPaymentMethods = async () => {
  try {
    const res: any = await getPaymentMethods()
    paymentMethods.value = res.data
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => {
  fetchPaymentMethods()
  fetchAuthInfo()
})

const handleUpdateProfile = async () => {
  try {
    await updateProfile(profileForm.value)
    ElMessage.success('个人信息修改成功')
    // Option to fetch user info again to update store
  } catch (error: any) {
    ElMessage.error(error.message || '修改失败')
  }
}

const handleUpdatePassword = async () => {
  if (!passwordForm.value.oldPassword || !passwordForm.value.newPassword) {
    ElMessage.warning('请填写完整')
    return
  }
  try {
    await updatePassword(passwordForm.value)
    ElMessage.success('密码修改成功，请重新登录')
    userStore.logout()
    router.push('/login')
  } catch (error: any) {
    ElMessage.error(error.message || '修改失败')
  }
}

const handleBindPaymentMethod = async () => {
  if (!paymentForm.value.account) {
    ElMessage.warning('请输入账号')
    return
  }
  try {
    await bindPaymentMethod(paymentForm.value)
    ElMessage.success('支付方式绑定成功')
    showPaymentDialog.value = false
    paymentForm.value = { type: 1, account: '', name: '', bankName: '' }
    fetchPaymentMethods()
  } catch (error: any) {
    ElMessage.error(error.message || '绑定失败')
  }
}
</script>

<style scoped>
.user-profile-container {
  padding: 20px;
  max-width: 1000px;
  margin: 0 auto;
}
.card-header {
  font-weight: bold;
}
</style>
