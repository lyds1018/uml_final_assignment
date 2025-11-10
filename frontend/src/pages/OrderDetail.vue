<template>
  <div class="order-detail-container">
    <h2>订单详情</h2>

    <!-- 订单加载状态 -->
    <div v-if="!order" class="loading">加载中...</div>

    <!-- 订单详情 -->
    <div v-else class="order-detail">
      <div class="order-header">
        <p>订单编号：{{ order.id }}</p>
        <p>下单时间：{{ formatDate(order.createTime) }}</p>
        <p>订单状态：{{ getStatusText(order.status) }}</p>
      </div>

      <!-- 商品列表 -->
      <div class="order-items">
        <h3>商品信息</h3>
        <div class="item" v-for="item in order.items" :key="item.id">
          <div class="item-info">
            <h4>{{ item.product.name }}</h4>
            <p class="price">单价：¥{{ item.product.price }}</p>
            <p class="quantity">数量：{{ item.quantity }}</p>
            <p class="subtotal">小计：¥{{ (item.product.price * item.quantity).toFixed(2) }}</p>
          </div>
        </div>
      </div>

      <!-- 订单总计与操作按钮 -->
      <div class="order-summary">
        <div class="total">
          <span>订单总计：</span>
          <span class="price">¥{{ order.totalPrice }}</span>
        </div>
        <div class="actions">
          <button 
            v-if="order.status === 'PENDING'" 
            class="pay-button" 
            @click="payOrder"
          >
            去支付
          </button>
          <button 
            v-if="order.status === 'SHIPPING'" 
            class="confirm-button" 
            @click="confirmOrder"
          >
            确认收货
          </button>
        </div>
      </div>
    </div>
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
    await this.loadOrder();
  },
  methods: {
    async loadOrder() {
      try {
        const orderId = this.$route.params.id;
        const response = await axios.get(`/api/orders/${orderId}`);
        this.order = response.data.data;
      } catch (error) {
        alert('获取订单详情失败：' + error.message);
      }
    },
    formatDate(date) {
      return new Date(date).toLocaleString();
    },
    getStatusText(status) {
      const statusMap = {
        'PENDING': '待支付',
        'PAID': '已支付',
        'SHIPPING': '已发货',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消'
      };
      return statusMap[status] || status;
    },
    async payOrder() {
      try {
        await axios.post('/api/orders/pay', { orderId: this.order.id });
        await this.loadOrder();
        alert('支付成功！');
      } catch (error) {
        alert('支付失败：' + error.message);
      }
    },
    async confirmOrder() {
      try {
        await axios.put(`/api/orders/${this.order.id}/complete`);
        await this.loadOrder();
        alert('确认收货成功！');
      } catch (error) {
        alert('确认收货失败：' + error.message);
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
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.order-header p {
  margin: 8px 0;
  color: #555;
}

.order-items {
  margin-top: 20px;
}

.item {
  display: flex;
  padding: 10px 0;
  border-bottom: 1px solid #eee;
}

.item-info h4 {
  margin: 0 0 5px;
}

.price, .subtotal {
  color: #f00;
  font-weight: bold;
}

.quantity {
  color: #666;
}

.order-summary {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 20px;
}

.total .price {
  color: #f00;
  font-size: 20px;
  font-weight: bold;
}

.actions button {
  padding: 8px 20px;
  border: none;
  border-radius: 4px;
  color: #fff;
  cursor: pointer;
  margin-left: 10px;
}

.pay-button { background-color: #f44336; }
.confirm-button { background-color: #4CAF50; }

.actions button:hover {
  opacity: 0.9;
}

.loading {
  text-align: center;
  padding: 40px;
  color: #666;
}
</style>
