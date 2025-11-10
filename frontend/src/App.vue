<template>
  <div id="app">
    <!-- 顶部导航栏，仅在登录状态且当前路由允许显示时显示 -->
    <nav class="navbar" v-if="isLoggedIn && $route.meta.showNavbar !== false">
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
      <router-view :token="token" :userRole="userRole" @login-success="setLogin" />
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

    // 如果 token 不存在且不是登录页/注册页，自动跳回登录页
    if (!this.token && !['/login', '/register'].includes(this.$route.path)) {
      this.$router.replace('/login')
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
      localStorage.removeItem('token')
      localStorage.removeItem('userRole')
      this.$router.push('/login')
    },
    setLogin(token, role) {
      this.token = token
      this.userRole = role
      localStorage.setItem('token', token)
      localStorage.setItem('userRole', role)
      this.$router.push('/products') // 登录成功后跳转到商品页
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

