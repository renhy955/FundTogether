<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <span>登录 FundTogether</span>
        </div>
      </template>
      
      <el-form 
        ref="loginFormRef" 
        :model="loginForm" 
        :rules="rules" 
        label-width="0"
        class="login-form"
      >
        <el-form-item prop="account">
          <el-input 
            v-model="loginForm.account" 
            placeholder="请输入手机号/邮箱/账号"
            size="large"
          >
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input 
            v-model="loginForm.password" 
            type="password" 
            placeholder="请输入密码" 
            show-password
            size="large"
            @keyup.enter="submitForm(loginFormRef)"
          >
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <div class="form-actions">
          <el-checkbox v-model="loginForm.rememberMe">记住我</el-checkbox>
          <el-link type="primary" :underline="false" @click="showResetDialog = true">忘记密码？</el-link>
        </div>

        <el-form-item>
          <el-button type="primary" class="submit-btn" size="large" @click="submitForm(loginFormRef)" :loading="loading">
            登录
          </el-button>
        </el-form-item>

        <div class="register-link">
          还没有账号？<router-link to="/register">立即注册</router-link>
        </div>
      </el-form>
    </el-card>

    <!-- 找回密码弹窗 -->
    <el-dialog v-model="showResetDialog" title="找回密码" width="400px">
      <el-form :model="resetForm" label-width="80px">
        <el-form-item label="账号" required>
          <el-input v-model="resetForm.account" placeholder="请输入注册账号" />
        </el-form-item>
        <el-form-item label="验证码" required>
          <div style="display: flex; gap: 10px;">
            <el-input v-model="resetForm.code" placeholder="请输入验证码" />
            <el-button type="primary" plain>获取验证码</el-button>
          </div>
        </el-form-item>
        <el-form-item label="新密码" required>
          <el-input v-model="resetForm.newPassword" type="password" show-password placeholder="请输入新密码" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showResetDialog = false">取消</el-button>
          <el-button type="primary" @click="handleResetPassword" :loading="resetting">确认重置</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { loginUser, resetPassword } from '../api/user'
import { useUserStore } from '../store/user'

const router = useRouter()
const userStore = useUserStore()
const loginFormRef = ref<FormInstance>()
const loading = ref(false)

const showResetDialog = ref(false)
const resetting = ref(false)
const resetForm = reactive({
  account: '',
  code: '',
  newPassword: ''
})

const loginForm = reactive({
  account: '',
  password: '',
  rememberMe: false
})

const rules = reactive<FormRules>({
  account: [
    { required: true, message: '请输入账号', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
})

const submitForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const res = await loginUser({
          account: loginForm.account,
          password: loginForm.password
        })
        userStore.setUserInfo(res.data)
        ElMessage.success('登录成功')
        router.push('/home')
      } catch (error) {
        // Error is handled in request interceptor
      } finally {
        loading.value = false
      }
    }
  })
}

const handleResetPassword = async () => {
  if (!resetForm.account || !resetForm.code || !resetForm.newPassword) {
    ElMessage.warning('请填写完整信息')
    return
  }
  resetting.value = true
  try {
    await resetPassword(resetForm)
    ElMessage.success('密码重置成功，请登录')
    showResetDialog.value = false
    resetForm.account = ''
    resetForm.code = ''
    resetForm.newPassword = ''
  } catch (error: any) {
    ElMessage.error(error.message || '重置失败')
  } finally {
    resetting.value = false
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f3f4f6;
}

.login-card {
  width: 400px;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
}

.card-header {
  text-align: center;
  font-size: 20px;
  font-weight: bold;
  color: #303133;
}

.login-form {
  padding: 20px 20px 0 20px;
}

.form-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.submit-btn {
  width: 100%;
}

.register-link {
  text-align: center;
  margin-top: 15px;
  font-size: 14px;
}

.register-link a {
  color: #409eff;
  text-decoration: none;
}

.register-link a:hover {
  text-decoration: underline;
}
</style>
