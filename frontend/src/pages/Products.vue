<template>
  <div class="products-container">
    <div class="search-bar">
      <input
        type="text"
        v-model="searchQuery"
        placeholder="搜索商品..."
        @input="searchProducts"
      />
    </div>
    <div class="products-grid">
      <div v-for="product in products" :key="product.id" class="product-card">
        <img :src="product.image" :alt="product.name" />
        <h3>{{ product.name }}</h3>
        <p class="price">¥{{ product.price }}</p>
        <p class="stock">库存: {{ product.stock }}</p>
        <button
          @click="addToCart(product)"
          :disabled="product.stock <= 0"
        >
          加入购物车
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'Products',
  data() {
    return {
      products: [],
      searchQuery: ''
    }
  },
  async created() {
    await this.fetchProducts()
  },
  methods: {
    async fetchProducts() {
      try {
        const response = await axios.get('/api/products')
        this.products = response.data.data
      } catch (error) {
        alert('获取商品列表失败：' + error.message)
      }
    },
    async searchProducts() {
      try {
        const response = await axios.get(`/api/products/search?query=${this.searchQuery}`)
        this.products = response.data.data
      } catch (error) {
        alert('搜索商品失败：' + error.message)
      }
    },
    async addToCart(product) {
      try {
        await axios.post('/api/cart/add', {
          productId: product.id,
          quantity: 1
        })
        alert('已添加到购物车！')
      } catch (error) {
        alert('添加到购物车失败：' + error.message)
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
  text-align: center;
}

.product-card img {
  width: 100%;
  max-height: 200px;
  object-fit: cover;
  border-radius: 4px;
}

.product-card h3 {
  margin: 10px 0;
  font-size: 1.2em;
}

.price {
  color: #e53935;
  font-size: 1.1em;
  font-weight: bold;
}

.stock {
  color: #666;
  margin: 5px 0;
}

button {
  background-color: #4CAF50;
  color: white;
  border: none;
  padding: 8px 15px;
  border-radius: 4px;
  cursor: pointer;
}

button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

button:hover:not(:disabled) {
  background-color: #45a049;
}
</style>