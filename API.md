## 1. 用户模块
| 方法   | 完整路径                  | 功能      | 角色  |
| ---- | --------------------- | ------- | --- |
| POST | `/api/users/register` | 注册      | 无   |
| POST | `/api/users/login`    | 登录拿 JWT | 无   |

## 2. 商品模块
| 方法  | 完整路径                          | 功能    | 角色  |
| --- | ----------------------------- | ----- | --- |
| GET | `/api/products`               | 商品列表  | 无   |
| GET | `/api/products/search?query=` | 关键字搜索 | 无   |

## 3. 购物车模块
| 方法     | 完整路径                       | 功能    | 角色  |
| ------ | -------------------------- | ----- | --- |
| GET    | `/api/cart`                | 我的购物车 | 需登录 |
| POST   | `/api/cart/add`            | 加入购物车 | 需登录 |
| DELETE | `/api/cart/items/{itemId}` | 删除商品项 | 需登录 |
| PUT    | `/api/cart/items/{itemId}` | 修改数量  | 需登录 |

## 4. 订单模块
| 方法   | 完整路径                             | 功能      | 角色  |
| ---- | -------------------------------- | ------- | --- |
| POST | `/api/orders/create`             | 创建待支付订单 | 需登录 |
| POST | `/api/orders/pay`                | 支付订单    | 需登录 |
| GET  | `/api/orders`                    | 我的订单列表  | 需登录 |
| GET  | `/api/orders/{id}`               | 订单详情    | 需登录 |
| PUT  | `/api/orders/{orderId}/complete` | 确认收货    | 需登录 |
| PUT  | `/api/orders/{orderId}/cancel`   | 取消订单    | 需登录 |

## 5. 后台管理模块
| 业务  | 方法     | 完整路径                               | 功能     | 角色   |
| --- | ------ | ---------------------------------- | ------ | ---- |
| 商品  | GET    | `/api/admin/products`              | 查看全部商品 | 需管理员 |
| 商品  | POST   | `/api/admin/products`              | 新增商品   | 需管理员 |
| 商品  | PUT    | `/api/admin/products/{id}`         | 修改商品   | 需管理员 |
| 商品  | DELETE | `/api/admin/products/{id}`         | 删除商品   | 需管理员 |
| 订单  | GET    | `/api/admin/orders`                | 查看全部订单 | 需管理员 |
| 订单  | POST   | `/api/admin/orders/{orderId}/ship` | 设置发货   | 需管理员 |
| 用户  | GET    | `/api/admin/users`                 | 查看全部用户 | 需管理员 |
| 用户  | DELETE | `/api/admin/users/{userId}`        | 删除用户   | 需管理员 |
