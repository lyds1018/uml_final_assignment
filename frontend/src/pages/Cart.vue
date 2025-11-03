<template>
  <div class="card">
    <h3>购物车</h3>
    <div v-if="!userId" class="muted">请先登录</div>
    <div v-else>
      <div v-for="ci in cartItems" :key="ci.id" class="product">
        <div>#{{ ci.productId }} × {{ ci.quantity }}</div>
        <div>
          <input type="number" min="1" v-model.number="ci.quantity" style="width:70px" />
          <button @click="updateCartItem(ci)">更新</button>
          <button @click="removeCartItem(ci)">删除</button>
        </div>
      </div>
      <div style="margin-top:8px;">
        <button @click="createOrder">提交订单</button>
        <span class="muted" style="margin-left:8px">订单ID：{{ currentOrderId || '-' }}</span>
        <button @click="payOrder" :disabled="!currentOrderId" style="margin-left:8px">支付订单</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, computed } from 'vue';
import { api, type ApiResponse } from '../api';

type CartItem = { id: number; productId: number; quantity: number };

const state = reactive({
  userId: 0,
  cartItems: [] as CartItem[],
  currentOrderId: 0
});

const userId = computed(() => state.userId);
const cartItems = computed(() => state.cartItems);
const currentOrderId = computed(() => state.currentOrderId);

async function refreshCart() {
  if (!state.userId) return;
  const r = await api.get<ApiResponse<CartItem[]>>(`/cart/${state.userId}`);
  state.cartItems = r.data.data;
}

async function updateCartItem(ci: CartItem) {
  await api.put(`/cart/item/${ci.id}`, { quantity: ci.quantity });
  refreshCart();
}

async function removeCartItem(ci: CartItem) {
  await api.delete(`/cart/item/${ci.id}`);
  refreshCart();
}

async function createOrder() {
  const r = await api.post<ApiResponse<any>>('/orders/create', { userId: state.userId });
  state.currentOrderId = r.data.data.id;
  alert('订单创建成功，订单ID：' + state.currentOrderId);
}

async function payOrder() {
  await api.post('/orders/pay', { orderId: state.currentOrderId, userId: state.userId });
  alert('支付成功');
  state.currentOrderId = 0;
  refreshCart();
}

onMounted(() => {
  state.userId = Number(localStorage.getItem('userId') || 0);
  refreshCart();
});
</script>

<style scoped>
.muted { color:#6b7280; }
</style>


