<template>
  <div class="app">
    <nav class="navbar" v-if="isLoggedIn">
      <div class="nav-brand">在线商城</div>
      <div class="nav-links">
        <router-link to="/products">商品</router-link>
        <router-link to="/cart">购物车</router-link>
        <router-link to="/orders">我的订单</router-link>
        <a href="#" @click.prevent="goAdmin">后台管理</a>
        <a href="#" @click.prevent="logout">退出登录</a>
      </div>
    </nav>

    <main class="app-container">
      <router-view :token="token" :userRole="userRole" />
    </main>
  </div>
</template>

<script>
export default {
  name: 'App',
  data() {
    return {
      token: null,
      userRole: null
    }
  },
  computed: {
    isLoggedIn() {
      return !!this.token
    },
    isAdmin() {
      return this.userRole === 'ADMIN'
    }
  },
  methods: {
    goAdmin() {
      if (this.isAdmin) {
        this.$router.push('/admin')
      } else {
        alert('权限不足，需要管理员权限')
      }
    },
    logout() {
      this.token = null
      this.userRole = null
      this.$router.push('/login')
    },
    setLogin(token, role) {
      this.token = token
      this.userRole = role
    }
  }
}
</script>

<style>
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
  background: #333;
  color: #fff;
}
.nav-links a {
  margin-left: 12px;
  color: #fff;
  text-decoration: none;
}
.app-container { padding: 20px }
</style>
