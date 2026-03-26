<template>
  <div class="register-container">
    <el-card class="register-card">
      <template #header>
        <div class="card-header">
          <span>注册 FundTogether 账号</span>
        </div>
      </template>
      
      <el-form 
        ref="registerFormRef" 
        :model="registerForm" 
        :rules="rules" 
        label-width="100px"
        class="register-form"
      >
        <el-form-item label="注册方式" prop="registerType">
          <el-radio-group v-model="registerForm.registerType">
            <el-radio label="phone">手机号注册</el-radio>
            <el-radio label="email">邮箱注册</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item :label="registerForm.registerType === 'phone' ? '手机号' : '邮箱'" prop="account">
          <el-input v-model="registerForm.account" :placeholder="'请输入' + (registerForm.registerType === 'phone' ? '手机号' : '邮箱')"></el-input>
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input v-model="registerForm.password" type="password" placeholder="请输入密码" show-password></el-input>
        </el-form-item>

        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="registerForm.confirmPassword" type="password" placeholder="请再次输入密码" show-password></el-input>
        </el-form-item>

        <el-form-item label="验证码" prop="code">
          <div class="code-container">
            <el-input v-model="registerForm.code" placeholder="请输入验证码 (测试请输入 123456)"></el-input>
            <el-button type="primary" class="code-btn" @click="sendCode" :disabled="countdown > 0">
              {{ countdown > 0 ? `${countdown}s 后重新获取` : '获取验证码' }}
            </el-button>
          </div>
        </el-form-item>

        <el-form-item prop="agreement" label-width="0" class="agreement-item">
          <el-checkbox v-model="registerForm.agreement">
            我已阅读并同意 <el-link type="primary">《平台用户协议》</el-link>
          </el-checkbox>
        </el-form-item>

        <el-form-item label-width="0">
          <el-button type="primary" class="submit-btn" @click="submitForm(registerFormRef)" :loading="loading">
            立即注册
          </el-button>
        </el-form-item>

        <div class="login-link">
          已有账号？<router-link to="/login">去登录</router-link>
        </div>
      </el-form>
    </el-card>
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
  height: 100vh;
  background-color: #f3f4f6;
}

.register-card {
  width: 500px;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
}

.card-header {
  text-align: center;
  font-size: 20px;
  font-weight: bold;
  color: #303133;
}

.register-form {
  padding: 20px 20px 0 0;
}

.code-container {
  display: flex;
  gap: 10px;
}

.code-btn {
  width: 140px;
}

.submit-btn {
  width: 100%;
}

.agreement-item {
  margin-bottom: 15px;
}

.login-link {
  text-align: right;
  margin-top: 15px;
  font-size: 14px;
}

.login-link a {
  color: #409eff;
  text-decoration: none;
}

.login-link a:hover {
  text-decoration: underline;
}
</style>
