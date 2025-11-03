# 简易在线购物系统

基于 Spring Boot 3 (JDK 17) + MySQL 的后端与最简前端（Vue CDN）示例，实现商品、购物车、下单、支付（模拟）与订单查询等核心流程。

## 运行方式（本地内网，无 Docker）

1) 本地启动后端

- 安装并启动 MySQL 8，本地创建数据库 `shopdb`，账号密码与 `backend/src/main/resources/application.yml` 保持一致（默认 root/root）。
- 进入 `backend` 目录，执行：

```bash
mvn spring-boot:run
```

2) 本地启动前端（开发模式）

```bash
cd frontend
npm install
npm run dev
```

打开：`http://localhost:5173`（已代理 `/api` 到 `http://localhost:8080`）

3) 本地预览构建产物

```bash
cd frontend
npm install
npm run build
npm run preview
```

打开：`http://localhost:4173`

更多详情见 `LOCAL_SETUP.md`。

## 主要接口

- 用户
  - POST `/api/users/register` {username,email,password}
  - POST `/api/users/login` {username,password}
- 商品
  - GET `/api/products`
  - POST `/api/products`
  - PUT `/api/products/{id}`
  - DELETE `/api/products/{id}`
- 购物车
  - GET `/api/cart/{userId}`
  - POST `/api/cart/{userId}/add` {productId,quantity}
  - PUT `/api/cart/item/{itemId}` {quantity}
  - DELETE `/api/cart/item/{itemId}`
- 订单
  - POST `/api/orders/create` {userId}
  - POST `/api/orders/pay` {orderId,userId}
  - GET `/api/orders/my/{userId}`
  - PUT `/api/orders/{orderId}/status` {status}

## 注意事项

- 当前登录使用简单接口返回用户信息，未接入完整鉴权（JWT/Session）。
- `DDL-AUTO=update` 便于演示，生产建议使用迁移工具（Flyway/Liquibase）。
- 可以在 `frontend/index.html` 直接演示完整购买流程。


