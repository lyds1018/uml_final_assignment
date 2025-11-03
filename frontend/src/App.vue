<template>
  <div class="app">
    <nav class="navbar" v-if="isLoggedIn">
      <div class="nav-brand">在线商城</div>
      <div class="nav-links">
        <router-link to="/products">商品</router-link>
        <router-link to="/cart">购物车</router-link>
        <router-link to="/orders">我的订单</router-link>
        <router-link to="/admin" v-if="isAdmin">后台管理</router-link>
        <a href="#" @click.prevent="logout">退出登录</a>
      </div>
    </nav>

    <main class="app-container">
      <router-view />
    </main>
  </div>
</template>

<script>
export default {
  name: 'App',
  computed: {
    isLoggedIn() {
      return !!localStorage.getItem('token')
    },
    isAdmin() {
      return localStorage.getItem('userRole') === 'ADMIN'
    }
  },
  methods: {
    logout() {
      localStorage.removeItem('token')
      localStorage.removeItem('userRole')
      this.$router.push('/login')
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