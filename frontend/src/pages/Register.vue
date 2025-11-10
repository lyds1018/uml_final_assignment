<template>
  <div class="register-container">
    <h2>注册</h2>
    <form @submit.prevent="handleSubmit">
      <div class="form-group">
        <label>用户名：</label>
        <input type="text" v-model="username" required />
      </div>

      <div class="form-group">
        <label>密码：</label>
        <input type="password" v-model="password" required />
      </div>

      <div class="form-group">
        <label>确认密码：</label>
        <input type="password" v-model="confirmPassword" required />
      </div>

      <button type="submit">注册</button>

      <p class="login-link">
        已有账号？<router-link to="/login">立即登录</router-link>
      </p>
    </form>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'Register',
  data() {
    return {
      username: '',
      password: '',
      confirmPassword: ''
    }
  },
  methods: {
    async handleSubmit() {
      if (this.password !== this.confirmPassword) {
        alert('两次输入的密码不一致')
        return
      }

      try {
        const res = await axios.post('/api/users/register', {
          username: this.username,
          password: this.password
        })

        if (res.data.success) {
          alert('注册成功！')
          this.$router.push('/login')
        } else {
          alert(res.data.message || '注册失败')
        }
      } catch (err) {
        alert('注册失败：' + (err.response?.data?.message || err.message))
      }
    }
  }
}
</script>

<style scoped>
.register-container {
  max-width: 400px;
  margin: 50px auto;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 5px;
  background-color: #fff;
}

.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  background-color: #fff;
}

button {
  width: 100%;
  padding: 10px;
  background-color: #4CAF50;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #45a049;
}

.login-link {
  text-align: center;
  margin-top: 10px;
}

.login-link a {
  color: #4CAF50;
  text-decoration: none;
}
</style>