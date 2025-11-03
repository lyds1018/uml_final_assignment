<template>
  <div class="container">
    <header class="nav">
      <h1>简易在线购物系统</h1>
      <nav>
        <router-link to="/">商品</router-link>
        <router-link to="/cart">购物车</router-link>
        <router-link to="/orders">我的订单</router-link>
        <router-link to="/login">登录</router-link>
        <router-link to="/register">注册</router-link>
        <router-link to="/admin">管理</router-link>
      </nav>
    </header>
    <main>
      <router-view />
    </main>
  </div>
  
</template>

<script setup lang="ts">
</script>

<style>
body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial; margin:0; }
.container { max-width: 1100px; margin: 0 auto; padding: 16px; }
.nav { display:flex; align-items:center; justify-content:space-between; gap:12px; }
.nav nav { display:flex; gap:12px; }
a { text-decoration:none; color:#2563eb; }
a.router-link-active { font-weight: 600; }
button { padding: 6px 10px; border: 1px solid #d1d5db; background: #f9fafb; border-radius: 6px; cursor: pointer; }
button:hover { background:#f3f4f6; }
input { padding: 6px 8px; border: 1px solid #d1d5db; border-radius: 6px; }
.card { border: 1px solid #e5e7eb; border-radius: 8px; padding: 16px; }
.row { display:flex; gap: 16px; align-items:flex-start; }
.product { display:flex; justify-content:space-between; padding:6px 0; border-bottom:1px dashed #eee; }
.product:last-child { border-bottom:none; }
</style>


