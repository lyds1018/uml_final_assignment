# 简易在线购物系统 — 实现方案

## 1. 功能模块划分
| 模块    | 功能描述            |
| ----- | --------------- |
| 用户模块  | 注册、登录           |
| 商品模块  | 浏览商品、查看详情、分类与搜索 |
| 购物车模块 | 添加商品、修改数量、删除商品  |
| 订单模块  | 下单、支付、订单记录      |
| 管理员模块 | 商品增删改查、订单管理     |
| 系统模块  | 权限控制、日志与异常处理    |

---

## 2. Python 技术需求
- **后端开发**
  - 使用 Flask 构建 REST API 
  - 使用 Flask Blueprint 模块化结构
- **前端展示**
  - Flask + Bootstrap / Vue 前端分离 
  - 实现商品展示、表单提交、分页与搜索 
- **数据库设计**
  - MySQL 
  - ORM 管理数据库实体
- **部署运行**
  - Docker 容器化

---

## 3. Java 技术需求
- **后端开发**
  - 使用 Spring Boot 构建 REST API 
  - 分层结构：Controller → Service → Repository 
  - 登录认证采用 Spring Security 
- **前端展示**
  - Thymeleaf 模板（单体项目）或 Vue + Axios（前后端分离）
- **数据库设计**
  - MySQL 
  - ORM 管理数据库实体
- **测试与部署**
  - 单元测试：JUnit 5 + MockMVC 
  - 打包部署：`mvn package` → `java -jar shop.jar` 
