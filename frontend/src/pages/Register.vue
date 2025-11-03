<template>
  <div class="card" style="max-width:480px">
    <h3>注册</h3>
    <div class="row">
      <input v-model="username" placeholder="用户名" />
      <input v-model="email" placeholder="邮箱" />
      <input v-model="password" type="password" placeholder="密码" />
      <button @click="register">注册</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { api } from '../api';

const router = useRouter();
const username = ref('');
const email = ref('');
const password = ref('');

async function register() {
  await api.post('/users/register', { username: username.value, email: email.value, password: password.value });
  alert('注册成功');
  router.push('/login');
}
</script>


