import { createRouter, createWebHistory } from 'vue-router'

// 引入所有页面组件
import Admin from './pages/Admin.vue'
import Cart from './pages/Cart.vue'
import Login from './pages/Login.vue'
import OrderDetail from './pages/OrderDetail.vue'
import Orders from './pages/Orders.vue'
import Products from './pages/Products.vue'
import Register from './pages/Register.vue'

// ----------------------------
// 路由配置表
// ----------------------------
const routes = [
  { 
    path: '/', 
    redirect: '/products' // 访问根路径时自动跳转到产品页
  },
  {
    path: '/login',
    name: 'Login',
    component: Login // 登录页
  },
  {
    path: '/register',
    name: 'Register',
    component: Register // 注册页
  },
  {
    path: '/products',
    name: 'Products',
    component: Products // 产品列表页，不需要登录
  },
  {
    path: '/cart',
    name: 'Cart',
    component: Cart,
    meta: { requiresAuth: true } // 购物车，需要登录
  },
  {
    path: '/orders',
    name: 'Orders',
    component: Orders,
    meta: { requiresAuth: true } // 订单列表，需要登录
  },
  {
    path: '/orders/:id',
    name: 'OrderDetail',
    component: OrderDetail,
    meta: { requiresAuth: true } // 订单详情，需要登录
  },
  {
    path: '/admin',
    name: 'Admin',
    component: Admin,
    meta: { requiresAuth: true, requiresAdmin: true } // 管理员页面，需要登录 + 管理员权限
  }
]

// ----------------------------
// 创建 Router 实例
// ----------------------------
const router = createRouter({
  history: createWebHistory(), // 使用无 # 的 history 模式
  routes,
})

// ----------------------------
// 全局路由守卫：每次跳转前执行
// ----------------------------
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token') // 判断是否登录
  const userRole = localStorage.getItem('userRole') // 获取用户角色（USER / ADMIN）

  // 判断路由是否需要登录
  if (to.matched.some(record => record.meta.requiresAuth)) {

    // 如果未登录，跳转到登录页
    if (!token) {
      next('/login')
      return
    }

    // 判断是否需要管理员权限且当前用户角色不是管理员
    if (to.matched.some(record => record.meta.requiresAdmin) && userRole !== 'ADMIN') {
      alert('权限不足，需要管理员权限')
      next(false)  // 取消导航，保持在当前页面
      return
    }
  }

  // 放行
  next()
})

export default router