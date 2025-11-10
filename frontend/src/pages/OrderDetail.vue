<template>
  <div class="order-detail-container">
    <h2>订单详情</h2>

    <div v-if="order" class="order-detail">
      <div class="order-header">
        <p>订单编号：{{ order.id }}</p>
        <p>下单时间：{{ formatDate(order.createTime) }}</p>
        <p>订单状态：{{ getStatusText(order.status) }}</p>
      </div>

      <div class="order-items">
        <h3>商品信息</h3>
        <div v-for="item in order.items" :key="item.id" class="item">
          <div class="item-info">
            <h4>{{ item.product.name }}</h4>
            <p>价格：¥{{ item.product.price }}</p>
            <p>数量：{{ item.quantity }}</p>
            <p>小计：¥{{ (item.product.price * item.quantity).toFixed(2) }}</p>
          </div>
        </div>
      </div>

      <div class="order-summary">
        <p>订单总计：¥{{ order.totalPrice }}</p>
        <div class="actions">
          <button
            v-if="order.status === 'PENDING'"
            class="pay-button"
            @click="pay"
          >去支付</button>
          <button
            v-if="order.status === 'PENDING'"
            class="cancel-button"
            @click="cancel"
          >取消订单</button>
          <button
            v-if="order.status === 'SHIPPING'"
            class="confirm-button"
            @click="complete"
          >确认收货</button>
        </div>
      </div>
    </div>

    <div v-else class="loading">加载中...</div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'OrderDetail',
  data() {
    return {
      order: null
    }
  },
  async created() {
    await this.fetchOrderDetail()
  },
  methods: {
    async fetchOrderDetail() {
      try {
        const token = localStorage.getItem('token')
        const orderId = this.$route.params.id
        const response = await axios.get(`/api/orders/${orderId}`, {
          headers: { Authorization: `Bearer ${token}` }
        })
        this.order = response.data.data
      } catch (error) {
        alert('获取订单详情失败：' + error.message)
      }
    },
    formatDate(date) {
      return new Date(date).toLocaleString()
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
    async pay() {
      try {
        const token = localStorage.getItem('token')
        await axios.post('/api/orders/pay', { orderId: this.order.id }, {
          headers: { Authorization: `Bearer ${token}` }
        })
        await this.fetchOrderDetail()
        alert('支付成功')
      } catch (error) {
        alert('支付失败：' + error.message)
      }
    },
    async cancel() {
      try {
        const token = localStorage.getItem('token')
        await axios.put(`/api/orders/${this.order.id}/cancel`, {}, {
          headers: { Authorization: `Bearer ${token}` }
        })
        await this.fetchOrderDetail()
        alert('订单已取消')
      } catch (error) {
        alert('取消失败：' + error.message)
      }
    },
    async complete() {
      try {
        const token = localStorage.getItem('token')
        await axios.put(`/api/orders/${this.order.id}/complete`, {}, {
          headers: { Authorization: `Bearer ${token}` }
        })
        await this.fetchOrderDetail()
        alert('确认收货成功')
      } catch (error) {
        alert('操作失败：' + error.message)
      }
    }
  }
}
</script>

<style scoped>
.order-detail-container { padding: 20px; max-width: 1000px; margin: 0 auto; }
.order-header { margin-bottom: 20px; }
.order-items { margin-bottom: 20px; }
.item { border-bottom: 1px solid #eee; padding: 10px 0; }
.item-info h4 { margin: 0 0 5px; }
.order-summary { display: flex; justify-content: space-between; align-items: center; }
.actions button { margin-left: 10px; padding: 6px 15px; border: none; border-radius: 4px; cursor: pointer; }
.pay-button { background-color: #4CAF50; color: white; }
.cancel-button { background-color: #f44336; color: white; }
.confirm-button { background-color: #2196F3; color: white; }
.loading { text-align: center; padding: 40px; font-size: 18px; color: #666; }
</style>
