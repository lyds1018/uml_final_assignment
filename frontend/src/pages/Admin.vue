<template>
  <div class="admin-container">
    <div class="sidebar">
      <h2>后台管理</h2>
      <nav>
        <button
          @click="currentView = 'products'"
          :class="{ active: currentView === 'products' }"
        >
          商品管理
        </button>
        <button
          @click="currentView = 'orders'"
          :class="{ active: currentView === 'orders' }"
        >
          订单管理
        </button>
        <button
          @click="currentView = 'users'"
          :class="{ active: currentView === 'users' }"
        >
          用户管理
        </button>
      </nav>
    </div>

    <div class="content">
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
              <td>{{ order.user.username }}</td>
              <td>¥{{ order.totalPrice }}</td>
              <td>{{ getStatusText(order.status) }}</td>
              <td>
                <button
                  v-if="order.status === 'PAID'"
                  @click="shipOrder(order.id)"
                >
                  发货
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

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
                <button @click="toggleUserStatus(user.id)">
                  {{ user.enabled ? '禁用' : '启用' }}
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- 添加/编辑商品的对话框 -->
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
      productForm: {
        name: '',
        price: 0,
        stock: 0,
        description: ''
      }
    }
  },
  async created() {
    await this.fetchData()
  },
  methods: {
    async fetchData() {
      switch (this.currentView) {
        case 'products':
          await this.fetchProducts()
          break
        case 'orders':
          await this.fetchOrders()
          break
        case 'users':
          await this.fetchUsers()
          break
      }
    },
    async fetchProducts() {
      try {
        const response = await axios.get('/api/admin/products')
        this.products = response.data.data
      } catch (error) {
        alert('获取商品列表失败：' + error.message)
      }
    },
    async fetchOrders() {
      try {
        const response = await axios.get('/api/admin/orders')
        this.orders = response.data.data
      } catch (error) {
        alert('获取订单列表失败：' + error.message)
      }
    },
    async fetchUsers() {
      try {
        const response = await axios.get('/api/admin/users')
        this.users = response.data.data
      } catch (error) {
        alert('获取用户列表失败：' + error.message)
      }
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
      } catch (error) {
        alert('保存商品失败：' + error.message)
      }
    },
    async deleteProduct(productId) {
      if (confirm('确定要删除这个商品吗？')) {
        try {
          await axios.delete(`/api/admin/products/${productId}`)
          await this.fetchProducts()
        } catch (error) {
          alert('删除商品失败：' + error.message)
        }
      }
    },
    async shipOrder(orderId) {
      try {
        await axios.post(`/api/admin/orders/${orderId}/ship`)
        await this.fetchOrders()
      } catch (error) {
        alert('发货失败：' + error.message)
      }
    },
    async toggleUserStatus(userId) {
      try {
        await axios.post(`/api/admin/users/${userId}/toggle-status`)
        await this.fetchUsers()
      } catch (error) {
        alert('更新用户状态失败：' + error.message)
      }
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
    }
  }
}
</script>

<style scoped>
.admin-container {
  display: flex;
  min-height: 100vh;
}

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
  padding: 10px;
  text-align: left;
  border: none;
  background: none;
  cursor: pointer;
}

.sidebar button.active {
  background-color: #4CAF50;
  color: white;
  border-radius: 4px;
}

.content {
  flex: 1;
  padding: 20px;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

th, td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

th {
  background-color: #f5f5f5;
}

.add-button {
  background-color: #4CAF50;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.delete-button {
  background-color: #ff4444;
  color: white;
}

button {
  padding: 5px 10px;
  margin: 0 5px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  width: 500px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.modal-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}
</style>