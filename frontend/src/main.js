import axios from 'axios'
import { createApp } from 'vue'
import App from './App.vue'
import './assets/main.css'
import router from './router'

// 配置 axios 默认值
axios.defaults.baseURL = 'http://localhost:8080'

// 添加请求拦截器
axios.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 添加响应拦截器
axios.interceptors.response.use(
  response => {
    return response
  },
  error => {
    if (error.response && error.response.status === 401) {
      // 如果是认证失败，清除 token 并跳转到登录页
      localStorage.removeItem('token')
      localStorage.removeItem('userRole')
      router.push('/login')
    }
    return Promise.reject(error)
  }
)

const app = createApp(App)
app.use(router)
app.mount('#app')