<template>
  <div class="card" style="max-width:420px">
    <h3>登录</h3>
    <div class="row">
      <input v-model="username" placeholder="用户名" />
      <input v-model="password" type="password" placeholder="密码" />
      <button @click="login">登录</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { api, type ApiResponse } from '../api';

const router = useRouter();
const username = ref('');
const password = ref('');

async function login() {
  const r = await api.post<ApiResponse<any>>('/users/login', { username: username.value, password: password.value });
  if (r.data.success) {
    localStorage.setItem('userId', String(r.data.data.id));
    alert('登录成功');
    router.push('/');
  } else {
    alert(r.data.message || '登录失败');
  }
}
</script>


