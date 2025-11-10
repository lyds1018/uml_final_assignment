<template>
  <div id="app">
    <!-- 顶部导航栏，仅在登录状态下显示 -->
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

    <!-- 主体内容 -->
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
  created() {
    // 页面加载时从 localStorage 初始化登录状态
    this.token = localStorage.getItem('token')
    this.userRole = localStorage.getItem('userRole')
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
      // 清空 localStorage
      localStorage.removeItem('token')
      localStorage.removeItem('userRole')
      this.$router.push('/login')
    },
    setLogin(token, role) {
      this.token = token
      this.userRole = role
      // 同步到 localStorage
      localStorage.setItem('token', token)
      localStorage.setItem('userRole', role)
    }
  }
}
</script>

<style>
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 24px;
  background-color: #333;
  color: #fff;
}
.nav-brand {
  font-size: 1.5em;
  font-weight: bold;
}
.nav-links a {
  margin-left: 12px;
  color: #fff;
  text-decoration: none;
}
.nav-links a:hover {
  text-decoration: underline;
}
.app-container {
  padding: 20px;
  min-height: calc(100vh - 60px);
}
</style>
