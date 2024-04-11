<script setup lang="ts">
import { onBeforeMount, ref } from 'vue'
import { loginApi, refreshCaptchaApi } from '@/api/userApi'
import type { LoginRequest } from '@/model/request'
import { setAccessToken } from '@/utils/cacheManager'
import { useRouter } from 'vue-router'

type LoginForm = {
  phoneNo: string,
  password: string,
  captcha: string
}

const router = useRouter()

const loginForm = ref<LoginForm>({
  phoneNo: '',
  password: '',
  captcha: ''
})
const captchaBase64 = ref<string>('')
const token = ref<string>('')

function refreshCaptcha() {
  refreshCaptchaApi({
    captchaWidth: 100,
    captchaHeight: 40
  }).then(response => {
    captchaBase64.value = 'data:image/png;base64,' + response.resultBody.captchaImage
    token.value = response.resultBody.token
  }).catch(() => {})
}

function userLogin() {
  const loginRequest: LoginRequest = {
    phoneNo: loginForm.value.phoneNo,
    password: loginForm.value.password,
    captcha: loginForm.value.captcha,
    token: token.value
  }
  loginApi(loginRequest)
    .then(response => {
      setAccessToken(response.resultBody.token)
      router.push({
        path: '/'
      })
    })
    .catch(() => {})
}

onBeforeMount(() => {
  refreshCaptcha()
})

</script>

<template>
  <div>
    <el-form :model="loginForm">
      <el-row class="mt-[30rem]">
        <el-col :span="8"></el-col>
        <el-col :span="8">
          <el-form-item prop="username">
            <el-input v-model="loginForm.phoneNo"/>
          </el-form-item>
        </el-col>
        <el-col :span="8"></el-col>
      </el-row>
      <el-row>
        <el-col :span="8"></el-col>
        <el-col :span="8">
          <el-form-item prop="password">
            <el-input v-model="loginForm.password"/>
          </el-form-item>
        </el-col>
        <el-col :span="8"></el-col>
      </el-row>
      <el-row>
        <el-col :span="8"></el-col>
        <el-col :span="8">
          <el-form-item prop="captcha">
            <el-input v-model="loginForm.captcha"/>
          </el-form-item>
        </el-col>
        <el-col :span="3">
          <img
            class="w-3/5 h-[5rem] ml-[1rem] mt-[1rem] rounded-[0.5rem] hover:cursor-pointer"
            :src="captchaBase64"
            alt="Captcha"
            @click="refreshCaptcha"/>
        </el-col>
        <el-col :span="5"></el-col>
      </el-row>
    </el-form>
    <el-row>
      <el-col :span="8"></el-col>
      <el-col :span="8">
        <div
          class="flex w-full h-[5rem] mt-[1rem] items-center bg-blue-200 rounded-[1rem] hover:cursor-pointer hover:bg-blue-300"
          @click="userLogin">
          <div class="w-full text-center text-[2rem]">Login</div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
@import 'userLogin.css';
</style>