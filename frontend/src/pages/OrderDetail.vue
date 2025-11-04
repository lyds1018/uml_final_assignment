<template>
  <div class="order-detail-container">
    <h2>订单详情</h2>
    <div v-if="order" class="order-detail">
      <div class="order-header">
        <div class="order-info">
          <p>订单编号：{{ order.id }}</p>
          <p>下单时间：{{ formatDate(order.createTime) }}</p>
          <p>订单状态：{{ getStatusText(order.status) }}</p>
        </div>
      </div>

      <div class="order-items">
        <h3>商品信息</h3>
        <div class="item" v-for="item in order.items" :key="item.id">
          <img :src="item.product.image" :alt="item.product.name" />
          <div class="item-info">
            <h4>{{ item.product.name }}</h4>
            <p class="price">¥{{ item.product.price }}</p>
            <p class="quantity">数量：{{ item.quantity }}</p>
            <p class="subtotal">小计：¥{{ (item.product.price * item.quantity).toFixed(2) }}</p>
          </div>
        </div>
      </div>

      <div class="order-summary">
        <div class="total">
          <span>订单总计：</span>
          <span class="price">¥{{ order.totalPrice }}</span>
        </div>
        <div class="actions">
          <button v-if="order.status === 'PENDING'" class="pay-button" @click="pay">去支付</button>
          <button v-if="order.status === 'SHIPPING'" class="confirm-button" @click="confirmReceive">确认收货</button>
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
        const orderId = this.$route.params.id
        const response = await axios.get(`/api/orders/${orderId}`)
        this.order = response.data.data
      } catch (error) {
        alert('获取订单详情失败：' + error.message)
      }
    },
    formatDate(date) {
      return new Date(date).toLocaleString()
    },
    getStatusText(status) {
      const statusMap = {
        'PENDING': '待支付',
        'PAID': '已支付',
        'SHIPPING': '已发货',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消'
      }
      return statusMap[status] || status
    },
    async pay() {
      try {
        await axios.post(`/api/orders/${this.order.id}/pay`)
        await this.fetchOrderDetail()
        alert('支付成功！')
      } catch (error) {
        alert('支付失败：' + error.message)
      }
    },
    async confirmReceive() {
      try {
        await axios.post(`/api/orders/${this.order.id}/receive`)
        await this.fetchOrderDetail()
        alert('确认收货成功！')
      } catch (error) {
        alert('确认收货失败：' + error.message)
      }
    }
  }
}
</script>

<style scoped>
.order-detail-container {
  padding: 20px;
  max-width: 1000px;
  margin: 0 auto;
}

.order-detail {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.order-header {
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.order-info p {
  margin: 10px 0;
  color: #666;
}

.order-items {
  padding: 20px 0;
}

.order-items h3 {
  margin-bottom: 20px;
}

.item {
  display: flex;
  padding: 15px 0;
  border-bottom: 1px solid #eee;
}

.item img {
  width: 100px;
  height: 100px;
  object-fit: cover;
  margin-right: 20px;
}

.item-info {
  flex: 1;
}

.item-info h4 {
  margin: 0 0 10px 0;
}

.price {
  color: #f00;
  font-weight: bold;
}

.quantity {
  color: #666;
  margin: 5px 0;
}

.subtotal {
  color: #333;
  font-weight: bold;
}

.order-summary {
  padding-top: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.total {
  font-size: 18px;
}

.total .price {
  color: #f00;
  font-size: 24px;
}

.actions button {
  padding: 8px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  margin-left: 10px;
}

.pay-button {
  background-color: #f00;
  color: #fff;
}

.confirm-button {
  background-color: #4CAF50;
  color: #fff;
}

.loading {
  text-align: center;
  padding: 40px;
  font-size: 18px;
  color: #666;
}
</style>