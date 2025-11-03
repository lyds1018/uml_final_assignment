## 一、项目总体结构

**前后端分离架构**：

- **后端**：使用 Flask 提供 RESTful API 接口 
    
- **前端**：使用 Vue.js 实现网页交互 
    
- **数据库**：使用 MySQL 存储用户、商品、订单等信息 
    
- **ORM**：通过 SQLAlchemy 管理数据库实体，实现对象化操作 
    
- **部署环境**：通过 Docker 容器化运行，方便移植与部署 
    

---

## 二、各部分技术实现

### 1. 后端（Flask）

- 框架：Flask 
    
- 路由管理：Flask Blueprint 模块化拆分（用户、商品、订单等模块）
    
- API 风格：RESTful API（统一接口格式）
    
- ORM：SQLAlchemy 
    
- 依赖管理：使用 uv 管理依赖 
    
- 运行：Gunicorn + Docker 
    

### 2. 前端（Vue.js）

- 框架：Vue 3 
    
- 组件库：Element Plus / BootstrapVue（简易管理界面）
    
- 状态管理：Pinia 或 Vuex 
    
- HTTP 请求：Axios 调用后端接口 
    
- 打包：Vite / Webpack 
    

### 3. 数据库（MySQL）

- 表结构：用户表、商品表、购物车表、订单表 
    
- 工具：Navicat 
    
- 数据迁移：Flask-Migrate 
    

### 4. ORM（SQLAlchemy）

- 作用：将 Python 类映射为数据库表，避免手写 SQL
    
- 模型示例：
 ```Python
 class User(db.Model):
	 id = db.Column(db.Integer, primary_key=True)
	 username = db.Column(db.String(50), unique=True, nullable=False)
	 password = db.Column(db.String(128), nullable=False)
 ```
### 5. 部署（Docker）

- 构建镜像：
    
    - `Dockerfile` 定义 Flask 容器
        
    - `docker-compose.yml` 启动 Flask + MySQL + Vue 前端
        
- 优点：
    
    - 一键部署
        
    - 环境一致性
        
    - 可在服务器上轻松运行
        

---

## 三、系统模块划分

|模块|功能|
|---|---|
|用户模块|注册、登录、修改信息|
|商品模块|浏览、搜索、分类查看|
|购物车模块|添加、删除、结算|
|订单模块|下单、支付、查看历史订单|
|管理员模块|管理商品、审核订单、统计数据|

---

## 四、开发流程建议

1. **数据库设计**：先建表，确定数据关系。
    
2. **后端API实现**：编写 Flask Blueprint 模块。
    
3. **前端页面开发**：Vue 组件化实现页面交互。
    
4. **前后端联调**：通过 Axios 调用 Flask 接口。
    
5. **Docker 部署**：编写 Dockerfile + docker-compose.yml。