<template>
  <div class="login-container">
    <h2>登录</h2>
    <form @submit.prevent="handleSubmit">
      <div class="form-group">
        <label>用户名：</label>
        <input type="text" v-model="username" required />
      </div>
      <div class="form-group">
        <label>密码：</label>
        <input type="password" v-model="password" required />
      </div>
      <button type="submit">登录</button>
      <p class="register-link">
        还没有账号？<router-link to="/register">立即注册</router-link>
      </p>
    </form>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'Login',
  data() {
    return {
      username: '',
      password: ''
    }
  },
  methods: {
    async handleSubmit() {
      try {
        const response = await axios.post('/api/users/login', {
          username: this.username,
          password: this.password
        })
        if (response.data.success) {
          // 存储token和用户信息（注意 ResponseDTO 的 data 字段包含实际数据）
          const payload = response.data.data || {}
          localStorage.setItem('token', payload.token)
          localStorage.setItem('userRole', payload.role)
          // 根据角色跳转
          if (payload.role === 'ADMIN') {
            this.$router.push('/admin')
          } else {
            this.$router.push('/products')
          }
        }
      } catch (error) {
        alert('登录失败：' + error.message)
      }
    }
  }
}
</script>

<style scoped>
.login-container {
  max-width: 400px;
  margin: 50px auto;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 5px;
}

.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
}

input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

button {
  width: 100%;
  padding: 10px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #45a049;
}

.register-link {
  text-align: center;
  margin-top: 10px;
}

a {
  color: #4CAF50;
  text-decoration: none;
}
</style>
