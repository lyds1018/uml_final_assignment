<template>
  <div class="orders-container">
    <h2>我的订单</h2>
    <div v-if="orders.length === 0" class="no-orders">
      <p>没有订单</p>
      <router-link to="/products" class="continue-shopping">去购物</router-link>
    </div>

    <div v-else class="order-list">
      <div v-for="order in orders" :key="order.id" class="order-card">
        <div class="order-header">
          <div>订单 #{{ order.id }}</div>
          <div class="order-status">{{ getStatusText(order.status) }}</div>
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
          <div class="total-price">总计：¥{{ order.totalPrice }}</div>
          <div class="order-actions">
            <router-link :to="`/orders/${order.id}`">查看详情</router-link>
            <button
              v-if="order.status === 'PENDING'"
              @click="pay(order.id)"
              class="pay-button"
            >去支付</button>
            <button
              v-if="order.status === 'PENDING'"
              @click="cancel(order.id)"
              class="cancel-button"
            >取消订单</button>
            <button
              v-if="order.status === 'SHIPPING'"
              @click="complete(order.id)"
              class="confirm-button"
            >确认收货</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'Orders',
  data() {
    return {
      orders: []
    }
  },
  async created() {
    await this.fetchOrders()
  },
  methods: {
    async fetchOrders() {
      try {
        const token = localStorage.getItem('token')
        const response = await axios.get('/api/orders', {
          headers: { Authorization: `Bearer ${token}` }
        })
        this.orders = response.data.data
      } catch (error) {
        alert('获取订单失败：' + error.message)
      }
    },
    getStatusText(status) {
      const map = {
        PENDING: '待支付',
        PAID: '已支付',
        SHIPPING: '已发货',
        COMPLETED: '已完成',
        CANCELLED: '已取消'
      }
      return map[status] || status
    },
    async pay(orderId) {
      try {
        const token = localStorage.getItem('token')
        await axios.post('/api/orders/pay', { orderId }, {
          headers: { Authorization: `Bearer ${token}` }
        })
        await this.fetchOrders()
        alert('支付成功')
      } catch (error) {
        alert('支付失败：' + error.message)
      }
    },
    async cancel(orderId) {
      try {
        const token = localStorage.getItem('token')
        await axios.put(`/api/orders/${orderId}/cancel`, {}, {
          headers: { Authorization: `Bearer ${token}` }
        })
        await this.fetchOrders()
        alert('订单已取消')
      } catch (error) {
        alert('取消失败：' + error.message)
      }
    },
    async complete(orderId) {
      try {
        const token = localStorage.getItem('token')
        await axios.put(`/api/orders/${orderId}/complete`, {}, {
          headers: { Authorization: `Bearer ${token}` }
        })
        await this.fetchOrders()
        alert('确认收货成功')
      } catch (error) {
        alert('操作失败：' + error.message)
      }
    }
  }
}
</script>

<style scoped>
.orders-container { padding: 20px; max-width: 1200px; margin: 0 auto; }
.order-list { display: grid; gap: 15px; }
.order-card { border: 1px solid #ddd; border-radius: 8px; padding: 15px; }
.order-header { display: flex; justify-content: space-between; margin-bottom: 10px; }
.order-status { font-weight: bold; color: #4CAF50; }
.order-items { margin-bottom: 10px; }
.order-item { display: flex; gap: 15px; border-bottom: 1px solid #eee; padding: 5px 0; }
.order-footer { display: flex; justify-content: space-between; align-items: center; }
.total-price { color: #e53935; font-weight: bold; }
button { padding: 6px 15px; border: none; border-radius: 4px; cursor: pointer; margin-left: 5px; }
.pay-button { background-color: #4CAF50; color: white; }
.cancel-button { background-color: #f44336; color: white; }
.confirm-button { background-color: #2196F3; color: white; }
</style>
