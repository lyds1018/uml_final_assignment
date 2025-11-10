<template>
  <div class="products-container">
    <!-- 搜索栏 -->
    <div class="search-bar">
      <input
        type="text"
        v-model="searchQuery"
        placeholder="搜索商品..."
        @input="debouncedSearch"
      />
    </div>

    <!-- 商品列表 -->
    <div class="products-grid">
      <div v-for="p in products" :key="p.id" class="product-card">
        <h3>{{ p.name }}</h3>
        <p class="price">¥{{ p.price }}</p>
        <p class="stock">库存: {{ p.stock }}</p>

        <button @click="addToCart(p)" :disabled="p.stock <= 0">
          {{ p.stock > 0 ? '加入购物车' : '已售罄' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'Products',

  data() {
    return {
      products: [],
      searchQuery: '',
      searchTimer: null
    }
  },

  async created() {
    this.fetchProducts()
  },

  methods: {
    // 基础商品获取
    async fetchProducts() {
      try {
        const res = await axios.get('/api/products')
        this.products = res.data.data
      } catch (err) {
        alert('获取商品失败：' + (err.response?.data?.message || err.message))
      }
    },

    // 关键字搜索 + 节流
    debouncedSearch() {
      clearTimeout(this.searchTimer)
      this.searchTimer = setTimeout(() => {
        this.searchProducts()
      }, 400) // 防止输入过快频繁调用
    },

    async searchProducts() {
      if (!this.searchQuery) {
        this.fetchProducts()
        return
      }

      try {
        const res = await axios.get(`/api/products/search?query=${this.searchQuery}`)
        this.products = res.data.data
      } catch (err) {
        alert('搜索失败：' + (err.response?.data?.message || err.message))
      }
    },

    // 加入购物车
    async addToCart(p) {
      try {
        await axios.post('/api/cart/add', {
          productId: p.id,
          quantity: 1
        })
        alert('已加入购物车！')
      } catch (err) {
        alert('加入购物车失败：' + (err.response?.data?.message || err.message))
      }
    }
  }
}
</script>

<style scoped>
.products-container {
  padding: 20px;
}

.search-bar {
  margin-bottom: 20px;
}

.search-bar input {
  width: 100%;
  max-width: 500px;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
}

.product-card {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 15px;
  background: #fff;
  text-align: center;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
}

.product-card h3 {
  margin: 10px 0;
  font-size: 1.2em;
  font-weight: bold;
}

.price {
  color: #e53935;
  font-size: 1.1em;
  font-weight: bold;
}

.stock {
  color: #555;
  margin: 6px 0;
}

button {
  background-color: #4CAF50;
  color: #fff;
  border: none;
  padding: 8px 15px;
  border-radius: 4px;
  cursor: pointer;
  transition: 0.2s;
}

button:disabled {
  background-color: #aaa;
  cursor: not-allowed;
}

button:hover:not(:disabled) {
  background-color: #45a049;
}
</style>