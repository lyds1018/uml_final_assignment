<template>
  <div class="cart-container">
    <h2>购物车</h2>
    <div v-if="cartItems.length === 0" class="empty-cart">
      <p>购物车是空的</p>
      <router-link to="/products" class="continue-shopping">继续购物</router-link>
    </div>
    <div v-else>
      <div class="cart-items">
        <div v-for="item in cartItems" :key="item.id" class="cart-item">
          <img :src="item.product.image" :alt="item.product.name" />
          <div class="item-details">
            <h3>{{ item.product.name }}</h3>
            <p class="price">¥{{ item.product.price }}</p>
          </div>
          <div class="quantity-controls">
            <button @click="updateQuantity(item.id, item.quantity - 1)" :disabled="item.quantity <= 1">-</button>
            <span>{{ item.quantity }}</span>
            <button @click="updateQuantity(item.id, item.quantity + 1)" :disabled="item.quantity >= item.product.stock">+</button>
          </div>
          <button class="remove-button" @click="removeItem(item.id)">删除</button>
        </div>
      </div>
      <div class="cart-summary">
        <div class="total">
          <span>总计：</span>
          <span class="total-price">¥{{ totalPrice }}</span>
        </div>
        <button class="checkout-button" @click="checkout">结算</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'Cart',
  data() {
    return {
      cartItems: []
    }
  },
  computed: {
    totalPrice() {
      return this.cartItems.reduce((total, item) => {
        return total + (item.product.price * item.quantity)
      }, 0).toFixed(2)
    }
  },
  async created() {
    await this.fetchCartItems()
  },
  methods: {
    async fetchCartItems() {
      try {
        const response = await axios.get('/api/cart')
        this.cartItems = response.data.data
      } catch (error) {
        alert('获取购物车信息失败：' + error.message)
      }
    },
    async updateQuantity(itemId, newQuantity) {
      try {
        await axios.put(`/api/cart/items/${itemId}`, {
          quantity: newQuantity
        })
        await this.fetchCartItems()
      } catch (error) {
        alert('更新数量失败：' + error.message)
      }
    },
    async removeItem(itemId) {
      try {
        await axios.delete(`/api/cart/items/${itemId}`)
        await this.fetchCartItems()
      } catch (error) {
        alert('删除商品失败：' + error.message)
      }
    },
    async checkout() {
      try {
        const response = await axios.post('/api/orders/create')
        this.$router.push(`/orders/${response.data.data.id}`)
      } catch (error) {
        alert('创建订单失败：' + error.message)
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
  color: white;
  text-decoration: none;
  border-radius: 4px;
}

.cart-items {
  margin-bottom: 20px;
}

.cart-item {
  display: grid;
  grid-template-columns: 100px 1fr auto auto;
  gap: 20px;
  align-items: center;
  padding: 15px;
  border-bottom: 1px solid #ddd;
}

.cart-item img {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: 4px;
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
  padding: 5px 10px;
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