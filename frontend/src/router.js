import { createRouter, createWebHistory } from 'vue-router'
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
    redirect: '/products' 
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: '/products',
    name: 'Products',
    component: Products
  },
  {
    path: '/cart',
    name: 'Cart',
    component: Cart,
    meta: { requiresAuth: true }
  },
  {
    path: '/orders',
    name: 'Orders',
    component: Orders,
    meta: { requiresAuth: true }
  },
  {
    path: '/orders/:id',
    name: 'OrderDetail',
    component: OrderDetail,
    meta: { requiresAuth: true }
  },
  {
    path: '/admin',
    name: 'Admin',
    component: Admin,
    meta: { requiresAuth: true, requiresAdmin: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const userRole = localStorage.getItem('userRole')

  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!token) {
      next('/login')
      return
    }

    if (to.matched.some(record => record.meta.requiresAdmin) && userRole !== 'ADMIN') {
      alert('权限不足，需要管理员权限')
      next(from.path) // 返回之前的页面
      return
    }
  }

  next()
})

export default router