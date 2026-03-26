<template>
  <div class="register-container">
    <div class="register-wrapper">
      <div class="register-illustration">
        <div class="illustration-content">
          <h1>Start Your Journey</h1>
          <p>Create an account to back projects or launch your own.</p>
        </div>
      </div>
      <div class="register-form-container">
        <div class="register-header">
          <h2>Create Account</h2>
          <p>Join FundTogether today</p>
        </div>
        
        <el-form 
          ref="registerFormRef" 
          :model="registerForm" 
          :rules="rules" 
          label-width="0"
          class="register-form"
        >
          <el-form-item prop="registerType">
            <el-radio-group v-model="registerForm.registerType" class="type-selector">
              <el-radio-button label="phone">Phone</el-radio-button>
              <el-radio-button label="email">Email</el-radio-button>
            </el-radio-group>
          </el-form-item>

          <el-form-item prop="account">
            <el-input 
              v-model="registerForm.account" 
              :placeholder="registerForm.registerType === 'phone' ? 'Phone number' : 'Email address'"
              size="large"
              class="custom-input"
            ></el-input>
          </el-form-item>

          <el-form-item prop="password">
            <el-input 
              v-model="registerForm.password" 
              type="password" 
              placeholder="Password" 
              show-password
              size="large"
              class="custom-input"
            ></el-input>
          </el-form-item>

          <el-form-item prop="confirmPassword">
            <el-input 
              v-model="registerForm.confirmPassword" 
              type="password" 
              placeholder="Confirm Password" 
              show-password
              size="large"
              class="custom-input"
            ></el-input>
          </el-form-item>

          <el-form-item prop="code">
            <div class="code-container">
              <el-input 
                v-model="registerForm.code" 
                placeholder="Verification code (test: 123456)"
                size="large"
                class="custom-input"
              ></el-input>
              <el-button type="primary" class="code-btn" @click="sendCode" :disabled="countdown > 0" size="large" plain>
                {{ countdown > 0 ? `${countdown}s` : 'Get Code' }}
              </el-button>
            </div>
          </el-form-item>

          <el-form-item prop="agreement" class="agreement-item">
            <el-checkbox v-model="registerForm.agreement">
              I agree to the <el-link type="primary" :underline="false">Terms of Service</el-link>
            </el-checkbox>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" class="submit-btn" @click="submitForm(registerFormRef)" :loading="loading" size="large">
              Create Account
            </el-button>
          </el-form-item>

          <div class="login-link">
            Already have an account? <router-link to="/login">Sign In</router-link>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { registerUser } from '../api/user'

const router = useRouter()
const registerFormRef = ref<FormInstance>()
const loading = ref(false)
const countdown = ref(0)

const registerForm = reactive({
  registerType: 'phone',
  account: '',
  password: '',
  confirmPassword: '',
  code: '',
  agreement: false
})

const validateAccount = (_rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error(`请输入${registerForm.registerType === 'phone' ? '手机号' : '邮箱'}`))
  } else {
    if (registerForm.registerType === 'phone') {
      const phoneReg = /^1[3-9]\d{9}$/
      if (!phoneReg.test(value)) {
        callback(new Error('手机号格式不正确'))
        return
      }
    } else {
      const emailReg = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/
      if (!emailReg.test(value)) {
        callback(new Error('邮箱格式不正确'))
        return
      }
    }
    callback()
  }
}

const validatePass2 = (_rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== registerForm.password) {
    callback(new Error('两次输入密码不一致!'))
  } else {
    callback()
  }
}

const validateAgreement = (_rule: any, value: any, callback: any) => {
  if (!value) {
    callback(new Error('请勾选平台用户协议'))
  } else {
    callback()
  }
}

const rules = reactive<FormRules>({
  account: [
    { required: true, validator: validateAccount, trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, validator: validatePass2, trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入验证码', trigger: 'blur' }
  ],
  agreement: [
    { validator: validateAgreement, trigger: 'change' }
  ]
})

const sendCode = () => {
  if (!registerForm.account) {
    ElMessage.warning(`请先输入${registerForm.registerType === 'phone' ? '手机号' : '邮箱'}`)
    return
  }
  ElMessage.success('验证码发送成功，测试请填写 123456')
  countdown.value = 60
  const timer = setInterval(() => {
    countdown.value--
    if (countdown.value <= 0) {
      clearInterval(timer)
    }
  }, 1000)
}

const submitForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await registerUser({
          registerType: registerForm.registerType,
          account: registerForm.account,
          password: registerForm.password,
          confirmPassword: registerForm.confirmPassword,
          code: registerForm.code
        })
        ElMessage.success('注册成功')
        router.push('/login')
      } catch (error) {
        // Error is handled in request interceptor
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: var(--bg-page);
  padding: 20px;
}

.register-wrapper {
  display: flex;
  width: 100%;
  max-width: 1000px;
  min-height: 600px;
  background: var(--bg-surface);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-xl);
  overflow: hidden;
}

.register-illustration {
  flex: 1;
  background: linear-gradient(135deg, var(--color-accent) 0%, #064E3B 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
  position: relative;
  overflow: hidden;
}

.register-illustration::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: radial-gradient(circle at 80% 20%, rgba(255,255,255,0.1) 0%, transparent 50%);
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

.register-form-container {
  flex: 1;
  padding: 60px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.register-header {
  margin-bottom: 40px;
}

.register-header h2 {
  font-family: var(--font-heading);
  font-size: 32px;
  font-weight: 800;
  color: var(--text-primary);
  margin-bottom: 8px;
}

.register-header p {
  color: var(--text-secondary);
  font-size: 16px;
}

.register-form {
  width: 100%;
}

.type-selector {
  width: 100%;
  display: flex;
}

.type-selector :deep(.el-radio-button) {
  flex: 1;
}

.type-selector :deep(.el-radio-button__inner) {
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

.code-container {
  display: flex;
  gap: 12px;
  width: 100%;
}

.code-btn {
  width: 120px;
}

.submit-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 700;
  border-radius: var(--radius-md);
  margin-top: 10px;
}

.agreement-item {
  margin-bottom: 24px;
}

.login-link {
  text-align: center;
  margin-top: 24px;
  font-size: 15px;
  color: var(--text-secondary);
}

.login-link a {
  color: var(--color-primary);
  font-weight: 600;
  text-decoration: none;
  margin-left: 4px;
}

.login-link a:hover {
  text-decoration: underline;
}

@media (max-width: 768px) {
  .register-wrapper {
    flex-direction: column;
    min-height: auto;
  }
  .register-illustration {
    padding: 40px 20px;
    text-align: center;
  }
  .illustration-content h1 {
    font-size: 32px;
  }
  .register-form-container {
    padding: 40px 20px;
  }
}
</style>
