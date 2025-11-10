<template>
  <div class="orders-container">
    <h2>我的订单</h2>

    <div v-if="orders.length === 0" class="no-orders">
      <p>没有订单</p>
      <router-link to="/products" class="continue-shopping">去购物</router-link>
    </div>

    <div v-else>
      <div class="order-list">
        <div v-for="order in orders" :key="order.id" class="order-card">
          <div class="order-header">
            <div>订单 #{{ order.id }}</div>
            <div class="order-status">{{ statusText(order.status) }}</div>
          </div>

          <div class="order-items">
            <div v-for="item in order.items" :key="item.id" class="order-item">
              <div class="item-details">
                <h4>{{ item.product.name }}</h4>
                <p>数量：{{ item.quantity }}</p>
                <p>小计：¥{{ (item.product.price * item.quantity).toFixed(2) }}</p>
              </div>
            </div>
          </div>

          <div class="order-footer">
            <div class="total-price">总计：¥{{ totalPrice(order) }}</div>
            <div class="order-actions">
              <router-link :to="`/orders/${order.id}`">查看详情</router-link>
              <button
                v-if="order.status === 'PENDING'"
                class="pay-button"
                @click="payOrder(order.id)"
              >去支付</button>
              <button
                v-if="order.status === 'SHIPPING'"
                class="confirm-button"
                @click="confirmReceive(order.id)"
              >确认收货</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'Orders',
  data() {
    return { orders: [] }
  },
  async created() {
    await this.loadOrders()
  },
  methods: {
    async loadOrders() {
      try {
        const res = await axios.get('/api/orders')
        this.orders = res.data.data || []
      } catch (err) {
        alert('获取订单失败：' + (err.response?.data?.message || err.message))
      }
    },

    statusText(status) {
      const map = {
        PENDING: '待支付',
        PAID: '已支付',
        SHIPPING: '已发货',
        COMPLETED: '已完成',
        CANCELLED: '已取消'
      }
      return map[status] || status
    },

    totalPrice(order) {
      return order.items
        .reduce((sum, item) => sum + item.product.price * item.quantity, 0)
        .toFixed(2)
    },

    async payOrder(orderId) {
      try {
        const res = await axios.post('/api/orders/pay', { orderId })
        if (res.data.success) {
          alert('支付成功！')
          await this.loadOrders()
        } else {
          alert('支付失败：' + (res.data.message || '未知错误'))
        }
      } catch (err) {
        alert('支付失败：' + (err.response?.data?.message || err.message))
      }
    },

    async confirmReceive(orderId) {
      try {
        const res = await axios.put(`/api/orders/${orderId}/complete`)
        if (res.data.success) {
          alert('确认收货成功！')
          await this.loadOrders()
        } else {
          alert('操作失败：' + (res.data.message || '未知错误'))
        }
      } catch (err) {
        alert('操作失败：' + (err.response?.data?.message || err.message))
      }
    }
  }
}
</script>

<style scoped>
.orders-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.no-orders {
  text-align: center;
  padding: 50px 0;
}

.continue-shopping {
  display: inline-block;
  padding: 10px 20px;
  background-color: #2196F3;
  color: white;
  text-decoration: none;
  border-radius: 4px;
}

.order-list {
  display: grid;
  gap: 15px;
}

.order-card {
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
}

.order-header {
  background-color: #f5f5f5;
  padding: 15px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.order-status {
  color: #4CAF50;
  font-weight: bold;
}

.order-items {
  padding: 15px;
}

.order-item {
  display: flex;
  gap: 20px;
  padding: 10px 0;
  border-bottom: 1px solid #eee;
}

.order-item:last-child {
  border-bottom: none;
}

.item-details h4 {
  margin: 0 0 5px 0;
}

.item-details p {
  margin: 5px 0;
  color: #666;
}

.order-footer {
  background-color: #f9f9f9;
  padding: 15px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.total-price {
  color: #e53935;
  font-weight: bold;
  font-size: 1.2em;
}

.order-actions button {
  padding: 8px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-left: 10px;
}

.pay-button {
  background-color: #4CAF50;
  color: white;
}

.confirm-button {
  background-color: #2196F3;
  color: white;
}

button:hover {
  opacity: 0.9;
}
</style>