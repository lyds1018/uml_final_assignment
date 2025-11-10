import { createRouter, createWebHistory } from 'vue-router'

// 引入页面组件
import Admin from './pages/Admin.vue'
import Cart from './pages/Cart.vue'
import Login from './pages/Login.vue'
import OrderDetail from './pages/OrderDetail.vue'
import Orders from './pages/Orders.vue'
import Products from './pages/Products.vue'
import Register from './pages/Register.vue'

const routes = [
  { 
    path: '/', 
    redirect: '/login' // 默认进入登录页
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { showNavbar: false } // 登录页不显示导航栏
  },
  {
    path: '/register',
    name: 'Register',
    component: Register,
    meta: { showNavbar: false } // 注册页也不显示导航栏
  },
  {
    path: '/products',
    name: 'Products',
    component: Products,
    meta: { requiresAuth: true, showNavbar: true }
  },
  {
    path: '/cart',
    name: 'Cart',
    component: Cart,
    meta: { requiresAuth: true, showNavbar: true }
  },
  {
    path: '/orders',
    name: 'Orders',
    component: Orders,
    meta: { requiresAuth: true, showNavbar: true }
  },
  {
    path: '/orders/:id',
    name: 'OrderDetail',
    component: OrderDetail,
    meta: { requiresAuth: true, showNavbar: true }
  },
  {
    path: '/admin',
    name: 'Admin',
    component: Admin,
    meta: { requiresAuth: true, requiresAdmin: true, showNavbar: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

// ----------------------------
// 全局路由守卫
// ----------------------------
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const userRole = localStorage.getItem('userRole')

  // 需要登录
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!token) {
      next('/login') // 未登录跳登录页
      return
    }
  }

  // 需要管理员权限
  if (to.matched.some(record => record.meta.requiresAdmin)) {
    if (userRole !== 'ADMIN') {
      alert('权限不足，需要管理员权限')
      next(false)
      return
    }
  }

  next()
})

export default router
