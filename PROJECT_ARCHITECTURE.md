
# 一、后端 （`backend/`）

## 1. 技术栈：

- Java 17、Spring Boot 3.x、Spring Data JPA、MySQL、JWT、BCryp、Lombok

- 构建工具：Maven（`pom.xml`）

- 主要配置：`src/main/resources/application.yml`
  - 默认运行端口：8080
  - 数据库：`SPRING_DATASOURCE_URL` / `SPRING_DATASOURCE_USERNAME` / `SPRING_DATASOURCE_PASSWORD`（默认定向到 MySQL）
  - JWT 密钥与过期时间可通过 `app.security.jwt.*` 配置
  - 文件上传设置在 `app.upload.*`

## 2. 包结构（`src/main/java/com/shop/`）：

- `controller/`：RESTful API 层

- `service/`：业务逻辑层

- `repository/`：数据访问层

```
前端 (Vue) 
   ↓ HTTP 请求 (JSON)
Controller 层
   ↓ 调用
Service 层
   ↓ 调用
Repository 层 (JPA)
   ↓
数据库 (MySQL)
```

- `model/`：JPA 实体（`User`, `Product`, `Order`, `OrderItem`, `Cart`, `CartItem` 等）

- `config/`：Spring 配置类（如 CORS / MVC 拦截器注册等）

- `interceptor/`：请求拦截器（例如 `AuthInterceptor`、`AdminAuthInterceptor`），用于 JWT 校验与权限控制

- `dto/`：定义通用的响应 DTO（如 `ResponseDTO`）

- `exception/`：自定义异常处理（如 `BusinessException`, `GlobalExceptionHandler`）

- `resources/static/`：静态资源（如后端打包时的前端静态页）

- 数据访问：使用 Spring Data JPA 管理实体

---

# 二、前端 （`frontend/`）

## 1. 技术栈：

- Vue 3、Vite、Vue Router、Axios、npm

## 2. 文件结构：

  - `src/main.js`：应用入口，配置 axios（baseURL、拦截器）、挂载路由

  - `src/router.js`：前端路由（包括保护路由与管理员权限守卫）

  - `src/App.vue`：顶层布局（导航栏、路由出口）

  - `src/pages/`：应用页面组件：
    - `Products.vue`：商品列表与搜索
    - `Cart.vue`：购物车
    - `Login.vue` / `Register.vue`：认证页面
    - `Orders.vue` / `OrderDetail.vue`：用户订单与订单详情
    - `Admin.vue`：后台管理页（商品/订单/用户管理）

  - `src/assets/`：样式和静态资源（`main.css`）
