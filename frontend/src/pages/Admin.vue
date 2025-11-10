<template>
  <div class="admin-container">
    <!-- 左侧导航 -->
    <div class="sidebar">
      <h2>后台管理</h2>
      <nav>
        <button
          @click="setView('products')"
          :class="{ active: currentView === 'products' }"
        >商品管理</button>
        <button
          @click="setView('orders')"
          :class="{ active: currentView === 'orders' }"
        >订单管理</button>
        <button
          @click="setView('users')"
          :class="{ active: currentView === 'users' }"
        >用户管理</button>
      </nav>
    </div>

    <!-- 内容区域 -->
    <div class="content">
      <!-- 商品管理 -->
      <div v-if="currentView === 'products'" class="products-management">
        <h3>商品管理</h3>
        <button class="add-button" @click="showAddProductForm = true">添加商品</button>
        <table>
          <thead>
            <tr>
              <th>商品名称</th>
              <th>价格</th>
              <th>库存</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="product in products" :key="product.id">
              <td>{{ product.name }}</td>
              <td>¥{{ product.price }}</td>
              <td>{{ product.stock }}</td>
              <td>
                <button @click="editProduct(product)">编辑</button>
                <button @click="deleteProduct(product.id)" class="delete-button">删除</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- 订单管理 -->
      <div v-if="currentView === 'orders'" class="orders-management">
        <h3>订单管理</h3>
        <table>
          <thead>
            <tr>
              <th>订单号</th>
              <th>用户</th>
              <th>总金额</th>
              <th>状态</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="order in orders" :key="order.id">
              <td>{{ order.id }}</td>
              <td>{{ order.user ? order.user.username : '' }}</td>
              <td>¥{{ order.totalPrice }}</td>
              <td>{{ getStatusText(order.status) }}</td>
              <td>
                <button v-if="order.status === 'PAID'" @click="shipOrder(order.id)">发货</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- 用户管理 -->
      <div v-if="currentView === 'users'" class="users-management">
        <h3>用户管理</h3>
        <table>
          <thead>
            <tr>
              <th>用户名</th>
              <th>邮箱</th>
              <th>角色</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="user in users" :key="user.id">
              <td>{{ user.username }}</td>
              <td>{{ user.email }}</td>
              <td>{{ user.role === 'ADMIN' ? '管理员' : '用户' }}</td>
              <td>
                <button @click="deleteUser(user.id)" class="delete-button">删除</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- 添加/编辑商品对话框 -->
    <div v-if="showAddProductForm" class="modal">
      <div class="modal-content">
        <h3>{{ editingProduct ? '编辑商品' : '添加商品' }}</h3>
        <form @submit.prevent="saveProduct">
          <div class="form-group">
            <label>商品名称：</label>
            <input v-model="productForm.name" required />
          </div>
          <div class="form-group">
            <label>价格：</label>
            <input type="number" v-model="productForm.price" min="0" step="0.01" required />
          </div>
          <div class="form-group">
            <label>库存：</label>
            <input type="number" v-model="productForm.stock" min="0" required />
          </div>
          <div class="form-group">
            <label>描述：</label>
            <textarea v-model="productForm.description"></textarea>
          </div>
          <div class="modal-buttons">
            <button type="submit">保存</button>
            <button type="button" @click="showAddProductForm = false">取消</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'Admin',
  data() {
    return {
      currentView: 'products',
      products: [],
      orders: [],
      users: [],
      showAddProductForm: false,
      editingProduct: null,
      productForm: { name: '', price: 0, stock: 0, description: '' }
    }
  },
  async created() {
    await this.fetchData()
  },
  methods: {
    async setView(view) {
      this.currentView = view
      await this.fetchData()
    },
    async fetchData() {
      if (this.currentView === 'products') await this.fetchProducts()
      else if (this.currentView === 'orders') await this.fetchOrders()
      else if (this.currentView === 'users') await this.fetchUsers()
    },

    // 商品
    async fetchProducts() {
      try {
        const res = await axios.get('/api/admin/products')
        this.products = res.data.data
      } catch (err) { alert('获取商品列表失败：' + err.message) }
    },
    editProduct(product) {
      this.editingProduct = product
      this.productForm = { ...product }
      this.showAddProductForm = true
    },
    async saveProduct() {
      try {
        if (this.editingProduct) {
          await axios.put(`/api/admin/products/${this.editingProduct.id}`, this.productForm)
        } else {
          await axios.post('/api/admin/products', this.productForm)
        }
        this.showAddProductForm = false
        this.editingProduct = null
        this.productForm = { name: '', price: 0, stock: 0, description: '' }
        await this.fetchProducts()
      } catch (err) { alert('保存商品失败：' + err.message) }
    },
    async deleteProduct(id) {
      if (!confirm('确定要删除这个商品吗？')) return
      try {
        await axios.delete(`/api/admin/products/${id}`)
        await this.fetchProducts()
      } catch (err) { alert('删除商品失败：' + err.message) }
    },

    // 订单
    async fetchOrders() {
      try {
        const res = await axios.get('/api/admin/orders')
        this.orders = res.data.data
      } catch (err) { alert('获取订单列表失败：' + err.message) }
    },
    async shipOrder(orderId) {
      try {
        await axios.post(`/api/admin/orders/${orderId}/ship`)
        await this.fetchOrders()
      } catch (err) { alert('发货失败：' + err.message) }
    },

    // 用户
    async fetchUsers() {
      try {
        const res = await axios.get('/api/admin/users')
        this.users = res.data.data
      } catch (err) { alert('获取用户列表失败：' + err.message) }
    },
    async deleteUser(userId) {
      if (!confirm('确定要删除这个用户吗？')) return
      try {
        await axios.delete(`/api/admin/users/${userId}`)
        await this.fetchUsers()
      } catch (err) { alert('删除用户失败：' + err.message) }
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
    }
  }
}
</script>

