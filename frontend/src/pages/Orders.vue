<template>
  <div class="card">
    <h3>我的订单</h3>
    <div v-if="!userId" class="muted">请先登录</div>
    <div v-else>
      <div v-for="o in orders" :key="o.id" class="product">
        <div>订单 #{{ o.id }}</div>
        <div class="muted">状态：{{ o.status }} ｜ 总额：¥{{ o.totalPrice }}</div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, computed } from 'vue';
import { api, type ApiResponse } from '../api';

type Order = { id: number; status: string; totalPrice: number };

const state = reactive({
  userId: 0,
  orders: [] as Order[]
});

const userId = computed(() => state.userId);
const orders = computed(() => state.orders);

async function refreshOrders() {
  if (!state.userId) return;
  const r = await api.get<ApiResponse<Order[]>>(`/orders/my/${state.userId}`);
  state.orders = r.data.data;
}

onMounted(() => {
  state.userId = Number(localStorage.getItem('userId') || 0);
  refreshOrders();
});
</script>

<style scoped>
.muted { color:#6b7280; }
</style>


