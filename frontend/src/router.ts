import type { RouteRecordRaw } from 'vue-router';
import Home from './pages/Home.vue';
import Login from './pages/Login.vue';
import Register from './pages/Register.vue';
import Cart from './pages/Cart.vue';
import Orders from './pages/Orders.vue';
import Admin from './pages/Admin.vue';

const routes: RouteRecordRaw[] = [
  { path: '/', name: 'Home', component: Home },
  { path: '/login', name: 'Login', component: Login },
  { path: '/register', name: 'Register', component: Register },
  { path: '/cart', name: 'Cart', component: Cart },
  { path: '/orders', name: 'Orders', component: Orders },
  { path: '/admin', name: 'Admin', component: Admin }
];

export default routes;