<style scoped>
.admin-container {
  display: flex;
  min-height: 100vh;
  font-family: Arial, sans-serif;
  background-color: #f9f9f9;
}

/* 左侧导航 */
.sidebar {
  width: 200px;
  background-color: #f5f5f5;
  padding: 20px;
}

.sidebar nav {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.sidebar button {
  width: 100%;
  padding: 12px;
  text-align: left;
  border: none;
  background: none;
  cursor: pointer;
  font-size: 14px;
  border-radius: 4px;
  transition: all 0.2s;
}

.sidebar button:hover {
  background-color: #e0e0e0;
}

.sidebar button.active {
  background-color: #4CAF50;
  color: white;
}

/* 内容区域 */
.content {
  flex: 1;
  padding: 20px;
  overflow-x: auto;
}

/* 表格样式 */
table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
  background-color: #fff;
  border-radius: 6px;
  overflow: hidden;
}

th, td {
  padding: 12px 15px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

th {
  background-color: #f5f5f5;
  font-weight: 600;
}

tr:hover {
  background-color: #f1f1f1;
}

/* 按钮样式 */
.add-button {
  background-color: #4CAF50;
  color: white;
  padding: 10px 20px;
  border-radius: 4px;
  border: none;
  cursor: pointer;
  margin-bottom: 10px;
  transition: all 0.2s;
}

.add-button:hover {
  background-color: #45a049;
}

.delete-button {
  background-color: #ff4444;
  color: white;
  padding: 5px 12px;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.2s;
}

.delete-button:hover {
  background-color: #e53935;
}

button {
  padding: 5px 10px;
  margin: 0 5px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.2s;
}

/* 弹窗样式 */
.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: #fff;
  padding: 25px 30px;
  border-radius: 8px;
  width: 500px;
  max-width: 90%;
  box-shadow: 0 4px 12px rgba(0,0,0,0.2);
}

/* 表单 */
.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 6px;
  font-weight: 500;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 8px 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.modal-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}
</style>

