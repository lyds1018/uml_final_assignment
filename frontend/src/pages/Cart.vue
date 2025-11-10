<template>
  <div class="cart-container">
    <h2>购物车</h2>

    <!-- 空购物车 -->
    <div v-if="items.length === 0" class="empty-cart">
      <p>购物车是空的</p>
      <router-link to="/products" class="continue-shopping">继续购物</router-link>
    </div>

    <!-- 有商品 -->
    <div v-else>
      <div class="cart-items">
        <div v-for="item in items" :key="item.id" class="cart-item">

          <div class="item-details">
            <h3>{{ item.product.name }}</h3>
            <p class="price">¥{{ item.product.price }}</p>
          </div>

          <div class="quantity-controls">
            <button
              @click="changeQty(item, item.quantity - 1)"
              :disabled="item.quantity <= 1"
            >-</button>

            <span>{{ item.quantity }}</span>

            <button
              @click="changeQty(item, item.quantity + 1)"
              :disabled="item.quantity >= item.product.stock"
            >+</button>
          </div>

          <button class="remove-button" @click="removeItem(item.id)">删除</button>
        </div>
      </div>

      <div class="cart-summary">
        <div class="total">
          <span>总计：</span>
          <span class="total-price">¥{{ total }}</span>
        </div>
        <button class="checkout-button" @click="checkout">结算</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'Cart',

  data() {
    return {
      items: []
    }
  },

  computed: {
    total() {
      return this.items
        .reduce((sum, i) => sum + i.product.price * i.quantity, 0)
        .toFixed(2)
    }
  },

  async created() {
    this.loadCart()
  },

  methods: {
    async loadCart() {
      try {
        const res = await axios.get('/api/cart')
        this.items = res.data.data
      } catch (err) {
        alert('获取购物车失败：' + (err.response?.data?.message || err.message))
      }
    },

    async changeQty(item, qty) {
      try {
        await axios.put(`/api/cart/items/${item.id}`, { quantity: qty })
        this.loadCart()
      } catch (err) {
        alert('更新数量失败：' + (err.response?.data?.message || err.message))
      }
    },

    async removeItem(id) {
      if (!confirm('确定删除该商品吗？')) return
      try {
        await axios.delete(`/api/cart/items/${id}`)
        this.loadCart()
      } catch (err) {
        alert('删除失败：' + (err.response?.data?.message || err.message))
      }
    },

    async checkout() {
      try {
        const res = await axios.post('/api/orders/create')
        this.$router.push(`/orders/${res.data.data.id}`)
      } catch (err) {
        alert('创建订单失败：' + (err.response?.data?.message || err.message))
      }
    }
  }
}
</script>

<style scoped>
.cart-container {
  padding: 20px;
  max-width: 1000px;
  margin: 0 auto;
}

.empty-cart {
  text-align: center;
  padding: 50px 0;
}

.continue-shopping {
  display: inline-block;
  padding: 10px 20px;
  background-color: #4CAF50;
  color: #fff;
  text-decoration: none;
  border-radius: 4px;
}

.cart-items {
  margin-bottom: 20px;
}

.cart-item {
  display: grid;
  grid-template-columns: 1fr auto auto;
  gap: 20px;
  align-items: center;
  padding: 15px;
  border-bottom: 1px solid #ddd;
}

.item-details h3 {
  margin: 0;
  font-size: 1.2em;
}

.price {
  color: #e53935;
  font-weight: bold;
}

.quantity-controls {
  display: flex;
  align-items: center;
  gap: 10px;
}

.quantity-controls button {
  width: 30px;
  height: 30px;
  border: 1px solid #ddd;
  background-color: white;
  border-radius: 4px;
  cursor: pointer;
}

.quantity-controls button:disabled {
  color: #ccc;
  cursor: not-allowed;
}

.remove-button {
  padding: 6px 12px;
  background-color: #ff4444;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.cart-summary {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 20px;
  padding: 20px 0;
}

.total {
  font-size: 1.2em;
}

.total-price {
  color: #e53935;
  font-weight: bold;
}

.checkout-button {
  padding: 10px 30px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1.1em;
}

.checkout-button:hover {
  background-color: #45a049;
}
</style>