<template>
  <div class="login-container">
    <div class="login-wrapper">
      <div class="login-illustration">
        <div class="illustration-content">
          <h1>FundTogether</h1>
          <p>Join our community and help bring creative ideas to life.</p>
        </div>
      </div>
      <div class="login-form-container">
        <div class="login-header">
          <h2>Welcome Back</h2>
          <p>Sign in to your account to continue</p>
        </div>
        
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
              placeholder="Email or Phone number"
              size="large"
              class="custom-input"
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
              placeholder="Password" 
              show-password
              size="large"
              class="custom-input"
              @keyup.enter="submitForm(loginFormRef)"
            >
              <template #prefix>
                <el-icon><Lock /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <div class="form-actions">
            <el-checkbox v-model="loginForm.rememberMe">Remember me</el-checkbox>
            <el-link type="primary" :underline="false" @click="showResetDialog = true">Forgot password?</el-link>
          </div>

          <el-form-item>
            <el-button type="primary" class="submit-btn" size="large" @click="submitForm(loginFormRef)" :loading="loading">
              Sign In
            </el-button>
          </el-form-item>

          <div class="register-link">
            Don't have an account? <router-link to="/register">Create one</router-link>
          </div>
        </el-form>
      </div>
    </div>

    <!-- 找回密码弹窗 -->
    <el-dialog v-model="showResetDialog" title="Reset Password" width="400px" custom-class="custom-dialog">
      <el-form :model="resetForm" label-width="80px">
        <el-form-item label="Account" required>
          <el-input v-model="resetForm.account" placeholder="Enter your account" />
        </el-form-item>
        <el-form-item label="Code" required>
          <div style="display: flex; gap: 10px;">
            <el-input v-model="resetForm.code" placeholder="Verification code" />
            <el-button type="primary" plain>Get Code</el-button>
          </div>
        </el-form-item>
        <el-form-item label="New Pwd" required>
          <el-input v-model="resetForm.newPassword" type="password" show-password placeholder="Enter new password" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showResetDialog = false">Cancel</el-button>
          <el-button type="primary" @click="handleResetPassword" :loading="resetting">Reset</el-button>
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
  min-height: 100vh;
  background-color: var(--bg-page);
  padding: 20px;
}

.login-wrapper {
  display: flex;
  width: 100%;
  max-width: 1000px;
  min-height: 600px;
  background: var(--bg-surface);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-xl);
  overflow: hidden;
}

.login-illustration {
  flex: 1;
  background: linear-gradient(135deg, var(--color-primary) 0%, #312E81 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
  position: relative;
  overflow: hidden;
}

.login-illustration::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: radial-gradient(circle at 20% 80%, rgba(255,255,255,0.1) 0%, transparent 50%);
}

.illustration-content {
  position: relative;
  z-index: 1;
  max-width: 400px;
}

.illustration-content h1 {
  font-family: var(--font-heading);
  font-size: 48px;
  font-weight: 800;
  margin-bottom: 20px;
  line-height: 1.2;
  color: white;
}

.illustration-content p {
  font-size: 18px;
  line-height: 1.6;
  opacity: 0.9;
}

.login-form-container {
  flex: 1;
  padding: 60px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.login-header {
  margin-bottom: 40px;
}

.login-header h2 {
  font-family: var(--font-heading);
  font-size: 32px;
  font-weight: 800;
  color: var(--text-primary);
  margin-bottom: 8px;
}

.login-header p {
  color: var(--text-secondary);
  font-size: 16px;
}

.login-form {
  width: 100%;
}

.custom-input :deep(.el-input__wrapper) {
  padding: 8px 15px;
  background-color: var(--bg-page);
  border: 1px solid var(--border-color);
  box-shadow: none !important;
}

.custom-input :deep(.el-input__wrapper.is-focus) {
  border-color: var(--color-primary);
  box-shadow: 0 0 0 2px var(--color-primary-light) !important;
  background-color: var(--bg-surface);
}

.form-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.submit-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 700;
  border-radius: var(--radius-md);
  margin-top: 10px;
}

.register-link {
  text-align: center;
  margin-top: 24px;
  font-size: 15px;
  color: var(--text-secondary);
}

.register-link a {
  color: var(--color-primary);
  font-weight: 600;
  text-decoration: none;
  margin-left: 4px;
}

.register-link a:hover {
  text-decoration: underline;
}

@media (max-width: 768px) {
  .login-wrapper {
    flex-direction: column;
    min-height: auto;
  }
  .login-illustration {
    padding: 40px 20px;
    text-align: center;
  }
  .illustration-content h1 {
    font-size: 32px;
  }
  .login-form-container {
    padding: 40px 20px;
  }
}
</style>
