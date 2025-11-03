<template>
  <div class="row">
    <div class="card" style="flex:1">
      <h3>商品</h3>
      <div v-for="p in products" :key="p.id" class="product">
        <div>
          <div><strong>{{ p.name }}</strong></div>
          <div class="muted">¥{{ p.price }} ｜ 库存 {{ p.stock }}</div>
        </div>
        <div>
          <input type="number" min="1" v-model.number="p._qty" style="width:70px" />
          <button @click="addToCart(p)">加入购物车</button>
        </div>
      </div>
      <div style="margin-top:8px;">
        <input v-model="newProduct.name" placeholder="名称" />
        <input v-model.number="newProduct.price" placeholder="价格" type="number" />
        <input v-model.number="newProduct.stock" placeholder="库存" type="number" />
        <button @click="createProduct">新增商品（模拟管理员）</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive } from 'vue';
import { api, type ApiResponse } from '../api';

type Product = { id: number; name: string; price: number; stock: number; _qty?: number };

const state = reactive({
  products: [] as Product[],
  newProduct: { name: '', price: 1, stock: 10 }
});

async function refreshProducts() {
  const r = await api.get<ApiResponse<Product[]>>('/products');
  state.products = r.data.data.map(p => ({ ...p, _qty: 1 }));
}

async function createProduct() {
  await api.post('/products', state.newProduct);
  state.newProduct = { name: '', price: 1, stock: 10 };
  refreshProducts();
}

async function addToCart(p: Product) {
  const userId = Number(localStorage.getItem('userId'));
  if (!userId) return alert('请先登录');
  await api.post(`/cart/${userId}/add`, { productId: p.id, quantity: p._qty || 1 });
  alert('已加入购物车');
}

onMounted(refreshProducts);

const products = state.products as unknown as Product[];
const newProduct = state.newProduct;
</script>

<style scoped>
.muted { color:#6b7280; }
</style>


